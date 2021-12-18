package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Tasks extends AppCompatActivity {

    private FloatingActionButton actionButton;
    private FloatingActionButton calendar;
    private FloatingActionButton notes;
    private FloatingActionButton tasks;
    private FloatingActionButton reminders;
    private FloatingActionButton settings;

    private Animation rotateopen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean clicked;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private int index = -1;
    private Button editButton;
    private Button deleteButton;
    private EditText editText;
    private boolean selected;
    private CheckedTextView checkedTextView;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.Theme_MyApplication);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        editButton = findViewById(R.id.editAction);
        deleteButton = findViewById(R.id.deleteAction);
        editText = findViewById(R.id.etNewItem);
        items = new ArrayList<String>();
        settings = findViewById(R.id.settingsButton);

        readItems();

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, items);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        registerForContextMenu(lvItems);
        setupListViewListener();

        actionButton = findViewById(R.id.floatingActionButton);
        calendar = findViewById(R.id.calendar);
        notes = findViewById(R.id.notes);
        tasks = findViewById(R.id.tasksButton);
        reminders = findViewById(R.id.remindersButton);


        rotateopen = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tasks.this, Notes.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tasks.this, Settings.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tasks.this, "You are already in tasks!!", Toast.LENGTH_SHORT).show();
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tasks.this, Reminders.class);
                startActivity(intent);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tasks.this, Calendar.class);
                startActivity(intent);
            }
        });
    }


    private void setVisibility(boolean clicked) {
        if (!clicked) {
            calendar.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            tasks.setVisibility(View.VISIBLE);
            reminders.setVisibility(View.VISIBLE);
            settings.setVisibility(View.VISIBLE);

        } else {
            calendar.setVisibility(View.INVISIBLE);
            notes.setVisibility(View.INVISIBLE);
            tasks.setVisibility(View.INVISIBLE);
            reminders.setVisibility(View.INVISIBLE);
            settings.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            actionButton.startAnimation(rotateopen);
            calendar.startAnimation(fromBottom);
            notes.startAnimation(fromBottom);
            tasks.startAnimation(fromBottom);
            reminders.startAnimation(fromBottom);
            settings.startAnimation(fromBottom);

        } else {
            actionButton.startAnimation(rotateClose);
            calendar.startAnimation(toBottom);
            notes.startAnimation(toBottom);
            tasks.startAnimation(toBottom);
            reminders.startAnimation(toBottom);
            settings.startAnimation(toBottom);

        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            calendar.setClickable(true);
            notes.setClickable(true);
            tasks.setClickable(true);
            reminders.setClickable(true);
            settings.setClickable(true);

        } else {
            calendar.setClickable(false);
            notes.setClickable(false);
            tasks.setClickable(false);
            reminders.setClickable(false);
            settings.setClickable(false);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        index = (int) info.id;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.setHeaderTitle("Select Action");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.editAction){
            editText.setText(items.get(index));
            selected = true;
            return true;
        } else if (item.getItemId() == R.id.deleteAction && index >= 0){
            items.remove(index);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            index = -1;
            return true;
        } else {
            return false;
        }
    }

    private void setupListViewListener(){
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                checkedTextView = ((CheckedTextView)item);
                checkedTextView.setChecked(true);
                deleteItem(pos);
            }
        });
    }

    public void deleteItem(int pos){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkedTextView.setChecked(false);
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }

        }, 500); // 500ms delay
    }

    public void onAddItem(View v){
        String itemText = editText.getText().toString();
        if(selected){
            items.remove(index);
            itemsAdapter.notifyDataSetChanged();
            selected = false;
            index = -1;
        }
        if(itemText.trim().isEmpty()){
            taskEmptyToast();
        } else
            itemsAdapter.add(itemText);
        editText.setText("");
        writeItems();
    }

    public void taskEmptyToast(){
        Context context = getApplicationContext();
        CharSequence text = "Can not add empty task";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
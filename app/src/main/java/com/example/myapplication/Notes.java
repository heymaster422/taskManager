package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Notes extends AppCompatActivity {

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

    ImageButton save;
    ArrayList<String> arrayListView = new ArrayList<String>();
    ArrayList<String> masterArrayList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText editText;
    EditText editTitle;
    ListView show;
    int index = -1;
    int itemSelected;
    String detailInput;
    String titleInput;
    File file;
    ImageButton delete;
    ImageButton edit;

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
        setContentView(R.layout.activity_main3);

        editTitle =(EditText)findViewById(R.id.inputTitle);
        editText =(EditText)findViewById(R.id.inputText);
        show = (ListView) findViewById(R.id.listView);
        save = (ImageButton) findViewById(R.id.save);
        delete = findViewById(R.id.delete);


        actionButton = findViewById(R.id.floatingActionButton);
        calendar = findViewById(R.id.calendar);
        notes = findViewById(R.id.notes);
        tasks = findViewById(R.id.tasksButton);
        reminders = findViewById(R.id.remindersButton);
        settings = findViewById(R.id.settingsButton);


        rotateopen = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                String[] split = masterArrayList.get(index).split("%%%");
                editTitle.setText(split[0]);
                editText.setText(split[1]);
                Toast.makeText(Notes.this, "Note Selected!!!", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new ArrayAdapter<String>(Notes.this, android.R.layout.simple_list_item_1, arrayListView);
        show.setAdapter(adapter);


        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index >= 0){
                    adapter.remove(adapter.getItem(index));
                    adapter.notifyDataSetChanged();
                    masterArrayList.remove(index);
                    index = -1;
                    editText.setText("");
                    editTitle.setText("");
                    Toast.makeText(Notes.this, "Note deleted!!", Toast.LENGTH_SHORT).show();
                    try {
                        write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }

        });

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detailInput = editText.getText().toString();
                titleInput = editTitle.getText().toString();
                if (index >= 0) {
                    adapter.remove(adapter.getItem(index));
                    adapter.notifyDataSetChanged();
                    masterArrayList.remove(index);
                    index = -1;
                    Toast.makeText(Notes.this, "Note Edited!!", Toast.LENGTH_SHORT).show();
                }
                try {
                    fillInputChecker(titleInput, detailInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, Settings.class);
                startActivity(intent);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Notes.this, "Notes app already open!", Toast.LENGTH_SHORT).show();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, Calendar.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, Tasks.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, Reminders.class);
                startActivity(intent);
            }
        });


    }

    void fillInputChecker(String titleInput, String detailInput) throws IOException {
        if(arrayListView.contains(titleInput)){
            Toast.makeText(Notes.this, "Item Already Added", Toast.LENGTH_LONG).show();

        }else if(titleInput == null || titleInput.trim().equals("")){
            Toast.makeText(Notes.this, "Title is empty", Toast.LENGTH_LONG).show();

        }else if (arrayListView.contains(detailInput)) {
            Toast.makeText(Notes.this, "Item Already Added", Toast.LENGTH_LONG).show();

        }else if (detailInput == null || detailInput.trim().equals("")) {
            Toast.makeText(Notes.this, "Description is empty", Toast.LENGTH_LONG).show();
        }else{
            String masterInfo = titleInput + "%%%" + detailInput;
            masterArrayList.add(masterInfo);
            arrayListView.add(titleInput);
            adapter.notifyDataSetChanged();

            ((EditText)findViewById(R.id.inputText)).setText("");
            ((EditText)findViewById(R.id.inputTitle)).setText("");
            Toast.makeText(this, "Note Saved!!", Toast.LENGTH_SHORT).show();
            write();

        }

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

    public  void write() throws IOException {
        //creating a new file in the same directory
        file = new File(this.getFilesDir(), "notes");
        FileOutputStream fout = new FileOutputStream(file);
        BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fout));

        //reading hashmap and writing to the file
        for (String s : masterArrayList) {
            buffer.write(s);
            buffer.newLine();
        }

        buffer.close();
        fout.close();

    }

    //this method reads the file and write the data to the hashtable
    public  void read() throws IOException {
        file = new File(this.getFilesDir(), "notes");
        FileInputStream fout = new FileInputStream(file);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(fout));

        //if file exists, then read file line by line and add it to the hashtable
        if (file.exists()) {
            String line = buffer.readLine();
            while (line != null) {
                masterArrayList.add(line);
                String[] split = line.split("%%%");
                arrayListView.add(split[0]);
                line = buffer.readLine();
            }

            buffer.close();
            fout.close();

        }else
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();

    }

}

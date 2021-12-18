package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Reminders extends AppCompatActivity {

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
    Button button_save;
    Button button_edit;
    Button button_delete;
    Context context;

    ListView listView;
    String date;
    int itemIndex;
    String itemSelected;




    //HashMap<String, ArrayList<String>> map;
    File file;
    ArrayList<String> listViewList;
    ArrayList<String> splitList;
    ArrayAdapter<String> adapter;

    public static String timeEdit = "time";
    public static String nameEdit = "name";
    public static String dateEdit = "date";

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
        setContentView(R.layout.activity_reminders);

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

        listViewList = new ArrayList<>();
        button_save = findViewById(R.id.buttonsave);
        listView = findViewById(R.id.reminlist);
        button_edit = findViewById(R.id.buttonedit);
        button_delete = findViewById(R.id.buttondelete);
        context = this;
        itemIndex = -1;


        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }


        splitList = new ArrayList<>();

        if (!listViewList.isEmpty()) {

            for (String s : listViewList) {
                splitList.add(s.replace("     ", "\n"));
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, splitList);
            listView.setAdapter(adapter);
        }



        button_save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, CreateReminder.class);
                startActivityForResult(intent, 7);
            }
        }));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemIndex = position;
                itemSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, "Item Selected!!", Toast.LENGTH_SHORT).show();
            }
        });


        //edit button
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sa = itemSelected.split("\n");
                nameEdit = sa[0];
                String[] sb = sa[1].split("  ");
                dateEdit = sb[0];
                timeEdit = sb[1];

                if (itemIndex >= 0) {
                    adapter.remove(adapter.getItem(itemIndex));
                    listViewList.remove(itemIndex);
                    adapter.notifyDataSetChanged();
                    itemIndex = -1;
                }

                Intent intent = new Intent(Reminders.this, CreateReminder.class);
                startActivityForResult(intent, 7);


            }
        });


        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemIndex >= 0) {
                    adapter.remove(adapter.getItem(itemIndex));
                    listViewList.remove(itemIndex);
                    adapter.notifyDataSetChanged();
                    itemIndex = -1;
                    try {
                        write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Reminder Deleted!!", Toast.LENGTH_SHORT).show();

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

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, Notes.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, Tasks.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, Settings.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Reminders.this, "You are already in Reminders!!", Toast.LENGTH_SHORT).show();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, Calendar.class);
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

    public  void write() throws IOException {
        file = new File(this.getFilesDir(), "remindersData");
        FileOutputStream fout = new FileOutputStream(file);
        BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fout));

        for (String s : listViewList) {
            buffer.write(s);
            buffer.newLine();
        }

        Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();

        buffer.close();
        fout.close();

    }


    public  void read() throws IOException {
        file = new File(this.getFilesDir(), "remindersData");
        FileInputStream fout = new FileInputStream(file);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(fout));

        if (file.exists()) {
            String line = buffer.readLine();
            while (line != null) {
                listViewList.add(line);
                line = buffer.readLine();
            }

            buffer.close();
            fout.close();

        }else
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();



    }

    //this overridden method is to get the user's input back from the other activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //checking if the request code matches
        if (requestCode == 7) {
            String t = data.getStringExtra(CreateReminder.EXTRA_NAME);
            String time = data.getStringExtra(CreateReminder.EXTRA_TIME);
            date = data.getStringExtra(CreateReminder.EXTRA_DATE);

            // class tomorrow
            // date  time
            if (!t.equals("")) {
                String info = t + "     " + date + "  " + time;


                listViewList.add(info);


                try {
                    write();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                adapter.add(info.replace("     ", "\n"));
                adapter.notifyDataSetChanged();
            }


        }

    }}

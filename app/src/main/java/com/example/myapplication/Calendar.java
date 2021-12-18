package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Calendar extends AppCompatActivity {

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


    private CalendarView calendarView;
    private String date;
    private Button save;
    private HashMap<String, ArrayList<String>> map;
    private File file;
    private ListView listView;
    private ArrayList<String> listViewList;
    private ArrayList<String> splitList;
    private Context context;
    private int itemIndex;
    private ArrayAdapter<String> adapter;
    private String itemSelected;
    public static String timeEdit = "time";
    public static String titleEdit = "title";
    public static String descriptionEdit = "description";

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
        setContentView(R.layout.activity_main);

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


        calendarView = findViewById(R.id.calendarView);
        //empty hashmap
        map = new HashMap<>();
        save = findViewById(R.id.save);
        listView = findViewById(R.id.listView);
        context = this;
        Button edit = findViewById(R.id.Edit);
        Button delete = findViewById(R.id.Delete);
        itemIndex = -1;
        date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());


        try {
            //as soon as we open the app, we read data from file and put it in hashtable
            read();
        } catch (IOException e) {
            System.out.println("File could not be read");
            e.printStackTrace();
        }

        if (map.containsKey(date)) {
            listViewList = map.get(date);
            splitList = new ArrayList<>();

            for (String s : listViewList) {
                splitList.add(s.replace("     ", "\n"));
            }


            //creating adapter for listView
            adapter = new ArrayAdapter<String>(context, R.layout.activity_row_of_list, splitList);
            listView.setAdapter(adapter);
        } else {
            //if the key is not in hashmap, then just set empty list in list view
            listViewList = new ArrayList<>();
            adapter = new ArrayAdapter<String>(context, R.layout.activity_row_of_list, listViewList);
            listView.setAdapter(adapter);
        }


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;

                //getting date from the calendar
                //8/23/2021
                if (month < 10 && dayOfMonth < 10) {
                    date = "0" + month + "/" + "0" + dayOfMonth + "/" + year;
                } else if (month < 10) {
                    date = "0" + month + "/" + dayOfMonth + "/" + year;
                } else if (dayOfMonth < 10) {
                    date = month + "/" + "0" + dayOfMonth + "/" + year;
                } else
                    date = month + "/" + dayOfMonth + "/" + year;

                //if hashtable contains a key with that date, then get the value of that key
                //and set it in the list view
                if (map.containsKey(date)) {
                    listViewList = map.get(date);
                    splitList = new ArrayList<>();

                    for (String s : listViewList) {
                        splitList.add(s.replace("     ", "\n"));
                    }


                    //creating adapter for listView
                    adapter = new ArrayAdapter<String>(context, R.layout.activity_row_of_list, splitList);
                    listView.setAdapter(adapter);
                } else {
                    //if the key is not in hashmap, then just set empty list in list view
                    listViewList = new ArrayList<>();
                    adapter = new ArrayAdapter<String>(context, R.layout.activity_row_of_list, listViewList);
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemIndex = position;
                itemSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, "Item Selected!!", Toast.LENGTH_SHORT).show();
            }
        });


        //edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sa = itemSelected.split("\n");
                timeEdit = sa[0];
                titleEdit = sa[1];
                descriptionEdit = sa[2];

                if (itemIndex >= 0) {
                    adapter.remove(adapter.getItem(itemIndex));
                    listViewList.remove(itemIndex);
                    adapter.notifyDataSetChanged();
                    itemIndex = -1;
                }

                Intent intent = new Intent(context, CreateAnEvent.class);
                startActivityForResult(intent, 7);


            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(context, "Item Deleted!!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEdit = "title";
                Intent intent = new Intent(context, CreateAnEvent.class);
                startActivityForResult(intent, 7);
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
                Intent intent = new Intent(Calendar.this, Notes.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Settings.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Tasks.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Reminders.class);
                startActivity(intent);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Calendar.this, "Calendar clicked", Toast.LENGTH_SHORT).show();
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


    // key: 12/12/2021  value: { string, sitejo, nkldfnlwn}
    //method to read data from the hashtable and write it to the file
    public void write() throws IOException {
        //creating a new file in the same directory
        file = new File(this.getFilesDir(), "eventsData");
        FileOutputStream fout = new FileOutputStream(file);
        BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fout));

        //reading hashmap and writing to the file
        for (String s : map.keySet()) {
            buffer.write(s);

            //12/12/2021$%string$5string$5string
            //12/13/2021$%string$5string$5string
            //12/14/2021$%string$5string$5string
            //12/15/2021$%string$5string$5string

            for (String ss : map.get(s)) {
                buffer.write("$%");
                buffer.write(ss);

            }
            buffer.newLine();
        }


        buffer.close();
        fout.close();

    }

    //this method reads the file and write the data to the hashtable
    public void read() throws IOException {
        file = new File(this.getFilesDir(), "eventsData");
        FileInputStream fout = new FileInputStream(file);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(fout));

        //if file exists, then read file line by line and add it to the hashtable
        if (file.exists()) {
            //12/12/2021$%string$5string$5string
            //12/13/2021$%string$5string$5string
            //12/14/2021$%string$5string$5string
            //12/15/2021$%string$5string$5string
            String line = buffer.readLine();
            while (line != null) {
                String[] listList = line.split("\\$%");
                //0. 12/12/2021
                //1. string
                //2. string
                //3. string
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 1; i < listList.length; i++) {
                    arrayList.add(listList[i]);
                }

                map.put(listList[0], arrayList);

                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                //key: 12/12/2021  value: {String, string, string}
                line = buffer.readLine();
            }

            buffer.close();
            fout.close();

        } else
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();

    }


    //this overridden method is to get the user's input back from the other activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //checking if the request code matches
        if (requestCode == 7) {
            String t = data.getStringExtra(CreateAnEvent.EXTRA_TITLE);
            String d = data.getStringExtra(CreateAnEvent.EXTRA_DESCRIPTION);
            String time = data.getStringExtra(CreateAnEvent.EXTRA_TIME);

            if (!t.equals("")) {
                String info = time + "     " + t + "     " + d;


                //putting the user input to hashtable
                if (!map.containsKey(date)) {
                    ArrayList<String> infoList = new ArrayList<>();
                    infoList.add(info);
                    //hashmap
                    map.put(date, infoList);
                } else {
                    map.get(date).add(info);
                }


                try {
                    write();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                adapter.add(info.replace("     ", "\n"));
                adapter.notifyDataSetChanged();

            }


        }

    }
}

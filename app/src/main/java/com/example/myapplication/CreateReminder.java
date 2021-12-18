package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateReminder extends AppCompatActivity {

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

    EditText mDisplayDate;
    EditText name;
    EditText time;
    String timeString;
    //    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button button;
    String date;
    int hour;
    int min;
    String amPm;


    public static final String EXTRA_TIME = "com.example.reminders.extra.title";
    public static final String EXTRA_NAME = "com.example.reminders.extra.name";
    public static final String EXTRA_DATE = "com.example.reminders.extra.date";

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
        setContentView(R.layout.activity_create_reminder);

        actionButton = findViewById(R.id.floatingActionButton);
        calendar = findViewById(R.id.calendar);
        notes = findViewById(R.id.notes);
        tasks = findViewById(R.id.tasksButton);
        reminders = findViewById(R.id.remindersButton);
        mDisplayDate = findViewById(R.id.editdate);
        button = findViewById(R.id.button);
        name = findViewById(R.id.editname);
        time = findViewById(R.id.edittime);
        settings = findViewById(R.id.settingsButton);



        rotateopen = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        if(!Reminders.nameEdit.equals("name")){
            name.setText(Reminders.nameEdit);
            time.setText(Reminders.timeEdit);
            mDisplayDate.setText(Reminders.dateEdit);


        }

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);



                DatePickerDialog dialog = new DatePickerDialog(
                        CreateReminder.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;

                                if (month < 10 && dayOfMonth < 10) {
                                    date = "0" + month + "/" + "0" + dayOfMonth + "/" + year;
                                } else if (month < 10) {
                                    date = "0" + month + "/" + dayOfMonth + "/" + year;
                                } else if (dayOfMonth < 10) {
                                    date = month + "/" + "0" + dayOfMonth + "/" + year;
                                } else
                                    date = month + "/" + dayOfMonth + "/" + year;


                                mDisplayDate.setText(date);
                            }
                        },
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                min = calendar.get(java.util.Calendar.MINUTE);


                TimePickerDialog picker = new TimePickerDialog(CreateReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            hourOfDay = hourOfDay - 12;

                        }else
                            amPm = "AM";

                        if(hourOfDay < 10 && minute < 10)
                            timeString =  "0" + hourOfDay + ":" + "0" + minute + " " + amPm;
                        else if (hourOfDay < 10)
                            timeString =  "0" + hourOfDay + ":" + minute + " " + amPm;
                        else if (minute <  10)
                            timeString =  hourOfDay + ":" + "0" + minute + " " + amPm;
                        else
                            timeString =  hourOfDay + ":" + minute + " " + amPm;


                        time.setText(timeString);
                    }
                }, hour, min, true);
                picker.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TIME, time.getText().toString());
                intent.putExtra(EXTRA_NAME, name.getText().toString());
                intent.putExtra(EXTRA_DATE, mDisplayDate.getText().toString());
                setResult(7, intent);
                finish();
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
                Intent intent = new Intent(CreateReminder.this, Notes.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReminder.this, Settings.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReminder.this, Tasks.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateReminder.this, "You are already in Reminders!!", Toast.LENGTH_SHORT).show();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReminder.this, Calendar.class);
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

}
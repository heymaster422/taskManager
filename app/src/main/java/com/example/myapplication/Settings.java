package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity {
    private FloatingActionButton actionButton;
    private FloatingActionButton calendar;
    private FloatingActionButton notes;
    private FloatingActionButton tasks;
    private FloatingActionButton reminders;

    private Animation rotateopen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean clicked;
    private FloatingActionButton settings;

    private Switch switch1;
    private TextView tvBottom;
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
        setContentView(R.layout.activity_settings);

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
                Intent intent = new Intent(Settings.this, Notes.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Tasks.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Reminders.class);
                startActivity(intent);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Calendar.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "You're already in settings!!" , Toast.LENGTH_SHORT).show();
            }
        });

        switch1 = (Switch) findViewById(R.id.switch1);
        tvBottom = (TextView) findViewById(R.id.tvBottom);


        if(sharedPref.loadNightModeState() == true) {
            switch1.setChecked(true);
            tvBottom.setText("Night Mode is activated");
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sharedPref.setNightModeState(true);
                    tvBottom.setText("Night Mode is activated");
                    restartApp();
                } else {
                    sharedPref.setNightModeState(false);
                    tvBottom.setText("Night Mode is deactivated");
                    restartApp();
                }
            }
        });

    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), Settings.class);
        startActivity(i);
        finish();
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
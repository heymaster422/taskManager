package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateAnEvent extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button timePicker;
    int hour, min;
    String time;
    private TextView timeView;
    String amPm;


    public static final String EXTRA_TITLE = "com.example.calendar.extra.title";
    public static final String EXTRA_DESCRIPTION = "com.example.calendar.extra.description";
    public static final String EXTRA_TIME = "com.example.calendar.extra.time";

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
        setContentView(R.layout.activity_main2);

        timePicker = findViewById(R.id.timePicker);
        timeView = findViewById(R.id.viewTime);
        title = findViewById(R.id.name);
        description = findViewById(R.id.descriptionEdit);


        if (!Calendar.timeEdit.equals("time") && !Calendar.descriptionEdit.equals("description") && !Calendar.titleEdit.equals("title")) {
            timeView.setText(Calendar.timeEdit);
            time = Calendar.timeEdit;
            title.setText(Calendar.titleEdit);
            description.setText(Calendar.descriptionEdit);
        }


        //implementing time picker
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                min = calendar.get(java.util.Calendar.MINUTE);

                TimePickerDialog picker = new TimePickerDialog(CreateAnEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            hourOfDay = hourOfDay - 12;

                        }else
                            amPm = "AM";

                        if(hourOfDay < 10 && minute < 10)
                            time =  "0" + hourOfDay + ":" + "0" + minute + " " + amPm;
                        else if (hourOfDay < 10)
                            time =  "0" + hourOfDay + ":" + minute + " " + amPm;
                        else if (minute <  10)
                            time =  hourOfDay + ":" + "0" + minute + " " + amPm;
                        else
                            time =  hourOfDay + ":" + minute + " " + amPm;


                        timeView.setText(time);
                    }
                }, hour, min, true);
                picker.show();

            }
        });


    }

    public void openActivity(View view){
        if(!title.getText().toString().equals("")){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TITLE, title.getText().toString());
            intent.putExtra(EXTRA_DESCRIPTION, description.getText().toString());
            intent.putExtra(EXTRA_TIME, time);

            setResult(7, intent);

            finish();

        }else{
            Toast.makeText(this, "Error: Title cannot be empty!!", Toast.LENGTH_SHORT).show();
        }



    }
}
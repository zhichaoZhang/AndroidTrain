package com.zzc.androidtrain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, CalendarActivity.class);
        return intent;
    }

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (CalendarView)findViewById(R.id.calendar);
//        calendarView.setShowWeekNumber(false);
    }
}

package com.example.kit305.tmqapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* public static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };
*/

public class activity_calendar extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;
    TextView calendarheader;

    tmqAppDatabasehandler database;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        database = new tmqAppDatabasehandler(this);

        calendarheader = (TextView) findViewById(R.id.Calendar_Date);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(activity_calendar.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(activity_calendar.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(activity_calendar.this, newTask.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(activity_calendar.this, activity_calendar.class);
                        startActivity(calendarIntent);
                        return true;
                }

                return false;
            }
        });

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.menu_open, R.string.menu_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendarheader.setText(compactCalendar.getFirstDayOfCurrentMonth().toString());

        /*Get events from database*/
        List<Event> calendarEvents = database.getAllEventDates();

        /*Loop over list and add events*/
        for(Event event : calendarEvents){
            Log.d("Calendar_Activity","Adding Event");
            compactCalendar.addEvent(event, false);
        }


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d("CalendarView", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("CalendarView", "Month was scrolled to: " + firstDayOfNewMonth);

                calendarheader.setText(firstDayOfNewMonth.toString());
            }
        });
    }

    private void addCalendar(){


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

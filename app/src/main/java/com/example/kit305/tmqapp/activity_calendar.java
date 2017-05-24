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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* PLEASE NOTE: The activity Calendar we used was developed by SundeepK on Gihub Copyright (c) [2017] [Sundeepk] https://github.com/SundeepK/CompactCalendarView */

public class activity_calendar extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;
    TextView calendarheader;

    /*Load up the database to find events*/
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

        /*Once we set the conext we set up the database handler*/
        database = new tmqAppDatabasehandler(this);

        /*This is the date header for the current calendar so we can see the month and year we are in*/
        calendarheader = (TextView) findViewById(R.id.Calendar_Date);

        /*These the navigation draw controllers for navigating through the app. Selected top right and extends from the left*/
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


        final DateFormat dfDisplay = new SimpleDateFormat("MMM yyyy");/*Date format we use for the top of the header*/
        final DateFormat dfSend = new SimpleDateFormat("dd/MM/yyyy"); /*Date format we use for sending a date string to the task list activity*/

        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view); /*We create a new calendar vieww object so we can add events and access dates and other methods*/

        Date intialheader_date = compactCalendar.getFirstDayOfCurrentMonth(); /*We get the staarting date of the currently display month*/

        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendarheader.setText(dfDisplay.format(intialheader_date)); /*We use the date we grabbed from the calendar to set the date header above the calendar and chop of the day for easier reading*/

        /*Get events from database*/
        List<Event> calendarEvents = database.getAllEventDates(); /*These are the events we are populating the calendar with. We retrieve the list from the database handler*/

        /*Loop over event list and add events*/
        for(Event event : calendarEvents){
            Log.d("Calendar_Activity","Adding Event");
            compactCalendar.addEvent(event, false);
        }

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            /*Listener attached to individual cells. Clicking one loads up the task list for that day*/
            public void onDayClick(Date dateClicked) {


                List<Event> events = compactCalendar.getEvents(dateClicked); /*Loads events for the selcted day so we can log them to the console for debuggin*/
                Log.d("CalendarView", "Day was clicked: " + dateClicked + " with events " + events);

                /*We are redirected to the task list for the selected day*/
                Intent task_list_intent = new Intent(activity_calendar.this,taskList.class);
                task_list_intent.putExtra("request","date");/*Since we can filter by date or priorty, we need to tell the task list wwe are searching by date so it gets the right values from the intent*/
                task_list_intent.putExtra("taskDate",dfSend.format(dateClicked)); /*The date in string form we will load values from*/

                Log.d("Calendar_Activity",dfSend.format(dateClicked));

                startActivity(task_list_intent);
            }
            /*Listener attached to swipping the calendar left or right cells. sipping only changes the date*/
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("CalendarView", "Month was scrolled to: " + firstDayOfNewMonth);
                Date scrollDate = firstDayOfNewMonth;

                calendarheader.setText(dfDisplay.format(scrollDate));/*Change header date to the month we swipped to*/
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.kit305.tmqapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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
                        Intent addTaskIntent = new Intent(activity_calendar.this, new_task.class);
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

        final CompactCalendarView compactCalendar = (CompactCalendarView)findViewById(R.id.compactcalendar_view);

        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        Event ev1 = new Event(Color.RED,1495352507L,"Testing Event");
        compactCalendar.addEvent(ev1);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d("CalendarView", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("CalendarView", "Month was scrolled to: " + firstDayOfNewMonth);
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

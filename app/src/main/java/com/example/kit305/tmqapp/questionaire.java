package com.example.kit305.tmqapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class questionaire extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(questionaire.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(questionaire.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(questionaire.this, new_task.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(questionaire.this, activity_calendar.class);
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

        initialiseSpinners();
    }

    public void initialiseSpinners() {
        Spinner question1 = (Spinner) findViewById(R.id.spinner1);
        Spinner question2 = (Spinner) findViewById(R.id.spinner2);
        Spinner question3 = (Spinner) findViewById(R.id.spinner3);
        Spinner question4 = (Spinner) findViewById(R.id.spinner4);
        Spinner question5 = (Spinner) findViewById(R.id.spinner5);
        Spinner question6 = (Spinner) findViewById(R.id.spinner6);
        Spinner question7 = (Spinner) findViewById(R.id.spinner7);
        Spinner question8 = (Spinner) findViewById(R.id.spinner8);
        Spinner question9 = (Spinner) findViewById(R.id.spinner9);
        Spinner question10 = (Spinner) findViewById(R.id.spinner10);
        Spinner question11 = (Spinner) findViewById(R.id.spinner11);
        Spinner question12 = (Spinner) findViewById(R.id.spinner12);
        Spinner question13 = (Spinner) findViewById(R.id.spinner13);
        Spinner question14 = (Spinner) findViewById(R.id.spinner14);
        Spinner question15 = (Spinner) findViewById(R.id.spinner15);
        Spinner question16 = (Spinner) findViewById(R.id.spinner16);
        Spinner question17 = (Spinner) findViewById(R.id.spinner17);
        Spinner question18 = (Spinner) findViewById(R.id.spinner18);

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.questionaire_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        question1.setAdapter(adapter);
        question2.setAdapter(adapter);
        question3.setAdapter(adapter);
        question4.setAdapter(adapter);
        question5.setAdapter(adapter);
        question6.setAdapter(adapter);
        question7.setAdapter(adapter);
        question8.setAdapter(adapter);
        question9.setAdapter(adapter);
        question10.setAdapter(adapter);
        question11.setAdapter(adapter);
        question12.setAdapter(adapter);
        question13.setAdapter(adapter);
        question14.setAdapter(adapter);
        question15.setAdapter(adapter);
        question16.setAdapter(adapter);
        question17.setAdapter(adapter);
        question18.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

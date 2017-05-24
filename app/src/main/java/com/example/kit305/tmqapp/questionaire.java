package com.example.kit305.tmqapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class questionaire extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    /*We need a database object to store the TMQ score*/
    tmqAppDatabasehandler database;

    /*All the spinner widgets we need to setup for the TMQ*/
    Spinner question1;
    Spinner question2;
    Spinner question3;
    Spinner question4;
    Spinner question5;
    Spinner question6;
    Spinner question7;
    Spinner question8;
    Spinner question9;
    Spinner question10;
    Spinner question11;
    Spinner question12;
    Spinner question13;
    Spinner question14;
    Spinner question15;
    Spinner question16;
    Spinner question17;
    Spinner question18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        database  = new tmqAppDatabasehandler(this.getApplicationContext());

        /*These the navigation draw controllers for navigating through the app. Selected top right and extends from the left*/
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
                        Intent addTaskIntent = new Intent(questionaire.this, newTask.class);
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

        /*Spinners are initialised with their default values 1-5*/
        initialiseSpinners();
    }

    public void initialiseSpinners() {
        question1 = (Spinner) findViewById(R.id.spinner1);
        question2 = (Spinner) findViewById(R.id.spinner2);
        question3 = (Spinner) findViewById(R.id.spinner3);
        question4 = (Spinner) findViewById(R.id.spinner4);
        question5 = (Spinner) findViewById(R.id.spinner5);
        question6 = (Spinner) findViewById(R.id.spinner6);
        question7 = (Spinner) findViewById(R.id.spinner7);
        question8 = (Spinner) findViewById(R.id.spinner8);
        question9 = (Spinner) findViewById(R.id.spinner9);
        question10 = (Spinner) findViewById(R.id.spinner10);
        question11 = (Spinner) findViewById(R.id.spinner11);
        question12 = (Spinner) findViewById(R.id.spinner12);
        question13 = (Spinner) findViewById(R.id.spinner13);
        question14 = (Spinner) findViewById(R.id.spinner14);
        question15 = (Spinner) findViewById(R.id.spinner15);
        question16 = (Spinner) findViewById(R.id.spinner16);
        question17 = (Spinner) findViewById(R.id.spinner17);
        question18 = (Spinner) findViewById(R.id.spinner18);

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

    public void calculateScore(View view) {

        /*We retrieve all the values from the spinners and convert to floats for calculations*/
        float value1 = Float.parseFloat(question1.getSelectedItem().toString());
        float value2 = Float.parseFloat(question2.getSelectedItem().toString());
        float value3 = Float.parseFloat(question3.getSelectedItem().toString());
        float value4 = Float.parseFloat(question4.getSelectedItem().toString());
        float value5 = Float.parseFloat(question5.getSelectedItem().toString());
        float value6 = Float.parseFloat(question6.getSelectedItem().toString());
        float value7 = Float.parseFloat(question7.getSelectedItem().toString());
        float value8 = Float.parseFloat(question8.getSelectedItem().toString());
        float value9 = Float.parseFloat(question9.getSelectedItem().toString());
        float value10 = Float.parseFloat(question10.getSelectedItem().toString());
        float value11 = Float.parseFloat(question11.getSelectedItem().toString());
        float value12 = Float.parseFloat(question12.getSelectedItem().toString());
        float value13 = Float.parseFloat(question13.getSelectedItem().toString());
        float value14 = Float.parseFloat(question14.getSelectedItem().toString());
        float value15 = Float.parseFloat(question15.getSelectedItem().toString());
        float value16 = Float.parseFloat(question16.getSelectedItem().toString());
        float value17 = Float.parseFloat(question17.getSelectedItem().toString());
        float value18 = Float.parseFloat(question18.getSelectedItem().toString());

        /*We calculate the sums into their categories*/
        int categoryAScore = (int)(((value1 + value2 + value3 + value4 + value5 + value6 + value7) / 35f) * 100f);
        int categoryBScore = (int)(((value8 + value9 + value10 + value11 + value12 + value13) / 30f) * 100f);
        int categoryCScore = (int)(((value14 + value15 + value16 + value17 + value18) / 25f) * 100f);
        int finalScore = (int)(((categoryAScore + categoryBScore + categoryCScore) / 300f) * 100f);

        Log.d("Category A", Float.toString(categoryAScore));
        Log.d("Category B", Float.toString(categoryBScore));
        Log.d("Category C", Float.toString(categoryCScore));
        Log.d("TMQ Score", Float.toString(finalScore));

        /*We save the final scores to the database*/
        database.updateTMQScore(Integer.toString(finalScore));

        /*We notfiy the user that their score has been updated*/
        Toast toast = Toast.makeText(getApplicationContext(),"Score has been updated!",Toast.LENGTH_SHORT);
        toast.show();

        jumpToResults(categoryAScore, categoryBScore, categoryCScore, finalScore);
    }

    /*This sets all the values into the intent so we can navigate to the questionaire results intent*/
    public void jumpToResults(int scoreA, int scoreB, int scoreC, int scoreD) {
        Intent intent = new Intent(questionaire.this, questionnaireResults.class);
        intent.putExtra("categoryA", Integer.toString(scoreA));
        intent.putExtra("categoryB", Integer.toString(scoreB));
        intent.putExtra("categoryC", Integer.toString(scoreC));
        intent.putExtra("finalScore", Integer.toString(scoreD));
        startActivity(intent);
    }
}

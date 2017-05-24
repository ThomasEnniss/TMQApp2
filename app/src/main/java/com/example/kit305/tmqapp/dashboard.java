package com.example.kit305.tmqapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dashboard extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    /*Once we set the conext we set up the database handler*/
    tmqAppDatabasehandler database;


    int taskCount[]; /*Array for pie chart which diplays how many tasks we have in each of the 4 categories*/
    String taskClass[] = { "U-I", "U-NI", "NU-I", "NU-NI" }; /*Array for pie chart which diplays 1 of the category titles for a relevant chunk*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        /*These the navigation draw controllers for navigating through the app. Selected top right and extends from the left*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(dashboard.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(dashboard.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(dashboard.this, newTask.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(dashboard.this, activity_calendar.class);
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

        /*We instatiate the datebase object*/
        database  = new tmqAppDatabasehandler(this.getApplicationContext());

        /*We load the array of counted tasks from the database*/
        taskCount = database.loadChartValues();
        Log.d("Dashboard", Arrays.toString(taskCount));
        /*We initialize pie chart with it's values*/
        setupPieChart();
        TextView scoreLabel = (TextView)findViewById(R.id.label4);

        /*Display the TMQ score wwith the value retrieved from that database*/
        scoreLabel.setText("Your Score: " + database.getTMQScore() + "% \n Class average: 59%");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPieChart() {

        /*Pie chunks for pie chart*/
        List<PieEntry> pieEntries = new ArrayList<>();

        /*We loop through the array and add pie chunks as needed with coloured styling to match the importance and urgency*/
        for (int i = 0; i < taskCount.length; i++) {
            if (taskCount[i] == 0)
                /*Value is zero and color is not inserted. Does not showw up in pie chart*/
                pieEntries.add(new PieEntry(taskCount[i]));
            else
                pieEntries.add(new PieEntry(taskCount[i], taskClass[i]));
        }

        /*We setup the pie chart using the arrays and data we used before*/

        PieDataSet dataSet = new PieDataSet(pieEntries, "Tasks");
        PieData chartData = new PieData(dataSet);
        PieChart chart = (PieChart) findViewById(R.id.pieChart);

        /*This listener is for allowing users to click chunks and navigate to task lists based on priority*/
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Intent listIntent = new Intent(dashboard.this, taskList.class);

                /*We start intents based on priority not date*/
                listIntent.putExtra("request", "priority");
                if (pe.getLabel() == "U-I") {
                    listIntent.putExtra("priority", "Urgent - Important");
                    listIntent.putExtra("urgency", "true");
                    listIntent.putExtra("importance", "true");
                    startActivity(listIntent);
                }
                else if (pe.getLabel() == "U-NI") {
                    listIntent.putExtra("priority", "Urgent - Not Important");
                    listIntent.putExtra("urgency", "true");
                    listIntent.putExtra("importance", "false");
                    startActivity(listIntent);
                }
                else if (pe.getLabel() == "NU-I") {
                    listIntent.putExtra("priority", "Not Urgent - Important");
                    listIntent.putExtra("urgency", "false");
                    listIntent.putExtra("importance", "true");
                    startActivity(listIntent);
                }
                else {
                    listIntent.putExtra("priority", "Not Urgent - Not Important");
                    listIntent.putExtra("urgency", "false");
                    listIntent.putExtra("importance", "false");
                    startActivity(listIntent);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /*Setting up other chart formating and data including the center text*/
        Description description = new Description();
        Legend chartLegend = chart.getLegend();

        chart.setData(chartData);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chartData.setDrawValues(false);

        description.setText("");
        chart.setDescription(description);
        chartLegend.setEnabled(false);

        chart.setCenterText(Integer.toString(tallyTaskCount()));
        chart.setCenterTextSize(48);
        chart.setCenterTextColor(Color.WHITE);
        chart.setHoleRadius(55f);
        chart.setTransparentCircleRadius(60f);
        chart.setTransparentCircleColor(Color.BLACK);
        chart.setHoleColor(R.color.colorPrimaryDark);

        chart.animateY(1000);
        chart.invalidate();
    }
    /*Returns a sum of all the tasks in the tasks array*/
    private int tallyTaskCount() {
        int totalTasks = 0;
        for (int i = 0; i < taskCount.length; i++) {
            totalTasks = totalTasks + taskCount[i];
        }

        return totalTasks;
    }
}

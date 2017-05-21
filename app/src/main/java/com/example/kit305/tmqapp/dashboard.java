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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dashboard extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    tmqAppDatabasehandler database;

    int taskCount[];
    String taskClass[] = { "U-I", "NU-I", "U-NI", "NU-NI" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

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
                        Intent calendarIntent = new Intent(dashboard.this, questionaire.class);
                        startActivity(calendarIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(dashboard.this, new_task.class);
                        startActivity(addTaskIntent);
                        return true;
                }

                return false;
            }
        });

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.menu_open, R.string.menu_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database  = new tmqAppDatabasehandler(this.getApplicationContext());
        taskCount = database.loadChartValues();
        Log.d("Dashboard", Arrays.toString(taskCount));
        setupPieChart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < taskCount.length; i++) {
            pieEntries.add(new PieEntry(taskCount[i], taskClass[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Tasks");
        PieData chartData = new PieData(dataSet);
        PieChart chart = (PieChart) findViewById(R.id.pieChart);
        Description description = new Description();
        Legend chartLegend = chart.getLegend();

        chart.setData(chartData);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueFormatter(new ChartValueFormatter());
        chartData.setDrawValues(false);

        description.setText("");
        chart.setDescription(description);
        chartLegend.setEnabled(false);

        chart.setCenterText(Integer.toString(tallyTaskCount()));
        chart.setCenterTextSize(48);
        chart.setCenterTextColor(Color.WHITE);
        chart.setHoleRadius(55f);
        chart.setTransparentCircleRadius(60f);
        chart.setHoleColor(R.color.colorPrimaryDark);

        chart.animateY(1000);
        chart.invalidate();
    }

    private int tallyTaskCount() {
        int totalTasks = 0;
        for (int i = 0; i < taskCount.length; i++) {
            totalTasks = totalTasks + taskCount[i];
        }

        return totalTasks;
    }
}

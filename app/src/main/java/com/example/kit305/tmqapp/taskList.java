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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class taskList extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    String request;
    String dateToLoadtasks;
    String urgency;
    String importance;
    String priority;


    tmqAppDatabasehandler database;

    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        database = new tmqAppDatabasehandler(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(taskList.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(taskList.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(taskList.this, newTask.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(taskList.this, activity_calendar.class);
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

        TextView taskListDateHeader = (TextView)findViewById(R.id.page_title);
        Intent intent = getIntent();
        request = intent.getStringExtra("request");



        if (request.equals("date")) {
            dateToLoadtasks = intent.getStringExtra("taskDate");
            taskList = database.loadTasksByDate(dateToLoadtasks);
            taskListDateHeader.setText(dateToLoadtasks);
        }
        else {
            priority = intent.getStringExtra("priority");
            urgency = intent.getStringExtra("urgency");
            importance = intent.getStringExtra("importance");

            Log.d("TaskList",urgency + " " + importance);

            taskList = database.loadTaskByPriority(urgency, importance);
            taskListDateHeader.setText(priority);
        }

        createTaskList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createTaskList() {
        //** This code is responsible for creating the individual list items
        //   and applying all required XML attributes/styles.
        LinearLayout listLayout = (LinearLayout) findViewById(R.id.activity_task_list);
        Log.d("taskList","Checking Array Size" + taskList.size());

        if(taskList.size()>0){

            for (int i = 0, j = 0, k = 0; i < taskList.size(); i++, j++, k++) {
                Log.d("Task item loop", Integer.toString(i));

                TextView taskDetails = new TextView(taskList.this);
                View divider = new View(taskList.this);

                //** Attributes for View divider
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, convertToDP(1)
                );
                divider.setBackgroundColor(Color.WHITE);

                //** Attributes for RadioButton
                LinearLayout.LayoutParams radioButtonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, convertToDP(90)
                );

                //** Attributes for TextView
                LinearLayout.LayoutParams taskDetailParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, convertToDP(90)
                );
                taskDetails.setPadding(convertToDP(15), convertToDP(10), 0, convertToDP(10));
                taskDetails.setText(
                        taskList.get(i).getName() + "\n" + taskList.get(i).getCode() + " - " + taskList.get(i).getDate()
                                + "\n\n" + taskList.get(i).getComment()
                );
                //** Setting appropriate text colors according to task urgency/importance
                if (taskList.get(i).getUrgent().equals("true") && taskList.get(i).getImportant().equals("true"))
                    taskDetails.setTextColor(Color.rgb(193, 37, 82));
                else if (taskList.get(i).getUrgent().equals("true") && taskList.get(i).getImportant().equals("false"))
                    taskDetails.setTextColor(Color.rgb(255, 102, 0));
                else if (taskList.get(i).getUrgent().equals("false") && taskList.get(i).getImportant().equals("true"))
                    taskDetails.setTextColor(Color.rgb(245, 199, 0));
                else
                    taskDetails.setTextColor(Color.rgb(106, 150, 31));

                taskDetails.setClickable(true);
                taskDetails.setId(taskList.get(i).getID());
                taskDetails.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Integer taskToLoad = v.getId();

                        Intent editIntent = new Intent(taskList.this, editTask.class);
                        editIntent.putExtra("taskIdtoLoad", Integer.toString(taskToLoad));
                        editIntent.putExtra("taskDate", dateToLoadtasks);
                        Log.d("Task_List",Integer.toString(taskToLoad));
                        startActivity(editIntent);
                    }
                });

                //** Add all views into the layout with specified LinearLayout parameters
                listLayout.addView(divider, dividerParams);
                listLayout.addView(taskDetails, taskDetailParams);
            }

        }else{
            Log.d("Task List","Empty");
            TextView taskDetails = new TextView(taskList.this);
            //** Attributes for TextView
            LinearLayout.LayoutParams taskDetailParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, convertToDP(90)
            );
            taskDetails.setPadding(convertToDP(15), convertToDP(10), 0, convertToDP(10));
            taskDetails.setText("No Tasks To Show!");
            taskDetails.setGravity(Gravity.CENTER_HORIZONTAL);
            taskDetails.setTextSize(24);
            taskDetails.setClickable(false);
            listLayout.addView(taskDetails, taskDetailParams);

        }

    }

    public int convertToDP(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}

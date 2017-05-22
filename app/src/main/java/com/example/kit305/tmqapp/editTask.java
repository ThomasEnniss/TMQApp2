package com.example.kit305.tmqapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class editTask extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    private Intent intent;
    private tmqAppDatabasehandler database;
    private Integer taskId;

    /*Get the indiviual form widgets so we can retrieve their values*/
    private EditText task_name_entry_field;
    private EditText unit_code_entry_field;
    private EditText due_date_entry_field;
    private Switch urgent_switch;
    private Switch important_switch;
    private EditText comments_section;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        database = new tmqAppDatabasehandler(this);
        intent = getIntent();
        taskId = Integer.parseInt(intent.getStringExtra("taskIdtoLoad"));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(editTask.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(editTask.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(editTask.this, editTask.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(editTask.this, activity_calendar.class);
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

        task_name_entry_field = (EditText)findViewById(R.id.nameText);
        unit_code_entry_field = (EditText)findViewById(R.id.codeText);
        due_date_entry_field = (EditText)findViewById(R.id.dateText);
        urgent_switch = (Switch)findViewById(R.id.urgentSwitch);
        important_switch = (Switch)findViewById(R.id.importantSwitch);
        comments_section = (EditText)findViewById(R.id.commentText);

        prefillForm();

        final tmqAppDatabasehandler database = new tmqAppDatabasehandler(this);

        Button updateButton = (Button)findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*We retrieve the values and get them ready to place in an array*/

                String task_name = task_name_entry_field.getText().toString();
                String unit_code = unit_code_entry_field.getText().toString();
                String due_date = due_date_entry_field.getText().toString();
                String urgent = (urgent_switch.isChecked() ? "true" : "false");
                String important = (important_switch.isChecked() ? "true" : "false");
                String comments = comments_section.getText().toString();

                /*Place values into the array for saving*/
                String[] taskValuesToSave = {task_name,unit_code,due_date,urgent,important,comments};

                /*Save Values into the database*/
                database.updateTask(taskValuesToSave,taskId);

                Intent listIntent = new Intent(editTask.this, activity_calendar.class);
                startActivity(listIntent);
            }
        });

        Button deleteButton = (Button)findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.deleteTask(taskId);

                Intent listIntent = new Intent(editTask.this, activity_calendar.class);
                startActivity(listIntent);
            }
        });


    }

    private void prefillForm(){
        Task taskToLoad = database.loadTaskByID(taskId);

        task_name_entry_field.setText(taskToLoad.getName());
        unit_code_entry_field.setText(taskToLoad.getCode());
        due_date_entry_field.setText(taskToLoad.dueDate);
        urgent_switch.setChecked(Boolean.parseBoolean(taskToLoad.getUrgent()));
        important_switch.setChecked(Boolean.parseBoolean(taskToLoad.getImportant()));

        comments_section.setText(taskToLoad.getComment());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
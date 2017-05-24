package com.example.kit305.tmqapp;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.util.Date;


public class newTask extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        /*These the navigation draw controllers for navigating through the app. Selected top right and extends from the left*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(newTask.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(newTask.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(newTask.this, newTask.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(newTask.this, activity_calendar.class);
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

        /*Once we set the conext we set up the database handler*/
        final tmqAppDatabasehandler database = new tmqAppDatabasehandler(this);

        /*This is the save button of course and we use it to read t he values from the form and save them if they are filled in properly*/
        Button saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Get the indiviual form widgets so we can retrieve their values*/
                EditText task_name_entry_field = (EditText)findViewById(R.id.nameText);
                EditText unit_code_entry_field = (EditText)findViewById(R.id.codeText);
                DatePicker due_date_entry_field = (DatePicker)findViewById(R.id.selectedDate);
                Switch urgent_switch = (Switch)findViewById(R.id.urgentSwitch);
                Switch important_switch = (Switch)findViewById(R.id.importantSwitch);
                EditText comments_section = (EditText)findViewById(R.id.commentText);

                /*We have to get the individual values from the date picker and peice them together into a string.
                 *This ensures dates are always in the correct format (dd/mm/yyyy) for the database and queries.
                 * The datepicker is annoying since it is not in dd mmm yyyy format but I researched and iot's hardcoded unless you change locale. Considered trivial since it breaks nothing.*/
                int year = due_date_entry_field.getYear();
                int month = due_date_entry_field.getMonth();
                int day = due_date_entry_field.getDayOfMonth();
                final DateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

                /*The variable we change to true if there are any entry errors. Stops the saving of illegal time entries (Ilegal time entries have no task name or unit code)*/
                boolean entryErrors= false;

                /*We retrieve the values from the widgets and get them ready to place in an array*/
                String task_name = task_name_entry_field.getText().toString();
                String unit_code = unit_code_entry_field.getText().toString();

                /*We finally set the date string*/
                String due_date = sdf.format(new Date(year-1900,month,day));
                Log.d("New Task","This is the date" + year);
                Log.d("New Task",due_date);
                String urgent = (urgent_switch.isChecked() ? "true" : "false");
                String important = (important_switch.isChecked() ? "true" : "false");
                String comments = comments_section.getText().toString();


                /*If user leaves task name blank, it is recorded as an error and the user is notified*/
                if(task_name.equals("")){
                    entryErrors=true;
                    Toast toast = Toast.makeText(getApplicationContext(),"Enter Task Name!",Toast.LENGTH_SHORT);
                    toast.show();
                }
                /*If user leaves unit code blank, it is recorded as an error and the user is notified*/
                if(unit_code.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Enter Unit Code!",Toast.LENGTH_SHORT);
                    toast.show();
                    entryErrors=true;
                }

                /*If there are errors we don't save the task and notify the user*/
                if(!entryErrors){
                    /*Place values into the array for saving*/
                    String[] taskValuesToSave = {task_name,unit_code,due_date,urgent,important,comments};

                    /*Save Values into the database*/
                    database.insertTask(taskValuesToSave);
                    /*Notify the user that the task has been saved*/
                    Toast toast = Toast.makeText(getApplicationContext(),"New Task Saved!",Toast.LENGTH_SHORT);
                    toast.show();
                    /*We head back to the dashboard and see the new task nicely nestled into the pie chart for the users viewing pleasure*/
                    Intent dashboardIntent = new Intent(newTask.this, dashboard.class);
                    startActivity(dashboardIntent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Could not save entry!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        /*Cancel buttion simply just heads us back to the dashboard*/
        Button cancelButton = (Button)findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Notfiy the user that they have cancelled the task*/
                Toast toast = Toast.makeText(getApplicationContext(),"Task Entry Cancelled!",Toast.LENGTH_SHORT);
                toast.show();

                Intent dashboardIntent = new Intent(newTask.this, dashboard.class);
                startActivity(dashboardIntent);
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

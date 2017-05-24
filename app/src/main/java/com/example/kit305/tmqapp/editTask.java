package com.example.kit305.tmqapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class editTask extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    /*Database object for querying database**/
    private tmqAppDatabasehandler database;

    private String dateToLoadtasks; /*This is so we can go back to task list which is expecting a date to load a list. I think this was why it was crashing*/

    /*Get the indiviual form widgets so we can set and retrieve their values*/
    private EditText task_name_entry_field;
    private EditText unit_code_entry_field;
    private DatePicker due_date_entry_field;
    private Switch urgent_switch;
    private Switch important_switch;
    private EditText comments_section;


    private Integer taskId;/*The ID of the task we are going to load. This is sent via intent*/

    /*These are all the variables sent via the intent. Some may not be used, but they are stored here so we can send back the vairables to the task list.
    * If we don't send back values, it will crash as it expects values from the intent for loading it's tasks and database queries*/
    private String request;
    private String urgency;
    private String importance;
    private String priority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        /*Once we set the conext we set up the database handler*/
        database = new tmqAppDatabasehandler(this);

        /*We load the intent so we can load and prefill the form with the task we loaded*/
        Intent intent = getIntent();
        taskId = Integer.parseInt(intent.getStringExtra("taskIdtoLoad"));

        request = intent.getStringExtra("request");

        /*These the navigation draw controllers for navigating through the app. Selected top right and extends from the left*/
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

        /*WWe grab and store the widgets for future access*/
        task_name_entry_field = (EditText)findViewById(R.id.nameText);
        unit_code_entry_field = (EditText)findViewById(R.id.codeText);
        due_date_entry_field = (DatePicker)findViewById(R.id.selectedEditDate);
        urgent_switch = (Switch)findViewById(R.id.urgentSwitch);
        important_switch = (Switch)findViewById(R.id.importantSwitch);
        comments_section = (EditText)findViewById(R.id.commentText);

        /*We get the rest of the values from the inent based. If the task list was generated by date or priority, we need to copy those values through and send them back to avoid a crash*/
        if (request.equals("date")) {
            dateToLoadtasks = intent.getStringExtra("taskDate");
        }
        else {
            priority = intent.getStringExtra("priority");
            urgency = intent.getStringExtra("urgency");
            importance = intent.getStringExtra("importance");

        }
        /*Load the task and use its values to fill the form*/
        prefillForm();
        /*We setup the database object*/
        final tmqAppDatabasehandler database = new tmqAppDatabasehandler(this);

        Button updateButton = (Button)findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*We retrieve the values and get them ready to place in an array*/

                String task_name = task_name_entry_field.getText().toString();
                String unit_code = unit_code_entry_field.getText().toString();
                String urgent = (urgent_switch.isChecked() ? "true" : "false");
                String important = (important_switch.isChecked() ? "true" : "false");
                String comments = comments_section.getText().toString();

                /*We get the date from the date picker and then convert them to a string*/
                int year = due_date_entry_field.getYear();
                int month = due_date_entry_field.getMonth();
                int day = due_date_entry_field.getDayOfMonth();
                final DateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                String due_date = sdf.format(new Date(year-1900,month,day));

                /*Place values into the array for saving*/
                String[] taskValuesToSave = {task_name,unit_code,due_date,urgent,important,comments};

                /*Update Values in the database*/
                database.updateTask(taskValuesToSave,taskId);

                /*Inform user they updated their task*/
                Toast toast = Toast.makeText(getApplicationContext(),"Task Updated!",Toast.LENGTH_SHORT);
                toast.show();

                /*We need to send values back to task list based on whether task list loaded using dates or urgency / importance.
                 We send the values back (Pass the parcel) to the task list so it can relaod itself properly and not crash */
                if (request.equals("date")) {
                    Intent listIntent = new Intent(editTask.this, taskList.class);
                    listIntent.putExtra("request", request);
                    listIntent.putExtra("taskDate", dateToLoadtasks);
                    startActivity(listIntent);
                }
                else {
                    Intent listIntent2 = new Intent(editTask.this, taskList.class);
                    listIntent2.putExtra("request", request);
                    listIntent2.putExtra("priority", priority);
                    listIntent2.putExtra("urgency", urgency);
                    listIntent2.putExtra("importance",importance );

                    startActivity(listIntent2);
                }
            }
        });

        Button deleteButton = (Button)findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*We delete the task from the database*/
                database.deleteTask(taskId);

                /*We notfiy users of the deletion*/
                Toast toast = Toast.makeText(getApplicationContext(),"Task Deleted!",Toast.LENGTH_SHORT);
                toast.show();
                /*We need to send values back to task list based on whether task list loaded using dates or urgency / importance.
                 We send the values back (Pass the parcel) to the task list so it can relaod itself properly and not crash */
                if (request.equals("date")) {
                    Intent listIntent = new Intent(editTask.this, taskList.class);
                    listIntent.putExtra("request", request);
                    listIntent.putExtra("taskDate", dateToLoadtasks);
                    startActivity(listIntent);
                }
                else {
                    Intent listIntent2 = new Intent(editTask.this, taskList.class);
                    listIntent2.putExtra("request", request);
                    listIntent2.putExtra("priority", priority);
                    listIntent2.putExtra("urgency", urgency);
                    listIntent2.putExtra("importance",importance );

                    startActivity(listIntent2);
                }
            }
        });


    }

   private void prefillForm(){

       /*We load the task from the database*/
        Task taskToLoad = database.loadTaskByID(taskId);

        /*We assign the parts of the task to the relevant fields in the edit task form*/
        task_name_entry_field.setText(taskToLoad.getName());
        unit_code_entry_field.setText(taskToLoad.getCode());
        urgent_switch.setChecked(Boolean.parseBoolean(taskToLoad.getUrgent()));
        important_switch.setChecked(Boolean.parseBoolean(taskToLoad.getImportant()));
        comments_section.setText(taskToLoad.getComment());

        /*We split the date which is in string form into int parts representing year. month, and day. we then apply them to the datepicker*/
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToSet = null;
        Log.d("Edit Task",taskToLoad.dueDate);
        try {
            dateToSet = sdf.parse(taskToLoad.dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*Set calendar up to retrieve the individual parts. Suggestion from stack overflow*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToSet);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

       /*We update the datepicker with the date*/
        due_date_entry_field.updateDate(year,month,day);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

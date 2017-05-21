package com.example.kit305.tmqapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class new_task extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(new_task.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent calendarIntent = new Intent(new_task.this, questionaire.class);
                        startActivity(calendarIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(new_task.this, new_task.class);
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





        final tmqAppDatabasehandler database = new tmqAppDatabasehandler(this.getApplicationContext());

        Button saveButton = (Button)findViewById(R.id.button3);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Get the indiviual form widgets so we can retrieve their values*/
                EditText task_name_entry_field = (EditText)findViewById(R.id.editText3);
                EditText unit_code_entry_field = (EditText)findViewById(R.id.editText4);
                EditText due_date_entry_field = (EditText)findViewById(R.id.editText5);

                Switch urgent_switch = (Switch)findViewById(R.id.switch1);
                Switch important_switch = (Switch)findViewById(R.id.switch2);

                EditText comments_section = (EditText)findViewById(R.id.editText);



                /*We retrieve the values and get them ready to place in an array*/

                String task_name = task_name_entry_field.getText().toString();
                String unit_code = unit_code_entry_field.getText().toString();
                String due_date = due_date_entry_field.getText().toString();
                String urgent = (urgent_switch.isChecked() ? "true" : "false");
                String important = (important_switch.isChecked() ? "true" : "false");
                String comments = comments_section.getText().toString();

                /*Log the values for error checking*/
                Log.d("New_Task",task_name);
                Log.d("New_Task",unit_code);
                Log.d("New_Task",due_date);
                Log.d("New_Task",urgent);
                Log.d("New_Task",important);
                Log.d("New_Task",comments);

                /*Place values into the array for saving*/
                String[] taskValuesToSave = {task_name,unit_code,due_date,urgent,important,comments};

                /*Save Values into the database*/
                database.insertTask(taskValuesToSave);
            }
        });
    }


}

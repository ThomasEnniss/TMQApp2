package com.example.kit305.tmqapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class questionnaireResults extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_results);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigationView);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent newTaskIntent = new Intent(questionnaireResults.this, dashboard.class);
                        startActivity(newTaskIntent);
                        return true;
                    case R.id.questionnaire:
                        Intent questionaireIntent = new Intent(questionnaireResults.this, questionaire.class);
                        startActivity(questionaireIntent);
                        return true;
                    case R.id.add_task:
                        Intent addTaskIntent = new Intent(questionnaireResults.this, new_task.class);
                        startActivity(addTaskIntent);
                        return true;
                    case R.id.task_calendar:
                        Intent calendarIntent = new Intent(questionnaireResults.this, activity_calendar.class);
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

        TextView categoryAScore = (TextView) findViewById(R.id.category_A_score);
        TextView categoryBScore = (TextView) findViewById(R.id.category_B_score);
        TextView categoryCScore = (TextView) findViewById(R.id.category_C_score);
        TextView finalScore = (TextView) findViewById(R.id.final_score);

        Intent intent = getIntent();
        String valueA = intent.getStringExtra("categoryA");
        String valueB = intent.getStringExtra("categoryB");
        String valueC = intent.getStringExtra("categoryC");
        String valueD = intent.getStringExtra("finalScore");

        categoryAScore.setText(valueA);
        categoryBScore.setText(valueB);
        categoryCScore.setText(valueC);
        finalScore.setText(valueD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void jumpToDashboard(View view) {
        Intent intent = new Intent(questionnaireResults.this, dashboard.class);
        startActivity(intent);
    }
}

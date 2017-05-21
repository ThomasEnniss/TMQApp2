package com.example.kit305.tmqapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class questionnaireResults extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_results);

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
}

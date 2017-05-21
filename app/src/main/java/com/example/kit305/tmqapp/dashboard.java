package com.example.kit305.tmqapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    int taskCount[] = { 1, 2, 3, 5 };
    String taskClass[] = { "U-I", "NU-I", "U-NI", "NU-NI" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        setupPieChart();
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
        chart.setHoleRadius(70f);
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

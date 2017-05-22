package com.example.kit305.tmqapp;

import android.util.Log;

/**
 * Created by DiGi on 22/05/2017.
 */

public class Task {
    public Integer taskID;
    public String taskName;
    public String taskCode;
    public String dueDate;
    public String isUrgent;
    public String isImportant;
    public String taskComment;

    public Task(Integer newid, String name, String code, String date, String urgent, String important, String notes) {
        taskID = newid;
        taskName = name;
        taskCode = code;
        dueDate = date;
        isUrgent = urgent;
        isImportant = important;
        taskComment = notes;
    }

    //////////////////////////
    //** Variable getters **//
    //////////////////////////
    public Integer getID(){return taskID;}
    public String getName() {
        return taskName;
    }
    public String getCode() {
        return taskCode;
    }
    public String getDate() {
        return dueDate;
    }
    public String getUrgent() {
        return isUrgent;
    }
    public String getImportant() {
        return isImportant;
    }
    public String getComment() {
        return taskComment;
    }

    //////////////////////////
    //** Variable setters **//
    //////////////////////////
    public void setID(Integer newId){taskID = newId;}
    public void setName(String name) {
        taskName = name;
    }
    public void setCode(String code) {
        taskCode = code;
    }
    public void setDate(String date) {
        dueDate = date;
    }
    public void setUrgent(String urgency) {
        isUrgent = urgency;
    }
    public void setImportant(String importance) {
        isImportant = importance;
    }
    public void setComment(String comment) {
        taskComment = comment;
    }

    public void logTaskDetails() {
        Log.d("Task Id", Integer.toString(taskID));
        Log.d("Task Name", taskName);
        Log.d("Unit Code", taskCode);
        Log.d("Due Date", dueDate);
        Log.d("Urgent?", isUrgent);
        Log.d("Important?", isImportant);
        Log.d("Comments", taskComment);
    }
}


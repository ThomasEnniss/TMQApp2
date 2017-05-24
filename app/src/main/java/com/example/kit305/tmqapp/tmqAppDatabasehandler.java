package com.example.kit305.tmqapp;

/**
 * Created by kit305 on 21/5/17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tmqAppDatabasehandler extends SQLiteOpenHelper {

    /*The tables in the database*/
    private static final String TASK_TABLE_NAME = "tasks_table";

    /*I felt it easier to store the score in the database*/
    private static final String TMQ_TABLE_NAME = "tmq_table";

    /*The columns in the task Table*/
    private static final String COLUMN_NAME_TASK_ID = "task_id";
    private static final String COLUMN_NAME_TASK_NAME = "task_name";
    private static final String COLUMN_NAME_TASK_UNIT_CODE = "task_unit_code";
    private static final String COLUMN_NAME_DUE_DATE = "due_date";
    private static final String COLUMN_NAME_URGENT = "urgent";
    private static final String COLUMN_NAME_IMPORTANT = "important";
    private static final String COLUMN_NAME_COMMENTS = "comments";

    /*The columns in the TMQ Table*/
    private static final String COLUMN_NAME_TMQ_ID = "tmq_id";
    private static final String COLUMN_NAME_TMQ_SCORE = "tmq_score";

    /*The query for genertaing the task table*/
    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE_NAME + " (" +
                    COLUMN_NAME_TASK_ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME_TASK_NAME + " TEXT," +
                    COLUMN_NAME_TASK_UNIT_CODE + " TEXT," +
                    COLUMN_NAME_DUE_DATE + " TEXT," +
                    COLUMN_NAME_URGENT + " TEXT," +
                    COLUMN_NAME_IMPORTANT + " TEXT," +
                    COLUMN_NAME_COMMENTS + " TEXT)";

    /*The query for genertaing the TMQ table*/
    private static final String SQL_CREATE_TMQ_TABLE =
            " CREATE TABLE " + TMQ_TABLE_NAME + " (" +
                    COLUMN_NAME_TMQ_ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME_TMQ_SCORE + " TEXT)";



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TMQApp.db";

    public tmqAppDatabasehandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Sets up and creates the database if it doesn't exits*/
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_TMQ_TABLE);
        setupTMQTable(db);
    }

    /*This must be impolemented according to the android developer website*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TASK_TABLE_NAME;
        String SQL_DELETE_SCORES = "DROP TABLE IF EXISTS " + TMQ_TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_SCORES);
        this.onCreate(db);
    }

    /*This must be impolemented according to the android developer website*/
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*inserts a single task into the databse. Accepts a String array of the values*/
    public void insertTask(String[] taskValues){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TASK_NAME,taskValues[0]);
        values.put(COLUMN_NAME_TASK_UNIT_CODE,taskValues[1]);
        values.put(COLUMN_NAME_DUE_DATE,taskValues[2]);
        values.put(COLUMN_NAME_URGENT,taskValues[3]);
        values.put(COLUMN_NAME_IMPORTANT,taskValues[4]);
        values.put(COLUMN_NAME_COMMENTS,taskValues[5]);

        long newRowId = db.insert(TASK_TABLE_NAME,null,values);


    }

    /*This loads all the tasks from the task table, and returns an array with 4 integers. Each integer representds 4 urgency / importance states. Used for pie chart*/
    public int[] loadChartValues(){

        Log.d("SqlLite","Loading Chart Values...");
        SQLiteDatabase db = this.getReadableDatabase();

        int[] numbers = {0,0,0,0};

        String[] columnsToQuery = {COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT};

        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,null,null,null,null,null);

        /*We loop through the tasks loaded by the query*/
        while(cursor.moveToNext()){
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));

            Log.d("SqlLite","Urgent: " + temp_urgent_value + " Important: " + temp_important_value);

            /*We check to see if the task is urgent and important. Depending on these condition decides which index in the array to increment*/
            if(temp_urgent_value.equals("false")){

                Log.d("SqlLite", "Not Urgent");

                if(temp_important_value.equals("false")){

                    Log.d("SqlLite", "Not Important");

                    numbers[3]++;
                }else{

                    Log.d("SqlLite", "Is Important");

                    numbers[2]++;
                }
            }else{

                Log.d("SqlLite", "Is Urgent");

                if(temp_important_value.equals("false")){
                    numbers[1]++;
                }else{

                    Log.d("SqlLite", "Is Important");

                    numbers[0]++;
                }
            }
        }
        Log.d("SqlLite","Chart Values Loaded");
        return numbers;
    }

    /*This adds an inital row and score of 0 to the TMQ table so the users score starts off as zero*/
    private void setupTMQTable(SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TMQ_ID,0);
        values.put(COLUMN_NAME_TMQ_SCORE,"0");


        long newRowId = db.insert(TMQ_TABLE_NAME,null,values);

    }

    /*Updates the single row in the TMQ table with the score*/
    public void updateTMQScore(String score){

        SQLiteDatabase db = this.getReadableDatabase();


        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TMQ_SCORE, score);


        String selection = COLUMN_NAME_TMQ_ID + " LIKE ?";
        String[] selectionArgs = { "0" };

        int count = db.update(
                TMQ_TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    /*Loads and returns the solitary TMQ score for the dashboard*/
    public String getTMQScore(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TMQ_SCORE};

        Cursor cursor = db.query(TMQ_TABLE_NAME,columnsToQuery,null,null,null,null,null);

        cursor.moveToNext();

        return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TMQ_SCORE));
    }

    /*This gets all the tasks fro mthe database and creates events based on them. These events aare returned to the activity calendar for plotting in ti*/
    public List<Event> getAllEventDates(){

        List<Event> eventList= new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_DUE_DATE,COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT};

        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,null,null,null,null,null);


        /*Loop through the database creating an event for each class*/
        while(cursor.moveToNext()){
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));
            String temp_date_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DUE_DATE));
            Event newEvent;

            /*We need to convert the date string into a string and then into a timestamp / epoch time so it can be properly stored in the calendar*/
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Log.d("SQlLite",temp_date_string);

            Date date = null;

            try {
                date = sdf.parse(temp_date_string);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            long millis = date.getTime();
            Log.d("SQlLite", Long.toString(millis));


            /*We use the importance and urgent fields to determine what color the dot or "event" should be for the calendar*/
            if(temp_urgent_value.equals("false")){

                if(temp_important_value.equals("false")){

                    newEvent = new Event(Color.rgb(106, 150, 31), millis + 'L', "Testing Event");

                    eventList.add(newEvent);
                }else{
                    newEvent = new Event(Color.rgb(245, 199, 0), millis + 'L', "Testing Event");

                    eventList.add(newEvent);
                }
            }else{

                if(temp_important_value.equals("false")){

                    newEvent = new Event(Color.rgb(255, 102, 0), millis + 'L', "Testing Event");

                    eventList.add(newEvent);
                }else{

                    newEvent = new Event(Color.rgb(193, 37, 82), millis + 'L', "Testing Event");

                    eventList.add(newEvent);
                }
            }
        }

        return eventList;

    }

    /*Used for returning a list of all the tasks for a particular date*/
    public List<Task> loadTasksByDate(String dateToLoad){

        List<Task> loadedTaskList  = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TASK_ID,COLUMN_NAME_TASK_NAME,COLUMN_NAME_TASK_UNIT_CODE,COLUMN_NAME_DUE_DATE,COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT,COLUMN_NAME_COMMENTS};

        String selection = COLUMN_NAME_DUE_DATE + " = ?";
        String[] selectionArgs = {dateToLoad};

        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,selection,selectionArgs,null,null,null);


        /*loop through tasks and add them to a list*/
        while(cursor.moveToNext()) {
            Integer temp_task_id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TASK_ID));
            String temp_task_name_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_NAME));
            String temp_unit_code_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_UNIT_CODE));
            String temp_due_date_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DUE_DATE));
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));
            String temp_comments_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENTS));

            Task newTask = new Task(temp_task_id,temp_task_name_value,temp_unit_code_value,temp_due_date_string,temp_urgent_value,temp_important_value,temp_comments_string);

            loadedTaskList.add(newTask);
        }

        return loadedTaskList;
    }

    /*Used to load 1 task by ID. Used for the edit task*/
    public Task loadTaskByID(Integer IDToLoad){

        Task newTask;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TASK_ID,COLUMN_NAME_TASK_NAME,COLUMN_NAME_TASK_UNIT_CODE,COLUMN_NAME_DUE_DATE,COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT,COLUMN_NAME_COMMENTS};

        String selection = COLUMN_NAME_TASK_ID + " = " + IDToLoad;


        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,selection,null,null,null,null);

        cursor.moveToNext();
            Integer temp_task_id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TASK_ID));
            String temp_task_name_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_NAME));
            String temp_unit_code_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_UNIT_CODE));
            String temp_due_date_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DUE_DATE));
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));
            String temp_comments_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENTS));

            newTask = new Task(temp_task_id,temp_task_name_value,temp_unit_code_value,temp_due_date_string,temp_urgent_value,temp_important_value,temp_comments_string);


        return newTask;
    }

    /*Used to load tasks matching a particular urgency or importance. Accepts bools in string format. */
    public List<Task> loadTaskByPriority(String urgent, String important){

        List<Task> loadedTaskList  = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TASK_ID,COLUMN_NAME_TASK_NAME,COLUMN_NAME_TASK_UNIT_CODE,COLUMN_NAME_DUE_DATE,COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT,COLUMN_NAME_COMMENTS};

        String selection = COLUMN_NAME_URGENT + " = ? AND " + COLUMN_NAME_IMPORTANT + " = ?";

        String[] selectionArgs = {urgent,important};


        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,selection,selectionArgs,null,null,null);

        /*Loops through dataabase tasks adding them to the list as a task object*/
        while(cursor.moveToNext()) {
            Integer temp_task_id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TASK_ID));
            String temp_task_name_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_NAME));
            String temp_unit_code_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_UNIT_CODE));
            String temp_due_date_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DUE_DATE));
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));
            String temp_comments_string = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENTS));

            Task newTask = new Task(temp_task_id, temp_task_name_value, temp_unit_code_value, temp_due_date_string, temp_urgent_value, temp_important_value, temp_comments_string);

            loadedTaskList.add(newTask);
        }

        return loadedTaskList;
    }

    /*We update a particular task identified by it's id with the values in the string array*/
    public void updateTask(String[] updateValues,Integer taskID){

        SQLiteDatabase db = this.getReadableDatabase();


        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_TASK_NAME, updateValues[0]);
        values.put(COLUMN_NAME_TASK_UNIT_CODE, updateValues[1]);
        values.put(COLUMN_NAME_DUE_DATE, updateValues[2]);
        values.put(COLUMN_NAME_URGENT, updateValues[3]);
        values.put(COLUMN_NAME_IMPORTANT, updateValues[4]);
        values.put(COLUMN_NAME_COMMENTS, updateValues[5]);

        String selection = COLUMN_NAME_TASK_ID + "=?";
        String[] selectionArgs = {Integer.toString(taskID)};

        int count = db.update(
                TASK_TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    /*We delete a task by it's id*/
    public void deleteTask(Integer taskID){

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TASK_TABLE_NAME,COLUMN_NAME_TASK_ID + "=?", new String[]{Integer.toString(taskID)} );
    }
}
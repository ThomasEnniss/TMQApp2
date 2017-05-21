package com.example.kit305.tmqapp;

/**
 * Created by kit305 on 21/5/17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class tmqAppDatabasehandler extends SQLiteOpenHelper {

    private static final String TASK_TABLE_NAME = "tasks_table";
    private static final String TMQ_TABLE_NAME = "tmq_table";

    private static final String COLUMN_NAME_TASK_ID = "task_id";
    private static final String COLUMN_NAME_TASK_NAME = "task_name";
    private static final String COLUMN_NAME_TASK_UNIT_CODE = "task_unit_code";
    private static final String COLUMN_NAME_DUE_DATE = "due_date";
    private static final String COLUMN_NAME_URGENT = "urgent";
    private static final String COLUMN_NAME_IMPORTANT = "important";
    private static final String COLUMN_NAME_COMMENTS = "comments";

    private static final String COLUMN_NAME_TMQ_ID = "tmq_id";
    private static final String COLUMN_NAME_TMQ_SCORE = "tmq_score";

    private SQLiteDatabase db;

    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE_NAME + " (" +
                    COLUMN_NAME_TASK_ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME_TASK_NAME + " TEXT," +
                    COLUMN_NAME_TASK_UNIT_CODE + " TEXT," +
                    COLUMN_NAME_DUE_DATE + " TEXT," +
                    COLUMN_NAME_URGENT + " TEXT," +
                    COLUMN_NAME_IMPORTANT + " TEXT," +
                    COLUMN_NAME_COMMENTS + " TEXT)";

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

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_TMQ_TABLE);
        setupTMQTable();
        this.db = db;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TASK_TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        this.onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

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

    public void viewTaskTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TASK_NAME,COLUMN_NAME_TASK_UNIT_CODE,COLUMN_NAME_DUE_DATE,COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT,COLUMN_NAME_COMMENTS};

        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,null,null,null,null,null);

        cursor.moveToNext();
        Log.d("Found",cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_NAME)));
    }

    public int[] loadChartValues(){

        Log.d("SqlLite","Loading Chart Values...");
        SQLiteDatabase db = this.getReadableDatabase();

        int[] numbers = {0,0,0,0};

        String[] columnsToQuery = {COLUMN_NAME_URGENT,COLUMN_NAME_IMPORTANT};

        Cursor cursor = db.query(TASK_TABLE_NAME,columnsToQuery,null,null,null,null,null);

        while(cursor.moveToNext()){
            String temp_urgent_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URGENT));
            String temp_important_value = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMPORTANT));

            Log.d("SqlLite","Urgent: " + temp_urgent_value + " Important: " + temp_important_value);

            if(temp_urgent_value.equals("false")){

                Log.d("SqlLite", "Not Urgent");

                if(temp_important_value.equals("false")){

                    Log.d("SqlLite", "Not Important");

                    numbers[3]++;
                }else{

                    Log.d("SqlLite", "Important");

                    numbers[2]++;
                }
            }else{

                Log.d("SqlLite", "Urgent");

                if(temp_important_value.equals("false")){
                    numbers[1]++;
                }else{

                    Log.d("SqlLite", "Important");

                    numbers[0]++;
                }
            }
        }
        Log.d("SqlLite","Chart Values Loaded");
        return numbers;
    }

    public void emptyDatabase(){

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TASK_TABLE_NAME, null, null);

    }

    private void setupTMQTable(){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TMQ_ID,0);
        values.put(COLUMN_NAME_TMQ_SCORE,"0");


        long newRowId = db.insert(TMQ_TABLE_NAME,null,values);

    }

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

    public String getTMQScore(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsToQuery = {COLUMN_NAME_TMQ_SCORE};

        Cursor cursor = db.query(TMQ_TABLE_NAME,columnsToQuery,null,null,null,null,null);

        cursor.moveToNext();

        return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TMQ_SCORE));
    }
}
package com.labs.listtodo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    //Database version, creates a new DB when changes
    private static final int DB_VERSION = 1;

    //Database Structure
    private static final String DB_NAME = "taskList";
    public static final String TABLE_NAME = "tasks";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "date";
    private static final String COL_PRIORITY = "priority";
    private static final String COL_ACTIVE = "active";

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Method to create database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "";
        CREATE_TABLE += "CREATE TABLE " + TABLE_NAME + " ( ";
        CREATE_TABLE += COL_ID + " INTEGER PRIMARY KEY, ";
        CREATE_TABLE += COL_TITLE + " TEXT, ";
        CREATE_TABLE += COL_DESCRIPTION + " TEXT, ";
        CREATE_TABLE += COL_DATE + " TEXT, ";
        CREATE_TABLE += COL_PRIORITY + " TEXT, ";
        CREATE_TABLE += COL_ACTIVE + " TEXT ";
        CREATE_TABLE += " ) ";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addTask(TaskBean taskBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, taskBean.getTitle());
        values.put(COL_DESCRIPTION, taskBean.getDescription());
        values.put(COL_DATE, taskBean.getDate());
        values.put(COL_PRIORITY, taskBean.getPriority());
        values.put(COL_ACTIVE, taskBean.isActive());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public TaskBean getSingleTaskItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        TaskBean taskBean = null;

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COL_ID, COL_TITLE, COL_DESCRIPTION, COL_PRIORITY, COL_DATE, COL_ACTIVE},
                COL_ID + "=?",
                new String[]{id + ""},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            taskBean = new TaskBean(Integer.valueOf(cursor.getString(0))
                    , cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    , Integer.valueOf(cursor.getString(4)), cursor.getInt(5) != 0);
        }
        db.close();
        return taskBean;
    }

    public List<TaskBean> getAllTasks() {
        List<TaskBean> allTasks = new ArrayList<TaskBean>();
        TaskBean t;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                t = new TaskBean(Integer.valueOf(cursor.getString(0))
                        , cursor.getString(1), cursor.getString(2), cursor.getString(3)
                        , Integer.valueOf(cursor.getString(4)), cursor.getInt(5) != 0);
                allTasks.add(t);

            } while (cursor.moveToNext());
        }

        db.close();
        return allTasks;
    }

    public void updateTasks(TaskBean taskBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, taskBean.getTitle());
        values.put(COL_DESCRIPTION, taskBean.getDescription());
        values.put(COL_DATE, taskBean.getDate());
        values.put(COL_PRIORITY, taskBean.getPriority());
        values.put(COL_ACTIVE, taskBean.isActive());

        db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{taskBean.getId() + ""});
        db.close();

    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID+"=?", new String[]{id + ""});
        db.close();
    }

    public void deleteDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
        db.close();

    }

    public int getTotalTasks(){
        int total = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        total = cursor.getCount();
        cursor.close();
        db.close();

        return total;

    }


}

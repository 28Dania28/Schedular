package com.dania.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseScheduler extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "SchedulerDatabase";
    private static final String TABLE_NAME = "Scheduler";
    private static final String COL1 = "Date";
    private static final String COL2 = "Title";
    private static final String COL3 = "Content";
    private static final String COL4 = "Timestamp";
    private static final String COL5 = "Status";
    private static final String COL6 = "DateTimestamp";


    public DatabaseScheduler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(String date, String title, String content, String timestamp, String status, String dateTimestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, date);
        contentValues.put(COL2, title);
        contentValues.put(COL3, content);
        contentValues.put(COL4, timestamp);
        contentValues.put(COL5, status);
        contentValues.put(COL6, dateTimestamp);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateSchedule(DataModel dm, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL5, status);
        long result = db.update(TABLE_NAME,contentValues,COL4+"=?", new String[]{dm.getTimestamp()});
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateFullSchedule(String date, String title, String content, String timestamp, String status, String dateTimestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, date);
        contentValues.put(COL2, title);
        contentValues.put(COL3, content);
        contentValues.put(COL4, timestamp);
        contentValues.put(COL5, status);
        contentValues.put(COL6, dateTimestamp);
        long result = db.update(TABLE_NAME,contentValues,COL4+"=?", new String[]{timestamp});
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }



    public boolean deleteSchedule(String timestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,COL4+"=?", new String[]{timestamp});
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" ORDER BY "+COL6+" DESC",null);
        return c;
    }


    public Cursor getDataFromDate(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" WHERE Date = '"+date+"'",null);
        return c;
    }

    public Cursor getIncompleteDataFromDate(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" WHERE Date = '"+date+"'"+" AND Status = '"+"Incomplete"+"'",null);
        return c;
    }

    public Cursor getCompleteDataFromDate(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" WHERE Date = '"+date+"'"+" AND Status = '"+"Complete"+"'",null);
        return c;
    }

}

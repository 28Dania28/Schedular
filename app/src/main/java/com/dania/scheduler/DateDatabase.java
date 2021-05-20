package com.dania.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateDatabase extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "DateDatabase";
    private static final String TABLE_NAME = "Dates";
    private static final String COL1 = "DateLable";
    private static final String COL2 = "DateTimestamp";
    private static final String COL3 = "Date";
    private static final String COL4 = "Month";
    private static final String COL5 = "Day";

    public DateDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDate(String dateLable, String dateTimestamp, String date, String month, String day){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, dateLable);
        contentValues.put(COL2, dateTimestamp);
        contentValues.put(COL3, date);
        contentValues.put(COL4, month);
        contentValues.put(COL5, day);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME,null);
        return c;
    }

    public boolean deleteDate(String dateLable){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,COL1+"=?", new String[]{dateLable});
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

}

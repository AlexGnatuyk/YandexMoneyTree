package com.sancho.gettest;

/**
 * Created by Sancho on 16.08.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE = "elements";
    public static final String NUMBER_COLUMN = "number";
    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String PARENT_COLUMN = "parent";
    public static final String LEVEL_COLUMN = "level";
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + NUMBER_COLUMN + " integer primary key, " + ID_COLUMN
            + " integer, " + TITLE_COLUMN
            + " text not null, " + PARENT_COLUMN + " text not null," + LEVEL_COLUMN + " integer);";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
        Log.d("LOG_createBD", "bd was created");


    }

    public void addItem(int id, String title, String parent, int level) {


        //mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();


        ContentValues newValues = new ContentValues();
        // Задаем значения для каждого столбца
        newValues.put(DatabaseHelper.ID_COLUMN, id);
        newValues.put(DatabaseHelper.TITLE_COLUMN, title);
        newValues.put(DatabaseHelper.PARENT_COLUMN, parent);
        newValues.put(DatabaseHelper.LEVEL_COLUMN, level);
        // Вставляем данные в таблицу
        mSqLiteDatabase.insert(DATABASE_TABLE, null, newValues);
        Log.d("LOG_addItem", "add item with id");
        mSqLiteDatabase.close();
    }

    public void addItem(String title, String parent, int level) {
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();


        ContentValues newValues = new ContentValues();
        // Задаем значения для каждого столбца
        newValues.put(DatabaseHelper.TITLE_COLUMN, title);
        newValues.put(DatabaseHelper.PARENT_COLUMN, parent);
        newValues.put(DatabaseHelper.LEVEL_COLUMN, level);
        // Вставляем данные в таблицу
        mSqLiteDatabase.insert(DATABASE_TABLE, null, newValues);
        Log.d("LOG_addItem", "add item withOUT id");
        mSqLiteDatabase.close();
    }

    //Вывод всех title
    public Cursor getTitles() {

        String selectQuery = "SELECT " + TITLE_COLUMN + " FROM " + DATABASE_TABLE + " ORDER BY " + NUMBER_COLUMN;
        Log.d("LOG_bd","get_Titles");

        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor temp= mSqLiteDatabase.rawQuery(selectQuery, null);

        return temp;
    }

    public void getItem() {
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d("LOG_bd", "id " + cursor.getString(0) + ", title " + cursor.getString(1) + ", parent " + cursor.getString(2));
            } while (cursor.moveToNext());
        }

    }


    public int checkEmpty() {
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db =  getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0) {
            Log.d("LOG_bdCheck", "bd  empty");

            return 0;
        } else {
            Log.d("LOG_bdCheck", "bd not empty");
            return 1;
        }


    }

    // Вывод title у подкатегори категории
    public Cursor getChildrenTitle(String parent) {
        Log.d("LOG_bd","get_childrenTitles");

        String selectQuery = "SELECT " + TITLE_COLUMN + " FROM " + DATABASE_TABLE + " WHERE " + PARENT_COLUMN + "=" + "'" + parent + "'";

        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        return mSqLiteDatabase.rawQuery(selectQuery, null);
    }

    public void groupByParent() {
        String selectQuery = "SELECT " + PARENT_COLUMN + " FROM " + DATABASE_TABLE + " GROUP BY " + PARENT_COLUMN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("LOG_bd", "parent " + cursor.getString(0));
                // Log.d("LOG_CountLines", " " + cursor.getCount());

            } while (cursor.moveToNext());
        }
    }

    //public int getVesion(){}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.d("LOG_SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);

    }

    public void upgradeDataBase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(DATABASE_CREATE_SCRIPT);

        db.close();
    }


}

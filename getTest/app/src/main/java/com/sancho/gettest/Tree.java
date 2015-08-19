package com.sancho.gettest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sancho on 19.08.2015.
 */
public class Tree {

    private static final String DATABASE_TABLE = "elements";
    public static final String TITLE_COLUMN = "title";
    public static final String PARENT_COLUMN = "parent";
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    List<String> listParent = new ArrayList<>();
    ArrayList<Item> items;






    public ArrayList<Item> buildingTree(DatabaseHelper mDatabaseHelper) {
        Log.d("LOG_tree", "Tree is building");
        items = new ArrayList<>();

        Cursor titleResponse = mDatabaseHelper.getTitles();

        if (titleResponse.moveToFirst()) {
            do {
                listParent.add(titleResponse.getString(0));
            } while (titleResponse.moveToNext());
        }

        for (int i = 0; i < listParent.size(); i++) {
            items.add(buildElement(mDatabaseHelper, listParent.get(i)));

        }

        listParent.clear();

        return items;
    }


    private ListItem buildElement(DatabaseHelper mDatabaseHelper, String title) {
        ListItem element = new ListItem(title);
        Cursor childs = mDatabaseHelper.getChildrenTitle(title);
        if (childs.moveToFirst()) {
            do {
                listParent.remove(childs.getString(0));
                element.addChild(buildElement(mDatabaseHelper, childs.getString(0)));

            } while (childs.moveToNext());
        }

        return element;
    }

    protected  ArrayList<Item> upgradeTree(DatabaseHelper mDatabaseHelper, ArrayList<Item> items ) {

        Log.d("LOG_update","updateTree");
        items.clear();
        mDatabaseHelper.upgradeDataBase();

        return items;


    }



}

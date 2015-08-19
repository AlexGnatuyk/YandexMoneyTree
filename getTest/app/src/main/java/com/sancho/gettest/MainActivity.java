package com.sancho.gettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class MainActivity extends ActionBarActivity {

    EditText etResponse;
    TextView tvIsConnected;
    List<String> elementText = new ArrayList<>();
    final String LOG_TAG = "myLogs";
    private static final String DATABASE_TABLE = "elements";
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private Tree tree = new Tree();
    ArrayList<Item> items;
    public ListView mList;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mDatabaseHelper = new DatabaseHelper(this, "database.db", null, 1);

        mList = (ListView) this.findViewById(R.id.listView);
        Tree tree = new Tree();


        // mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        if (mDatabaseHelper.checkEmpty() != 0) {
            // mDatabaseHelper.getItem();
            items = tree.buildingTree(mDatabaseHelper);
            adapter = new ListAdapter(this, items);
            mList.setAdapter(adapter);

            //mDatabaseHelper.groupByParent();
        } else {
            aqueryGet();

        }


        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.clickOnItem(position);
            }
        });


    }


    private void aqueryGet() {
        Log.d("LOG_aqueryGet", "getStart");
        AQuery aq = new AQuery(this);
        String URL = "https://money.yandex.ru/api/categories-list";
        aq.ajax(URL, JSONArray.class, this, "jsonCallback");
    }

    public void jsonCallback(String URL, JSONArray json, AjaxStatus status) {

        
        if (json != null) {
            Log.d("LOG_callBack", "jsonParsing");
            parseJsonArray(json, "Default");


        } else {
            //ajax error
        }
    }

    protected void parseJsonArray(JSONArray arr, String parent) {
        try {

            for (int i = 0; i < arr.length(); i++) {

                JSONObject item = arr.getJSONObject(i);
                Log.d("LOG_PARSING", "jsonParsing #2");
                try {
                    Log.d("LOG_item", item.getString("id") + " " + item.getString("title"));
                    mDatabaseHelper.addItem(item.getInt("id"), item.getString("title"), parent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    mDatabaseHelper.addItem(item.getString("title"), parent);
                    //Log.d("LOG_title", item.getString("title"));
                }

                try {

                    JSONArray array = item.getJSONArray("subs");
                    parseJsonArray(array, item.getString("title"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDatabaseHelper.getItem();
        items = tree.buildingTree(mDatabaseHelper);
        adapter = new ListAdapter(this, items);
        mList.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        menu.add("Upgrade");

        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getTitle() == "Upgrade") {
            Toast.makeText(MainActivity.this, "Updrading", Toast.LENGTH_SHORT).show();
            items = tree.upgradeTree(mDatabaseHelper, items);
            aqueryGet();
            Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();

        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
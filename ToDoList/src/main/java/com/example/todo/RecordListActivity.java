package com.example.todo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class RecordListActivity extends AppCompatActivity {


    ListView mListView;
    ArrayList<Task> mList;
    RecordListAdapter mAdapter = null;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Task List");

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.setupDatabase();
        //get all data from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM TASKS ORDER BY deadline ASC");
        mList.clear();
        while (cursor.moveToNext()){
            try{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Task.Priority priority = Task.Priority.valueOf(cursor.getString(3));
                Calendar deadline = Calendar.getInstance();
                deadline.setTime(SQLiteHelper.format.parse(cursor.getString(4)));
                //add to list
                mList.add(new Task(id, name, description, priority, deadline));
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No record found...", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecordListActivity.this, DetailActivity.class);
                intent.putExtra("id", String.valueOf(mList.get(position).getId()));
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab = findViewById(R.id.addBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this, MainActivity.class));
                finish();
            }
        });
    }


}

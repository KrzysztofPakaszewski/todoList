package com.example.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailActivity extends FragmentActivity implements DetailFragment.DetailFragmentListener, taskListFragment.TaskListInterface {

    String id;

    public static SQLiteHelper sqLiteHelper;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, RecordListActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        sqLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        Intent intent = getIntent();
        if(savedInstanceState != null){
            id = savedInstanceState.getString("id");
        }else {
            id = intent.getStringExtra("id");
        }

        setContentView(R.layout.detail_activity);

        AppCompatDelegate appCompatDelegate = AppCompatDelegate.create(this,null);

        ActionBar actionBar = appCompatDelegate.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("id", this.id);
    }

    @Override
    public void onDelete(int id) {
        sqLiteHelper.deleteData(id);
        startActivity(new Intent(this, RecordListActivity.class));
        finish();
    }

    @Override
    public Task getTask(){
        return sqLiteHelper.getTask(id);
    }

    @Override
    public ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM TASKS ORDER BY deadline ASC");
        while (cursor.moveToNext()){
            try{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Task.Priority priority = Task.Priority.valueOf(cursor.getString(3));
                Calendar deadline = Calendar.getInstance();
                deadline.setTime(SQLiteHelper.format.parse(cursor.getString(4)));

                tasks.add(new Task(id, name, description, priority, deadline));
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return tasks;
    }

    @Override
    public void setId(int id){
        this.id = String.valueOf(id);
    }
}

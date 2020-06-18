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

        //Insert 2 sample tasks, put into pref so it can only added once
        /*
        SharedPreferences firstTime = getSharedPreferences("FirstTime", MODE_PRIVATE);
        if (!firstTime.getBoolean("isFirstTime", false)) {
            //your code goes here
            try {
                mSQLiteHelper.insertData("Mindful breathing".trim(), "15".trim(), "New".trim());
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                mSQLiteHelper.insertData("Bedtime breathing".trim(), "15".trim(), "New".trim());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            final SharedPreferences pref = getSharedPreferences("FirstTime", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirstTime", true);
            editor.apply();
        }
         */

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

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Warning");
        dialogDelete.setMessage("Are you sure?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    MainActivity.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_main);
        dialog.setTitle("Update");

        final EditText edtTask = dialog.findViewById(R.id.edtName);
        final EditText edtDuration = dialog.findViewById(R.id.edtDescription);
        final EditText edtStatus = dialog.findViewById(R.id.edtStatus);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

//        Toast.makeText(activity, ""+position, Toast.LENGTH_SHORT).show();

        int iddd = position;
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM TASKS WHERE id="+iddd);
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String task = cursor.getString(1);
            edtTask.setText(task);
            String duration = cursor.getString(2);
            edtDuration.setText(duration);
            String status = cursor.getString(3);
            edtStatus.setText(status);
            //add to list
            //mList.add(new Task(id, task, duration, status));
        }

        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set hieght of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                 //   mSQLiteHelper.updateData(
                         //   edtTask.getText().toString().trim(),
                       //     edtDuration.getText().toString().trim(),
                     //       edtStatus.getText().toString().trim(),
                   //         position);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                updateRecordList();
            }
        });

    }

    private void updateRecordList() {
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
    }

}

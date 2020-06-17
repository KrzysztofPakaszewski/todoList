package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

public class DetailActivity extends FragmentActivity implements DetailFragment.OnDeleteListener {

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
        setContentView(R.layout.detail_activity);

        AppCompatDelegate appCompatDelegate = AppCompatDelegate.create(this,null);

        ActionBar actionBar = appCompatDelegate.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Details");

        sqLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        Task task = sqLiteHelper.getTask(id);

        if(savedInstanceState != null){
            return;
        }
        DetailFragment detailFragment = new DetailFragment();

        detailFragment.callback = this;

        detailFragment.task = task;

        getSupportFragmentManager().beginTransaction().add(R.id.detailFrag, detailFragment).commit();

    }

    @Override
    public void onDelete(int id) {
        sqLiteHelper.deleteData(id);
        startActivity(new Intent(this, RecordListActivity.class));
        finish();
    }
}

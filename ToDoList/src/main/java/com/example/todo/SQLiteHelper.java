package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SQLiteHelper extends SQLiteOpenHelper{

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //constructor
    SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void setupDatabase(){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "CREATE TABLE IF NOT EXISTS TASKS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR, " +
                "description VARCHAR, " +
                "priority VARCHAR, " +
                "deadline VARCHAR)";

        database.execSQL(sql);
    }

    //insertData
    public void insertData(String name, String description, Task.Priority priority, Calendar deadline){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO TASKS VALUES(NULL, ?, ?, ?, ?)"; //where "TASKS" is table name

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, description);
        statement.bindString(3, priority.toString());
        statement.bindString(4, format.format(deadline.getTime()));

        statement.executeInsert();
    }

    //updateData
    public void updateData(String name, String description, Task.Priority priority, Calendar deadline, int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE TASKS SET name=?, description=?, priority=?, deadline=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, description);
        statement.bindString(3, priority.toString());
        statement.bindString(4, format.format(deadline));
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM TASKS WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

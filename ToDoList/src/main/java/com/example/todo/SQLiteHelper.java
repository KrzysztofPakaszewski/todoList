package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SQLiteHelper extends SQLiteOpenHelper{

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //constructor Create a helper object to create, open, and/or manage a database.
    SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
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
    public Task getTask(String id){
        SQLiteDatabase database = getReadableDatabase();

        String sql = "SELECT * FROM TASKS WHERE id="+ id;
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            try {
                int newId = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Task.Priority priority = Task.Priority.valueOf(cursor.getString(3));
                Calendar deadline = Calendar.getInstance();
                deadline.setTime(format.parse(cursor.getString(4)));

                return new Task(newId, name, description, priority, deadline);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return null;
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

package com.example.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    EditText mEdtName, mEdtDescription;
    Spinner mEdtPriority;
    TextView mSelectDate, mSelectTime;
    Button mBtnAdd;

    Calendar date = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Task");

        mEdtName = findViewById(R.id.edtName);
        mEdtDescription = findViewById(R.id.edtDescription);
        mBtnAdd = findViewById(R.id.btnAdd);
        mEdtPriority = findViewById(R.id.edtPriority);
        mSelectDate = findViewById(R.id.selectDate);
        mSelectTime = findViewById(R.id.selectTime);

        mEdtPriority.setAdapter(new ArrayAdapter<Task.Priority>(this, android.R.layout.simple_list_item_1, Task.Priority.values()));

        // Dialog date picker
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateText = month + "/"+ dayOfMonth + "/" + year;
                mSelectDate.setText(dateText);
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH,month);
                date.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            }
        };

        //Dialog time picker
        mSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE),
                        true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeText = hourOfDay + ":"+ minute;
                mSelectTime.setText(timeText);
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
            }
        };

        //connecting database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);


        //add record to sqlite
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Condition to catch empty entity added into db, avoid error
                if (mEdtName.getText().toString().trim().matches("") || mEdtDescription.getText().toString().trim().matches("")
                        || mSelectDate.getText().toString().contains("Select") || mSelectTime.getText().toString().contains("Select")
                ){
                    Toast.makeText(MainActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        mSQLiteHelper.insertData(
                                mEdtName.getText().toString().trim(),
                                mEdtDescription.getText().toString().trim(),
                                (Task.Priority) mEdtPriority.getSelectedItem(),
                                date
                        );
                        Toast.makeText(MainActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        //reset views
                        mEdtName.setText("");
                        mEdtDescription.setText("");
                        mSelectTime.setText(R.string.timeSelect);
                        mSelectDate.setText(R.string.dateSelect);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}

package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Task> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Task> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView txtName, txtDuration, txtPriority;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtDuration = row.findViewById(R.id.txtDuration);
            holder.txtPriority = row.findViewById(R.id.txtPriority);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Task task = recordList.get(i);

        long diff = task.getDeadline().getTime().getTime() - new Date().getTime();
        boolean neg = diff > 0;
        long minutes = abs(diff)/1000/60;
        String sign = "";
        if(!neg){
            sign = "- ";
        }
        // format DAYS:HOURS:MINUTES
        String time =  sign + minutes/24/60 + ":" + minutes/60%24 + ":" +minutes%60;

        holder.txtName.setText(task.getName());
        holder.txtDuration.setText( time);
        holder.txtPriority.setText(task.getPriority().toString());

        return row;
    }
}

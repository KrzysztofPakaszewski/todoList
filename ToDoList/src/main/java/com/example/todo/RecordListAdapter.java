package com.example.todo;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Task> recordList;
    private ArrayList<ViewHolder> lstHolders = new ArrayList<>();
    private Handler mhandler = new Handler();

    private Runnable updateTimers = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders){
                long current = System.currentTimeMillis();
                for(ViewHolder holder: lstHolders){
                    holder.updateRemaining(current);
                }
            }
        }
    };


    public RecordListAdapter(Context context, int layout, ArrayList<Task> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
        startTimer();
    }

    private void startTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mhandler.post(updateTimers);
            }
        }, 1000,1000);
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
        Task task;

        public void updateRemaining(long currentTime){
            long deadline = task.getDeadline().getTimeInMillis();
            long diff = deadline - currentTime;
            boolean neg = diff > 0;
            long seconds = abs(diff)/1000;
            String sign = "";
            if(!neg){
                sign = "- ";
            }
            // format DAYS:HOURS:MINUTES:SECONDS
            String time =  sign + seconds/24/3600 + ":" + seconds/3600%24 + ":" +seconds/60%60 + ":" + seconds%60;
            txtDuration.setText( time);
            if(!neg || seconds < 3600){
                txtDuration.setBackgroundColor(Color.argb(255,255,0,0));
            }else if(seconds < 3600 * 24){
                txtDuration.setBackgroundColor(Color.argb(255,255,255,0));
            }else{
                txtDuration.setBackgroundColor(Color.argb(255,0,255,0));
            }
        }
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
            holder.task = recordList.get(i);
            row.setTag(holder);

            synchronized (lstHolders){
                lstHolders.add(holder);
            }
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Task task = recordList.get(i);

        holder.txtName.setText(task.getName());
        holder.txtPriority.setText(task.getPriority().toString());
        holder.txtPriority.setBackgroundColor(task.getPriority().getValue());
        holder.updateRemaining(System.currentTimeMillis());

        return row;
    }
}

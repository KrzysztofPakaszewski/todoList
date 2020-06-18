package com.example.todo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    TextView mName, mPriority, mDeadline, mDescription;
    Button mFinish;

    int position = -1;

    Task task;

    DetailFragmentListener callback;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){

        View view = layoutInflater.inflate(R.layout.detail_fragment, container, false);
        callback = (DetailActivity) getActivity();

        mName = view.findViewById(R.id.dName);
        mPriority = view.findViewById(R.id.dPriority);
        mDeadline = view.findViewById(R.id.dDeadline);
        mDescription = view.findViewById(R.id.dDescription);
        mFinish = view.findViewById(R.id.dFinish);

        if(task == null) {
            task = callback.getTask();
        }

        updateView();

        return view;
    }

    public void updateView(){
        if(task != null){
            mName.setText(task.getName());
            mPriority.setText(task.getPriority().toString());
            mDeadline.setText(SQLiteHelper.format.format(task.getDeadline().getTime()));
            mDescription.setText(task.getDescription());

            mFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onDelete(task.getId());
                }
            });
        }
    }

    public interface DetailFragmentListener{
        void onDelete(int id);
        Task getTask();
    }
}

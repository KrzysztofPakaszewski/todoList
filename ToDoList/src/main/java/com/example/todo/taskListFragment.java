package com.example.todo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class taskListFragment extends ListFragment {

    int position = 0;

    ArrayList<Task> tasks;
    ArrayList<String> names = new ArrayList<>();

    TaskListInterface callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callback = (DetailActivity) getActivity();

        String id = ((DetailActivity) getActivity()).id;

        tasks = callback.getTasks();

        if(tasks.size()> 0) {
            for (Task t : tasks) {
                names.add(t.getName());
                if (String.valueOf(t.getId()).equals(id)) {
                    position = names.size() - 1;
                }
            }


            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, names));

            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            showDetails(position);
        }else {
            Toast.makeText(getContext(), "No Tasks found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    void showDetails(int index) {
        position = index;

        getListView().setItemChecked(index, true);

        DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.details);
        if (details == null || details.position != index) {
            // Make new fragment to show this selection.

            details =  new DetailFragment();
            details.position = index;
            details.task = tasks.get(index);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.details, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    public interface TaskListInterface{
        ArrayList<Task> getTasks();
    }
}


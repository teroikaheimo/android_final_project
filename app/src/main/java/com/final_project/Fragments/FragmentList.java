package com.final_project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.final_project.EventAdapter;
import com.final_project.EventItem;
import com.final_project.R;

import java.util.ArrayList;

public class FragmentList extends ListFragment implements AdapterView.OnItemClickListener {
    private ListView itemList;
    private ArrayList<EventItem> items;
    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        itemList = v.findViewById(android.R.id.list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        items = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), items);
        setListAdapter(eventAdapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    public void addItemListView(ArrayList<EventItem> list) {
        eventAdapter.clear();
        eventAdapter.addAll(list);
        eventAdapter.notifyDataSetChanged();
    }

}

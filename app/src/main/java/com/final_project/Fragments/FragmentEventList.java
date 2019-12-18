package com.final_project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.final_project.EventAdapter;
import com.final_project.EventItem;
import com.final_project.R;

import java.util.ArrayList;

public class FragmentEventList extends ListFragment {
    private Context context;
    private ListView itemList;
    private ArrayList<EventItem> items;
    private EventAdapter eventAdapter;
    private EventListListener listener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        items = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), items);
        setListAdapter(eventAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        itemList = v.findViewById(android.R.id.list);
        return v;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("CLICK", "onListItemClick: " + position);
        EventItem item = items.get(position);
        listener.onItemClick(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Just to give an informative error IF interface NOT implemented
        if (context instanceof FragmentEventList.EventListListener) {
            listener = (FragmentEventList.EventListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " remember to IMPLEMENT listener");
        }
    }

    public void addItemListView(ArrayList<EventItem> list) {
        eventAdapter.clear();
        eventAdapter.addAll(list);
        eventAdapter.notifyDataSetChanged();
    }

    public void clearList() {
        eventAdapter.clear();
        eventAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface EventListListener {
        void onItemClick(EventItem item);
    }

}

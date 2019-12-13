package com.final_project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.final_project.PlaceAdapter;
import com.final_project.PlaceItem;
import com.final_project.R;

import java.util.ArrayList;

public class FragmentSearchPlaceList extends ListFragment {
    PlaceAdapter placeAdapter;
    Context context;
    FragmentSearchPlaceListener listener;
    ListView listView;
    private ArrayList<PlaceItem> items;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_place_list, container, false);
        listView = v.findViewById(android.R.id.list);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        items = new ArrayList<>();

        context = getActivity();
        placeAdapter = new PlaceAdapter(getActivity(), items);
        setListAdapter(placeAdapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        PlaceItem item = items.get(position);
        listener.onPlaceSelected(item);
    }

    public void addItemListView(ArrayList<PlaceItem> list) {
        placeAdapter.clear();
        placeAdapter.addAll(list);
        placeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Just to give an informative error IF interface NOT implemented
        if (context instanceof FragmentSearchPlaceList.FragmentSearchPlaceListener) {
            listener = (FragmentSearchPlaceList.FragmentSearchPlaceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " remember to IMPLEMENT FragmentSearchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface FragmentSearchPlaceListener {
        void onPlaceSelected(PlaceItem input);
    }
}

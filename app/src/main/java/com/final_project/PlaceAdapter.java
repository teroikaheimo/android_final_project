package com.final_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PlaceAdapter extends ArrayAdapter<PlaceItem> {
    private ArrayList<PlaceItem> rowItems;

    public PlaceAdapter(@NonNull Context context, ArrayList<PlaceItem> items) {
        super(context, 0, items);
        this.rowItems = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the item to be displayed
        final PlaceItem item = rowItems.get(position);

        // IF null then inflate the layout. Otherwise just update layout contents.
        if (convertView == null) {
            // Get the custom layout and inflate it
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place, parent, false);
        }

        TextView name = convertView.findViewById(R.id.place_name);
        name.setText(item.getName());


        return convertView;
    }

}

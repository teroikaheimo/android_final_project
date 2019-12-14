package com.final_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<EventItem> {
    private Context context;
    private List<EventItem> rowItems;

    public EventAdapter(@NonNull Context context, ArrayList<EventItem> dates) {
        super(context, 0, dates);
        this.context = context;
        this.rowItems = dates;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the item to be displayed
        final EventItem item = rowItems.get(position);

        // IF null then inflate the layout. Otherwise just update layout contents.
        if (convertView == null) {
            // Get the custom layout and inflate it
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_event, parent, false);
        }

        // Get the customLayout components
        TextView title = convertView.findViewById(R.id.place_name);
        TextView price = convertView.findViewById(R.id.price);
        TextView shortDescription = convertView.findViewById(R.id.short_description);
        TextView startDate = convertView.findViewById(R.id.start_date);
        TextView endDate = convertView.findViewById(R.id.end_date);

        title.setText(item.name);
        price.setText(item.price);
        shortDescription.setText(item.short_description);
        startDate.setText(item.start_time);
        endDate.setText(item.end_time);


        return convertView;
    }
}

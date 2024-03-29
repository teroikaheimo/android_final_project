package com.final_project;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
        TextView eventPlaceName = convertView.findViewById(R.id.event_place_name);
        TextView dateSeparator = convertView.findViewById(R.id.event_date_separator);
        LinearLayout timeLayout = convertView.findViewById(R.id.event_time_layout);
        LinearLayout priceLayout = convertView.findViewById(R.id.event_price_layout);

        title.setText(item.name);

        if (item.price.length() > 0) {
            price.setText(Html.fromHtml(item.price));
        } else {
            priceLayout.setVisibility(View.GONE);
        }
        shortDescription.setText(Html.fromHtml(item.short_description));


        if (item.start_time.length() > 0) {
            if (item.end_time.length() < 1) {
                dateSeparator.setVisibility(View.GONE);
                endDate.setVisibility(View.GONE);
            } else {
                endDate.setText(item.end_time);
            }
            startDate.setText(item.start_time);
        } else {
            timeLayout.setVisibility(View.GONE);
        }
        eventPlaceName.setText(item.place_name);


        return convertView;
    }
}

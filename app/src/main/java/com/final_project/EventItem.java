package com.final_project;

import java.util.ArrayList;

public class EventItem {
    String id; // obj.data[i].id
    String name;  // obj.data[i].name.fi
    String price;  // obj.data[i].offers[0].price.fi IF null = free
    String audience_min_age; // null = for all
    String audience_max_age; // null = for all
    ArrayList<String> imageUrls; // All the images linked to the event
    String description; // obj.data[i].description.fi
    String short_description; // obj.data[i].short_description.fi
    String start_time; // obj.data[i].start_time
    String end_time; // obj.data[i].end_time

    public EventItem(String id, String name, String price, String audience_min_age, String audience_max_age, ArrayList<String> imageUrls, String description, String short_description, String start_time, String end_time) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.audience_min_age = audience_min_age;
        this.audience_max_age = audience_max_age;
        this.imageUrls = imageUrls;
        this.description = description;
        this.short_description = short_description;
        this.start_time = start_time;
        this.end_time = end_time;
    }
}

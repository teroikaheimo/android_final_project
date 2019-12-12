package com.final_project;

public class EventItem {
    String title;  // obj.data[i].name.fi
    String price;  // obj.data[i].offers[0].price.fi
    String shortDescription; // obj.data[i].short_description.fi
    String startDate; // obj.data[i].start_time
    String endDate; // obj.data[i].end_time

    public EventItem(String title, String price, String shortDescription, String startDate, String endDate) {
        this.title = title;
        this.price = price;
        this.shortDescription = shortDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

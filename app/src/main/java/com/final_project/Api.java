package com.final_project;

public class Api {
    public String baseUrl = "https://api.hel.fi/linkedevents/v1/";
    public String placesAll = "place/";
    public String eventsAllToday = "event/?start=today"; // GET ALL events that start today or after.
    public String placesSearch = "search/?type=place&input=";
    public String eventsSearch = "search/?type=event&q=";

    public String getPlacesSearchUrl(){
        return baseUrl+placesSearch;
    }
    public String getPlacesAllUrl(){
        return baseUrl + placesAll;
    }
    public String getEventsUrl(){
        return baseUrl + eventsSearch;
    }

    public String getEventsAllTodayUrl() {
        return baseUrl + eventsAllToday;
    }

    public String formatDateTime(String dt) {
        // 2019-08-13T04:14:15.980685Z -> 13.08.2019   klo 04:14:15
        String date = dt.substring(8, 10) + "." + dt.substring(5, 7) + "." + dt.substring(0, 4);
        String time = "klo " + dt.substring(11, 13) + ":" + dt.substring(14, 16) + ":" + dt.substring(17, 19);
        return date + "  " + time;
    }

}

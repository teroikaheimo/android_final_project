package com.final_project;

public class Api {
    public String baseUrl = "https://api.hel.fi/linkedevents/v1/";
    public String placesAll = "place/";
    public String placesSearch = "search/?type=place&input=";
    public String events = "search/?type=event&q=";

    public String getPlacesSearchUrl(){
        return baseUrl+placesSearch;
    }
    public String getPlacesAllUrl(){
        return baseUrl + placesAll;
    }
    public String getEventsUrl(){
        return baseUrl+events;
    }
}

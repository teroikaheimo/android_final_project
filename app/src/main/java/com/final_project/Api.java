package com.final_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Api {
    public String baseUrl = "https://api.hel.fi/linkedevents/v1/";
    public String placesAll = "place/";
    public String eventsAllToday = "event/?sort=start_time&start=today"; // GET ALL events that start today or after.
    public String placesSearch = "search/?type=place&q=";
    public String eventsSearch = "event/?sort=start_time&start=today";

    public String getPlacesSearchUrl(){
        return baseUrl+placesSearch;
    }
    public String getPlacesAllUrl(){
        return baseUrl + placesAll;
    }

    public String getEventsSearchUrl() {
        return baseUrl + eventsSearch;
    }

    public String getEventsAllTodayUrl() {
        return baseUrl + eventsAllToday;
    }

    public String formatDateTime(String dt) {
        // 2019-08-13T04:14:15.980685Z -> 13.08.2019   klo 04:14:15
        // OR 2019-08-13 -> 13.08.2019
        String formatted;
        if (dt.length() <= 10) {
            return dt.substring(8, 10) + "." + dt.substring(5, 7) + "." + dt.substring(0, 4);
        } else {
            try {
                String date = dt.substring(8, 10) + "." + dt.substring(5, 7) + "." + dt.substring(0, 4);
                String time = "    klo " + dt.substring(11, 13) + ":" + dt.substring(14, 16) + ":" + dt.substring(17, 19);
                formatted = date + time;
            } catch (Error err) {
                Log.d("DATE PARSE ERROR: ", err.toString());
                formatted = "-";
            }
        }
        return formatted;
    }

    public ArrayList<EventItem> jsonArrayToEventArray(JSONArray jsonArr) {
        ArrayList<EventItem> returnData = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {

            try {
                JSONObject jObj = jsonArr.getJSONObject(i);
                String id = jObj.getString("id");

                // Get event name. FI priority
                JSONObject jObjName = jObj.getJSONObject("name");
                String name = "";
                if (jObjName.has("fi")) {
                    name = jObjName.getString("fi");
                } else if (jObjName.has("en")) {
                    name = jObjName.getString("en");
                } else if (jObjName.has("sv")) {
                    name = jObjName.getString("sv");
                }

                // TODO fix price data bug where data might be null or other...
                String price = "Ilmainen";
                if (!jObj.getJSONArray("offers").isNull(0)) {
                    price = jObj.getJSONArray("offers").getJSONObject(0).getJSONObject("price").getString("fi");
                }

                String audience_min_age = jObj.getString("audience_min_age");
                String audience_max_age = jObj.getString("audience_max_age");

                // get event image urls if any.
                JSONArray images = jObj.getJSONArray("images");
                ArrayList<String> imageUrls = new ArrayList<>();
                for (int j = 0; j < images.length(); j++) {
                    imageUrls.add(images.getJSONObject(j).getString("url"));
                }

                // Event description
                String description = "";
                if (!jObj.isNull("description")) {
                    JSONObject jObjDescription = jObj.getJSONObject("description");
                    if (jObjDescription.has("fi")) {
                        description = jObjDescription.getString("fi");
                    } else if (jObjDescription.has("en")) {
                        description = jObjDescription.getString("en");
                    } else if (jObjDescription.has("sv")) {
                        description = jObjDescription.getString("sv");
                    }
                }


                // Event short description
                String short_description = "";
                if (!jObj.isNull("short_description")) {
                    JSONObject jObjShortDescription = jObj.getJSONObject("short_description");
                    if (jObjShortDescription.has("fi")) {
                        short_description = jObjShortDescription.getString("fi");
                    } else if (jObjShortDescription.has("en")) {
                        short_description = jObjShortDescription.getString("en");
                    } else if (jObjShortDescription.has("sv")) {
                        short_description = jObjShortDescription.getString("sv");
                    }
                }


                String start_time = formatDateTime(jObj.getString("start_time"));


                String end_time = "-";
                if (!jObj.isNull("end_time")) {
                    Log.d(" INVALID ", jObj.getString("end_time"));
                    end_time = formatDateTime(jObj.getString("end_time"));
                }

                EventItem event = new EventItem(id, name, price, audience_min_age, audience_max_age, imageUrls, description, short_description, start_time, end_time);
                returnData.add(event);
            } catch (JSONException error) {
                Log.d("JSON Error", error.toString());
            }

        }
        return returnData;
    }

}

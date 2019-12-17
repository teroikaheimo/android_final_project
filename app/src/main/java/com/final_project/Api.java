package com.final_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Api { // Singleton class!
    private static Api api = new Api();
    // Places
    public String places = "place/";
    // Events
    public String events = "event/";
    public String eventsSearch = events;
    private SearchParametersList eventSearchParameters;

    private Api() {
        eventSearchParameters = new SearchParametersList();
        eventSearchParameters.addSearchParameter(new SearchParameter("sort", "start_time")); // show only on going on newer events
        eventSearchParameters.addSearchParameter(new SearchParameter("include", "location")); // include location data to results
        eventSearchParameters.addSearchParameter(new SearchParameter("start", "today")); // default is to fetch only events that are on going or newer.
        eventSearchParameters.addSearchParameter(new SearchParameter("location", "")); // location id:s comma separated. tprek:28473,tprek:34142...
        eventSearchParameters.addSearchParameter(new SearchParameter("end", "")); // end date for events
        eventSearchParameters.addSearchParameter(new SearchParameter("text", "")); // text search
    }
    public String baseUrl = "https://api.hel.fi/linkedevents/v1/";

    public static Api getInstance() {
        return api;
    }
    public String placesSearch = "search/?type=place&q=";

    public String getEventsUrl() {
        return baseUrl + events + eventSearchParameters.getSearchParameterString();
    }

    public boolean updateSearchParameter(String prefix, String value) {
        return eventSearchParameters.updateSearchParameter(prefix, value);
    }

    public String getPlacesSearchUrl(){
        return baseUrl+placesSearch;
    }

    public String getPlacesAllUrl(){
        return baseUrl + places;
    }
    public String getEventsSearchUrl() {
        return baseUrl + eventsSearch;
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


                String price = "-";

                // IF has the is_free property AND event IS free. Do.
                if (jObj.getJSONArray("offers").getJSONObject(0).has("is_free") && jObj.getJSONArray("offers").getJSONObject(0).getBoolean("is_free")) {
                    price = "Ilmainen";
                } else if (jObj.getJSONArray("offers").getJSONObject(0).getJSONObject("price").has("fi")) {
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
                    end_time = formatDateTime(jObj.getString("end_time"));
                }

                String place_name = "";
                if (jObj.getJSONObject("location").getJSONObject("name").has("fi")) {
                    place_name = jObj.getJSONObject("location").getJSONObject("name").getString("fi");
                }

                EventItem event = new EventItem(id, name, price, audience_min_age, audience_max_age, imageUrls, description, short_description, start_time, end_time, place_name);
                returnData.add(event);
            } catch (JSONException error) {
                Log.d("JSON Error", error.toString());
            }

        }
        return returnData;
    }

}

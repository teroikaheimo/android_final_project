package com.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.Fragments.FragmentList;
import com.final_project.Fragments.FragmentSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity implements FragmentSearch.FragmentSearchListener {
    private FragmentList fragmentList;
    String selectedPlaceId;
    String selectedPlaceName;
    private FragmentSearch fragmentSearch;
    private Api api;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        api = new Api();
        requestQueue = Volley.newRequestQueue(this);

        fragmentList = new FragmentList();
        fragmentSearch = new FragmentSearch();

        // Get selected place info from previous intent
        String selectedPlaceId = getIntent().getStringExtra("SELECTED_PLACE_ID");
        String selectedPlaceName = getIntent().getStringExtra("SELECTED_PLACE_NAME");

        Bundle bundle = new Bundle();
        bundle.putString("SELECTED_PLACE_NAME", selectedPlaceName);
        fragmentSearch.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, fragmentList)
                .replace(R.id.search_container, fragmentSearch)
                .commit();

        // At start load recent events
        requestAllEvents();

    }

    @Override
    public void onSearchInputSend(CharSequence input) {

    }

    private ArrayList<EventItem> jsonArrayToEventArray(JSONArray jsonArr) {
        ArrayList<EventItem> returnData = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {

            try {
                JSONObject jObj = jsonArr.getJSONObject(i);
                Log.d(" ** JSON ** ", jObj.toString());
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

                String price = "Ilmainen";
                if (!jObj.getJSONArray("offers").isNull(0)) {
                    price = jObj.getJSONArray("offers").getJSONObject(0).getString("price");
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


                String start_time = api.formatDateTime(jObj.getString("start_time"));


                String end_time = "-";
                if (!jObj.isNull("end_time")) {
                    end_time = api.formatDateTime(jObj.getString("end_time"));
                }

                EventItem event = new EventItem(id, name, price, audience_min_age, audience_max_age, imageUrls, description, short_description, start_time, end_time);
                returnData.add(event);
                Log.d(" **DATA OK**", "ROUND:" + i);
            } catch (JSONException error) {
                Log.d("JSON Error", error.toString());
            }

        }
        return returnData;
    }

    private void requestAllEvents() {
        final Toast loading = Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG);
        loading.show();

        requestQueue.add(
                new JsonObjectRequest
                        (Request.Method.GET, api.getEventsAllTodayUrl(), null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    loading.cancel();
                                    fragmentList.addItemListView(jsonArrayToEventArray(jsonArray));
                                } catch (JSONException err) {
                                    err.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Query failed...", Toast.LENGTH_LONG).show();
                                Log.d(" **Query failed**", error.toString());
                            }
                        }));
    }
}

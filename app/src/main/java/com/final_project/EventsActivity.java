package com.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.Fragments.FragmentEventList;
import com.final_project.Fragments.FragmentEventSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventsActivity extends AppCompatActivity implements FragmentEventSearch.FragmentSearchListener {
    private FragmentEventList fragmentEventList;
    String selectedPlaceId;
    String selectedPlaceName;
    private FragmentEventSearch fragmentEventSearch;
    private Api api;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        api = new Api();
        requestQueue = Volley.newRequestQueue(this);

        fragmentEventList = new FragmentEventList();
        fragmentEventSearch = new FragmentEventSearch();

        // Get selected place info from previous intent
        String selectedPlaceId = getIntent().getStringExtra("SELECTED_PLACE_ID");
        String selectedPlaceName = getIntent().getStringExtra("SELECTED_PLACE_NAME");

        Bundle bundle = new Bundle();
        bundle.putString("SELECTED_PLACE_NAME", selectedPlaceName);
        fragmentEventSearch.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, fragmentEventList)
                .replace(R.id.search_container, fragmentEventSearch)
                .commit();

        requestEventSearch("", selectedPlaceId);


    }

    @Override
    public void onSearchInputSend(CharSequence input) {

    }

    @Override
    public void onClearPlaceClicked() {
        selectedPlaceName = "";
        selectedPlaceId = "";
        requestAllEvents();
    }

    private void requestEventSearch(String searchText, String location) {
        String finalUrl = api.getEventsSearchUrl();
        if (location.length() > 0) {
            finalUrl = finalUrl + "&location=" + location + "&q=" + searchText;
        } else {
            finalUrl = finalUrl + "&q=" + searchText;
        }
        Log.d("Request ULR:", api.getEventsSearchUrl() + searchText);
        final Toast loading = Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG);
        loading.show();

        requestQueue.add(
                new JsonObjectRequest
                        (Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    loading.cancel();
                                    fragmentEventList.addItemListView(api.jsonArrayToEventArray(jsonArray));
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
                        }).setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
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
                                    fragmentEventList.addItemListView(api.jsonArrayToEventArray(jsonArray));
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
                        }).setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }


}

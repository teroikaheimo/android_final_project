package com.final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.Fragments.FragmentPlaceList;
import com.final_project.Fragments.FragmentPlaceSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentPlaceList.FragmentSearchPlaceListener, FragmentPlaceSearch.FragmentSearchPlaceListener {
    ConstraintLayout mainLayout;
    ConnectivityManager connMan;
    private FragmentPlaceList fragmentPlaceList;
    private FragmentPlaceSearch fragmentPlaceSearch;
    // Request HTTP
    private RequestQueue requestQueue;
    private PlaceItem selectedPlace;


    private Api api = Api.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main_layout);

        //// HTTP Requests
        // Get connection manager for internet connection status check.
        connMan = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        requestQueue = Volley.newRequestQueue(this);
        requestAllPlaces();


        //// FRAGMENTS
        fragmentPlaceList = new FragmentPlaceList();
        fragmentPlaceSearch = new FragmentPlaceSearch();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_place_list_container, fragmentPlaceList)
                .replace(R.id.search_place_container, fragmentPlaceSearch)
                .commit();
    }

    @Override
    public void onPlaceSelected(PlaceItem item) {
        Bundle oldBundle = getIntent().getExtras();
        selectedPlace = item;
        Intent intent = new Intent(MainActivity.this, EventsActivity.class);
        oldBundle.putString("SELECTED_PLACE_ID", item.getId());
        oldBundle.putString("SELECTED_PLACE_NAME", item.getName());
        intent.putExtras(oldBundle);
        startActivity(intent);
    }

    @Override
    public void onPlaceSearchInputSend(String input) {
        hideKeyboard();
        requestSearchPlaces(input);
        fragmentPlaceSearch.clearSearch();
    }

    private void requestAllPlaces() {
        final Toast loading = Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG);
        loading.show();
        Log.d(" URL ", api.getPlacesAllUrl());

        requestQueue.add(
                new JsonObjectRequest
                        (Request.Method.GET, api.getPlacesAllUrl(), null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ArrayList<PlaceItem> responceData = new ArrayList<>();
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String name = jsonArray.getJSONObject(i).getJSONObject("name").getString("fi");
                                        String id = jsonArray.getJSONObject(i).getString("id");
                                        PlaceItem place = new PlaceItem(name, id);
                                        responceData.add(place);
                                    }
                                    loading.cancel();
                                    fragmentPlaceList.addItemListView(responceData);
                                } catch (JSONException err) {
                                    Log.d("JSON Error", err.toString());
                                    err.printStackTrace();
                                } catch (Error err) {
                                    Log.d("REQUESTS Error: ", err.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Query failed...", Toast.LENGTH_LONG).show();
                                checkInternetConnection();
                                Log.d(" **Query failed**", error.toString());

                            }
                        }).setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }

    private void requestSearchPlaces(String searchTerms) {
        final Toast loading = Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG);
        loading.show();
        if (searchTerms.length() < 1) {
            return;
        }
        String url = api.getPlacesSearchUrl() + searchTerms;

        Log.d(" ** URL ***", url);
        requestQueue.add(
                new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ArrayList<PlaceItem> responceData = new ArrayList<>();
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String name = jsonArray.getJSONObject(i).getJSONObject("name").getString("fi");
                                        String id = jsonArray.getJSONObject(i).getString("id");
                                        PlaceItem place = new PlaceItem(name, id);
                                        responceData.add(place);
                                    }
                                    loading.cancel();
                                    fragmentPlaceList.addItemListView(responceData);
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


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean checkInternetConnection() {
        // Return true IF there is a active network with internet connection.
        NetworkInfo network = connMan.getActiveNetworkInfo();
        return network != null && network.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        Bundle b = getIntent().getExtras();
        intent.putExtras(b);
        setResult(Activity.RESULT_OK, intent);
    }

}

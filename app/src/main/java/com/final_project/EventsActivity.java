package com.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.Fragments.FragmentDatePicker;
import com.final_project.Fragments.FragmentEventList;
import com.final_project.Fragments.FragmentEventSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventsActivity extends AppCompatActivity implements FragmentEventSearch.FragmentSearchListener, FragmentDatePicker.ListenerDatePicker, FragmentEventList.EventListListener {
    private FragmentEventList fragmentEventList;
    private FragmentEventSearch fragmentEventSearch;
    // State to bundle
    String selectedPlaceId = "";
    private RequestQueue requestQueue;
    String selectedPlaceName = "";
    String startDateIso = "";
    String startDateDisplay = "";
    String endDateIso = "";
    String endDateDisplay = "";
    String searchText = "";
    private Api api = Api.getInstance();
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mainLayout = findViewById(R.id.event_main_layout);
        requestQueue = Volley.newRequestQueue(this);

        fragmentEventList = new FragmentEventList();
        fragmentEventSearch = new FragmentEventSearch();


        // GET state if any
        if (getIntent().getStringExtra("SELECTED_PLACE_ID") != null) {
            selectedPlaceId = getIntent().getStringExtra("SELECTED_PLACE_ID");
            selectedPlaceName = getIntent().getStringExtra("SELECTED_PLACE_NAME");
            startDateIso = getIntent().getStringExtra("START_DATE_ISO");
            startDateDisplay = getIntent().getStringExtra("START_DATE_DISPLAY");
            endDateIso = getIntent().getStringExtra("END_DATE_ISO");
            endDateDisplay = getIntent().getStringExtra("END_DATE_DISPLAY");
            searchText = getIntent().getStringExtra("SEARCH_TEXT");
            updateState();

            Bundle bundle = new Bundle();
            bundle.putString("SELECTED_PLACE_NAME", selectedPlaceName);
            bundle.putString("START_DATE_DISPLAY", startDateDisplay);
            bundle.putString("END_DATE_DISPLAY", endDateDisplay);
            bundle.putString("SEARCH_TEXT", searchText);
            fragmentEventSearch.setArguments(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("TOGGLE_CHOOSE_PLACE", "true");
            fragmentEventSearch.setArguments(bundle);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, fragmentEventList)
                .replace(R.id.search_container, fragmentEventSearch)
                .commit();

        requestEventsUpdate();
    }

    private void updateState() {
        api.updateSearchParameter("location", selectedPlaceId);
        api.updateSearchParameter("start", startDateIso);
        api.updateSearchParameter("end", endDateIso);
        api.updateSearchParameter("text", searchText);

    }

    @Override
    public void onItemClick(EventItem item) {

    }


    @Override
    public void onUserSetDate(int id, int year, int month, int dayOfMonth) {
        switch (id) {
            case 101: // Start date
                startDateDisplay = dayOfMonth + "." + month + "." + year;
                startDateIso = year + "-" + month + "-" + dayOfMonth;
                fragmentEventSearch.setSelectedStartDate(startDateDisplay);
                api.updateSearchParameter("start", startDateIso);
                requestEventsUpdate();
                break;
            case 102: // End Date
                endDateDisplay = dayOfMonth + "." + month + "." + year;
                endDateIso = year + "-" + month + "-" + dayOfMonth;
                fragmentEventSearch.setSelectedEndDate(endDateDisplay);
                api.updateSearchParameter("end", endDateIso);
                requestEventsUpdate();
                break;
        }
    }


    @Override
    public void onSearchInputSend(CharSequence input) {
        searchText = input.toString();
        api.updateSearchParameter("text", searchText);
        requestEventsUpdate();
        hideKeyboard();
    }

    @Override
    public void onClearPlaceClicked() {
        selectedPlaceName = "";
        selectedPlaceId = "";
        api.updateSearchParameter("location", "");
        requestEventsUpdate();
    }

    @Override
    public void onStartDateClicked() {
        FragmentDatePicker picker = new FragmentDatePicker();
        Bundle b = new Bundle();
        b.putInt("PICKER_ID", 101);
        picker.setArguments(b);
        picker.show(getSupportFragmentManager(), "startDatePicker");
    }

    @Override
    public void onEndDateClicked() {
        FragmentDatePicker picker = new FragmentDatePicker();
        Bundle b = new Bundle();
        b.putInt("PICKER_ID", 102);
        picker.setArguments(b);
        picker.show(getSupportFragmentManager(), "endDatePicker");
    }

    @Override
    public void onSelectPlaceClicked() {
        Intent intent = new Intent(EventsActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("SELECTED_PLACE_ID", selectedPlaceId);
        b.putString("SELECTED_PLACE_NAME", selectedPlaceName);
        b.putString("START_DATE_ISO", startDateIso);
        b.putString("START_DATE_DISPLAY", startDateDisplay);
        b.putString("END_DATE_ISO", endDateIso);
        b.putString("END_DATE_DISPLAY", endDateDisplay);
        b.putString("SEARCH_TEXT", searchText);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void requestEventsUpdate() {
        final Toast loading = Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG);
        loading.show();
        Log.d("EVENTS URL", api.getEventsUrl());
        requestQueue.add(
                new JsonObjectRequest
                        (Request.Method.GET, api.getEventsUrl(), null, new Response.Listener<JSONObject>() {

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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

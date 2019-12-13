package com.final_project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.Fragments.FragmentSearch;
import com.final_project.Fragments.FragmentSearchPlace;
import com.final_project.Fragments.FragmentSearchPlaceList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentSearch.FragmentSearchListener, FragmentSearchPlaceList.FragmentSearchPlaceListener, FragmentSearchPlace.FragmentSearchPlaceListener {
    private final String baseUrl = "api.hel.fi/linkedevents/v1/";
    ConstraintLayout mainLayout;
    ConnectivityManager connMan;
    private FragmentSearchPlaceList fragmentSearchPlaceList;
    private FragmentSearchPlace fragmentSearchPlace;
    private FragmentSearch fragmentSearch;
    // Request HTTP
    private RequestQueue requestQueue;
    private PlaceItem selectedPlace;

private Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main_layout);

        //// HTTP Requests
        // Get connection manager for internet connection status check.
        connMan = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        requestQueue = Volley.newRequestQueue(this);


        //// FRAGMENTS
        fragmentSearchPlaceList = new FragmentSearchPlaceList();
        fragmentSearchPlace = new FragmentSearchPlace();
        fragmentSearch = new FragmentSearch();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_place_list_container, fragmentSearchPlaceList)
                .replace(R.id.search_place_container, fragmentSearchPlace)
                .replace(R.id.search_container, fragmentSearch)
                .commit();
    }

    @Override
    public void onSearchInputSend(CharSequence input) {
        // TODO search events
    }

    @Override
    public void onPlaceSelected(PlaceItem item) {
        selectedPlace = item;
        fragmentSearch.updateChosenPlace(item.getName());
    }

    @Override
    public void onPlaceSearchInputSend(CharSequence input) {

    }

    private JsonObjectRequest jsonRequestBuilder(String searchWord) {
        // Exits the program IF search is empty
        if (searchWord.length() < 1) {
            this.finish();
        }
        String requestUrl = baseUrl + searchWord;
        Toast.makeText(getApplicationContext(), "Loading data..", Toast.LENGTH_LONG).show();

        return new JsonObjectRequest
                (Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
                    String imgUrl;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            imgUrl = response.getString("file");

                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Query failed...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearSearch() {
        // TODO clear search
        hideKeyboard();
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
}

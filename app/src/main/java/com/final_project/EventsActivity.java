package com.final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.final_project.Fragments.FragmentList;
import com.final_project.Fragments.FragmentSearch;

public class EventsActivity extends AppCompatActivity implements FragmentSearch.FragmentSearchListener {
    private FragmentList fragmentList;
    String selectedPlaceId;
    String selectedPlaceName;
    private FragmentSearch fragmentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

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


    }

    @Override
    public void onSearchInputSend(CharSequence input) {

    }
}

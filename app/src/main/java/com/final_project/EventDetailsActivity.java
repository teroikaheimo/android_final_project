package com.final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.final_project.Fragments.FragmentEventDetails;

public class EventDetailsActivity extends AppCompatActivity {
    FragmentEventDetails fragmentEventDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        fragmentEventDetails = new FragmentEventDetails();
        fragmentEventDetails.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_detail_fragment_holder, fragmentEventDetails)
                .commit();

    }

}

package com.final_project;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        Bundle b = getIntent().getExtras();
        intent.putExtras(b);
        setResult(Activity.RESULT_OK, intent);
    }
}

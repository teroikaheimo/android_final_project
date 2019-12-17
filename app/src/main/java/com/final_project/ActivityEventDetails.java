package com.final_project;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.final_project.Fragments.FragmentEventDetails;

public class ActivityEventDetails extends AppCompatActivity implements FragmentEventDetails.OnFragmentInteractionListener {
    FragmentEventDetails fragmentEventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        fragmentEventDetails = new FragmentEventDetails();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

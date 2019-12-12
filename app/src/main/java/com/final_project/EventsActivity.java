package com.final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.final_project.Fragments.FragmentList;

public class EventsActivity extends AppCompatActivity {
    private FragmentList fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        fragmentList = new FragmentList();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, fragmentList)
                .commit();


    }
}

package com.final_project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.final_project.Api;
import com.final_project.R;


public class FragmentEventSearch extends Fragment implements View.OnClickListener {
    private FragmentSearchListener listener;
    private EditText searchEditText;
    private TextView chosenPlace, startDate, endDate;
    private Button searchButton, clearPlaceButton, selectPlaceButton;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_search, container, false);
        Api api = Api.getInstance();
        searchEditText = v.findViewById(R.id.search_place_text);
        searchButton = v.findViewById(R.id.search_event_button);
        selectPlaceButton = v.findViewById(R.id.select_place_button);
        clearPlaceButton = v.findViewById(R.id.button_clear_place);
        chosenPlace = v.findViewById(R.id.chosen_place);
        startDate = v.findViewById(R.id.start_date);
        endDate = v.findViewById(R.id.end_date);

        searchButton.setOnClickListener(this);
        clearPlaceButton.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        selectPlaceButton.setOnClickListener(this);

        if (getArguments() != null) {
            if (getArguments().getString("SELECTED_PLACE_NAME") != null) {
                chosenPlace.setText(getArguments().getString("SELECTED_PLACE_NAME"));
                startDate.setText(getArguments().getString("START_DATE_DISPLAY"));
                endDate.setText(getArguments().getString("END_DATE_DISPLAY"));
                searchEditText.setText(getArguments().getString("SEARCH_TEXT"));
            } else {
                // No previous state detected...
                // Set the base value for the startDate picker
                setSelectedStartDate(api.getDateTodayStringDisplay());
            }

            if (getArguments().getString("TOGGLE_CHOOSE_PLACE") != null && getArguments().getString("TOGGLE_CHOOSE_PLACE").equals("true")) {
                selectPlaceButton.setVisibility(View.VISIBLE);
            } else {
                selectPlaceButton.setVisibility(View.GONE);
            }
        }


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_place_button:
                listener.onSelectPlaceClicked();
                break;
            case R.id.search_event_button:
                CharSequence input = searchEditText.getText();
                listener.onSearchInputSend(input);
                break;
            case R.id.button_clear_place:
                chosenPlace.setText("");
                selectPlaceButton.setVisibility(View.VISIBLE);
                listener.onClearPlaceClicked();
                break;
            case R.id.start_date:
                listener.onStartDateClicked();
                break;
            case R.id.end_date:
                listener.onEndDateClicked();
                break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Just to give an informative error IF interface NOT implemented
        if (context instanceof FragmentSearchListener) {
            listener = (FragmentSearchListener) context;
        } else {
            throw new RuntimeException(context.toString() + " remember to IMPLEMENT FragmentSearchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public void setSelectedStartDate(String date) {
        startDate.setText(date);
    }

    public void setSelectedEndDate(String date) {
        endDate.setText(date);
    }
    public interface FragmentSearchListener {
        void onSearchInputSend(CharSequence input);
        void onClearPlaceClicked();

        void onStartDateClicked();

        void onEndDateClicked();

        void onSelectPlaceClicked();
    }
}

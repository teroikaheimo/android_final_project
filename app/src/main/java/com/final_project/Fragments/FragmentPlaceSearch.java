package com.final_project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.final_project.R;

import java.util.List;

public class FragmentPlaceSearch extends Fragment {
    private FragmentSearchPlaceListener listener;
    private EditText searchEditText;
    private Button searchButton;
    private List<String> places;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place_search, container, false);
        searchEditText = v.findViewById(R.id.search_place_text);
        searchButton = v.findViewById(R.id.search_place_button);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = searchEditText.getText().toString();
                listener.onPlaceSearchInputSend(input);
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Just to give an informative error IF interface NOT implemented
        if (context instanceof FragmentSearchPlaceListener) {
            listener = (FragmentSearchPlaceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " remember to IMPLEMENT FragmentPlaceSearch");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void clearSearch() {
        searchEditText.setText("");
    }
    public interface FragmentSearchPlaceListener {
        void onPlaceSearchInputSend(String input);
    }
}

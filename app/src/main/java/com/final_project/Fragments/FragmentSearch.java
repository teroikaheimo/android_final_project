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

import com.final_project.R;

import java.util.List;

public class FragmentSearch extends Fragment {
    private FragmentSearchListener listener;
    private EditText searchEditText;
    private TextView chosenPlace;
    private Button searchButton;
    private List<String> places;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = v.findViewById(R.id.search_place_text);
        searchButton = v.findViewById(R.id.searchButton);
        chosenPlace = v.findViewById(R.id.chosen_place);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = searchEditText.getText();
                listener.onSearchInputSend(input);
            }
        });

        chosenPlace.setText(getArguments().getString("SELECTED_PLACE_NAME"));

        return v;
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

    public interface FragmentSearchListener {
        void onSearchInputSend(CharSequence input);
    }
}

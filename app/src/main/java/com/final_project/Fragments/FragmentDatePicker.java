package com.final_project.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private ListenerDatePicker listener;
    private int pickerId; // Identifies the dialog result

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = this.getArguments();
        if (b != null) {
            pickerId = b.getInt("PICKER_ID");
        }
        final Calendar c = Calendar.getInstance();

        // Default values for the picker
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1; // Because Calendar month is zero based and it needs to be 1-12
        listener.onUserSetDate(pickerId, year, month, dayOfMonth);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Just to give an informative error IF interface NOT implemented
        if (context instanceof FragmentDatePicker.ListenerDatePicker) {
            listener = (FragmentDatePicker.ListenerDatePicker) context;
        } else {
            throw new RuntimeException(context.toString() + " remember to IMPLEMENT the listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ListenerDatePicker {
        void onUserSetDate(int id, int year, int month, int dayOfMonth);
    }
}

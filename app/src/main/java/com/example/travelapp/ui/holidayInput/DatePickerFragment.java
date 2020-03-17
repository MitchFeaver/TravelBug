package com.example.travelapp.ui.holidayInput;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;


import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener dateSetListener; // listener object to get calling fragment listener

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateSetListener = (DatePickerDialog.OnDateSetListener) getTargetFragment(); // getting passed fragment
        Log.d(TAG, "onCreateDialog: " + day + "/" + month + "/" + year);
        return new DatePickerDialog(getContext(), dateSetListener, year, month, day); // DatePickerDialog gets callBack listener as 2nd parameter
        // Create a new instance of DatePickerDialog and return it
    }
}
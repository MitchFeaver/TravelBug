package com.example.travelapp.ui.holidayInput;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.ui.holiday.Holiday;
import com.example.travelapp.ui.holiday.HolidayListAdapter;
import com.example.travelapp.ui.holiday.HolidayViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class HolidayInputFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private HolidayViewModel mHolidayViewModel;
    private Holiday mHoliday;
    private EditText mEditHolidayView;
    private NavController navController;
    private Button startDateButton;
    private Button endDateButton;
    private TextView startDateText;
    private TextView endDateText;
    private TextView holidayDesc;
    private Boolean editHoliday = false;
    private int holidayEditID;

    public HolidayInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
// Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_holiday_input, container, false);
        mEditHolidayView = v.findViewById(R.id.holidayName);
        mHolidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class );

        startDateButton = v.findViewById(R.id.startDateButton);
        startDateText = v.findViewById(R.id.startDateText);
        endDateButton = v.findViewById(R.id.endDateButton);
        endDateText = v.findViewById(R.id.endDateText);
        holidayDesc = v.findViewById(R.id.notesText);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource (R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mEditHolidayView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if(!editHoliday){
                        Holiday h = new Holiday(mEditHolidayView.getText().toString());
                        h.setHolidayMemory(holidayDesc.getText().toString());
                        mHolidayViewModel.insert(h);
                        NavDirections action =
                                HolidayInputFragmentDirections.actionHolidayInputToNavHoliday();
                        Navigation.findNavController(v).navigate(action);
                    }else{
                        Holiday holiday = new Holiday(mEditHolidayView.getText().toString());
                        holiday.setHolidayMemory(holidayDesc.getText().toString());
                        holiday.set_id(holidayEditID);
                        mHolidayViewModel.update(holiday);

                        NavDirections action =
                                HolidayInputFragmentDirections.actionHolidayInputToNavHoliday();
                        Navigation.findNavController(v).navigate(action);
                    }
                }
            });
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),
                        getString(R.string.datepicker));
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),
                        getString(R.string.datepicker));
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            navController = Navigation.findNavController(view);
            holidayEditID = (int) getArguments().getLong("ID");
            mEditHolidayView.setText(getArguments().getString("Name"));
            holidayDesc.setText(getArguments().getString("Memory"));
            editHoliday = true;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { // what should be done when a date is selected
        StringBuilder sb = new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1);
        String formattedDate = sb.toString();
        startDateText.setText(formattedDate);
    }
}
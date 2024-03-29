package com.example.travelapp.ui.holidayInput;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.ui.holiday.Holiday;
import com.example.travelapp.ui.holiday.HolidayViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class HolidayInputFragment extends Fragment {

    private HolidayViewModel mHolidayViewModel;
    private EditText mEditHolidayView;
    private NavController navController;
    private Button startDateButton;
    private Button endDateButton;
    private TextView startDateText;
    private TextView endDateText;
    private TextView holidayDesc;
    private TextView travelBuddy;
    private Boolean editHoliday = false;
    private int holidayEditID;

    private String startDateString;
    private String endDateString;

    private int startDay;
    private int startMonth;
    private int startYear;
    private int endDay;
    private int endMonth;
    private int endYear;
    private Holiday holiday;
    private Boolean endBeforeStart = false;
    DatePickerDialog picker;


    public HolidayInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_holiday_input, container, false);
        mEditHolidayView = v.findViewById(R.id.holidayName);
        mHolidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class );

        startDateButton = v.findViewById(R.id.dateButton);
        startDateText = v.findViewById(R.id.dateText);
        endDateButton = v.findViewById(R.id.endDateButton);
        endDateText = v.findViewById(R.id.endDateText);
        holidayDesc = v.findViewById(R.id.notesText);
        travelBuddy = v.findViewById(R.id.photoLocationText);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource (R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        if (startDateString != null && endDateString != null) {
                            if (dfDate.parse(endDateString).before(dfDate.parse(startDateString))) {
                                endBeforeStart = true;
                            } else {
                                endBeforeStart = false;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(mEditHolidayView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if (TextUtils.isEmpty(startDateText.getText()) ||
                            TextUtils.isEmpty(endDateText.getText())) {
                        Snackbar.make(view, "You need to enter both dates", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if (endBeforeStart) {
                        Snackbar.make(view, "The start date needs to be before the end date", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else {
                        Log.d(TAG, "onClick: !editHoliday");
                        Holiday h = new Holiday(mEditHolidayView.getText().toString());
                        h.setHolidayMemory(holidayDesc.getText().toString());
                        h.setTravelBuddy(travelBuddy.getText().toString());
                        h.setStartDate(startDateText.getText().toString());
                        h.setEndDate(endDateText.getText().toString());
                        h.setStartDateF(startDateString);
                        h.setEndDateF(endDateString);

                        if (editHoliday) {
                            h.set_id(holidayEditID);
                            mHolidayViewModel.update(h);
                        } else {
                            mHolidayViewModel.insert(h);
                        }
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
                final Calendar calendar = Calendar.getInstance();
                startDay = calendar.get(Calendar.DAY_OF_MONTH);
                startMonth = calendar.get(Calendar.MONTH);
                startYear = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // create date variables to make sure end date is after start date.
                                startDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                startDay = dayOfMonth;
                                startMonth = (monthOfYear + 1);
                                startYear = year;
                                startDateString = ((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                                Log.d(TAG, "onDateSet: " + startDateString);
                            }
                        }, startYear, startMonth, startDay);
                picker.show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                endDay = calendar.get(Calendar.DAY_OF_MONTH);
                endMonth = calendar.get(Calendar.MONTH);
                endYear = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // create date variables to make sure end date is after start date.
                                endDay = dayOfMonth;
                                endMonth = monthOfYear + 1;
                                endYear = year;
                                endDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                endDateString = ((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                                Log.d(TAG, "onDateSet: " + endDateString);
                            }
                        }, endYear, endMonth, endDay);
                picker.show();
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                shareHoliday();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareHoliday(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out my holiday: " + mEditHolidayView.getText().toString()
                + ", This is when I went: " + startDateText.getText().toString()
                + " - " + endDateText.getText().toString() + ", This is who I visited with: "
                + travelBuddy.getText().toString() + ", This is how I described it: "
                + holidayDesc.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Send this holiday to:"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            HolidayInputFragmentArgs args = HolidayInputFragmentArgs.fromBundle(getArguments());
            holiday = args.getHoliday();
            holidayEditID = holiday.get_id();
            mEditHolidayView.setText(holiday.getName());
            holidayDesc.setText(holiday.getHolidayMemory());
            travelBuddy.setText(holiday.getTravelBuddy());
            startDateText.setText(holiday.getStartDate());
            endDateText.setText(holiday.getEndDate());
            startDateString = holiday.getStartDateF();
            endDateString = holiday.getEndDateF();
            editHoliday = true;

        }
    }
}
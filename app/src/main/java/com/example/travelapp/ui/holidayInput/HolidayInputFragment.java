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
        travelBuddy = v.findViewById(R.id.location);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource (R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mEditHolidayView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    }else if (TextUtils.isEmpty(startDateText.getText()) ||
                                    TextUtils.isEmpty(endDateText.getText())){
                        Snackbar.make(view, "You need to enter both dates", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else {
                        Log.d(TAG, "onClick: !editHoliday");
                        Holiday h = new Holiday(mEditHolidayView.getText().toString());
                        h.setHolidayMemory(holidayDesc.getText().toString());
                        h.setTravelBuddy(travelBuddy.getText().toString());
                        h.setStartDate(startDateText.getText().toString());
                        h.setEndDate(endDateText.getText().toString());

                    if (editHoliday){
                        h.set_id(holidayEditID);
                        mHolidayViewModel.update(h);
                    }else{
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
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        
        endDateButton.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
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
            navController = Navigation.findNavController(view);
            holidayEditID = (int) getArguments().getLong("ID");
            mEditHolidayView.setText(getArguments().getString("Name"));
            holidayDesc.setText(getArguments().getString("Memory"));
            travelBuddy.setText(getArguments().getString("TravelBuddy"));
            startDateText.setText(getArguments().getString("StartDate"));
            endDateText.setText(getArguments().getString("EndDate"));
            editHoliday = true;
        }
    }
}
package com.example.travelapp.ui.holidayInput;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.ui.holiday.Holiday;
import com.example.travelapp.ui.holiday.HolidayViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HolidayInputFragment extends Fragment {
    private HolidayViewModel mHolidayViewModel ;
    private EditText mEditHolidayView;
    private NavController navController;
    private TextView startDateText;
    private TextView endDateText;

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
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource (R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View . OnClickListener () {
                @Override
                public void onClick ( View view ) {
                    if (TextUtils.isEmpty(mEditHolidayView.getText())) {
                        Snackbar.make(view, " You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction(" Action ",null).show();
                    } else {
                        Holiday h = new Holiday(mEditHolidayView.getText().toString());
                        mHolidayViewModel.insert (h);
                        NavDirections action =
                                HolidayInputFragmentDirections.actionHolidayInputToNavHoliday();
                        Navigation.findNavController(v).navigate(action);
                    }
                }
            });
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}


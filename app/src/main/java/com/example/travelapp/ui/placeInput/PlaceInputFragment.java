package com.example.travelapp.ui.placeInput;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.ui.place.Place;
import com.example.travelapp.ui.place.PlaceViewModel;
import com.example.travelapp.ui.placeInput.PlaceInputFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class PlaceInputFragment extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int LOCATION_PERMISSION_CODE = 2;
    private static final int IMAGE_REQUEST_CODE = 3;

    private PlaceViewModel mPlaceViewModel;
    private EditText mEditPlaceView;
    private NavController navController;
    private Button dateButton;
    private Button currentLocationButton;
    private Button addImageButton;
    private TextView dateText;
    private TextView placeDesc;
    private TextView location;
    private Boolean editPlace = false;
    private int placeEditID;
    private ImageView imageView;

    DatePickerDialog picker;

    public PlaceInputFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_place_input, container, false);
        mEditPlaceView = v.findViewById(R.id.placeName);
        mPlaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class );

        dateButton = v.findViewById(R.id.dateButton);
        dateText = v.findViewById(R.id.dateText);
        placeDesc = v.findViewById(R.id.notesText);
        location = v.findViewById(R.id.location);
//        currentLocationButton = v.findViewById(R.id.currentLocationButton);
        addImageButton = v.findViewById(R.id.addImageButton);
        imageView = v.findViewById(R.id.imageView);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource (R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mEditPlaceView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if (TextUtils.isEmpty(dateText.getText())) {
                        Snackbar.make(view, "You need to enter both dates", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else {
                        Log.d(TAG, "onClick: !editPlace");
                        Place h = new Place(mEditPlaceView.getText().toString());
                        h.setPlaceMemory(placeDesc.getText().toString());
                        h.setLocation(location.getText().toString());
                        h.setDate(dateText.getText().toString());

                        if (editPlace){
                            h.set_id(placeEditID);
                            mPlaceViewModel.update(h);
                        }else{
                            mPlaceViewModel.insert(h);
                        }
                        NavDirections action =
                                PlaceInputFragmentDirections.actionPlaceInputToNavPlace();
                        Navigation.findNavController(v).navigate(action);
                    }
                }
            });
        }

        dateButton.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
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
                                dateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                } else {
                    requestStoragePermission();
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            navController = Navigation.findNavController(view);
            placeEditID = (int) getArguments().getLong("ID");
            mEditPlaceView.setText(getArguments().getString("Name"));
            placeDesc.setText(getArguments().getString("Memory"));
            location.setText(getArguments().getString("TravelBuddy"));
            dateText.setText(getArguments().getString("Date"));
            editPlace = true;
        }

        // here need to get all holidays to populate spinner
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    private void requestLocationPermission() {
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.}, STORAGE_PERMISSION_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " + Result code: " + resultCode);

        if(requestCode == IMAGE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }
}
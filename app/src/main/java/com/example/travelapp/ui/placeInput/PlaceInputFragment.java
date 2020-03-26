package com.example.travelapp.ui.placeInput;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.ui.holiday.Holiday;
import com.example.travelapp.ui.holiday.HolidayViewModel;
import com.example.travelapp.ui.place.Place;
import com.example.travelapp.ui.place.PlaceViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlaceInputFragment extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int LOCATION_PERMISSION_CODE = 2;
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final String TAG = "PlaceInputFragment";

    private PlaceViewModel mPlaceViewModel;
    private EditText mEditPlaceView;
    private Button dateButton;
    private Button currentLocationButton;
    private Button addImageButton;
    private TextView dateText;
    private TextView placeDesc;
    private EditText locationText;
    private Boolean editPlace = false;
    private int placeEditID;
    private ImageView imageView;
    private Uri selectedImageUri;
    private DatePickerDialog picker;
    private AutocompleteSupportFragment autocompleteFragment;
    private PlacesClient placesClient;
    private String selectedPlace;
    private LatLng latLng;
    private HolidayViewModel holidayViewModel;
    private List<String> holidayNames;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Double latitude;
    private Double longitude;
    private String holidayName;
    private Place place;

    public PlaceInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_place_input, container, false);
        mEditPlaceView = v.findViewById(R.id.placeName);
        mPlaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);

        holidayNames = new ArrayList<>();
        spinner = v.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, holidayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d(TAG, "onCreateView: Spinner adapter set.");

        dateButton = v.findViewById(R.id.dateButton);
        dateText = v.findViewById(R.id.dateText);
        placeDesc = v.findViewById(R.id.notesText);
        currentLocationButton = v.findViewById(R.id.currentLocationButton);
        addImageButton = v.findViewById(R.id.addImageButton);
        imageView = v.findViewById(R.id.imageView);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mEditPlaceView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if (TextUtils.isEmpty(dateText.getText())) {
                        Snackbar.make(view, "You need to enter a date", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else if (selectedPlace == null || selectedPlace == "") {
                        Snackbar.make(view, "You need to enter a location", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else {
                        Place p = new Place(mEditPlaceView.getText().toString());
                        p.setPlaceMemory(placeDesc.getText().toString());
                        p.setLocation(selectedPlace);
                        p.setDate(dateText.getText().toString());
                        p.setPlaceHoliday(spinner.getSelectedItem().toString());
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        Log.d(TAG, "onViewCreated: " + latitude + " : " + longitude);
                        p.setLatitude(latitude);
                        p.setLongitude(longitude);
                        if (selectedImageUri != null) {
                            p.setImage(selectedImageUri.toString());
                        } else {
                            p.setImage("");
                        }

                        if (editPlace) {
                            p.set_id(placeEditID);
                            mPlaceViewModel.update(p);
                        } else {
                            mPlaceViewModel.insert(p);
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
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                } else {
                    requestStoragePermission();
                }
            }
        });

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        selectedPlace = hereLocation(location.getLatitude(), location.getLongitude());
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        locationText.setText(selectedPlace);
                        Log.d(TAG, "CurrentLocationButton" + selectedPlace);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestLocationPermission();
                }
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
                sharePlace();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePlace() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this place: " + mEditPlaceView.getText().toString()
                + ", Its at the location: " + locationText.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            PlaceInputFragmentArgs args = PlaceInputFragmentArgs.fromBundle(getArguments());
            place = args.getPlace();
            placeEditID = place.get_id();
            mEditPlaceView.setText(place.getName());
            placeDesc.setText(place.getPlaceMemory());
            selectedPlace = place.getLocation();
            dateText.setText(place.getDate());
            selectedImageUri = Uri.parse(place.getImage());
            Picasso.with(getContext()).load(selectedImageUri).into(imageView);
            latitude = place.getLatitude();
            longitude = place.getLongitude();
            latLng = new LatLng(latitude, longitude);
            editPlace = true;
            holidayName = place.getPlaceHoliday();
        }

        holidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class);
        holidayViewModel.getAllHolidays().observe(this, new Observer<List<Holiday>>() {
            @Override
            public void onChanged(@Nullable final List<Holiday> holidays) {
                for (Holiday holiday : holidays) {
                    holidayNames.add(holiday.getName());
                    Log.d(TAG, "onChanged: " + holiday.getName());
                }
                adapter.notifyDataSetChanged();

                if (holidayName != null) {
                    int i = holidayNames.indexOf(holidayName);
                    spinner.setSelection(i);
                }
            }
        });

        Places.initialize(getContext(), "AIzaSyBH41wZLc_0fN1BdxX_uCa4ION1gS8Uf6g");
        placesClient = Places.createClient(getContext());
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.photoLocationText);

        autocompleteFragment.setPlaceFields(Arrays.asList(
                com.google.android.libraries.places.api.model.Place.Field.NAME,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
        ));

        locationText = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        locationText.setTextSize(16.0f);
        locationText.setText(selectedPlace);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(com.google.android.libraries.places.api.model.Place place) {
                selectedPlace = place.getName();
                latLng = place.getLatLng();
                Log.d(TAG, "onPlaceSelected: " + selectedPlace);
                locationText.setText(selectedPlace);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.d(TAG, "onClick: location2");
                    try {
                        selectedPlace = hereLocation(location.getLatitude(), location.getLongitude());
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        locationText.setText(selectedPlace);
                        Log.d(TAG, "onClick: setText2" + selectedPlace);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: failText2");
                    }
                } else {
                    Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String hereLocation(double lat, double lon) {
        String cityName = "";
        Log.d(TAG, "hereLocation: enter");
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 5);
            if (addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getSubLocality() != null && adr.getSubLocality().length() > 0) {
                        cityName = adr.getSubLocality() + ", " + adr.getCountryName();
                        break;
                    } else if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityName = adr.getLocality() + ", " + adr.getCountryName();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "hereLocation: " + cityName);
        return cityName;
    }
}

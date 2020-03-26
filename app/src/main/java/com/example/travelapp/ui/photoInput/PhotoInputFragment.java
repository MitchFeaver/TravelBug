package com.example.travelapp.ui.photoInput;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.ui.holiday.Holiday;
import com.example.travelapp.ui.holiday.HolidayViewModel;
import com.example.travelapp.ui.holidayInput.HolidayInputFragmentDirections;
import com.example.travelapp.ui.photo.Photo;
import com.example.travelapp.ui.photo.PhotoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PhotoInputFragment extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_CODE = 2;
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int CAMERA_PERMISSION_CODE = 4;

    private PhotoViewModel mPhotoViewModel;
    private EditText mEditPhotoView;
    private Boolean editPhoto = false;
    private ImageView imageViewGallery;
    private Button choosePhotoButton;
    private Button takePhotoButton;
    private int photoEditID;
    private Uri selectedImageUri;
    private String mCurrentPhotoPath;
    private Photo photo;
    private String holidayName;
    private HolidayViewModel holidayViewModel;
    private List<String> holidayNames;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;


    public PhotoInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_photo_input, container, false);
        mEditPhotoView = v.findViewById(R.id.photoName);
        mPhotoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);

        holidayNames = new ArrayList<>();
        spinner = v.findViewById(R.id.spinnerPhoto);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, holidayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d(TAG, "onCreateView: Spinner adapter set.");

        choosePhotoButton = v.findViewById(R.id.chosePhotoButton);
        takePhotoButton = v.findViewById(R.id.takePhotoButton);
        imageViewGallery = v.findViewById(R.id.imageViewGallery);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_save_black_24dp);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mEditPhotoView.getText())) {
                        Snackbar.make(view, "You need to enter a name", Snackbar.LENGTH_LONG)
                                .setAction("Action ", null).show();
                    } else {
                        Photo photo = new Photo(mEditPhotoView.getText().toString());
                        photo.setHolidayName(spinner.getSelectedItem().toString());
                        if (selectedImageUri != null) {
                            photo.setPhotoURL(selectedImageUri.toString());
                        } else {
                            photo.setPhotoURL("");
                        }
                        if (editPhoto) {
                            photo.set_id(photoEditID);
                            mPhotoViewModel.update(photo);
                        } else {
                            mPhotoViewModel.insert(photo);
                        }
                        NavDirections action =
                                PhotoInputFragmentDirections.actionPhotoInputToNavPhoto();
                        Navigation.findNavController(v).navigate(action);
                    }
                }
            });
        }

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
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

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageFile = null;

                    try{
                        imageFile = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null){
                        Uri imageUri = FileProvider.getUriForFile(getContext(), "com.example.travelapp.provider", imageFile);
                        imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
                    }

                } else {
                    requestCameraPermission();
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
                sharePhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePhoto(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
        sendIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(sendIntent, "Send this photo to:"));
    }

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            PhotoInputFragmentArgs args = PhotoInputFragmentArgs.fromBundle(getArguments());
            photo = args.getPhoto();
            photoEditID = photo.get_id();
            mEditPhotoView.setText(photo.getPhotoName());
            selectedImageUri = Uri.parse(photo.getPhotoURL());
            imageViewGallery.setImageURI(selectedImageUri);
            editPhoto = true;
            holidayName = photo.getHolidayName();
        }

        holidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class);
        holidayViewModel.getAllHolidays().observe(this, new Observer<List<Holiday>>() {
            @Override
            public void onChanged(@Nullable final List<Holiday> holidays) {
                for (Holiday holiday : holidays) {
                    holidayNames.add(holiday.getName());
                    Log.d(TAG, "onChanged: " +  holiday.getName());
                }
                adapter.notifyDataSetChanged();
                if (holidayName != null) {
                    int i = holidayNames.indexOf(holidayName);
                    spinner.setSelection(i);
                }
            }
        });
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            imageViewGallery.setImageURI(selectedImageUri);
        } else if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            imageViewGallery.setImageBitmap(bitmap);
            selectedImageUri = Uri.parse(mCurrentPhotoPath);
        }
    }
}
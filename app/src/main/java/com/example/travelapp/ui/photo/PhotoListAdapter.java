package com.example.travelapp.ui.photo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Photo> mPhotos; // Cached copy of photos
    private OnPhotoListener mOnPhotoListener;
    private boolean editPhoto = false;

    PhotoListAdapter(Context context, OnPhotoListener onPhotoListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnPhotoListener = onPhotoListener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.photo_item, parent, false);

        return new PhotoViewHolder(itemView, mOnPhotoListener);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        if (mPhotos != null) {
            Photo current = mPhotos.get(position);
            holder.imageName.setText(current.getPhotoName());
            holder.photoImage.setImageURI(Uri.parse(current.getPhotoURL()));
        } else {
            // Covers the case of data not being ready yet.
            holder.imageName.setText("No Photo");
        }
    }

    void setPhotos(List<Photo> photos){
        mPhotos = photos;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPhotos != null)
            return mPhotos.size();
        else return 0;
    }

    public Photo getPhotoAtPosition(int position) {
        return mPhotos.get(position);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView imageName;
        private ImageView photoImage;
        OnPhotoListener onPhotoListener;

        private PhotoViewHolder(View itemView, OnPhotoListener onPhotoListener) {
            super(itemView);
            imageName = itemView.findViewById(R.id.photoName);
            photoImage = itemView.findViewById(R.id.photoImage);
            this.onPhotoListener = onPhotoListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPhotoListener.onPhotoClick(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putLong("ID", mPhotos.get(getAdapterPosition()).get_id());
            bundle.putString("Name", mPhotos.get(getAdapterPosition()).getPhotoName());
            bundle.putString("PhotoURL", mPhotos.get(getAdapterPosition()).getPhotoURL());
            bundle.putString("HolidayName", mPhotos.get(getAdapterPosition()).getHolidayName());
            Navigation.findNavController(itemView).navigate(R.id.photo_input, bundle);
        }
    }

    public interface OnPhotoListener{
        void onPhotoClick(int position);
    }
}

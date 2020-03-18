package com.example.travelapp.ui.place;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.ui.place.Place;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private final LayoutInflater mInflater;
    private List<Place> mPlaces; // Cached copy of places
    private OnPlaceListener mOnPlaceListener;

    PlaceListAdapter(Context context, OnPlaceListener onPlaceListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnPlaceListener = onPlaceListener;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new PlaceViewHolder(itemView, mOnPlaceListener);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        if (mPlaces != null) {
            Place current = mPlaces.get(position);
            holder.placeItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.placeItemView.setText("No Place");
        }
    }

    void setPlaces(List<Place> places){
        mPlaces = places;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlaces != null)
            return mPlaces.size();
        else return 0;
    }

    public Place getPlaceAtPosition(int position) {
        return mPlaces.get(position);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView placeItemView;
        OnPlaceListener onPlaceListener;

        private PlaceViewHolder(View itemView, OnPlaceListener onPlaceListener) {
            super(itemView);
            placeItemView = itemView.findViewById(R.id.textView);
            this.onPlaceListener = onPlaceListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPlaceListener.onPlaceClick(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putLong("ID", mPlaces.get(getAdapterPosition()).get_id());
            bundle.putString("Name", mPlaces.get(getAdapterPosition()).getName());
            bundle.putString("Memory", mPlaces.get(getAdapterPosition()).getPlaceMemory());
            bundle.putString("Location", mPlaces.get(getAdapterPosition()).getLocation());
            bundle.putString("Date", mPlaces.get(getAdapterPosition()).getDate());
            bundle.putString("Image", mPlaces.get(getAdapterPosition()).getImage());
            Navigation.findNavController(itemView).navigate(R.id.place_input, bundle);
        }
    }

    public interface OnPlaceListener{
        void onPlaceClick(int position);
    }
}

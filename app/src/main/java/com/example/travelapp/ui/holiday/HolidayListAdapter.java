package com.example.travelapp.ui.holiday;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.ui.holidayInput.HolidayInputFragment;

import java.util.List;

import static android.content.ContentValues.TAG;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder> {

    private final LayoutInflater mInflater;
    private List<Holiday> mHolidays; // Cached copy of holidays
    private OnHolidayListener mOnHolidayListener;

    HolidayListAdapter(Context context, OnHolidayListener onHolidayListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnHolidayListener = onHolidayListener;
    }

    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new HolidayViewHolder(itemView, mOnHolidayListener);
    }

    @Override
    public void onBindViewHolder(HolidayViewHolder holder, int position) {
        if (mHolidays != null) {
            Holiday current = mHolidays.get(position);
            holder.holidayItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.holidayItemView.setText("No Holiday");
        }
    }

    void setHolidays(List<Holiday> holidays){
        mHolidays = holidays;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mHolidays != null)
            return mHolidays.size();
        else return 0;
    }

    public Holiday getHolidayAtPosition(int position) {
        return mHolidays.get(position);
    }

    class HolidayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView holidayItemView;
        OnHolidayListener onHolidayListener;

        private HolidayViewHolder(View itemView, OnHolidayListener onHolidayListener) {
            super(itemView);
            holidayItemView = itemView.findViewById(R.id.textView);
            this.onHolidayListener = onHolidayListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHolidayListener.onHolidayClick(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putLong("ID", mHolidays.get(getAdapterPosition()).get_id());
            bundle.putString("Name", mHolidays.get(getAdapterPosition()).getName());
            bundle.putString("Memory", mHolidays.get(getAdapterPosition()).getHolidayMemory());

            Navigation.findNavController(itemView).navigate(R.id.holiday_input, bundle);
        }
    }

    public interface OnHolidayListener{
        void onHolidayClick(int position);
    }
}

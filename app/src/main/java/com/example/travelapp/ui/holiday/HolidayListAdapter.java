package com.example.travelapp.ui.holiday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;

import java.util.List;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder> {

    private final LayoutInflater mInflater;
    private List<Holiday> mHolidays; // Cached copy of holidays

    HolidayListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new HolidayViewHolder(itemView);
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

    class HolidayViewHolder extends RecyclerView.ViewHolder {
        private final TextView holidayItemView;

        private HolidayViewHolder(View itemView) {
            super(itemView);
            holidayItemView = itemView.findViewById(R.id.textView);
        }
    }
}

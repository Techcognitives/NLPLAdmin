package com.example.nlpladmin.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nlpladmin.R;
import com.example.nlpladmin.model.DriverModel;
import com.example.nlpladmin.ui.activity.ViewTruckDetailsActivity;

import java.util.ArrayList;

public class DriversListAdapter extends RecyclerView.Adapter<DriversListAdapter.DriverViewHolder> {

    private ArrayList<DriverModel> driverList;
    private ViewTruckDetailsActivity activity;

    public DriversListAdapter(ViewTruckDetailsActivity activity, ArrayList<DriverModel> driverList) {
        this.driverList = driverList;
        this.activity = activity;
    }

    @Override
    public DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_single, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DriverModel obj = driverList.get(position);

        holder.list_title.setText(" " + obj.getDriver_name());
        holder.list_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onClickReAssignDriver(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public void updateData(ArrayList<DriverModel> driverList) {
        this.driverList = driverList;
        notifyDataSetChanged();
    }

    public class DriverViewHolder extends RecyclerView.ViewHolder {
        private TextView list_title;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.text1);
        }

    }
//--------------------------------------------------------------------------------------------------
}
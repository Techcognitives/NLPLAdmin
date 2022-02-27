package com.example.nlpladmin.ui.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nlpladmin.R;
import com.example.nlpladmin.model.TruckModel;
import com.example.nlpladmin.ui.activity.ViewTruckDetailsActivity;

import java.util.ArrayList;

public class TrucksAdapter extends RecyclerView.Adapter<TrucksAdapter.TruckViewHolder> {

    private ArrayList<TruckModel> truckList;
    private ViewTruckDetailsActivity activity;

    public TrucksAdapter(ViewTruckDetailsActivity activity, ArrayList<TruckModel> truckList) {
        this.truckList = truckList;
        this.activity = activity;
    }

    @Override
    public TruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_truck_list, parent, false);
        return new TruckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TruckViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TruckModel obj = truckList.get(position);
//---------------------------------- Set Title -----------------------------------------------------
        String name1 = obj.getVehicle_no();
        Log.i("File Name:", name1);

        holder.list_title.setText(" " + name1);
//--------------------------------------------------------------------------------------------------
        holder.list_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getTruckDetails(obj);
            }
        });

        holder.list_preview_rc_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getOnClickPreviewTruckRcBook(obj);
            }
        });

        holder.list_preview_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getOnClickPreviewTruckInsurance(obj);
            }
        });

        holder.list_view_driver_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getDriverDetailsOnTruckActivity(obj);
            }
        });

        holder.list_truck_type.setText("Model: " + obj.getTruck_type());
        holder.list_feet.setText("Feet: " + obj.getTruck_ft());
        holder.list_capacity.setText("Capacity: " + obj.getTruck_carrying_capacity());
        holder.list_vehicle_type.setText("Body: " + obj.getVehicle_type());
    }

    @Override
    public int getItemCount() {
        return truckList.size();
    }

    public void updateData(ArrayList<TruckModel> truckList) {
        this.truckList = truckList;
        notifyDataSetChanged();
    }

    public class TruckViewHolder extends RecyclerView.ViewHolder {
        private TextView list_view_driver_details,  list_title, list_edit, list_truck_type, list_feet, list_capacity, list_vehicle_type, list_preview_rc_book, list_preview_insurance;

        public TruckViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.my_truck_list_vehicle_number_text_view);
            list_edit = itemView.findViewById(R.id.my_truck_list_edit_text_view);
            list_preview_rc_book = itemView.findViewById(R.id.my_truck_list_preview_rc_book_text_view);
            list_preview_insurance = itemView.findViewById(R.id.my_truck_list_preview_insurance_text_view);
            list_truck_type = itemView.findViewById(R.id.my_truck_list_truck_type);
            list_feet = itemView.findViewById(R.id.my_truck_list_ft);
            list_capacity = itemView.findViewById(R.id.my_truck_list_capacity);
            list_vehicle_type = itemView.findViewById(R.id.my_truck_list_vehicle_type);
            list_view_driver_details = itemView.findViewById(R.id.my_truck_list_driver_details);

        }

    }
//--------------------------------------------------------------------------------------------------
}
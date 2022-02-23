package com.example.nlpladmin.ui.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nlpladmin.R;
import com.example.nlpladmin.model.UserResponses;
import com.example.nlpladmin.ui.activity.DashboardActivity;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.UserViewHolder> {

    private ArrayList<UserResponses> userList;
    private DashboardActivity activity;

    public DashboardAdapter(DashboardActivity activity, ArrayList<UserResponses> userList) {
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserResponses obj = userList.get(position);
//---------------------------------- Get Details ---------------------------------------------------
        String name = obj.getName();
        String mobile = obj.getPhone_number();
        String email = obj.getEmail_id();
        String role = obj.getUser_type();
        String address = obj.getAddress() + ", " + obj.getPreferred_location() + ", " + obj.getState_code() + ", " + obj.getPin_code();

//---------------------------------- Set Details ---------------------------------------------------
        holder.user_name.setText(name);
        holder.user_mobile.setText("+" + mobile);
        holder.user_role.setText("User Role: " + role);
        holder.user_address.setText("Address: " + address);

        if (email != null) {
            holder.user_email.setText(email);
            holder.user_email.setVisibility(View.VISIBLE);
        } else {
            holder.user_email.setVisibility(View.GONE);
        }

        if (role.equals("Customer")) {
            holder.user_truck.setVisibility(View.GONE);
            holder.user_driver.setVisibility(View.GONE);
            holder.truck_status.setVisibility(View.GONE);
            holder.driver_status.setVisibility(View.GONE);
        } else {
            holder.user_truck.setVisibility(View.VISIBLE);
            holder.user_driver.setVisibility(View.VISIBLE);
            holder.truck_status.setVisibility(View.VISIBLE);
            holder.driver_status.setVisibility(View.VISIBLE);
        }

        holder.user_email.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(ArrayList<UserResponses> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView user_name, user_mobile, user_email, user_role, user_address, user_profile, user_bank, user_truck, user_driver, user_view_details;
        private ImageView profile_status, bank_status, truck_status, driver_status;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_list_name);
            user_mobile = itemView.findViewById(R.id.user_list_number);
            user_email = itemView.findViewById(R.id.user_list_email);
            user_role = itemView.findViewById(R.id.user_list_role);
            user_address = itemView.findViewById(R.id.user_list_address);
            user_profile = itemView.findViewById(R.id.user_list_profile_added);
            user_bank = itemView.findViewById(R.id.user_list_bank_added);
            user_truck = itemView.findViewById(R.id.user_list_truck_added);
            user_driver = itemView.findViewById(R.id.user_list_driver_added);
            user_view_details = itemView.findViewById(R.id.user_list_view_and_edit_button);

            profile_status = itemView.findViewById(R.id.user_list_profile_added_image);
            bank_status = itemView.findViewById(R.id.user_list_bank_added_image);
            truck_status = itemView.findViewById(R.id.user_list_truck_added_image);
            driver_status = itemView.findViewById(R.id.user_list_driver_added_image);
        }

    }
//--------------------------------------------------------------------------------------------------
}

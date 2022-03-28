package com.example.nlpladmin.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.UserResponses;
import com.example.nlpladmin.ui.activity.DashboardActivity;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.UserViewHolder> {

    private ArrayList<UserResponses> userList;
    private DashboardActivity activity;
    private RequestQueue mQueue;

    public DashboardAdapter(DashboardActivity activity, ArrayList<UserResponses> userList) {
        this.userList = userList;
        this.activity = activity;
        mQueue = Volley.newRequestQueue(activity);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserResponses obj = userList.get(position);

//---------------------------------- Set Details ---------------------------------------------------
        holder.user_name.setText(obj.getName());
        holder.user_mobile.setText("+" + obj.getPhone_number());
        if (obj.getUser_type().equals("Customer")){
            holder.user_role.setText("Load Poster");
        } else {
            holder.user_role.setText(obj.getUser_type());
        }

        holder.viewUser.setText("View");

        holder.user_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onClickOpenDiler("+"+obj.getPhone_number());
            }
        });


//        String url1 = activity.getString(R.string.baseURL) + "/imgbucket/Images/" + obj.getUser_id();
//        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray imageList = response.getJSONArray("data");
//                    for (int i = 0; i < imageList.length(); i++) {
//                        JSONObject obj = imageList.getJSONObject(i);
//                        String imageType = obj.getString("image_type");
//
//                        String profileImgUrl = "";
//                        if (imageType.equals("profile")) {
//                            profileImgUrl = obj.getString("image_url");
//                            if (profileImgUrl.equals("null")){
//                                holder.profile.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue_profile_small));
//                            } else {
//                                new DownloadImageTask(holder.profile).execute(profileImgUrl);
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        mQueue.add(request1);


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.ViewProfileOfUser(obj);
            }
        });

        holder.viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpTo.viewUserDetailsActivity(activity, obj.getUser_id());
            }
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
        private TextView user_name, user_mobile,  user_role, viewUser;
        private ImageView profile;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.users_list_profilePhto);
            user_name = itemView.findViewById(R.id.users_list_name);
            user_mobile = itemView.findViewById(R.id.user_list_number);
            user_role = itemView.findViewById(R.id.users_list_role);
            viewUser = itemView.findViewById(R.id.users_list_view_user);
            viewUser.setVisibility(View.VISIBLE);
        }

    }
//--------------------------------------------------------------------------------------------------
}

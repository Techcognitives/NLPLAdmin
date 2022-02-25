package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityDashboardBinding;
import com.example.nlpladmin.model.UserResponses;
import com.example.nlpladmin.ui.adapter.DashboardAdapter;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    SwipeRefreshLayout swipeRefreshLayout;

    private RequestQueue mQueue;
    private ArrayList<UserResponses> userResponsesArrayList = new ArrayList<>();
    private DashboardAdapter dashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.setHandlers(DashboardActivity.this);

        mQueue = Volley.newRequestQueue(DashboardActivity.this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.dashboard_constrain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                JumpTo.dashboardActivity(DashboardActivity.this);
            }
        });

        LinearLayoutManager linearLayoutManagerBank = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerBank.setReverseLayout(true);
        binding.usersListView.setLayoutManager(linearLayoutManagerBank);
        binding.usersListView.setHasFixedSize(true);

        dashboardAdapter = new DashboardAdapter(DashboardActivity.this, userResponsesArrayList);
        binding.usersListView.setAdapter(dashboardAdapter);
        getUsersList();
    }

    public void getUsersList() {
        //---------------------------- Get User Details --------------------------------------------
        String url = getString(R.string.baseURL) + "/user/get";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userResponsesArrayList = new ArrayList<>();
                    JSONArray bankLists = response.getJSONArray("data");
                    for (int i = 0; i < bankLists.length(); i++) {
                        JSONObject obj = bankLists.getJSONObject(i);
                        UserResponses userResponses = new UserResponses();

                        userResponses.setUser_id(obj.getString("user_id"));
                        userResponses.setName(obj.getString("name"));
                        userResponses.setPhone_number(obj.getString("phone_number"));
                        userResponses.setIsProfile_pic_added(obj.getString("isProfile_pic_added"));
                        userResponses.setUser_type(obj.getString("user_type"));
                        userResponses.setPreferred_location(obj.getString("preferred_location"));
                        userResponses.setIsRegistration_done(obj.getString("isRegistration_done"));
                        userResponses.setPreferred_language(obj.getString("preferred_language"));
                        userResponses.setAddress(obj.getString("address"));
                        userResponses.setState_code(obj.getString("state_code"));
                        userResponses.setPin_code(obj.getString("pin_code"));
                        userResponses.setIsTruck_added(obj.getString("isTruck_added"));
                        userResponses.setIsDriver_added(obj.getString("isDriver_added"));
                        userResponses.setIsBankDetails_given(obj.getString("isBankDetails_given"));
                        userResponses.setIsCompany_added(obj.getString("isCompany_added"));
                        userResponses.setIsPersonal_dt_added(obj.getString("isPersonal_dt_added"));
                        userResponses.setEmail_id(obj.getString("email_id"));
                        userResponses.setPay_type(obj.getString("pay_type"));
                        userResponses.setIs_user_verfied(obj.getString("is_user_verfied"));
                        userResponses.setUser_ratings(obj.getString("user_ratings"));
                        userResponses.setIs_account_active(obj.getString("is_account_active"));
                        userResponses.setCreated_at(obj.getString("created_at"));
                        userResponses.setUpdated_at(obj.getString("updated_at"));
                        userResponses.setUpdated_by(obj.getString("updated_by"));
                        userResponses.setDeleted_at(obj.getString("deleted_at"));
                        userResponses.setDeleted_by(obj.getString("deleted_by"));

                        userResponsesArrayList.add(userResponses);
                    }
                    if (userResponsesArrayList.size() > 0) {
                        dashboardAdapter.updateData(userResponsesArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //-------------------------------------------------------------------------------------------
    }

    public void viewUserDetails(UserResponses obj) {
    }
}
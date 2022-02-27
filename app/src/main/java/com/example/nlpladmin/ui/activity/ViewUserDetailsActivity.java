package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityDashboardBinding;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewUserDetailsActivity extends AppCompatActivity {

    String userId;
    TextView personalViewBtn, bankViewBtn, truckViewBtn, driverViewBtn, truck, driver, personalCompleted, bankCompleted, truckCompleted, driverCompleted;
    CheckBox personalCheckBox, bankCheckBox, truckCheckBox, driverCheckBox;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        mQueue = Volley.newRequestQueue(ViewUserDetailsActivity.this);

        personalViewBtn = (TextView) findViewById(R.id.view_user_details_personal_details_view_btn);
        bankViewBtn = (TextView) findViewById(R.id.view_user_details_bank_details_view_btn);
        truckViewBtn = (TextView) findViewById(R.id.view_user_details_truck_details_view_btn);
        driverViewBtn = (TextView) findViewById(R.id.view_user_details_driver_details_view_btn);
        personalCompleted = (TextView) findViewById(R.id.view_user_details_personal_details);
        bankCompleted = (TextView) findViewById(R.id.view_user_details_bank_details);
        truckCompleted = (TextView) findViewById(R.id.view_user_details_truck_details);
        driverCompleted = (TextView) findViewById(R.id.view_user_details_driver_details);
        personalCheckBox = (CheckBox) findViewById(R.id.view_user_details_personal_details_checkBox);
        bankCheckBox = (CheckBox) findViewById(R.id.view_user_details_bank_details_checkBox2);
        truckCheckBox = (CheckBox) findViewById(R.id.view_user_details_truck_details_checkBox3);
        driverCheckBox = (CheckBox) findViewById(R.id.view_user_details_driver_details_checkBox4);
        driver = (TextView) findViewById(R.id.view_user_details_truck_details_textView);
        truck = (TextView) findViewById(R.id.view_user_details_driver_details_textView);

        personalViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpTo.viewPersonalDetailsActivity(ViewUserDetailsActivity.this, userId);
            }
        });

        getUserDetails();

    }

    private void getUserDetails() {

        String url = getString(R.string.baseURL) + "/user/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);

                        if (obj.getString("user_type").equals("Owner") || obj.getString("user_type").equals("Driver") || obj.getString("user_type").equals("Broker")){
                            truckCompleted.setVisibility(View.VISIBLE);
                            driverCompleted.setVisibility(View.VISIBLE);
                            truck.setVisibility(View.VISIBLE);
                            driver.setVisibility(View.VISIBLE);
                            truckCheckBox.setVisibility(View.VISIBLE);
                            driverCheckBox.setVisibility(View.VISIBLE);
                            truckViewBtn.setVisibility(View.VISIBLE);
                            driverViewBtn.setVisibility(View.VISIBLE);
                        } else {
                            truckCompleted.setVisibility(View.GONE);
                            driverCompleted.setVisibility(View.GONE);
                            truck.setVisibility(View.GONE);
                            driver.setVisibility(View.GONE);
                            truckCheckBox.setVisibility(View.GONE);
                            driverCheckBox.setVisibility(View.GONE);
                            truckViewBtn.setVisibility(View.GONE);
                            driverViewBtn.setVisibility(View.GONE);
                        }
                        //--------------------------------------------------------------------------------------------------------
                        if (obj.getString("isPersonal_dt_added").equals("1")) {
                            personalCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            personalCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isBankDetails_given").equals("1")) {
                            bankCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            bankCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isTruck_added").equals("1")) {
                            truckCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            truckCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isDriver_added").equals("1")) {
                            driverCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            driverCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

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
    }

    public void onClickViewBankDetails(View view) {
        JumpTo.viewBankDetailsActivity(ViewUserDetailsActivity.this, userId, false);
    }

    public void onClickViewTruckDetails(View view) {
        JumpTo.viewTruckDetailsActivity(ViewUserDetailsActivity.this, userId, false);
    }

    public void onClickViewDriverDetails(View view) {
        JumpTo.viewDriverDetailsActivity(ViewUserDetailsActivity.this, userId, false);
    }
}
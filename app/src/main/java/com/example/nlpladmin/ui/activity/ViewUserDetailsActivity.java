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
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewUserDetailsActivity extends AppCompatActivity {

    String userId;
    CheckBox VSPersonalCheckBox, VSBankCheckBox, VSTruckCheckBox, VSDriverCheckBox, PCPersonalCheckBox, PCBankCheckBox, PCTruckCheckBox, PCDriverCheckBox;
    private RequestQueue mQueue;
    View verification_status_view, profile_completeness_view, user_list_view;
    TextView VSTick, PCTick, VSKyc, VSBank, VSTruck, VSDriver, PCPersonal, PCBank, PCTruck, PCDriver, VSTitle, PCTitle, VSViewBtn1, VSViewBtn2, VSViewBtn3, VSViewBtn4, PCViewBtn1, PCViewBtn2, PCViewBtn3, PCViewBtn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        verification_status_view = findViewById(R.id.customer_setting_and_preferences_verification_status);
        profile_completeness_view = findViewById(R.id.customer_setting_and_preferences_profile_completeness);
        user_list_view = findViewById(R.id.customer_setting_and_preferences_user_list);

        PCTitle = profile_completeness_view.findViewById(R.id.verification_dialog_title);
        PCPersonal = profile_completeness_view.findViewById(R.id.personal_textview);
        PCBank = profile_completeness_view.findViewById(R.id.bank_textview);
        PCTruck = profile_completeness_view.findViewById(R.id.truck_textview);
        PCDriver = profile_completeness_view.findViewById(R.id.driver_textview);

        PCTitle.setText("Profile Completeness");
        PCPersonal.setText("  Personal Details");
        PCBank.setText("  Bank Details");
        PCTruck.setText("  Truck Details");
        PCDriver.setText("  Driver Details");

        VSTitle = verification_status_view.findViewById(R.id.verification_dialog_title);
        VSKyc = verification_status_view.findViewById(R.id.personal_textview);
        VSBank = verification_status_view.findViewById(R.id.bank_textview);
        VSTruck = verification_status_view.findViewById(R.id.truck_textview);
        VSDriver = verification_status_view.findViewById(R.id.driver_textview);

        PCViewBtn1 = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_view_btn2);
        PCViewBtn2 = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_view_btn3);
        PCViewBtn3 = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_view_btn4);
        PCViewBtn4 = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_view_btn5);

        PCViewBtn1.setText("View & Manage");
        PCViewBtn2.setText("View & Manage");
        PCViewBtn3.setText("View & Manage");
        PCViewBtn4.setText("View & Manage");

        VSViewBtn1 = verification_status_view.findViewById(R.id.view_user_details_personal_details_view_btn2);
        VSViewBtn2 = verification_status_view.findViewById(R.id.view_user_details_personal_details_view_btn3);
        VSViewBtn3 = verification_status_view.findViewById(R.id.view_user_details_personal_details_view_btn4);
        VSViewBtn4 = verification_status_view.findViewById(R.id.view_user_details_personal_details_view_btn5);

        PCPersonalCheckBox = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_checkBox2);
        PCBankCheckBox = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_checkBox3);
        PCTruckCheckBox = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_checkBox4);
        PCDriverCheckBox = profile_completeness_view.findViewById(R.id.view_user_details_personal_details_checkBox5);
        PCTick = profile_completeness_view.findViewById(R.id.tick_textview);

        VSPersonalCheckBox = verification_status_view.findViewById(R.id.view_user_details_personal_details_checkBox2);
        VSBankCheckBox = verification_status_view.findViewById(R.id.view_user_details_personal_details_checkBox3);
        VSTruckCheckBox = verification_status_view.findViewById(R.id.view_user_details_personal_details_checkBox4);
        VSDriverCheckBox = verification_status_view.findViewById(R.id.view_user_details_personal_details_checkBox5);
        VSTick = verification_status_view.findViewById(R.id.tick_textview);

        VSPersonalCheckBox.setVisibility(View.VISIBLE);
        VSBankCheckBox.setVisibility(View.VISIBLE);
        VSTruckCheckBox.setVisibility(View.VISIBLE);
        VSDriverCheckBox.setVisibility(View.VISIBLE);
        VSTick.setVisibility(View.VISIBLE);

        PCPersonalCheckBox.setVisibility(View.GONE);
        PCBankCheckBox.setVisibility(View.GONE);
        PCTruckCheckBox.setVisibility(View.GONE);
        PCDriverCheckBox.setVisibility(View.GONE);
        PCTick.setVisibility(View.INVISIBLE);

        mQueue = Volley.newRequestQueue(ViewUserDetailsActivity.this);

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
                            PCTruck.setVisibility(View.VISIBLE);
                            PCDriver.setVisibility(View.VISIBLE);
                            PCViewBtn3.setVisibility(View.VISIBLE);
                            PCViewBtn4.setVisibility(View.VISIBLE);
                            VSTruck.setVisibility(View.VISIBLE);
                            VSDriver.setVisibility(View.VISIBLE);
                            VSTruckCheckBox.setVisibility(View.VISIBLE);
                            VSDriverCheckBox.setVisibility(View.VISIBLE);
                            VSViewBtn3.setVisibility(View.VISIBLE);
                            VSViewBtn4.setVisibility(View.VISIBLE);
                        } else {
                            PCTruck.setVisibility(View.GONE);
                            PCDriver.setVisibility(View.GONE);
                            PCViewBtn3.setVisibility(View.GONE);
                            PCViewBtn4.setVisibility(View.GONE);
                            VSTruck.setVisibility(View.GONE);
                            VSDriver.setVisibility(View.GONE);
                            VSTruckCheckBox.setVisibility(View.GONE);
                            VSDriverCheckBox.setVisibility(View.GONE);
                            VSViewBtn3.setVisibility(View.GONE);
                            VSViewBtn4.setVisibility(View.GONE);
                        }
                        //--------------------------------------------------------------------------------------------------------
                        if (obj.getString("isPersonal_dt_added").equals("1")) {
                            PCPersonal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            PCPersonal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isBankDetails_given").equals("1")) {
                            PCBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            PCBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isTruck_added").equals("1")) {
                            PCTruck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            PCTruck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isDriver_added").equals("1")) {
                            PCDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            PCDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
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

    public void onClickViewAndVerifyKyc(View view){
        JumpTo.viewPersonalDetailsActivity(ViewUserDetailsActivity.this, userId);
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
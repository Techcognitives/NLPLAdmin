package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityDashboardBinding;
import com.example.nlpladmin.model.UserResponses;
import com.example.nlpladmin.ui.adapter.DashboardAdapter;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.JumpTo;
import com.example.nlpladmin.utils.ShowAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    String filterBy = "Results for All Users";
    Dialog previewDialogProfile, dialogMenu;
    private RequestQueue mQueue;
    private ArrayList<UserResponses> userResponsesArrayList = new ArrayList<>();
    private DashboardAdapter dashboardAdapter;
    ImageView backButton;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.setHandlers(DashboardActivity.this);

        mQueue = Volley.newRequestQueue(DashboardActivity.this);

        LinearLayoutManager linearLayoutManagerBank = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerBank.setReverseLayout(false);
        binding.usersListView.setLayoutManager(linearLayoutManagerBank);
        binding.usersListView.setHasFixedSize(true);

        dashboardAdapter = new DashboardAdapter(DashboardActivity.this, userResponsesArrayList);
        binding.usersListView.setAdapter(dashboardAdapter);
        getUsersList(filterBy);

        previewDialogProfile = new Dialog(DashboardActivity.this);
        previewDialogProfile.setContentView(R.layout.dialog_preview_images);
        previewDialogProfile.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        dialogMenu = new Dialog(DashboardActivity.this);
        dialogMenu.setContentView(R.layout.dialog_menu);

        binding.dashboardSearchUser.addTextChangedListener(searchUser);

        View actionBar = binding.dashboardActionBar;
        title = actionBar.findViewById(R.id.action_bar_title_text);
        title.setText("Admin");
        backButton = actionBar.findViewById(R.id.action_bar_back_button);
        backButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(view -> {
            binding.dashboardConstrain.setVisibility(View.INVISIBLE);
            binding.dashboardConstrainMenu.setVisibility(View.VISIBLE);
        });

    }

    public void onClickOpenMenu(View view){

    }

    public void onClickFilterBy() {
        Collections.reverse(userResponsesArrayList);
        dashboardAdapter.updateData(userResponsesArrayList);
        binding.dashboardActivityFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                if (parent.getSelectedItem().equals("Results for All Users")) {
                    filterBy = "Results for All Users";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Service Provider")) {
                    filterBy = "Results for Service Provider";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Load Poster")) {
                    filterBy = "Results for Load Poster";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Driver")) {
                    filterBy = "Results for Driver";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results forBroker")) {
                    filterBy = "Results for Broker";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Unverified Profiles")) {
                    filterBy = "Results for Unverified Profiles";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Active Profiles")) {
                    filterBy = "Results for Active Profiles";
                    getUsersList(filterBy);
                }
                if (parent.getSelectedItem().equals("Results for Inactive Profiles")) {
                    filterBy = "Results for Inactive Profiles";
                    getUsersList(filterBy);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void getUsersList(String filterBy) {
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
                        userResponses.setIs_account_active(obj.getString("is_account_active"));
                        userResponses.setCreated_at(obj.getString("created_at"));
                        userResponses.setUpdated_at(obj.getString("updated_at"));
                        userResponses.setUpdated_by(obj.getString("updated_by"));
                        userResponses.setDeleted_at(obj.getString("deleted_at"));
                        userResponses.setDeleted_by(obj.getString("deleted_by"));

                        if (filterBy.equals("Results for All Users")) {
                            userResponsesArrayList.add(userResponses);
                        }
                        if (filterBy.equals("Results for Service Provider")) {
                            if (obj.getString("user_type").equals("Owner")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Load Poster")) {
                            if (obj.getString("user_type").equals("Customer")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Driver")) {
                            if (obj.getString("user_type").equals("Driver")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Broker")) {
                            if (obj.getString("user_type").equals("Broker")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Unverified Profiles")) {
                            if (obj.getString("is_user_verfied").equals("1")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Active Profiles")) {
                            if (obj.getString("is_account_active").equals("1")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                        if (filterBy.equals("Results for Inactive Profiles")) {
                            if (obj.getString("is_account_active").equals("0") || obj.getString("is_account_active").equals("null")) {
                                userResponsesArrayList.add(userResponses);
                            }
                        }
                    }
                    if (userResponsesArrayList.size() > 0) {
                        binding.usersListView.setVisibility(View.VISIBLE);
                        binding.adminDashboardNoUserAvailableText.setVisibility(View.INVISIBLE);
                        onClickFilterBy();
                    } else {
                        binding.usersListView.setVisibility(View.INVISIBLE);
                        binding.adminDashboardNoUserAvailableText.setVisibility(View.VISIBLE);
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

    public void onClickOpenDiler(String phone_number) {
        Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone_number));
        startActivity(i1);
    }

    public void ViewProfileOfUser(UserResponses obj) {

        String url1 = getString(R.string.baseURL) + "/imgbucket/Images/" + obj.getUser_id();
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray imageList = response.getJSONArray("data");
                    for (int i = 0; i < imageList.length(); i++) {
                        JSONObject obj = imageList.getJSONObject(i);
                        String imageType = obj.getString("image_type");

                        String profileImgUrl = "";
                        if (imageType.equals("profile")) {
                            profileImgUrl = obj.getString("image_url");
                            if (profileImgUrl.equals("null")) {

                            } else {
                                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                                lp2.copyFrom(previewDialogProfile.getWindow().getAttributes());
                                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                                lp2.gravity = Gravity.CENTER;

                                previewDialogProfile.show();
                                previewDialogProfile.getWindow().setAttributes(lp2);
                                new DownloadImageTask((ImageView) previewDialogProfile.findViewById(R.id.dialog_preview_image_view)).execute(profileImgUrl);
                            }
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
        mQueue.add(request1);
    }

    private TextWatcher searchUser = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                RearrangeItems();
                binding.dashboardConstrain.setVisibility(View.INVISIBLE);
                binding.dashboardConstrainMenu.setVisibility(View.VISIBLE);
            } else {
                binding.dashboardConstrain.setVisibility(View.VISIBLE);
                binding.dashboardConstrainMenu.setVisibility(View.INVISIBLE);
            }
            filter(editable.toString());
        }
    };

    public void RearrangeItems() {
        JumpTo.dashboardActivity(DashboardActivity.this);
    }

    private void filter(String text) {
        ArrayList<UserResponses> searchVehicleList = new ArrayList<>();
        for (UserResponses item : userResponsesArrayList) {
            if (item.getPhone_number().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getEmail_id().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getPin_code().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getUser_type().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getPreferred_location().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getState_code().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                searchVehicleList.add(item);
            }
        }
        dashboardAdapter.updateData(searchVehicleList);
    }

    public void onClickManageBidOrLoad(View view){
        Dialog manage = new Dialog(DashboardActivity.this);
        manage.setContentView(R.layout.dialog_manage_load);
        manage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(manage.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        manage.show();
        manage.getWindow().setAttributes(lp);
        manage.setCancelable(true);

        TextView alertTitle = (TextView) manage.findViewById(R.id.dialog_manage_load_title);
        EditText search = (EditText) manage.findViewById(R.id.dialog_manage_load_search);
        TextView ok = (TextView) manage.findViewById(R.id.dialog_manage_load_left_button);
        TextView cancel = (TextView) manage.findViewById(R.id.dialog_manage_load_right_button);
        alertTitle.setText("Manage Bid / Load Post");

        ok.setOnClickListener(view1 -> {
            manage.dismiss();
            JumpTo.goToCustomerDashboard(DashboardActivity.this, "91"+search.getText().toString(), true, false);
        });

        cancel.setOnClickListener(view1 -> {
            manage.dismiss();
        });
    }

    public void onClickKYCVerification(View view){
        backButton.setVisibility(View.VISIBLE);
        title.setText("KYC Verification");
        binding.dashboardConstrain.setVisibility(View.VISIBLE);
        binding.dashboardConstrainMenu.setVisibility(View.INVISIBLE);
    }

}
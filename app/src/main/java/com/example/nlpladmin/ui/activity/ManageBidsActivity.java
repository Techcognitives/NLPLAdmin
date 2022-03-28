package com.example.nlpladmin.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.BidSubmittedModel;
import com.example.nlpladmin.model.LoadNotificationModel;
import com.example.nlpladmin.model.Requests.BidLoadRequest;
import com.example.nlpladmin.model.Responses.BidLadResponse;
import com.example.nlpladmin.model.UpdateMethods.UpdateBidDetails;
import com.example.nlpladmin.ui.adapter.LoadNotificationAdapter;
import com.example.nlpladmin.ui.adapter.LoadSubmittedAdapter;
import com.example.nlpladmin.utils.ApiClient;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.EnglishNumberToWords;
import com.example.nlpladmin.utils.FooThread;
import com.example.nlpladmin.utils.GetCurrentLocation;

import com.example.nlpladmin.utils.JumpTo;
import com.example.nlpladmin.utils.ShowAlert;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ManageBidsActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    FusedLocationProviderClient fusedLocationProviderClient;
    RequestQueue mQueue;
    GetCurrentLocation getCurrentLocation;
    ArrayList<LoadNotificationModel> loadList = new ArrayList<>(), loadListToCompare = new ArrayList<>();
    ArrayList<BidSubmittedModel> loadSubmittedList = new ArrayList<>(), updatedLoadSubmittedList = new ArrayList<>();
    LoadNotificationAdapter loadListAdapter;
    LoadSubmittedAdapter loadSubmittedAdapter;
    RecyclerView loadListRecyclerView, loadSubmittedRecyclerView;
    Dialog loadingDialog, setBudget, selectTruckDialog, previewDialogBidNow, dialogAcceptRevisedBid, dialogViewConsignment;
    String updateAssignedDriverId, mobile, name, address, pinCode, city, role, emailIdAPI, s1, updateAssignedTruckId, bidStatus, vehicle_no, loadId, selectedDriverId, selectedDriverName, userId, userIdAPI, phone, mobileNoAPI, truckId, isProfileAdded, isPersonalDetailsDone, isBankDetailsDone, isTruckDetailsDone, isDriverDetailsDone, isFirmDetailsDone;
    ArrayList<String> arrayUserId, arrayTruckId, arrayDriverId, arrayDriverName, arrayTruckList, arrayMobileNo, arrayDriverMobileNo, arrayPinCode, arrayName, arrayRole, arrayCity, arrayAddress, arrayRegDone;
    View actionBar;
    TextView customerNumber, viewProfile, spNumberProfile, spNameProfile, spRoleProfile, loadNotificationTextView, bidsSubmittedTextView, currentLocationText, customerNumberHeading, customerName, timeLeft00, timeLeftTextview, partitionTextview, customerNameHeading, customerFirstBudget, customerSecondBudget, cancel2, cancel, acceptAndBid, spQuote, addDriver, selectDriver, addTruck, selectTruck, selectedTruckModel, selectedTruckFeet, selectedTruckCapacity, selectedTruckBodyType, actionBarTitle;
    EditText notesSp;
    CheckBox declaration;
    RadioButton negotiable_yes, negotiable_no;
    Boolean isLoadNotificationSelected, loadNotificationSelected, isTruckSelectedToBid = false, negotiable = null, isNegotiableSelected = false, fromAdapter = false;
    ImageView actionBarBackButton, profilePicture, actionBarMenuButton;
    ConstraintLayout loadNotificationConstrain, bidsSubmittedConstrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bids);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("mobile2");
            Log.i("Mobile No Registration", phone);
        }
        if (bundle != null) {
            isLoadNotificationSelected = bundle.getBoolean("loadNotification");
        } else {
            isLoadNotificationSelected = true;
        }

        loadNotificationConstrain = (ConstraintLayout) findViewById(R.id.dashboard_load_notification_constrain);
        bidsSubmittedConstrain = (ConstraintLayout) findViewById(R.id.dashboard_bids_submitted_constrain);
        loadNotificationTextView = (TextView) findViewById(R.id.dashboard_load_notification_button);
        bidsSubmittedTextView = (TextView) findViewById(R.id.dashboard_bids_submitted_button);
        profilePicture = (ImageView) findViewById(R.id.users_list_profilePhto);
        spNameProfile = (TextView) findViewById(R.id.users_list_name);
        spRoleProfile = (TextView) findViewById(R.id.users_list_role);
        spNumberProfile = (TextView) findViewById(R.id.user_list_number);
        viewProfile = (TextView) findViewById(R.id.users_list_view_user);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpTo.viewPersonalDetailsActivity(ManageBidsActivity.this, userId);
            }
        });

        getCurrentLocation = new GetCurrentLocation();

        if (isLoadNotificationSelected) {
            loadNotificationSelected = true;
            loadNotificationConstrain.setVisibility(View.VISIBLE);
            bidsSubmittedConstrain.setVisibility(View.INVISIBLE);
            loadNotificationTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
            bidsSubmittedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
        } else {
            loadNotificationSelected = false;
            loadNotificationConstrain.setVisibility(View.INVISIBLE);
            bidsSubmittedConstrain.setVisibility(View.VISIBLE);
            bidsSubmittedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
            loadNotificationTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
        }

        mQueue = Volley.newRequestQueue(ManageBidsActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentLocationText = (TextView) findViewById(R.id.dashboard_current_location_text_view);
        getLocation();

        actionBar = findViewById(R.id.profile_registration_action_bar);
        actionBarTitle = (TextView) actionBar.findViewById(R.id.action_bar_title_text);
        actionBarBackButton = (ImageView) actionBar.findViewById(R.id.action_bar_back_button);
        actionBarMenuButton = (ImageView) actionBar.findViewById(R.id.action_bar_menu_button);

        actionBarTitle.setText("Service Provider Dashboard");
        actionBarMenuButton.setVisibility(View.GONE);
        actionBarBackButton.setVisibility(View.VISIBLE);
        actionBarBackButton.setOnClickListener(view -> {
            ManageBidsActivity.this.finish();
        });

        //------------------------------------------------------------------------------------------
        arrayUserId = new ArrayList<>();
        arrayMobileNo = new ArrayList<>();
        arrayDriverMobileNo = new ArrayList<>();
        arrayAddress = new ArrayList<>();
        arrayCity = new ArrayList<>();
        arrayPinCode = new ArrayList<>();
        arrayName = new ArrayList<>();
        arrayRole = new ArrayList<>();
        arrayRegDone = new ArrayList<>();
        arrayTruckList = new ArrayList<>();
        arrayTruckId = new ArrayList<>();
        arrayDriverId = new ArrayList<>();
        arrayDriverName = new ArrayList<>();

        getUserId(phone);
        loadListRecyclerView = (RecyclerView) findViewById(R.id.dashboard_load_notification_recycler_view);
        loadSubmittedRecyclerView = (RecyclerView) findViewById(R.id.dashboard_load_notification_submitted_recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.profile_registration_constrain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                RearrangeItems();
            }
        });

        loadingDialog = new Dialog(ManageBidsActivity.this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(loadingDialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.CENTER;

        ImageView loading_img = loadingDialog.findViewById(R.id.dialog_loading_image_view);

//        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setAttributes(lp2);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.clockwiserotate);
        loading_img.startAnimation(rotate);

//        swipeListener = new SwipeListener(loadListRecyclerView);

        previewDialogBidNow = new Dialog(ManageBidsActivity.this);
        previewDialogBidNow.setContentView(R.layout.dialog_bid_now);
        previewDialogBidNow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogAcceptRevisedBid = new Dialog(ManageBidsActivity.this);
        dialogAcceptRevisedBid.setContentView(R.layout.dialog_bid_now);
        dialogAcceptRevisedBid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogViewConsignment = new Dialog(ManageBidsActivity.this);
        dialogViewConsignment.setContentView(R.layout.dialog_bid_now);
        dialogViewConsignment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void RearrangeItems() {
        getLocation();
        JumpTo.goToServiceProviderDashboard(ManageBidsActivity.this, phone, loadNotificationSelected, true);
    }

    private void getUserId(String userMobileNumber) {
        //------------------------------get user details by mobile Number---------------------------------
        //-----------------------------------Get User Details---------------------------------------
        String url = getString(R.string.baseURL) + "/user/get";
        Log.i("URL at Profile:", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        userIdAPI = data.getString("user_id");
                        arrayUserId.add(userIdAPI);
                        mobileNoAPI = data.getString("phone_number");
                        arrayMobileNo.add(mobileNoAPI);
                    }

                    for (int j = 0; j < arrayMobileNo.size(); j++) {
                        if (arrayMobileNo.get(j).equals(userMobileNumber)) {
                            userId = arrayUserId.get(j);
                            Log.i("userIDAPI:", userId);
                        }
                    }

                    if (userId == null) {
                        bidsSubmittedTextView.setVisibility(View.GONE);
                    } else {
                        bidsSubmittedTextView.setVisibility(View.VISIBLE);
                    }
                    getUserDetails();
                    //---------------------------- Get Load Details -------------------------------------------
                    getLoadNotificationList();

                    LinearLayoutManager linearLayoutManagerBank = new LinearLayoutManager(getApplicationContext());
//                    linearLayoutManagerBank.setReverseLayout(false);
                    loadListRecyclerView.setLayoutManager(linearLayoutManagerBank);
                    loadListRecyclerView.setHasFixedSize(true);

                    LinearLayoutManager linearLayoutManagerBank1 = new LinearLayoutManager(getApplicationContext());
//                    linearLayoutManagerBank1.setReverseLayout(false);
                    loadSubmittedRecyclerView.setLayoutManager(linearLayoutManagerBank1);
                    loadSubmittedRecyclerView.setHasFixedSize(true);

                    loadSubmittedAdapter = new LoadSubmittedAdapter(ManageBidsActivity.this, updatedLoadSubmittedList);
                    loadSubmittedRecyclerView.setAdapter(loadSubmittedAdapter);
                    loadSubmittedRecyclerView.scrollToPosition(loadSubmittedAdapter.getItemCount() - 1);

                    loadListAdapter = new LoadNotificationAdapter(ManageBidsActivity.this, loadListToCompare);
//                    loadListRecyclerView.setAdapter(loadListAdapter);
                    loadListRecyclerView.scrollToPosition(loadListAdapter.getItemCount() - 1);

                    //------------------------------------------------------------------------------------------

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);

        //------------------------------------------------------------------------------------------------

    }

    private void getUserDetails() {

        String url = getString(R.string.baseURL) + "/user/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        name = obj.getString("name");
                        mobile = obj.getString("phone_number");
                        address = obj.getString("address");
                        city = obj.getString("preferred_location");
                        pinCode = obj.getString("pin_code");
                        role = obj.getString("user_type");

                        emailIdAPI = obj.getString("email_id");

                        String isRegistrationDone = obj.getString("isRegistration_done");
                        Log.i("IsREg", isRegistrationDone);
                        isPersonalDetailsDone = obj.getString("isPersonal_dt_added");
                        isFirmDetailsDone = obj.getString("isCompany_added");
                        isBankDetailsDone = obj.getString("isBankDetails_given");
                        isTruckDetailsDone = obj.getString("isTruck_added");
                        isDriverDetailsDone = obj.getString("isDriver_added");
                        isProfileAdded = obj.getString("isProfile_pic_added");

                        Log.i("isProfileAdded at SP", isProfileAdded);

                        //-------------------------------------Personal details ---- -------------------------------------
                        s1 = mobile.substring(2, 12);

                        spNameProfile.setText(name);
                        spRoleProfile.setText(role);
                        spNumberProfile.setText("+91 "+s1);

                        if (isProfileAdded.equals("1")) {
                            getProfilePic();
                        } else {
//                            profilePic.setImageDrawable(getResources().getDrawable(blue_profile_small));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }

    private void getProfilePic() {
        String url1 = getString(R.string.baseURL) + "/imgbucket/Images/" + userId;
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
                            new DownloadImageTask(profilePicture).execute(profileImgUrl);
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

    public void onClickLoadAndBids(View view) {
        switch (view.getId()) {
            case R.id.dashboard_load_notification_button:
                loadNotificationSelected = true;
                loadNotificationConstrain.setVisibility(View.VISIBLE);
                bidsSubmittedConstrain.setVisibility(View.INVISIBLE);
                loadNotificationTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
                bidsSubmittedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));

                break;

            case R.id.dashboard_bids_submitted_button:
                loadNotificationSelected = false;
                loadNotificationConstrain.setVisibility(View.INVISIBLE);
                bidsSubmittedConstrain.setVisibility(View.VISIBLE);
                loadNotificationTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
                bidsSubmittedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));

                break;
        }
    }

    private void compareAndRemove(ArrayList<LoadNotificationModel> loadListToCompare) {
        Log.i("Load list", String.valueOf(loadListToCompare.size()));
        for (int i = 0; i < loadListToCompare.size(); i++) {
            for (int j = 0; j < updatedLoadSubmittedList.size(); j++) {
                if (loadListToCompare.get(i).getIdpost_load().equals(updatedLoadSubmittedList.get(j).getIdpost_load())) {
                    loadListToCompare.remove(i);
                }
            }
        }

//        Collections.reverse(loadListToCompare);

        loadListAdapter = new LoadNotificationAdapter(ManageBidsActivity.this, loadListToCompare);
        loadListRecyclerView.setAdapter(loadListAdapter);

        if (loadListToCompare.size() > 0) {
            loadListAdapter.updateData(loadListToCompare);
        }
    }

    public void getLoadNotificationList() {
        //---------------------------- Get Bank Details -------------------------------------------
        String url1 = getString(R.string.baseURL) + "/loadpost/getAllPosts";
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadList = new ArrayList<>();
                    loadSubmittedList = new ArrayList<>();

                    JSONArray loadLists = response.getJSONArray("data");
                    for (int i = 0; i < loadLists.length(); i++) {

                        JSONObject obj = loadLists.getJSONObject(i);
                        LoadNotificationModel modelLoadNotification = new LoadNotificationModel();
                        modelLoadNotification.setIdpost_load(obj.getString("idpost_load"));
                        modelLoadNotification.setUser_id(obj.getString("user_id"));
                        modelLoadNotification.setPick_up_date(obj.getString("pick_up_date"));
                        modelLoadNotification.setPick_up_time(obj.getString("pick_up_time"));
                        modelLoadNotification.setBudget(obj.getString("budget"));
                        modelLoadNotification.setBid_status(obj.getString("bid_status"));
                        modelLoadNotification.setVehicle_model(obj.getString("vehicle_model"));
                        modelLoadNotification.setFeet(obj.getString("feet"));
                        modelLoadNotification.setCapacity(obj.getString("capacity"));
                        modelLoadNotification.setBody_type(obj.getString("body_type"));
                        modelLoadNotification.setPick_add(obj.getString("pick_add"));
                        modelLoadNotification.setPick_pin_code(obj.getString("pick_pin_code"));
                        modelLoadNotification.setPick_city(obj.getString("pick_city"));
                        modelLoadNotification.setPick_state(obj.getString("pick_state"));
                        modelLoadNotification.setPick_country(obj.getString("pick_country"));
                        modelLoadNotification.setDrop_add(obj.getString("drop_add"));
                        modelLoadNotification.setDrop_pin_code(obj.getString("drop_pin_code"));
                        modelLoadNotification.setDrop_city(obj.getString("drop_city"));
                        modelLoadNotification.setDrop_state(obj.getString("drop_state"));
                        modelLoadNotification.setDrop_country(obj.getString("drop_country"));
                        modelLoadNotification.setKm_approx(obj.getString("km_approx"));
                        modelLoadNotification.setNotes_meterial_des(obj.getString("notes_meterial_des"));
                        modelLoadNotification.setBid_ends_at(obj.getString("bid_ends_at"));

                        if (obj.getString("bid_status").equals("loadPosted") || obj.getString("bid_status").equals("loadReactivated")) {
                            loadList.add(modelLoadNotification);
                        }
                    }

                    Collections.reverse(loadList);
                    TextView noLoadAvailable = (TextView) findViewById(R.id.dashboard_load_here_text);

                    loadListAdapter = new LoadNotificationAdapter(ManageBidsActivity.this, loadList);
                    loadListRecyclerView.setAdapter(loadListAdapter);

                    if (loadList.size() > 0) {
                        noLoadAvailable.setVisibility(View.GONE);
                        loadListAdapter.updateData(loadList);
                    } else {
                        noLoadAvailable.setVisibility(View.VISIBLE);
                    }

                    getBidListByUserId(loadList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //-------------------------------------------------------------------------------------------
    }

    public void onClickBidNow(LoadNotificationModel obj) {
        if (userId == null) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(ManageBidsActivity.this);
            alert.setContentView(R.layout.dialog_alert);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;

            alert.show();
            alert.getWindow().setAttributes(lp);
            alert.setCancelable(false);

            TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
            TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
            TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
            TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

            alertTitle.setText("Please Register");
            alertMessage.setText("You can not bid without Registration");
            alertPositiveButton.setText("Register Now");
            alertNegativeButton.setText("Cancel");

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });

            alertPositiveButton.setOnClickListener(view -> {
                alert.dismiss();
//                JumpTo.goToRegistrationActivity(ServiceProviderDashboardActivity.this, phone, true);
            });
            //------------------------------------------------------------------------------------------
        } else {
            loadId = obj.getIdpost_load();
            bidStatus = obj.getBid_status();
            String pick_up_date = obj.getPick_up_date();
            String pick_up_time = obj.getPick_up_time();
            String required_budget = obj.getBudget();
            String distance = obj.getKm_approx();
            String required_model = obj.getVehicle_model();
            String required_feet = obj.getFeet();
            String required_capacity = obj.getCapacity();
            String required_truck_body = obj.getBody_type();
            String pick_up_location = obj.getPick_add() + " " + obj.getPick_city() + " " + obj.getPick_state() + " " + obj.getPick_pin_code();
            String drop_location = obj.getDrop_add() + " " + obj.getDrop_city() + " " + obj.getDrop_state() + " " + obj.getDrop_pin_code();
            String received_notes_description = obj.getNotes_meterial_des();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(previewDialogBidNow.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            previewDialogBidNow.show();
            previewDialogBidNow.setCancelable(false);
            previewDialogBidNow.getWindow().setAttributes(lp);

            //-------------------------------------------Display Load Information---------------------------------------------
            TextView pickUpDate = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_pick_up_date_textview);
            TextView pickUpTime = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_pick_up_time_textview);
            customerFirstBudget = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_budget_textview);
            TextView approxDistance = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_distance_textview);
            TextView reqModel = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_req_model_textview);
            TextView reqFeet = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_req_feet_textview);
            TextView reqCapacity = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_req_capacity_textview);
            TextView reqBodyType = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_req_bodyType_textview);
            TextView pickUpLocation = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_pick_up_location_textview);
            TextView dropLocation = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_drop_location_textview);
            TextView receivedNotes = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_received_notes_textview);
            TextView loadIdHeading = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_loadId_heading);

            pickUpDate.setText(pick_up_date);
            pickUpTime.setText(pick_up_time);
            customerFirstBudget.setText(required_budget);
            approxDistance.setText(distance);
            reqModel.setText(required_model);
            reqFeet.setText(required_feet);
            reqCapacity.setText(required_capacity);
            reqBodyType.setText(required_truck_body);
            pickUpLocation.setText(pick_up_location);
            dropLocation.setText(drop_location);
            receivedNotes.setText(received_notes_description);
            loadIdHeading.setText("Load ID: " + obj.getPick_city() + "-" + obj.getDrop_city() + "-000");
            //----------------------------------------------------------------------------------------------------------------

            //-------------------------------------------------Accept Load and Bid now-----------------------------------------
            spQuote = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_sp_quote_textview);
            selectTruck = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_select_truck_textview);
            selectDriver = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_select_driver_textview);
            addTruck = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_add_truck_textview);
            addDriver = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_add_driver_textview);
            selectedTruckModel = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_truck_model_textview);
            selectedTruckFeet = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_truck_feet_textview);
            selectedTruckCapacity = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_truck_capacity_textview);
            selectedTruckBodyType = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_truck_body_type_textview);
            notesSp = (EditText) previewDialogBidNow.findViewById(R.id.dialog_bid_now_notes_editText);
            declaration = (CheckBox) previewDialogBidNow.findViewById(R.id.dialog_bid_now_declaration);
            acceptAndBid = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_accept_and_bid_btn);
            cancel = (TextView) previewDialogBidNow.findViewById(R.id.dialog_bid_now_cancel_btn);
            negotiable_yes = previewDialogBidNow.findViewById(R.id.dialog_bid_now_radio_btn_yes);
            negotiable_no = previewDialogBidNow.findViewById(R.id.dialog_bid_now_radio_btn_no);

            acceptAndBid.setEnabled(false);
            cancel.setEnabled(true);
            cancel.setBackgroundResource((R.drawable.button_active));

            negotiable_no.setChecked(false);
            negotiable_yes.setChecked(false);
            isNegotiableSelected = false;


            if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                acceptAndBid.setEnabled(true);
                acceptAndBid.setBackgroundResource((R.drawable.button_active));
            } else {
                acceptAndBid.setEnabled(false);
                acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
            }

            declaration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }
                }
            });

//            cancel.setOnClickListener(view -> JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected));

        acceptAndBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTruckFeet.equals(obj.getFeet()) || selectedTruckCapacity.equals(obj.getCapacity())) {

                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {

                        if (spQuote.getText().toString().equals(customerFirstBudget.getText().toString())) {
                            saveBid(createBidRequest("Accepted", spQuote.getText().toString()));
                        } else if (!spQuote.getText().toString().equals(customerFirstBudget.getText().toString()) && !negotiable) {
                            saveBid(createBidRequest("submittedNonNego", spQuote.getText().toString()));
                        } else if (!spQuote.getText().toString().equals(customerFirstBudget.getText().toString()) && negotiable) {
                            saveBid(createBidRequest("submittedNego", spQuote.getText().toString()));
                        }

                        Log.i("loadId bidded", obj.getIdpost_load());
                        //----------------------- Alert Dialog -------------------------------------------------
                        Dialog alert = new Dialog(ManageBidsActivity.this);
                        alert.setContentView(R.layout.dialog_alert);
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alert.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;

                        alert.show();
                        alert.getWindow().setAttributes(lp);
                        alert.setCancelable(false);

                        TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                        TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                        TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                        TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                        alertTitle.setText("Post Bid");
                        alertMessage.setText("Bid Posted Successfully");
                        alertPositiveButton.setVisibility(View.GONE);
                        alertNegativeButton.setText("OK");
                        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
//                                JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                                previewDialogBidNow.dismiss();
                            }
                        });
                        //------------------------------------------------------------------------------------------
                    }
                } else {
                    //----------------------- Alert Dialog -------------------------------------------------
                    Dialog alert = new Dialog(ManageBidsActivity.this);
                    alert.setContentView(R.layout.dialog_alert);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(alert.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;

                    alert.show();
                    alert.getWindow().setAttributes(lp);
                    alert.setCancelable(false);

                    TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                    TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                    TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                    TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                    alertTitle.setText("Truck requirement doesn't Match");
                    alertMessage.setText("Ft. and capacity of your truck doesn't match Load Poster requirements.");
                    alertPositiveButton.setVisibility(View.VISIBLE);
                    alertPositiveButton.setText("Continue");
                    alertNegativeButton.setText("Assign another Truck");
                    alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                    alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                    alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
//                            JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                            previewDialogBidNow.dismiss();
                        }
                    });
                    //------------------------------------------------------------------------------------------

                    alertPositiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                                if (spQuote.getText().toString().equals(customerFirstBudget.getText().toString())) {
                                    saveBid(createBidRequest("Accepted", spQuote.getText().toString()));
                                } else if (!spQuote.getText().toString().equals(customerFirstBudget.getText().toString()) && !negotiable) {
                                    saveBid(createBidRequest("submittedNonNego", spQuote.getText().toString()));
                                } else if (!spQuote.getText().toString().equals(customerFirstBudget.getText().toString()) && negotiable) {
                                    saveBid(createBidRequest("submittedNego", spQuote.getText().toString()));
                                }

                                Log.i("loadId bidded", obj.getIdpost_load());
                                //----------------------- Alert Dialog -------------------------------------------------
                                Dialog alert = new Dialog(ManageBidsActivity.this);
                                alert.setContentView(R.layout.dialog_alert);
                                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alert.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.gravity = Gravity.CENTER;

                                alert.show();
                                alert.getWindow().setAttributes(lp);
                                alert.setCancelable(false);

                                TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                                TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                                TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                                TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                                alertTitle.setText("Post Bid");
                                alertMessage.setText("Bid Posted Successfully");
                                alertPositiveButton.setVisibility(View.GONE);
                                alertNegativeButton.setText("OK");
                                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alert.dismiss();
//                                        JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                                        previewDialogBidNow.dismiss();
                                    }
                                });
                                //------------------------------------------------------------------------------------------
                            }
                        }
                    });
                }
            }
        });

            negotiable_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isNegotiableSelected = true;
                    negotiable = true;

                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }

                    negotiable_yes.setChecked(true);
                    negotiable_no.setChecked(false);
                }
            });

            negotiable_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isNegotiableSelected = true;
                    negotiable = false;

                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }

                    negotiable_yes.setChecked(false);
                    negotiable_no.setChecked(true);
                }
            });

            spQuote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    budgetSet(spQuote.getText().toString());

                }
            });

            selectTruck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayTruckId.clear();
                    getTrucksByUserId();
                    arrayTruckList.clear();
                }
            });

            selectDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTruckSelectedToBid) {
                        arrayDriverId.clear();
                        getDriversByUserId();
                        arrayDriverName.clear();
                    }
                }
            });

            addTruck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            addDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private void getDriversByUserId() {

        String url = getString(R.string.baseURL) + "/driver/userId/" + userId;
        Log.i("url for driverByUserId", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        selectedDriverId = obj.getString("driver_id");
                        selectedDriverName = obj.getString("driver_name");
                        arrayDriverId.add(selectedDriverId);
                        arrayDriverName.add(selectedDriverName);
                    }
                    if (arrayDriverName.size() == 0) {
                        //----------------------- Alert Dialog -------------------------------------------------
                        Dialog alert = new Dialog(ManageBidsActivity.this);
                        alert.setContentView(R.layout.dialog_alert);
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alert.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;

                        alert.show();
                        alert.getWindow().setAttributes(lp);
                        alert.setCancelable(true);

                        TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                        TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                        TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                        TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                        alertTitle.setText("Add a Driver");
                        alertMessage.setText("Please add a Driver to submit your response");
                        alertPositiveButton.setVisibility(View.GONE);
                        alertNegativeButton.setText("OK");
                        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
//                                JumpTo.goToDriverDetailsActivity(ServiceProviderDashboardActivity.this, userId, mobile, false, true, false, null, null);
                            }
                        });
                        //------------------------------------------------------------------------------------------
                    } else {
                        selectDriverToBid(arrayDriverId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void selectTruckToBid(ArrayList<String> arrayTruckId) {

        selectTruckDialog = new Dialog(ManageBidsActivity.this);
        selectTruckDialog.setContentView(R.layout.dialog_spinner);
        selectTruckDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectTruckDialog.show();
        selectTruckDialog.setCancelable(true);
        TextView model_title = selectTruckDialog.findViewById(R.id.dialog_spinner_title);
        model_title.setText("Select Truck to Bid");

        ListView modelList = (ListView) selectTruckDialog.findViewById(R.id.list_state);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.custom_list_row, arrayTruckList);
        modelList.setAdapter(adapter1);


        modelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isTruckSelectedToBid = true;
                if (!fromAdapter) {
                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }
                } else {
                    acceptAndBid.setEnabled(true);
                    acceptAndBid.setBackgroundResource((R.drawable.button_active));
                }
                getTruckDetailsByTruckId(arrayTruckId.get(i));
                selectTruckDialog.dismiss();
                arrayTruckList.clear();
            }
        });
    }

    private void selectDriverToBid(ArrayList<String> arrayDriverId) {

        selectTruckDialog = new Dialog(ManageBidsActivity.this);
        selectTruckDialog.setContentView(R.layout.dialog_spinner);
        selectTruckDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectTruckDialog.show();
        selectTruckDialog.setCancelable(true);
        TextView model_title = selectTruckDialog.findViewById(R.id.dialog_spinner_title);
        model_title.setText("Select Driver to Bid");

        ListView modelList = (ListView) selectTruckDialog.findViewById(R.id.list_state);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.custom_list_row, arrayDriverName);
        modelList.setAdapter(adapter1);

        modelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectDriver.setText(adapter1.getItem(i));
                getDriverDetailsByDriverId(arrayDriverId.get(i));
                if (!fromAdapter) {
                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }
                } else {
                    acceptAndBid.setEnabled(true);
                    acceptAndBid.setBackgroundResource((R.drawable.button_active));
                }
                selectTruckDialog.dismiss();
                arrayDriverName.clear();
            }
        });
    }


    private void getDriverDetailsByDriverId(String driverIdSelected) {

        updateAssignedDriverId = driverIdSelected;

        Log.i("Driver selected", driverIdSelected);
        String url = getString(R.string.baseURL) + "/driver/driverId/" + driverIdSelected;
        Log.i("url for truckByTruckId", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        selectedDriverName = obj.getString("driver_name");
                    }

                    selectDriver.setText(selectedDriverName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void getTruckDetailsByTruckId(String truckIdSelected) {

        updateAssignedTruckId = truckIdSelected;


        Log.i("truckId selected", truckIdSelected);
        truckId = truckIdSelected;
        String url = getString(R.string.baseURL) + "/truck/" + truckIdSelected;
        Log.i("url for truckByTruckId", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        String truckModel = obj.getString("truck_type");
                        String truckFeet = obj.getString("truck_ft");
                        String truckCapacity = obj.getString("truck_carrying_capacity");
                        String bodyType = obj.getString("vehicle_type");
                        String vehicleNo = obj.getString("vehicle_no");
                        selectedDriverId = obj.getString("driver_id");

                        selectTruck.setText(vehicleNo);
                        selectedTruckModel.setText(truckModel);
                        selectedTruckFeet.setText(truckFeet);
                        selectedTruckBodyType.setText(bodyType);
                        selectedTruckCapacity.setText(truckCapacity);
                    }

                    if (selectedDriverId.equals("null")) {
                        selectDriver.setText("");
                        Log.i("driverId null", "There is no driver Id for this truck");
                    } else {
                        getDriverDetailsByDriverId(selectedDriverId);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void getBidListByUserId(ArrayList<LoadNotificationModel> loadListToCompare) {

        String url = getString(R.string.baseURL) + "/spbid/getBidDtByUserId/" + userId;
        Log.i("url betBidByUserID", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        String postId = obj.getString("idpost_load");
                        String bidId = obj.getString("sp_bid_id");
                        getBidSubmittedList(postId, bidId, loadListToCompare);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void getBidSubmittedList(String loadIdReceived, String
            bidId, ArrayList<LoadNotificationModel> loadListToCompare) {
        //---------------------------- Get Bank Details ------------------------------------------
        String url1 = getString(R.string.baseURL) + "/loadpost/getLoadDtByPostId/" + loadIdReceived;
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadSubmittedList = new ArrayList<>();
                    loadSubmittedList.clear();

                    JSONArray loadLists = response.getJSONArray("data");
                    for (int i = 0; i < loadLists.length(); i++) {
                        JSONObject obj = loadLists.getJSONObject(i);
                        BidSubmittedModel bidSubmittedModel = new BidSubmittedModel();
                        bidSubmittedModel.setIdpost_load(obj.getString("idpost_load"));
                        bidSubmittedModel.setUser_id(obj.getString("user_id"));
                        bidSubmittedModel.setPick_up_date(obj.getString("pick_up_date"));
                        bidSubmittedModel.setPick_up_time(obj.getString("pick_up_time"));
                        bidSubmittedModel.setBudget(obj.getString("budget"));
                        bidSubmittedModel.setBid_status(obj.getString("bid_status"));
                        bidSubmittedModel.setVehicle_model(obj.getString("vehicle_model"));
                        bidSubmittedModel.setFeet(obj.getString("feet"));
                        bidSubmittedModel.setCapacity(obj.getString("capacity"));
                        bidSubmittedModel.setBody_type(obj.getString("body_type"));
                        bidSubmittedModel.setPick_add(obj.getString("pick_add"));
                        bidSubmittedModel.setPick_pin_code(obj.getString("pick_pin_code"));
                        bidSubmittedModel.setPick_city(obj.getString("pick_city"));
                        bidSubmittedModel.setPick_state(obj.getString("pick_state"));
                        bidSubmittedModel.setPick_country(obj.getString("pick_country"));
                        bidSubmittedModel.setDrop_add(obj.getString("drop_add"));
                        bidSubmittedModel.setDrop_pin_code(obj.getString("drop_pin_code"));
                        bidSubmittedModel.setDrop_city(obj.getString("drop_city"));
                        bidSubmittedModel.setDrop_state(obj.getString("drop_state"));
                        bidSubmittedModel.setDrop_country(obj.getString("drop_country"));
                        bidSubmittedModel.setKm_approx(obj.getString("km_approx"));
                        bidSubmittedModel.setNotes_meterial_des(obj.getString("notes_meterial_des"));
                        bidSubmittedModel.setBid_ends_at(obj.getString("bid_ends_at"));
                        bidSubmittedModel.setBidId(bidId);

                        if (!obj.getString("bid_status").equals("delete") && !obj.getString("bid_status").equals("loadExpired")) {
                            loadSubmittedList.add(bidSubmittedModel);
                        }
                    }


                    TextView noBidsSubmittedTextView = (TextView) findViewById(R.id.dashboard_no_bids_submitted_text);
                    if (loadSubmittedList.size() > 0) {
                        FooThread fooThread = new FooThread(handler);
                        fooThread.start();
                        updatedLoadSubmittedList.addAll(loadSubmittedList);
                        loadSubmittedAdapter.updateData(updatedLoadSubmittedList);
                        if (updatedLoadSubmittedList.size() > 0) {
                            noBidsSubmittedTextView.setVisibility(View.GONE);
                        } else {
                            noBidsSubmittedTextView.setVisibility(View.VISIBLE);
                        }
                        compareAndRemove(loadListToCompare);
                    }
//
//                    else {
//                        loadListAdapter = new LoadNotificationAdapter(DashboardActivity.this, loadListToCompare);
//                        loadListRecyclerView.setAdapter(loadListAdapter);
//
//                        if (loadListToCompare.size() > 0) {
//                            loadListAdapter.updateData(loadListToCompare);
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //-------------------------------------------------------------------------------------------

    }


//    private void getDriverDetailsBySelectedDriver(String driverId){
//        Log.i("driver selected", driverId);
//        String url = getString(R.string.baseURL) + "/driver/driverId/" + driverId;
//        Log.i("url for truckByTruckId", url);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray truckLists = response.getJSONArray("data");
//                    for (int i = 0; i < truckLists.length(); i++) {
//                        JSONObject obj = truckLists.getJSONObject(i);
//                        selectedDriverName = obj.getString("driver_name");
//                    }
//                    selectDriver.setText(selectedDriverName);
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
//        mQueue.add(request);
//
//    }


    private void budgetSet(String previousBudget) {

        setBudget = new Dialog(ManageBidsActivity.this);
        setBudget.setContentView(R.layout.dialog_budget);

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(setBudget.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.CENTER;

        setBudget.show();
        setBudget.setCancelable(true);
        setBudget.getWindow().setAttributes(lp2);

        EditText budget = setBudget.findViewById(R.id.dialog_budget_edit);
        Button okBudget = setBudget.findViewById(R.id.dialog_budget_ok_btn);
        budget.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        String newPreviousBudget = previousBudget.replaceAll(",", "");
        budget.setText(newPreviousBudget);

        if (!previousBudget.isEmpty()) {
            okBudget.setEnabled(true);
            okBudget.setBackgroundResource((R.drawable.button_active));
        } else {
            okBudget.setEnabled(false);
            okBudget.setBackgroundResource((R.drawable.button_de_active));
        }

        budget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String budgetEditText = budget.getText().toString();
                if (!budgetEditText.isEmpty()) {

                    String finalBudget, lastThree = "";
                    String budget1 = budget.getText().toString();
                    if (budget1.length() > 3) {
                        lastThree = budget1.substring(budget1.length() - 3);
                    }
                    if (budget1.length() == 1) {
                        finalBudget = budget1;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 2) {
                        finalBudget = budget1;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 3) {
                        finalBudget = budget1;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 4) {
                        Character fourth = budget1.charAt(0);
                        finalBudget = fourth + "," + lastThree;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 5) {
                        Character fifth = budget1.charAt(0);
                        Character fourth = budget1.charAt(1);
                        finalBudget = fifth + "" + fourth + "," + lastThree;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 6) {
                        Character fifth = budget1.charAt(1);
                        Character fourth = budget1.charAt(2);
                        Character sixth = budget1.charAt(0);
                        finalBudget = sixth + "," + fifth + "" + fourth + "," + lastThree;
                        spQuote.setText(finalBudget);
                    } else if (budget1.length() == 7) {
                        Character seventh = budget1.charAt(0);
                        Character sixth = budget1.charAt(1);
                        Character fifth = budget1.charAt(2);
                        Character fourth = budget1.charAt(3);
                        finalBudget = seventh + "" + sixth + "," + fifth + "" + fourth + "," + lastThree;
                        spQuote.setText(finalBudget);
                    }

                    if (spQuote.getText().toString().equals(customerFirstBudget.getText().toString())) {
                        spQuote.setTextColor(getResources().getColor(R.color.green));
                        negotiable_no.setChecked(true);
                        negotiable_yes.setChecked(false);
                        negotiable_yes.setEnabled(false);
                        negotiable = false;
                        isNegotiableSelected = true;
                    } else {
                        negotiable_yes.setEnabled(true);
                        spQuote.setTextColor(getResources().getColor(R.color.redDark));
                    }
                    okBudget.setEnabled(true);
                    okBudget.setBackgroundResource((R.drawable.button_active));
                } else {
                    okBudget.setEnabled(false);
                    okBudget.setBackgroundResource((R.drawable.button_de_active));
                }

                TextView amountInWords = setBudget.findViewById(R.id.dialog_budget_amount_in_words);
                if (budgetEditText.length() > 0) {
                    String return_val_in_english = EnglishNumberToWords.convert(Long.parseLong(budgetEditText));
                    amountInWords.setText(return_val_in_english);
                } else {
                    amountInWords.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        okBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cb = customerFirstBudget.getText().toString().replaceAll(",", "");
                int customer50Budget = Integer.valueOf(cb) / 2;
                String sb = budget.getText().toString().replaceAll(",", "");
                int spBudget = Integer.valueOf(sb);

                if (spBudget < customer50Budget) {
                    ShowAlert.showAlert(ManageBidsActivity.this, "Enter Appropriate Quote", "You cannot bid less than 50% of customer Budget", true, false, "Ok", "null");
                } else {
                    if (isNegotiableSelected && isTruckSelectedToBid && !spQuote.getText().toString().isEmpty() && !selectDriver.getText().toString().isEmpty() && declaration.isChecked()) {
                        acceptAndBid.setEnabled(true);
                        acceptAndBid.setBackgroundResource((R.drawable.button_active));
                    } else {
                        acceptAndBid.setEnabled(false);
                        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));
                    }
                    setBudget.dismiss();
                }

            }
        });
    }

    private void getTrucksByUserId() {

        String url = getString(R.string.baseURL) + "/truck/truckbyuserID/" + userId;
        Log.i("url for truckByUserId", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        vehicle_no = obj.getString("vehicle_no");
                        truckId = obj.getString("truck_id");
                        Log.i("vehicle no", vehicle_no);
                        arrayTruckList.add(vehicle_no);
                        arrayTruckId.add(truckId);
                    }
                    if (arrayTruckId.size() == 0) {
                        //----------------------- Alert Dialog -------------------------------------------------
                        Dialog alert = new Dialog(ManageBidsActivity.this);
                        alert.setContentView(R.layout.dialog_alert);
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alert.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;

                        alert.show();
                        alert.getWindow().setAttributes(lp);
                        alert.setCancelable(true);

                        TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                        TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                        TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                        TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                        alertTitle.setText("Add a Truck");
                        alertMessage.setText("Please add a Truck to submit your response");
                        alertPositiveButton.setVisibility(View.GONE);
                        alertNegativeButton.setText("OK");
                        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
//                                JumpTo.goToVehicleDetailsActivity(ServiceProviderDashboardActivity.this, userId, phone, false, true, false, false, null, null);
                            }
                        });
                        //------------------------------------------------------------------------------------------
                    } else {
                        selectTruckToBid(arrayTruckId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    //--------------------------------------create Bank Details in API -------------------------------------
    public BidLoadRequest createBidRequest(String status, String spFinal) {
        BidLoadRequest bidLoadRequest = new BidLoadRequest();
        bidLoadRequest.setUser_id(userId);
        bidLoadRequest.setAssigned_truck_id(truckId);
        bidLoadRequest.setAssigned_driver_id(selectedDriverId);
        bidLoadRequest.setIdpost_load(loadId);
        bidLoadRequest.setBid_status(status);
        bidLoadRequest.setBody_type(selectedTruckBodyType.getText().toString());
        bidLoadRequest.setVehicle_model(selectedTruckModel.getText().toString());
        bidLoadRequest.setFeet(selectedTruckFeet.getText().toString());
        bidLoadRequest.setCapacity(selectedTruckCapacity.getText().toString());
        bidLoadRequest.setNotes(notesSp.getText().toString());
        bidLoadRequest.setIs_negatiable(negotiable);
        bidLoadRequest.setSp_quote(spQuote.getText().toString());
        bidLoadRequest.setIs_bid_accpted_by_sp(spFinal);
        return bidLoadRequest;
    }

    public void saveBid(BidLoadRequest bidLoadRequest) {
        Call<BidLadResponse> bidLadResponseCall = ApiClient.getBidLoadService().saveBid(bidLoadRequest);
        bidLadResponseCall.enqueue(new Callback<BidLadResponse>() {
            @Override
            public void onResponse(Call<BidLadResponse> call, retrofit2.Response<BidLadResponse> response) {

            }

            @Override
            public void onFailure(Call<BidLadResponse> call, Throwable t) {

            }
        });
    }

    public void acceptRevisedBid(BidSubmittedModel obj) {

        fromAdapter = true;
        loadId = obj.getIdpost_load();
        bidStatus = obj.getBid_status();
        String pick_up_date = obj.getPick_up_date();
        String pick_up_time = obj.getPick_up_time();
        String distance = obj.getKm_approx();
        String required_model = obj.getVehicle_model();
        String required_feet = obj.getFeet();
        String required_capacity = obj.getCapacity();
        String required_truck_body = obj.getBody_type();
        String pick_up_location = obj.getPick_add() + " " + obj.getPick_city() + " " + obj.getPick_state() + " " + obj.getPick_pin_code();
        String drop_location = obj.getDrop_add() + " " + obj.getDrop_city() + " " + obj.getDrop_state() + " " + obj.getDrop_pin_code();
        String received_notes_description = obj.getNotes_meterial_des();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogAcceptRevisedBid.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogAcceptRevisedBid.show();
        dialogAcceptRevisedBid.setCancelable(false);
        dialogAcceptRevisedBid.getWindow().setAttributes(lp);

        //-------------------------------------------Display Load Information---------------------------------------------
        TextView pickUpDate = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_pick_up_date_textview);
        TextView pickUpTime = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_pick_up_time_textview);
        customerSecondBudget = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_budget_textview);
        TextView approxDistance = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_distance_textview);
        TextView reqModel = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_req_model_textview);
        TextView reqFeet = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_req_feet_textview);
        TextView reqCapacity = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_req_capacity_textview);
        TextView reqBodyType = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_req_bodyType_textview);
        TextView pickUpLocation = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_pick_up_location_textview);
        TextView dropLocation = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_drop_location_textview);
        TextView receivedNotes = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_received_notes_textview);
        TextView loadIdHeading = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_loadId_heading);
        customerNameHeading = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customerName_heading);
        customerName = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customerName);
        customerNumberHeading = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customer_phone_heading);
        customerNumber = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customer_mobile_no);

        customerNameHeading.setVisibility(View.GONE);
        customerName.setVisibility(View.GONE);
        customerNumber.setVisibility(View.GONE);
        customerNumberHeading.setVisibility(View.GONE);

        pickUpDate.setText(pick_up_date);
        pickUpTime.setText(pick_up_time);
        approxDistance.setText(distance);
        reqModel.setText(required_model);
        reqFeet.setText(required_feet);
        reqCapacity.setText(required_capacity);
        reqBodyType.setText(required_truck_body);
        pickUpLocation.setText(pick_up_location);
        dropLocation.setText(drop_location);
        receivedNotes.setText(received_notes_description);
        loadIdHeading.setText("Load ID: " + obj.getPick_city() + "-" + obj.getDrop_city() + "-000");
        //----------------------------------------------------------------------------------------------------------------

        //------------------------------------Accept Load and Bid now-----------------------------------------
        spQuote = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_sp_quote_textview);
        selectTruck = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_select_truck_textview);
        selectDriver = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_select_driver_textview);
        addTruck = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_add_truck_textview);
        addDriver = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_add_driver_textview);
        selectedTruckModel = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_truck_model_textview);
        selectedTruckFeet = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_truck_feet_textview);
        selectedTruckCapacity = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_truck_capacity_textview);
        selectedTruckBodyType = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_truck_body_type_textview);
        notesSp = (EditText) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_notes_editText);
        declaration = (CheckBox) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_declaration);
        acceptAndBid = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_accept_and_bid_btn);
        cancel = (TextView) dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_cancel_btn);
        negotiable_yes = dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_radio_btn_yes);
        negotiable_no = dialogAcceptRevisedBid.findViewById(R.id.dialog_bid_now_radio_btn_no);

        negotiable_yes.setEnabled(false);
        negotiable_yes.setChecked(false);
        negotiable_no.setChecked(true);
        declaration.setVisibility(View.INVISIBLE);

        getBidDetailsByBidId(obj.getBidId());
        spQuote.setTextColor(getResources().getColor(R.color.green));

        cancel.setEnabled(true);
        cancel.setBackgroundResource((R.drawable.button_active));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                previewDialogBidNow.dismiss();
            }
        });

        acceptAndBid.setEnabled(true);
        acceptAndBid.setBackgroundResource((R.drawable.button_active));

        acceptAndBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateBidDetails.updateSPNoteForCustomer(obj.getBidId(), notesSp.getText().toString());
                UpdateBidDetails.updateBidStatus(obj.getBidId(), "AcceptedBySp");
                UpdateBidDetails.updateSPQuoteFinal(obj.getBidId(), spQuote.getText().toString());
                UpdateBidDetails.updateAssignedTruckId(obj.getBidId(), updateAssignedTruckId);
                UpdateBidDetails.updateAssignedDriverId(obj.getBidId(), updateAssignedDriverId);

                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(ManageBidsActivity.this);
                alert.setContentView(R.layout.dialog_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alert.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;

                alert.show();
                alert.getWindow().setAttributes(lp);
                alert.setCancelable(true);

                TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                alertTitle.setText("Bid Revised and Responded");
                alertMessage.setText("Bid Revised and Responded Successfully");
                alertPositiveButton.setVisibility(View.GONE);
                alertNegativeButton.setText("OK");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
//                        JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                        previewDialogBidNow.dismiss();
                    }
                });
                //------------------------------------------------------------------------------------------
            }

        });

        selectTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayTruckId.clear();
                getTrucksByUserId();
                arrayTruckList.clear();
            }
        });

        selectDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTruckSelectedToBid) {
                    arrayDriverId.clear();
                    getDriversByUserId();
                    arrayDriverName.clear();
                }
            }
        });

        addTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    //-----------------------------------------------------------------------------------------------------

    private void getBidDetailsByBidId(String bidId) {
        //-------------------------------------------------------------------------------------------
        String url = getString(R.string.baseURL) + "/spbid/bidDtByBidId/" + bidId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj1 = truckLists.getJSONObject(i);
                        String truck_id = obj1.getString("assigned_truck_id");
                        getTruckDetailsByTruckId(truck_id);
                        String driver_id = obj1.getString("assigned_driver_id");
                        getDriverDetailsByDriverId(driver_id);
                        spQuote.setText(obj1.getString("is_bid_accpted_by_sp"));
                        customerSecondBudget.setText(obj1.getString("is_bid_accpted_by_sp"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //----------------------------------------------------------

    }

    public void viewConsignment(BidSubmittedModel obj) {

        loadId = obj.getIdpost_load();
        bidStatus = obj.getBid_status();
        String pick_up_date = obj.getPick_up_date();
        String pick_up_time = obj.getPick_up_time();
        String distance = obj.getKm_approx();
        String required_model = obj.getVehicle_model();
        String required_feet = obj.getFeet();
        String required_capacity = obj.getCapacity();
        String required_truck_body = obj.getBody_type();
        String pick_up_location = obj.getPick_add() + " " + obj.getPick_city() + " " + obj.getPick_state() + " " + obj.getPick_pin_code();
        String drop_location = obj.getDrop_add() + " " + obj.getDrop_city() + " " + obj.getDrop_state() + " " + obj.getDrop_pin_code();
        String received_notes_description = obj.getNotes_meterial_des();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogViewConsignment.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogViewConsignment.show();
        dialogViewConsignment.setCancelable(false);
        dialogViewConsignment.getWindow().setAttributes(lp);

        //-------------------------------------------Display Load Information---------------------------------------------
        TextView pickUpDate = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_pick_up_date_textview);
        TextView pickUpTime = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_pick_up_time_textview);
        customerSecondBudget = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_budget_textview);
        TextView approxDistance = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_distance_textview);
        TextView reqModel = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_req_model_textview);
        TextView reqFeet = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_req_feet_textview);
        TextView reqCapacity = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_req_capacity_textview);
        TextView reqBodyType = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_req_bodyType_textview);
        TextView pickUpLocation = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_pick_up_location_textview);
        TextView dropLocation = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_drop_location_textview);
        TextView receivedNotes = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_received_notes_textview);
        TextView loadIdHeading = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_loadId_heading);
        customerNameHeading = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customerName_heading);
        customerName = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customerName);
        customerNumberHeading = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customer_phone_heading);
        customerNumber = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_customer_mobile_no);

        customerNameHeading.setVisibility(View.VISIBLE);
        customerName.setVisibility(View.VISIBLE);
        customerNumber.setVisibility(View.VISIBLE);
        customerNumberHeading.setVisibility(View.VISIBLE);

        pickUpDate.setText(pick_up_date);
        pickUpTime.setText(pick_up_time);
        approxDistance.setText(distance);
        reqModel.setText(required_model);
        reqFeet.setText(required_feet);
        reqCapacity.setText(required_capacity);
        reqBodyType.setText(required_truck_body);
        pickUpLocation.setText(pick_up_location);
        dropLocation.setText(drop_location);
        receivedNotes.setText(received_notes_description);
        loadIdHeading.setText("Load ID: " + obj.getPick_city() + "-" + obj.getDrop_city() + "-000");
        //----------------------------------------------------------------------------------------------------------------

        //------------------------------------Accept Load and Bid now-----------------------------------------
        spQuote = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_sp_quote_textview);
        selectTruck = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_select_truck_textview);
        selectDriver = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_select_driver_textview);
        addTruck = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_add_truck_textview);
        addDriver = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_add_driver_textview);
        selectedTruckModel = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_truck_model_textview);
        selectedTruckFeet = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_truck_feet_textview);
        selectedTruckCapacity = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_truck_capacity_textview);
        selectedTruckBodyType = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_truck_body_type_textview);
        notesSp = (EditText) dialogViewConsignment.findViewById(R.id.dialog_bid_now_notes_editText);
        declaration = (CheckBox) dialogViewConsignment.findViewById(R.id.dialog_bid_now_declaration);
        acceptAndBid = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_accept_and_bid_btn);
        cancel = (TextView) dialogViewConsignment.findViewById(R.id.dialog_bid_now_cancel_btn);
        negotiable_yes = dialogViewConsignment.findViewById(R.id.dialog_bid_now_radio_btn_yes);
        negotiable_no = dialogViewConsignment.findViewById(R.id.dialog_bid_now_radio_btn_no);
        partitionTextview = dialogViewConsignment.findViewById(R.id.bid_now_middle_textview_partition);
        timeLeftTextview = dialogViewConsignment.findViewById(R.id.bid_now_time_left_textView);
        timeLeft00 = dialogViewConsignment.findViewById(R.id.bid_now_time_left_00_textview);
        cancel2 = dialogViewConsignment.findViewById(R.id.dialog_bid_now_cancel_btn2);

        cancel2.setVisibility(View.VISIBLE);
        cancel2.setEnabled(true);
        cancel2.setBackgroundTintList(getResources().getColorStateList(R.color.button_blue));

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
            }
        });

        notesSp.setVisibility(View.GONE);
        addTruck.setVisibility(View.INVISIBLE);
        addDriver.setVisibility(View.INVISIBLE);
        timeLeft00.setVisibility(View.GONE);
        partitionTextview.setText("My Bid Response");
        timeLeftTextview.setText("CONSIGNMENT");
        timeLeftTextview.setTextColor(getResources().getColorStateList(R.color.black));
        timeLeftTextview.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        negotiable_yes.setEnabled(false);
        negotiable_yes.setChecked(false);
        negotiable_no.setChecked(true);
        declaration.setVisibility(View.INVISIBLE);

        spQuote.setTextColor(getResources().getColor(R.color.green));

        getBidDetailsByBidId(obj.getBidId());
        getCustomerNameAndNumber(obj.getUser_id());

        cancel.setText("Withdraw");
        cancel.setEnabled(true);
        cancel.setBackgroundTintList(getResources().getColorStateList(R.color.button_blue));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(ManageBidsActivity.this);
                alert.setContentView(R.layout.dialog_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alert.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;

                alert.show();
                alert.getWindow().setAttributes(lp);
                alert.setCancelable(false);

                TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                alertTitle.setText("Withdraw Bid");
                alertMessage.setText("Do you really want to withdraw bid.");
                alertPositiveButton.setVisibility(View.VISIBLE);
                alertNegativeButton.setText("Cancel");
                alertPositiveButton.setText("Withdraw");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
//                        JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                        dialogViewConsignment.dismiss();
                    }
                });

                alertPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UpdateBidDetails.updateBidStatus(obj.getBidId(), "withdrawnBySp");
                        //----------------------- Alert Dialog -------------------------------------------------
                        Dialog alert = new Dialog(ManageBidsActivity.this);
                        alert.setContentView(R.layout.dialog_alert);
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alert.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;

                        alert.show();
                        alert.getWindow().setAttributes(lp);
                        alert.setCancelable(true);

                        TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                        TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                        TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                        TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                        alertTitle.setText("Withdrawn Bid");
                        alertMessage.setText("Bid is withdrawn successfully. Customer will no longer see your Bid.");
                        alertPositiveButton.setVisibility(View.GONE);
                        alertNegativeButton.setText("Ok");
                        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                                dialogViewConsignment.dismiss();
                            }
                        });
                    }
                });
                //------------------------------------------------------------------------------------------

            }
        });

        acceptAndBid.setText("Start Trip");
        acceptAndBid.setEnabled(false);
        acceptAndBid.setBackgroundResource((R.drawable.button_de_active));

        acceptAndBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(ManageBidsActivity.this);
                alert.setContentView(R.layout.dialog_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alert.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;

                alert.show();
                alert.getWindow().setAttributes(lp);
                alert.setCancelable(true);

                TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
                TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
                TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
                TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);

                alertTitle.setText("Trip Started Successfully");
                alertMessage.setText("You can track your trip in track section");
                alertPositiveButton.setVisibility(View.GONE);
                alertNegativeButton.setText("OK");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
//                        JumpTo.goToServiceProviderDashboard(ServiceProviderDashboardActivity.this, phone, loadNotificationSelected);
                        dialogViewConsignment.dismiss();
                    }
                });
                //------------------------------------------------------------------------------------------
            }
        });
    }

    private void getCustomerNameAndNumber(String user_id) {
        //-------------------------------------------------------------------------------------------
        String url = getString(R.string.baseURL) + "/user/" + user_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj1 = truckLists.getJSONObject(i);
                        customerName.setText(obj1.getString("name"));
                        customerNumber.setText(obj1.getString("phone_number"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //----------------------------------------------------------
    }

    public void openMaps(LoadNotificationModel obj) {
        String sDestination = obj.getPick_add() + obj.getPick_city();
        DisplayTrack("", sDestination);
    }

    private void DisplayTrack(String sSource, String sDestination) {
        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    public void onClickOpenPhone(View view) {
        String numberOpen = customerNumber.getText().toString();
        Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + numberOpen));
        startActivity(i2);
    }

    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();
                    try {
                        if (Math.abs(xDiff) > Math.abs(yDiff)) {
                            if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                if (xDiff < 0) {
                                    //Swiped Left
                                    actionBarMenuButton.performClick();
                                } else {
                                    //Swiped Right
                                }
                                return true;
                            }
                        } else {
                            if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold) {
                                if (yDiff > 0) {
                                    //Swiped Down
                                } else {
                                    //Swiped Up
                                }
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(ManageBidsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(ManageBidsActivity.this, Locale.getDefault());
                        try {
                            String latitudeCurrent, longitudeCurrent, countryCurrent, stateCurrent, cityCurrent, subCityCurrent, addressCurrent, pinCodeCurrent;
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitudeCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getLatitude()));
                            longitudeCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getLongitude()));
                            countryCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getCountryName()));
                            stateCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getAdminArea()));
                            cityCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getLocality()));
                            subCityCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getSubLocality()));
                            addressCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getAddressLine(0)));
                            pinCodeCurrent = String.valueOf(Html.fromHtml("" + addresses.get(0).getPostalCode()));

                            currentLocationText.setText(addressCurrent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(ManageBidsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int state = msg.getData().getInt("state");
            if (state == 1) {
                loadingDialog.dismiss();
            }
        }
    };
}
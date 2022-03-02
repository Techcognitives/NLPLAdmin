package com.example.nlpladmin.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.BidsAcceptedModel;
import com.example.nlpladmin.model.BidsReceivedModel;
import com.example.nlpladmin.model.BidsResponsesModel;
import com.example.nlpladmin.model.UpdateMethods.UpdateBidDetails;
import com.example.nlpladmin.model.UpdateMethods.UpdatePostLoadDetails;
import com.example.nlpladmin.ui.adapter.BidsAcceptedAdapter;
import com.example.nlpladmin.ui.adapter.BidsReceivedAdapter;
import com.example.nlpladmin.ui.adapter.BidsResponsesAdapter;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.EnglishNumberToWords;
import com.example.nlpladmin.utils.FooThread;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManageLoadActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue mQueue;

    private ArrayList<BidsReceivedModel> bidsList = new ArrayList<>();
    private BidsReceivedAdapter bidsListAdapter;
    private RecyclerView bidsListRecyclerView;

    private ArrayList<BidsAcceptedModel> acceptedList;
    private BidsAcceptedAdapter bidsAcceptedAdapter;
    private RecyclerView bidsAcceptedRecyclerView;

    private BidsResponsesAdapter bidsResponsesAdapter;
    boolean dismiss, isBackPressed = false, bidsReceivedSelected = true, isbidsReceivedSelected;

    private int CAMERA_PIC_REQUEST2 = 4;
    private int GET_FROM_GALLERY2 = 5;

    Dialog loadingDialog;
    TextView spNumber, driverNumber;
    Dialog previewDialogProfileOfSp;
    ImageView profilePic;

    String isPersonalDetailsDone, isBankDetailsDone, isProfileAdded;
    int count = 0;

    View actionBar;
    TextView actionBarTitle;
    ImageView actionBarBackButton, actionBarMenuButton;
    Dialog previewDialogAcceptANdBid, setBudget, acceptFinalBid, viewConsignmentCustomer;

    ConstraintLayout loadAcceptedConstrain, bidsReceivedConstrain;
    TextView quoteBySp1, timeLeftTextview, timeLeft00, loadAcceptedTextView, bidsReceivedTextView, customerQuote, submitResponseBtn, cancleBtn;
    RadioButton negotiable_yes, negotiable_no;
    EditText notesCustomer;
    String userId, phone, s1, customerEmail;
    String spQuoteByApi, bid_idByAPI, noteByApi;

    ArrayList<String> arrayAssignedDriverId, arrayBidId, arrayUserId, arrayBidStatus, arrayNotesFromSP;
    String fianlBidId, noteBySPToCustomer, assignedDriverId, assignedDriverIdAPI, assignedUserId, assignedUserIdAPI, bidStatusAPI, customerNameAPI;
    String loadIdForUpdate, spBidIdForUpdate, noteForUpdate, quoteForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_load);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("mobile");
        }
        isbidsReceivedSelected = bundle.getBoolean("bidsReceived");
        mQueue = Volley.newRequestQueue(ManageLoadActivity.this);

        loadAcceptedConstrain = (ConstraintLayout) findViewById(R.id.customer_dashboard_loads_accepted_constrain);
        bidsReceivedConstrain = (ConstraintLayout) findViewById(R.id.customer_dashboard_bids_received_constrain);
        loadAcceptedTextView = (TextView) findViewById(R.id.customer_dashboard_loads_accepted_button);
        bidsReceivedTextView = (TextView) findViewById(R.id.customer_dashboard_bids_received_button);

        if (isbidsReceivedSelected) {
            bidsReceivedSelected = true;
            loadAcceptedConstrain.setVisibility(View.INVISIBLE);
            bidsReceivedConstrain.setVisibility(View.VISIBLE);
            bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
            loadAcceptedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));

        } else {
            bidsReceivedSelected = false;
            loadAcceptedConstrain.setVisibility(View.VISIBLE);
            bidsReceivedConstrain.setVisibility(View.INVISIBLE);
            loadAcceptedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
            bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
        }

        getUserId(phone);

        loadingDialog = new Dialog(ManageLoadActivity.this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(loadingDialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.CENTER;

        ImageView loading_img = loadingDialog.findViewById(R.id.dialog_loading_image_view);

        loadingDialog.show();
        loadingDialog.setCancelable(true);
        loadingDialog.getWindow().setAttributes(lp2);

        Animation rotate = AnimationUtils.loadAnimation(ManageLoadActivity.this, R.anim.clockwiserotate);
        loading_img.startAnimation(rotate);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                RearrangeItems();
            }
        });
//--------------------------- action bar -----------------------------------------------------------
        actionBar = findViewById(R.id.customer_dashboard_action_bar);
        actionBarTitle = (TextView) actionBar.findViewById(R.id.action_bar_title_text);
        actionBarBackButton = (ImageView) actionBar.findViewById(R.id.action_bar_back_button);
        actionBarMenuButton = (ImageView) actionBar.findViewById(R.id.action_bar_menu_button);

        actionBarTitle.setText("Load Poster Dashboard");
        actionBarBackButton.setVisibility(View.VISIBLE);
        actionBarMenuButton.setVisibility(View.GONE);
        actionBarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageLoadActivity.this.finish();
            }
        });
        //------------------------------------------------------------------------------------------
        acceptedList = new ArrayList<>();
        arrayAssignedDriverId = new ArrayList<>();
        arrayUserId = new ArrayList<>();
        arrayBidId = new ArrayList<>();
        arrayBidStatus = new ArrayList<>();
        arrayNotesFromSP = new ArrayList<>();
        //---------------------------- Get Load Details -------------------------------------------
        bidsListRecyclerView = (RecyclerView) findViewById(R.id.customer_dashboard_load_notification_recycler_view);

        LinearLayoutManager linearLayoutManagerBank = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerBank.setReverseLayout(false);
        linearLayoutManagerBank.setOrientation(LinearLayoutManager.VERTICAL);
        bidsListRecyclerView.setLayoutManager(linearLayoutManagerBank);
        bidsListRecyclerView.setHasFixedSize(true);

        bidsListAdapter = new BidsReceivedAdapter(ManageLoadActivity.this, bidsList);
        bidsListRecyclerView.setAdapter(bidsListAdapter);
        //------------------------------------------------------------------------------------------

        //---------------------------- Get Accepted Load Details -----------------------------------
        bidsAcceptedRecyclerView = (RecyclerView) findViewById(R.id.customer_dashboard_loads_accepted_recycler_view);

        LinearLayoutManager linearLayoutManagerAccepted = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerAccepted.setReverseLayout(false);
        linearLayoutManagerAccepted.setOrientation(LinearLayoutManager.VERTICAL);
        bidsAcceptedRecyclerView.setLayoutManager(linearLayoutManagerAccepted);
        bidsAcceptedRecyclerView.setHasFixedSize(true);

        bidsAcceptedAdapter = new BidsAcceptedAdapter(ManageLoadActivity.this, acceptedList);
        bidsAcceptedRecyclerView.setAdapter(bidsAcceptedAdapter);
        //------------------------------------------------------------------------------------------

        previewDialogAcceptANdBid = new Dialog(ManageLoadActivity.this);
        previewDialogAcceptANdBid.setContentView(R.layout.dialog_acept_bid_customer);
        previewDialogAcceptANdBid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        acceptFinalBid = new Dialog(ManageLoadActivity.this);
        acceptFinalBid.setContentView(R.layout.dialog_acept_bid_customer);
        acceptFinalBid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewConsignmentCustomer = new Dialog(ManageLoadActivity.this);
        viewConsignmentCustomer.setContentView(R.layout.dialog_acept_bid_customer);
        viewConsignmentCustomer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void getUserId(String userMobileNumber) {
        ArrayList<String> arrayUserId = new ArrayList<>(), arrayMobileNo = new ArrayList<>(), arrayCustomerName = new ArrayList<>(), arrayCustomerEmail = new ArrayList<>(), isPersonalD = new ArrayList<>(), isProfileArray = new ArrayList<>(), isBankD = new ArrayList<>();
        //------------------------------get user details by mobile Number---------------------------------
        //-----------------------------------Get User Details---------------------------------------
        String url = getString(R.string.baseURL) + "/user/get";
        Log.i("URL at Profile:", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        String userIdAPI = data.getString("user_id");
                        arrayUserId.add(userIdAPI);
                        String mobileNoAPI = data.getString("phone_number");
                        arrayMobileNo.add(mobileNoAPI);
                        String userName = data.getString("name");
                        arrayCustomerName.add(userName);
                        String emailAPI = data.getString("email_id");
                        arrayCustomerEmail.add(emailAPI);

                        String isPer = data.getString("isPersonal_dt_added");
                        isPersonalD.add(isPer);
                        isProfileArray.add(data.getString("isProfile_pic_added"));
                        String isBank = data.getString("isBankDetails_given");
                        isBankD.add(isBank);
                    }

                    for (int j = 0; j < arrayMobileNo.size(); j++) {
                        if (arrayMobileNo.get(j).equals(userMobileNumber)) {
                            userId = arrayUserId.get(j);
                            customerNameAPI = arrayCustomerName.get(j);
                            String customerNumberAPI = arrayMobileNo.get(j);
                            s1 = customerNumberAPI.substring(2, 12);
                            customerEmail = arrayCustomerEmail.get(j);

                            isPersonalDetailsDone = isPersonalD.get(j);
                            isProfileAdded = isProfileArray.get(j);
                            isBankDetailsDone = isBankD.get(j);

                            getUserDetails(userId);

                            if (isProfileAdded.equals("1")) {
//                                getProfilePic();
                            } else {
//                                profilePic.setImageDrawable(getResources().getDrawable(blue_profile_small));
                            }

                        }
                    }
                    getBidsReceived();
                    getBidsAccepted();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);

        //------------------------------------------------------------------------------------------------

    }

    public void RearrangeItems() {
        JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
    }

    public void getBidsAccepted() {
        //---------------------------- Get Bank Details -------------------------------------------
        String url1 = getString(R.string.baseURL) + "/loadpost/getLoadDtByUser/" + userId;
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray acceptedLoadList = response.getJSONArray("data");
                    for (int i = 0; i < acceptedLoadList.length(); i++) {

                        JSONObject obj = acceptedLoadList.getJSONObject(i);
                        BidsAcceptedModel bidsAcceptedModel = new BidsAcceptedModel();
                        bidsAcceptedModel.setIdpost_load(obj.getString("idpost_load"));
                        bidsAcceptedModel.setUser_id(obj.getString("user_id"));
                        bidsAcceptedModel.setPick_up_date(obj.getString("pick_up_date"));
                        bidsAcceptedModel.setPick_up_time(obj.getString("pick_up_time"));
                        bidsAcceptedModel.setBudget(obj.getString("budget"));
                        bidsAcceptedModel.setBid_status(obj.getString("bid_status"));
                        bidsAcceptedModel.setVehicle_model(obj.getString("vehicle_model"));
                        bidsAcceptedModel.setFeet(obj.getString("feet"));
                        bidsAcceptedModel.setCapacity(obj.getString("capacity"));
                        bidsAcceptedModel.setBody_type(obj.getString("body_type"));
                        bidsAcceptedModel.setPick_add(obj.getString("pick_add"));
                        bidsAcceptedModel.setPick_pin_code(obj.getString("pick_pin_code"));
                        bidsAcceptedModel.setPick_city(obj.getString("pick_city"));
                        bidsAcceptedModel.setPick_state(obj.getString("pick_state"));
                        bidsAcceptedModel.setPick_country(obj.getString("pick_country"));
                        bidsAcceptedModel.setDrop_add(obj.getString("drop_add"));
                        bidsAcceptedModel.setDrop_pin_code(obj.getString("drop_pin_code"));
                        bidsAcceptedModel.setDrop_city(obj.getString("drop_city"));
                        bidsAcceptedModel.setDrop_state(obj.getString("drop_state"));
                        bidsAcceptedModel.setDrop_country(obj.getString("drop_country"));
                        bidsAcceptedModel.setKm_approx(obj.getString("km_approx"));
                        bidsAcceptedModel.setNotes_meterial_des(obj.getString("notes_meterial_des"));
                        bidsAcceptedModel.setBid_ends_at(obj.getString("bid_ends_at"));

                        if (obj.getString("bid_status").equals("loadSubmitted")) {
                            acceptedList.add(bidsAcceptedModel);
                        }
                    }

                    FooThread fooThread = new FooThread(handler);
                    fooThread.start();

                    TextView noAcceptedLoads = (TextView) findViewById(R.id.customer_dashboard_no_load_accepted_text);
//                    for (int i=0; i< acceptedList.size(); i++){
//                        if (acceptedList.get(i).getBid_status().equals("FinalAccepted")){
                    if (acceptedList.size() > 0) {
//                        bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
                        noAcceptedLoads.setVisibility(View.GONE);
                        bidsAcceptedAdapter.updateData(acceptedList);
                    } else {
//                        bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
                        noAcceptedLoads.setVisibility(View.VISIBLE);
                    }
//                        }
//                    }

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

    public void onClickBidsAndLoads(View view) {
        switch (view.getId()) {
            case R.id.customer_dashboard_bids_received_button:
                bidsReceivedSelected = true;
                loadAcceptedConstrain.setVisibility(View.INVISIBLE);
                bidsReceivedConstrain.setVisibility(View.VISIBLE);
                bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
                loadAcceptedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));

                break;

            case R.id.customer_dashboard_loads_accepted_button:
                bidsReceivedSelected = false;
                loadAcceptedConstrain.setVisibility(View.VISIBLE);
                bidsReceivedConstrain.setVisibility(View.INVISIBLE);
                bidsReceivedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_de_active));
                loadAcceptedTextView.setBackground(getResources().getDrawable(R.drawable.personal_details_buttons_active));
                break;
        }
    }

    public void onClickPostALoad(View view) {
        JumpTo.goToPostALoad(ManageLoadActivity.this, userId, phone, false, false, null, false);
    }

    public void getBidsReceived() {
        String url1 = getString(R.string.baseURL) + "/loadpost/getLoadDtByUser/" + userId;
        Log.i("URL: ", url1);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bidsList = new ArrayList<>();
                    JSONArray bidsLists = response.getJSONArray("data");
                    for (int i = 0; i < bidsLists.length(); i++) {
                        JSONObject obj = bidsLists.getJSONObject(i);
                        BidsReceivedModel bidsReceivedModel = new BidsReceivedModel();
                        bidsReceivedModel.setIdpost_load(obj.getString("idpost_load"));
                        bidsReceivedModel.setUser_id(obj.getString("user_id"));
                        bidsReceivedModel.setPick_up_date(obj.getString("pick_up_date"));
                        bidsReceivedModel.setPick_up_time(obj.getString("pick_up_time"));
                        bidsReceivedModel.setBudget(obj.getString("budget"));
                        bidsReceivedModel.setBid_status(obj.getString("bid_status"));
                        bidsReceivedModel.setVehicle_model(obj.getString("vehicle_model"));
                        bidsReceivedModel.setFeet(obj.getString("feet"));
                        bidsReceivedModel.setCapacity(obj.getString("capacity"));
                        bidsReceivedModel.setBody_type(obj.getString("body_type"));
                        bidsReceivedModel.setPick_add(obj.getString("pick_add"));
                        bidsReceivedModel.setPick_pin_code(obj.getString("pick_pin_code"));
                        bidsReceivedModel.setPick_city(obj.getString("pick_city"));
                        bidsReceivedModel.setPick_state(obj.getString("pick_state"));
                        bidsReceivedModel.setPick_country(obj.getString("pick_country"));
                        bidsReceivedModel.setDrop_add(obj.getString("drop_add"));
                        bidsReceivedModel.setDrop_pin_code(obj.getString("drop_pin_code"));
                        bidsReceivedModel.setDrop_city(obj.getString("drop_city"));
                        bidsReceivedModel.setDrop_state(obj.getString("drop_state"));
                        bidsReceivedModel.setSp_count(obj.getInt("sp_count"));
                        bidsReceivedModel.setDrop_country(obj.getString("drop_country"));
                        bidsReceivedModel.setKm_approx(obj.getString("km_approx"));
                        bidsReceivedModel.setNotes_meterial_des(obj.getString("notes_meterial_des"));
                        bidsReceivedModel.setBid_ends_at(obj.getString("bid_ends_at"));

                        if (!obj.getString("bid_status").equals("loadSubmitted") && !obj.getString("bid_status").equals("delete") && !obj.getString("bid_status").equals("loadExpired")) {
                            bidsList.add(bidsReceivedModel);
                        }
                    }

                    FooThread fooThread = new FooThread(handler);
                    fooThread.start();

                    Collections.reverse(bidsList);
                    TextView noLoadTextView = (TextView) findViewById(R.id.customer_dashboard_no_load_text);

                    if (bidsList.size() > 0) {
                        noLoadTextView.setVisibility(View.GONE);
                        bidsListAdapter.updateData(bidsList);
                    } else {
                        noLoadTextView.setVisibility(View.VISIBLE);
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

    public void onClickViewAndAcceptBid(BidsResponsesModel obj) {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(previewDialogAcceptANdBid.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        previewDialogAcceptANdBid.show();
        previewDialogAcceptANdBid.setCancelable(false);
        previewDialogAcceptANdBid.getWindow().setAttributes(lp);

        //-------------------------------------------Display Load Information---------------------------------------------
        TextView nameSP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bit_service_provider_name);
        TextView modelBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_model_textview);
        TextView feetBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_feet_textview);
        TextView capacityBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_capacity_textview);
        TextView bodyTypeBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_body_type_textview);
        quoteBySp1 = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_bidder_quote_textview);
        TextView negotiableBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_negotiable_textview);
        TextView notesBySP = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_received_notes_textview);

        //----------------------------------------------------------
        String url = getString(R.string.baseURL) + "/user/" + obj.getUser_id();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        String spName = obj.getString("name");
                        nameSP.setText(spName);
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
        //----------------------------------------------------------

        Log.i("Bid-id", obj.getSp_bid_id());
        Log.i("Load-id", obj.getIdpost_load());

        quoteBySp1.setText(obj.getSp_quote());
        modelBySP.setText(obj.getVehicle_model());
        feetBySP.setText(obj.getFeet());
        capacityBySP.setText(obj.getCapacity());
        bodyTypeBySP.setText(obj.getBody_type());
        notesBySP.setText(obj.getNotes());
        String isNegotiableBySP = obj.getIs_negatiable();
        if (isNegotiableBySP.equals("1")) {
            negotiableBySP.setText("Yes");
        } else {
            negotiableBySP.setText("No");
        }
        //----------------------------------------------------------------------------------------------------------------
        customerQuote = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_customer_final_quote_textview);
        negotiable_yes = previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_radio_btn_yes);
        negotiable_no = previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_radio_btn_no);
        notesCustomer = (EditText) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_notes_editText);
        submitResponseBtn = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_submit_response_btn);
        cancleBtn = (TextView) previewDialogAcceptANdBid.findViewById(R.id.dialog_accept_bid_cancel_btn);

        negotiable_yes.setChecked(false);
        negotiable_yes.setEnabled(false);
        negotiable_no.setChecked(true);


        if (!customerQuote.getText().toString().isEmpty()) {
            submitResponseBtn.setEnabled(true);
            submitResponseBtn.setBackgroundResource((R.drawable.button_active));
        } else {
            submitResponseBtn.setEnabled(false);
            submitResponseBtn.setBackgroundResource((R.drawable.button_de_active));
        }

        customerQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetSet(customerQuote.getText().toString());
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                previewDialogAcceptANdBid.dismiss();
            }
        });

        getLoadDetails(obj.getIdpost_load());

        submitResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdatePostLoadDetails.updateNotes(obj.getIdpost_load(), notesCustomer.getText().toString());
                UpdateBidDetails.updateBidStatus(obj.getSp_bid_id(), "RespondedByLp");
                UpdateBidDetails.updateCustomerBudgetForSP(obj.getSp_bid_id(), customerQuote.getText().toString());
                UpdatePostLoadDetails.updateCount(obj.getIdpost_load(), count + 1);

                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(ManageLoadActivity.this);
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

                alertTitle.setText("Load Response");
                alertMessage.setText("Response submitted Successfully");
                alertPositiveButton.setVisibility(View.GONE);
                alertNegativeButton.setText("OK");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (count == 3) {
                            //----------------------- Alert Dialog -------------------------------------------------
                            Dialog alert = new Dialog(ManageLoadActivity.this);
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

                            alertTitle.setText("Successfully selected three Bidders");
                            alertMessage.setText("Please Review & Accept any one offer.");
                            alertPositiveButton.setVisibility(View.GONE);
                            alertNegativeButton.setText("OK");
                            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    Intent intent = new Intent(ManageLoadActivity.this, ManageLoadActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("mobile", phone);
                                    intent.putExtra("bidsReveived", bidsReceivedSelected);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            alert.dismiss();
                            //------------------------------------------------------------------------------------------
                        } else {
                            alert.dismiss();
                            Intent intent = new Intent(ManageLoadActivity.this, ManageLoadActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("mobile", phone);
                            intent.putExtra("bidsReveived", bidsReceivedSelected);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                //------------------------------------------------------------------------------------------
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------

    private void getLoadDetails(String loadId) {
        String url1 = getString(R.string.baseURL) + "/loadpost/getLoadDtByPostId/" + loadId;
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bidsList = new ArrayList<>();
                    JSONArray bidsLists = response.getJSONArray("data");
                    for (int i = 0; i < bidsLists.length(); i++) {
                        JSONObject obj = bidsLists.getJSONObject(i);
                        count = obj.getInt("sp_count");
                    }

                    Collections.reverse(bidsList);
                    TextView noLoadTextView = (TextView) findViewById(R.id.customer_dashboard_no_load_text);

                    if (bidsList.size() > 0) {
                        noLoadTextView.setVisibility(View.GONE);
                        bidsListAdapter.updateData(bidsList);
                    } else {
                        noLoadTextView.setVisibility(View.VISIBLE);
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

    private void budgetSet(String previousBudget) {

        setBudget = new Dialog(ManageLoadActivity.this);
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
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 2) {
                        finalBudget = budget1;
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 3) {
                        finalBudget = budget1;
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 4) {
                        Character fourth = budget1.charAt(0);
                        finalBudget = fourth + "," + lastThree;
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 5) {
                        Character fifth = budget1.charAt(0);
                        Character fourth = budget1.charAt(1);
                        finalBudget = fifth + "" + fourth + "," + lastThree;
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 6) {
                        Character fifth = budget1.charAt(1);
                        Character fourth = budget1.charAt(2);
                        Character sixth = budget1.charAt(0);
                        finalBudget = sixth + "," + fifth + "" + fourth + "," + lastThree;
                        customerQuote.setText(finalBudget);
                    } else if (budget1.length() == 7) {
                        Character seventh = budget1.charAt(0);
                        Character sixth = budget1.charAt(1);
                        Character fifth = budget1.charAt(2);
                        Character fourth = budget1.charAt(3);
                        finalBudget = seventh + "" + sixth + "," + fifth + "" + fourth + "," + lastThree;
                        customerQuote.setText(finalBudget);
                    }

                    if (quoteBySp1.getText().toString().equals(customerQuote.getText().toString())) {
                        customerQuote.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        customerQuote.setTextColor(getResources().getColor(R.color.redDark));
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
                if (!customerQuote.getText().toString().isEmpty()) {
                    submitResponseBtn.setEnabled(true);
                    submitResponseBtn.setBackgroundResource((R.drawable.button_active));
                } else {
                    submitResponseBtn.setEnabled(false);
                    submitResponseBtn.setBackgroundResource((R.drawable.button_de_active));
                }
                setBudget.dismiss();
            }
        });
    }

    public void onClickEditLoadPost(BidsReceivedModel obj) {
        JumpTo.goToPostALoad(ManageLoadActivity.this, userId, phone, false, true, obj.getIdpost_load(), false);
    }


    public void getBidsResponsesList(BidsReceivedModel obj1, RecyclerView bidsResponsesRecyclerView, TextView bidsReceived, ConstraintLayout showRecyclerView, String sortBy) {
        ArrayList<BidsResponsesModel> bidResponsesList = new ArrayList<>();
        bidResponsesList.clear();
//        bidsList.clear();

        String url1 = getString(R.string.baseURL) + "/spbid/getBidDtByPostId/" + obj1.getIdpost_load();
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray bidResponsesLists = response.getJSONArray("data");
                    for (int i = 0; i < bidResponsesLists.length(); i++) {
                        JSONObject obj = bidResponsesLists.getJSONObject(i);
                        BidsResponsesModel bidsResponsesModel2 = new BidsResponsesModel();
                        bidsResponsesModel2.setSp_bid_id(obj.getString("sp_bid_id"));
                        bidsResponsesModel2.setUser_id(obj.getString("user_id"));
                        bidsResponsesModel2.setIdpost_load(obj.getString("idpost_load"));
                        bidsResponsesModel2.setSp_quote(obj.getString("sp_quote"));
                        bidsResponsesModel2.setIs_negatiable(obj.getString("is_negatiable"));
                        bidsResponsesModel2.setAssigned_truck_id(obj.getString("assigned_truck_id"));
                        bidsResponsesModel2.setAssigned_driver_id(obj.getString("assigned_driver_id"));
                        bidsResponsesModel2.setVehicle_model(obj.getString("vehicle_model"));
                        bidsResponsesModel2.setFeet(obj.getString("feet"));
                        bidsResponsesModel2.setCapacity(obj.getString("capacity"));
                        bidsResponsesModel2.setBody_type(obj.getString("body_type"));
                        bidsResponsesModel2.setNotes(obj.getString("notes"));
                        bidsResponsesModel2.setBid_status(obj.getString("bid_status"));
                        bidsResponsesModel2.setIs_bid_accpted_by_sp(obj.getString("is_bid_accpted_by_sp"));

                        if (obj1.getSp_count() >= 3) {
                            if (obj.getString("bid_status").equals("AcceptedBySp") || obj.getString("bid_status").equals("RespondedByLp")) {
                                bidResponsesList.add(bidsResponsesModel2);
                            }
                        } else {
                            if (!obj.getString("bid_status").equals("withdrawnByLp")) {
                                bidResponsesList.add(bidsResponsesModel2);
                            }
                        }
                    }

                    if (obj1.getSp_count() >= 3) {
                        bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                        bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                        bidsResponsesAdapter.updateData(bidResponsesList);
                    } else {
                        for (int i = 0; i < bidResponsesList.size(); i++) {
                            if (obj1.getIdpost_load().equals(bidResponsesList.get(i).getIdpost_load())) {

                                bidsReceived.setText(String.valueOf(bidResponsesList.size() + " Responses Received"));

                                if (!sortBy.equals("Initial Responses")) {
                                    Collections.reverse(bidResponsesList);
                                }

                                if (sortBy.equals("Sort By")) {
                                    Collections.sort(bidResponsesList, new Comparator<BidsResponsesModel>() {
                                        @Override
                                        public int compare(BidsResponsesModel bidsResponsesModel, BidsResponsesModel t1) {
                                            return bidsResponsesModel.getBid_status().compareTo(t1.getBid_status());
                                        }
                                    });

                                    bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                                    bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                                    bidsResponsesAdapter.updateData(bidResponsesList);
                                }

                                if (sortBy.equals("Recent Responses")) {
                                    bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                                    bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                                    bidsResponsesAdapter.updateData(bidResponsesList);
                                }

                                if (sortBy.equals("Initial Responses")) {
                                    bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                                    bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                                    bidsResponsesAdapter.updateData(bidResponsesList);
                                }

                                if (sortBy.equals("Price High-low")) {
                                    Collections.sort(bidResponsesList, new Comparator<BidsResponsesModel>() {
                                        @Override
                                        public int compare(BidsResponsesModel bidsResponsesModel, BidsResponsesModel t1) {
                                            String Quote1 = bidsResponsesModel.getSp_quote().replaceAll(",", "");
                                            String Quote2 = t1.getSp_quote().replaceAll(",", "");
                                            int q1 = Integer.valueOf(Quote1);
                                            int q2 = Integer.valueOf(Quote2);
                                            return q2 - q1;
                                        }
                                    });

                                    bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                                    bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                                    bidsResponsesAdapter.updateData(bidResponsesList);
                                }

                                if (sortBy.equals("Price Low-high")) {
                                    Collections.sort(bidResponsesList, new Comparator<BidsResponsesModel>() {
                                        @Override
                                        public int compare(BidsResponsesModel bidsResponsesModel, BidsResponsesModel t1) {
                                            String Quote1 = bidsResponsesModel.getSp_quote().replaceAll(",", "");
                                            String Quote2 = t1.getSp_quote().replaceAll(",", "");
                                            int q1 = Integer.valueOf(Quote1);
                                            int q2 = Integer.valueOf(Quote2);
                                            return q1 - q2;
                                        }
                                    });

                                    bidsResponsesAdapter = new BidsResponsesAdapter(ManageLoadActivity.this, bidResponsesList);
                                    bidsResponsesRecyclerView.setAdapter(bidsResponsesAdapter);
                                    bidsResponsesAdapter.updateData(bidResponsesList);
                                }

                            }
                        }
                    }

                    if (bidsReceived.getText().toString().equals("0 Responses Received")) {
                        showRecyclerView.setVisibility(View.GONE);
                        bidsResponsesRecyclerView.setVisibility(View.GONE);
                    } else {
                        showRecyclerView.setVisibility(View.VISIBLE);
                        bidsResponsesRecyclerView.setVisibility(View.VISIBLE);
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

    public void acceptFinalOffer(BidsResponsesModel obj) {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(acceptFinalBid.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        acceptFinalBid.show();
        acceptFinalBid.setCancelable(false);
        acceptFinalBid.getWindow().setAttributes(lp);

        //-------------------------------------------Display Load Information---------------------------------------------
        TextView nameSP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bit_service_provider_name);
        TextView modelBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_model_textview);
        TextView feetBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_feet_textview);
        TextView capacityBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_capacity_textview);
        TextView bodyTypeBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_body_type_textview);
        TextView quoteBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_bidder_quote_textview);
        TextView negotiableBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_negotiable_textview);
        TextView notesBySP = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_received_notes_textview);

        //----------------------------------------------------------
        String url = getString(R.string.baseURL) + "/user/" + obj.getUser_id();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        String spName = obj.getString("name");
                        nameSP.setText(spName);
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
        //----------------------------------------------------------

        Log.i("Bid-id", obj.getSp_bid_id());
        Log.i("Load-id", obj.getIdpost_load());

        quoteBySP.setText(obj.getSp_quote());
        modelBySP.setText(obj.getVehicle_model());
        feetBySP.setText(obj.getFeet());
        capacityBySP.setText(obj.getCapacity());
        bodyTypeBySP.setText(obj.getBody_type());
        notesBySP.setText(obj.getNotes());
        negotiableBySP.setText("No");
        //----------------------------------------------------------------------------------------------------------------

        customerQuote = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_customer_final_quote_textview);
        negotiable_yes = acceptFinalBid.findViewById(R.id.dialog_accept_bid_radio_btn_yes);
        negotiable_no = acceptFinalBid.findViewById(R.id.dialog_accept_bid_radio_btn_no);
        notesCustomer = (EditText) acceptFinalBid.findViewById(R.id.dialog_accept_bid_notes_editText);
        submitResponseBtn = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_submit_response_btn);
        cancleBtn = (TextView) acceptFinalBid.findViewById(R.id.dialog_accept_bid_cancel_btn);

        negotiable_yes.setChecked(false);
        negotiable_yes.setEnabled(false);
        negotiable_no.setChecked(true);
        customerQuote.setText(obj.getSp_quote());
        submitResponseBtn.setText("Accept Final Offer");

        submitResponseBtn.setEnabled(true);
        submitResponseBtn.setBackgroundResource((R.drawable.button_active));

        submitResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadIdForUpdate = obj.getIdpost_load();
                spBidIdForUpdate = obj.getSp_bid_id();
                noteForUpdate = notesCustomer.getText().toString();
                quoteForUpdate = obj.getSp_quote();

            }
        });

        cancleBtn.setEnabled(true);
        cancleBtn.setBackgroundResource((R.drawable.button_active));

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                acceptFinalBid.dismiss();
            }
        });
    }

    private void updateAfterSuccess(String loadId, String bidId, String note, String quoteUpdate) {
        UpdatePostLoadDetails.updateNotes(loadId, note);
        UpdatePostLoadDetails.updateStatus(loadId, "loadSubmitted");
        UpdateBidDetails.updateBidStatus(bidId, "FinalAccepted");
        UpdateBidDetails.updateCustomerBudgetForSP(bidId, quoteUpdate);
        UpdatePostLoadDetails.updateBudget(loadId, quoteUpdate);
    }

    public void onClickViewConsignment(BidsAcceptedModel obj) {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(viewConsignmentCustomer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        viewConsignmentCustomer.show();
        viewConsignmentCustomer.setCancelable(false);
        viewConsignmentCustomer.getWindow().setAttributes(lp);

        //-------------------------------------------Display Load Information---------------------------------------------
        TextView nameSP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bit_service_provider_name);
        TextView modelBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_model_textview);
        TextView feetBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_feet_textview);
        TextView capacityBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_capacity_textview);
        TextView bodyTypeBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_body_type_textview);
        TextView quoteBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_bidder_quote_textview);
        TextView negotiableBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_negotiable_textview);
        TextView notesBySP = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_received_notes_textview);
        spNumber = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_sp_number);
        TextView companyNameHeading = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_company_name_heading);
        TextView companyName = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_company_name);
        TextView driverNameHeading = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_driver_name_heading);
        TextView driverName = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_driver_name);
        driverNumber = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_driver_number);

        TextView rateSp = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_rate_sp);
        TextView rateCustomer = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_rate_driver);

        rateSp.setVisibility(View.GONE);
        rateCustomer.setVisibility(View.INVISIBLE);

        spNumber.setVisibility(View.VISIBLE);
        driverName.setVisibility(View.VISIBLE);
        driverNumber.setVisibility(View.VISIBLE);
        driverNameHeading.setVisibility(View.VISIBLE);

        String url1 = getString(R.string.baseURL) + "/spbid/getBidDtByPostId/" + obj.getIdpost_load();
        Log.i("URL: ", url1);

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray bidResponsesLists = response.getJSONArray("data");
                    for (int i = 0; i < bidResponsesLists.length(); i++) {
                        JSONObject obj = bidResponsesLists.getJSONObject(i);
                        noteByApi = obj.getString("notes");
                        bid_idByAPI = obj.getString("sp_bid_id");
                        assignedDriverIdAPI = obj.getString("assigned_driver_id");
                        assignedUserIdAPI = obj.getString("user_id");
                        bidStatusAPI = obj.getString("bid_status");

                        arrayBidId.add(bid_idByAPI);
                        arrayUserId.add(assignedUserIdAPI);
                        arrayAssignedDriverId.add(assignedDriverIdAPI);
                        arrayBidStatus.add(bidStatusAPI);
                        arrayNotesFromSP.add(noteByApi);
                    }

                    for (int j = 0; j < arrayBidStatus.size(); j++) {
                        if (arrayBidStatus.get(j).equals("FinalAccepted")) {
                            fianlBidId = arrayBidId.get(j);
                            assignedUserId = arrayUserId.get(j);
                            assignedDriverId = arrayAssignedDriverId.get(j);
                            noteBySPToCustomer = arrayNotesFromSP.get(j);
                        }

                        //--------------------------------------------------------------------------
                        String url4 = getString(R.string.baseURL) + "/spbid/bidDtByBidId/" + acceptFinalBid;
                        JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.GET, url4, null, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray truckLists = response.getJSONArray("data");
                                    for (int i = 0; i < truckLists.length(); i++) {
                                        JSONObject obj = truckLists.getJSONObject(i);
                                        spQuoteByApi = obj.getString("sp_quote");
                                        quoteBySP.setText(spQuoteByApi);
                                        modelBySP.setText(obj.getString("vehicle_model"));
                                        feetBySP.setText(obj.getString("feet"));
                                        capacityBySP.setText(obj.getString("capacity"));
                                        bodyTypeBySP.setText(obj.getString("body_type"));
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
                        mQueue.add(request4);
                        //--------------------------------------------------------------------------

                        //----------------------------------------------------------
                        String url = getString(R.string.baseURL) + "/user/" + assignedUserId;
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray truckLists = response.getJSONArray("data");
                                    for (int i = 0; i < truckLists.length(); i++) {
                                        JSONObject obj = truckLists.getJSONObject(i);
                                        nameSP.setText(obj.getString("name"));
                                        spNumber.setText(obj.getString("phone_number"));

                                        int isCompAded = obj.getInt("isCompany_added");

                                        if (isCompAded == 1) {
                                            companyName.setVisibility(View.VISIBLE);
                                            companyNameHeading.setVisibility(View.VISIBLE);
                                            //----------------------------------------------------------
                                            String url2 = getString(R.string.baseURL) + "/company/get/" + obj.getString("user_id");
                                            JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        JSONArray truckLists = response.getJSONArray("data");
                                                        for (int i = 0; i < truckLists.length(); i++) {
                                                            JSONObject obj = truckLists.getJSONObject(i);
                                                            companyName.setText(obj.getString("company_name"));
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
                                            mQueue.add(request2);
                                            //----------------------------------------------------------
                                        } else {
                                            companyName.setVisibility(View.GONE);
                                            companyNameHeading.setVisibility(View.GONE);
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
                        //----------------------------------------------------------

                        //----------------------------------------------------------
                        String url1 = getString(R.string.baseURL) + "/driver/driverId/" + assignedDriverId;
                        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray truckLists = response.getJSONArray("data");
                                    for (int i = 0; i < truckLists.length(); i++) {
                                        JSONObject obj = truckLists.getJSONObject(i);
                                        driverName.setText(obj.getString("driver_name"));
                                        driverNumber.setText(obj.getString("driver_number"));
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
                        //----------------------------------------------------------
                    }
                    negotiableBySP.setText("No");
                    notesBySP.setText(noteBySPToCustomer);
                    //----------------------------------------------------------------------------------------------------------------

                    customerQuote = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_customer_final_quote_textview);
                    negotiable_yes = viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_radio_btn_yes);
                    negotiable_no = viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_radio_btn_no);
                    notesCustomer = (EditText) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_notes_editText);
                    submitResponseBtn = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_submit_response_btn);
                    cancleBtn = (TextView) viewConsignmentCustomer.findViewById(R.id.dialog_accept_bid_cancel_btn);
                    timeLeftTextview = viewConsignmentCustomer.findViewById(R.id.accept_bid_time_left_textview);
                    timeLeft00 = viewConsignmentCustomer.findViewById(R.id.accept_bid_time_left_00_textview);
                    TextView noteHeading = viewConsignmentCustomer.findViewById(R.id.notes_text_heading_view_consignment_customer);

                    timeLeftTextview.setText("CONSIGNMENT");
                    timeLeft00.setVisibility(View.GONE);
                    timeLeftTextview.setTextColor(getResources().getColorStateList(R.color.black));
                    timeLeftTextview.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    noteHeading.setVisibility(View.GONE);
                    notesCustomer.setVisibility(View.GONE);
                    negotiable_yes.setChecked(false);
                    negotiable_yes.setEnabled(false);
                    negotiable_no.setChecked(true);
                    customerQuote.setText(spQuoteByApi);

                    submitResponseBtn.setText("Withdraw");
                    submitResponseBtn.setBackgroundResource((R.drawable.button_active));
                    submitResponseBtn.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
                    submitResponseBtn.setEnabled(true);

                    submitResponseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //----------------------- Alert Dialog -------------------------------------------------
                            Dialog alert = new Dialog(ManageLoadActivity.this);
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
                            TextView alertCancelButton = (TextView) alert.findViewById(R.id.dialog_alert_cancel);

                            alertTitle.setText("Withdrawn Load");
                            alertMessage.setText("Do you want withdraw fully or continue with other Service Provider");
                            alertPositiveButton.setVisibility(View.VISIBLE);
                            alertPositiveButton.setText("Withdraw Fully");
                            alertNegativeButton.setText("Withdraw Partially");
                            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
                            alertCancelButton.setVisibility(View.VISIBLE);

                            alertPositiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
//                                    viewConsignmentCustomer.dismiss();
                                    UpdatePostLoadDetails.updateStatus(obj.getIdpost_load(), "delete");

                                    //----------------------- Alert Dialog -------------------------------------------------
                                    Dialog alert = new Dialog(ManageLoadActivity.this);
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

                                    alertTitle.setText("Withdraw Load");
                                    alertMessage.setText("Load withdrawn successfully and no longer visible for anyone");
                                    alertPositiveButton.setVisibility(View.GONE);
                                    alertNegativeButton.setText("Ok");
                                    alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                                    alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                                    alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alert.dismiss();
                                            viewConsignmentCustomer.dismiss();
                                            JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                                        }
                                    });
                                    //------------------------------------------------------------------------------------------
                                }
                            });
                            //------------------------------------------------------------------------------------------

                            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    viewConsignmentCustomer.dismiss();
                                    UpdatePostLoadDetails.updateStatus(obj.getIdpost_load(), "loadPosted");
                                    UpdateBidDetails.updateBidStatus(fianlBidId, "withdrawnByLp");

                                    //----------------------- Alert Dialog -------------------------------------------------
                                    Dialog alert = new Dialog(ManageLoadActivity.this);
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

                                    alertTitle.setText("Withdraw from current Service Provider");
                                    alertMessage.setText("Load withdrawn from current Service Provider and visible for other Service Provider");
                                    alertPositiveButton.setVisibility(View.GONE);
                                    alertNegativeButton.setText("Ok");
                                    alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                                    alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                                    alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alert.dismiss();
                                            viewConsignmentCustomer.dismiss();
                                            JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                                        }
                                    });
                                    //------------------------------------------------------------------------------------------

                                }
                            });

                            alertCancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            });
                        }
                    });

                    cancleBtn.setEnabled(true);
                    cancleBtn.setBackgroundResource((R.drawable.button_active));
                    cancleBtn.setText("Back");

                    cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                            viewConsignmentCustomer.dismiss();
                        }
                    });
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
        //-------------------------------------------------------------------------------------------
    }

    public void onClickOpenDialer(View view) {
        switch (view.getId()) {
            case R.id.dialog_accept_bid_sp_number:
                String numberOpen = spNumber.getText().toString();
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + numberOpen));
                startActivity(i1);
                break;
            case R.id.dialog_accept_bid_driver_number:
                String numberOpened = driverNumber.getText().toString();
                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + numberOpened));
                startActivity(i2);
                break;
        }
    }

    public void ViewProfileOfSPToCustomer(BidsResponsesModel obj) {
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
                                lp2.copyFrom(previewDialogProfileOfSp.getWindow().getAttributes());
                                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                                lp2.gravity = Gravity.CENTER;

                                previewDialogProfileOfSp.show();
                                previewDialogProfileOfSp.getWindow().setAttributes(lp2);
                                new DownloadImageTask((ImageView) previewDialogProfileOfSp.findViewById(R.id.dialog_preview_image_view)).execute(profileImgUrl);
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
                            new DownloadImageTask(profilePic).execute(profileImgUrl);
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

    public void continueWithOtherSp(BidsAcceptedModel obj) {
        //----------------------- Alert Dialog -------------------------------------------------
        Dialog alert = new Dialog(ManageLoadActivity.this);
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

        alertTitle.setText("Continue with other Service Provider");
        alertMessage.setText("Current Bidder have withdrawn Bid, do you still want to continue with other Service Provider?");
        alertPositiveButton.setVisibility(View.VISIBLE);
        alertPositiveButton.setText("Continue");
        alertNegativeButton.setText("Cancel");
        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                viewConsignmentCustomer.dismiss();
                JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
            }
        });
        //------------------------------------------------------------------------------------------

        alertPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                viewConsignmentCustomer.dismiss();
                UpdatePostLoadDetails.updateStatus(obj.getIdpost_load(), "loadPosted");

                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(ManageLoadActivity.this);
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

                alertTitle.setText("Continue with other Service Provider");
                alertMessage.setText("Load withdrawn from current Service Provider and visible for other Service Provider");
                alertPositiveButton.setVisibility(View.GONE);
                alertNegativeButton.setText("Ok");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        viewConsignmentCustomer.dismiss();
                        JumpTo.goToCustomerDashboard(ManageLoadActivity.this, phone, bidsReceivedSelected, true);
                    }
                });
                //------------------------------------------------------------------------------------------
            }
        });
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int state = msg.getData().getInt("state");
            if (state == 1) {
                loadingDialog.dismiss();
            }
        }
    };

    private void getUserDetails(String userIdForGet) {

        String url = getString(R.string.baseURL) + "/user/" + userIdForGet;
        Log.i("get user details", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
//                        name = obj.getString("name");
//                        mobile = obj.getString("phone_number");
//                        address = obj.getString("address");
//                        city = obj.getString("preferred_location");
//                        pinCode = obj.getString("pin_code");
                        String role = obj.getString("user_type");

                        if (role.equals("Customer")) {
                        } else {
                            loadingDialog.dismiss();
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
}
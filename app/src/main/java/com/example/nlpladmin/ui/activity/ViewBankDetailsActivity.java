package com.example.nlpladmin.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.BankModel;
import com.example.nlpladmin.ui.adapter.BanksAdapter;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewBankDetailsActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private ArrayList<BankModel> bankList = new ArrayList<>();
    private BanksAdapter bankListAdapter;
    private RecyclerView bankListRecyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    String userId, roleAPI;
    Dialog previewDialogCancelledCheque;
    ImageView previewDialogCancelledChequeImageView;

    View actionBar;
    TextView actionBarTitle;
    ImageView actionBarBackButton, actionBarMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bank_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        mQueue = Volley.newRequestQueue(ViewBankDetailsActivity.this);
        getUserDetails();
        //-------------------------------- Action Bar ----------------------------------------------
        actionBar = findViewById(R.id.view_bank_details_action_bar);
        actionBarTitle = (TextView) actionBar.findViewById(R.id.action_bar_title_text);
        actionBarBackButton = (ImageView) actionBar.findViewById(R.id.action_bar_back_button);
        actionBarMenuButton = (ImageView) actionBar.findViewById(R.id.action_bar_menu_button);

        actionBarTitle.setText("My Bank");
        actionBarMenuButton.setVisibility(View.GONE);
        actionBarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewBankDetailsActivity.this.finish();
            }
        });

        //---------------------------- Get Bank Details --------------------------------------------
        bankListRecyclerView = (RecyclerView) findViewById(R.id.bank_list_view);

        LinearLayoutManager linearLayoutManagerBank = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerBank.setReverseLayout(true);
        bankListRecyclerView.setLayoutManager(linearLayoutManagerBank);
        bankListRecyclerView.setHasFixedSize(true);

        bankListAdapter = new BanksAdapter(ViewBankDetailsActivity.this, bankList);
        bankListRecyclerView.setAdapter(bankListAdapter);
        getBankDetailsList();
        //------------------------------------------------------------------------------------------

        previewDialogCancelledCheque = new Dialog(ViewBankDetailsActivity.this);
        previewDialogCancelledCheque.setContentView(R.layout.dialog_preview_images);
        previewDialogCancelledCheque.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        previewDialogCancelledChequeImageView = (ImageView) previewDialogCancelledCheque.findViewById(R.id.dialog_preview_image_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view_bank_details_refresh_constrain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                RearrangeItems();
            }
        });
    }

    public void RearrangeItems() {
        JumpTo.viewBankDetailsActivity(ViewBankDetailsActivity.this, userId, true);
    }

    public void getBankDetailsList() {
        //---------------------------- Get Bank Details -------------------------------------------
        String url1 = getString(R.string.baseURL) + "/bank/getBkByUserId/" + userId;
        Log.i("URL: ", url1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bankList = new ArrayList<>();
                    JSONArray bankLists = response.getJSONArray("data");
                    for (int i = 0; i < bankLists.length(); i++) {
                        JSONObject obj = bankLists.getJSONObject(i);
                        BankModel modelBank = new BankModel();
                        modelBank.setUser_id(obj.getString("user_id"));
                        modelBank.setAccountholder_name(obj.getString("accountholder_name"));
                        modelBank.setBank_name(obj.getString("bank_name"));
                        modelBank.setAccount_number(obj.getString("account_number"));
                        modelBank.setRe_enter_acc_num(obj.getString("re_enter_acc_num"));
                        modelBank.setIFSI_CODE(obj.getString("IFSI_CODE"));
                        modelBank.setBank_id(obj.getString("bank_id"));
                        modelBank.setCancelled_cheque(obj.getString("cancelled_cheque"));
                        bankList.add(modelBank);
                    }
                    if (bankList.size() > 0) {
                        bankListAdapter.updateData(bankList);
                    } else {
                    }
//                    if (bankList.size() > 5) {
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        params.height = 235; //height recycleviewer
//                        bankListRecyclerView.setLayoutParams(params);
//                    } else {
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        bankListRecyclerView.setLayoutParams(params);
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

    public void getBankDetails(BankModel obj) {

    }

    public void onClickPreviewBankDetails(BankModel obj) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(previewDialogCancelledCheque.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        previewDialogCancelledCheque.show();
        previewDialogCancelledCheque.getWindow().setAttributes(lp);

        String cancelledChequeURL = obj.getCancelled_cheque();
        Log.i("IMAGE CHEQUE URL", cancelledChequeURL);

        new DownloadImageTask((ImageView) previewDialogCancelledCheque.findViewById(R.id.dialog_preview_image_view)).execute(cancelledChequeURL);

    }

    public void onClickAddBankDetails(View view) {
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
                        String nameAPI = obj.getString("name");
                        String mobileAPI = obj.getString("phone_number");
                        String addressAPI = obj.getString("address");
                        String stateAPI = obj.getString("state_code");
                        String cityAPI = obj.getString("preferred_location");
                        String pinCodeAPI = obj.getString("pin_code");
                        roleAPI = obj.getString("user_type");
                        String emailAPI = obj.getString("email_id");

//                        name.setText(nameAPI);
//
//                        String s1 = mobileAPI.substring(2, 12);
//                        mobileEdit.setText(s1);

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
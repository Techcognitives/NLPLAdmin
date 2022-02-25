package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewPersonalDetailsActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    TextView userFirmGSTTextview, userFirmGSTTextviewTitle, userFirmPANTextview, userFirmPANTextviewTitle, userNameTextView, userPhoneNumberTextView, userEmailTextView, userAddressTextView, userFirmNameTextView, userFirmAddressTextView, userFirmNameTitleTextView, userFirmAddressTitleTextView, userFirmTitle, userFirmAddCompany, userEditFirmDetailsTextView;
    String userNameAPI, userMobileNumberAPI, userAddressAPI, userCityAPI, userPinCodeAPI, userRoleAPI, userEmailIdAPI, isPersonalDetailsDoneAPI, isFirmDetailsDoneAPI, isBankDetailsDoneAPI, isTruckDetailsDoneAPI, isDriverDetailsDoneAPI;
    String companyNameAPI, companyAddressAPI, companyCityAPI, companyZipAPI, companyPanAPI, companyGstAPI;
    String  userId, isPersonalDetailsDone, isProfilePicAdded;
    TextView uploadPanAAdharBtn,  uploadPanAAdharBtnTitle, uploadProfilebtn;
    Dialog previewDialogPan, previewDialogAadhar, previewDialogProfile;
    View actionBar;
    TextView actionBarTitle, previewAadharBtn, previewPANBtn, previewProfileBtn;
    ImageView actionBarBackButton, actionBarMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        mQueue = Volley.newRequestQueue(ViewPersonalDetailsActivity.this);

        userNameTextView = (TextView) findViewById(R.id.view_personal_details_name_text_view);
        userPhoneNumberTextView = (TextView) findViewById(R.id.view_personal_details_phone_number_text_view);
        userEmailTextView = (TextView) findViewById(R.id.view_personal_details_email_id_text_view);
        userAddressTextView = (TextView) findViewById(R.id.view_personal_details_address_text_view);

        uploadPanAAdharBtnTitle = (TextView) findViewById(R.id.view_personal_details_complete_personal_details_text);
        uploadPanAAdharBtn = (TextView) findViewById(R.id.view_personal_details_add_personal_details);
        uploadProfilebtn = (TextView) findViewById(R.id.view_personal_details_add_profile);
        userFirmGSTTextview = (TextView) findViewById(R.id.view_personal_details_firm_gst_number_set);
        userFirmGSTTextviewTitle = (TextView) findViewById(R.id.view_personal_details_firm_gst_number);
        userFirmPANTextview = (TextView) findViewById(R.id.view_personal_details_firm_pan_number_set);
        userFirmPANTextviewTitle = (TextView) findViewById(R.id.view_personal_details_firm_pan_number);

        userFirmTitle = (TextView) findViewById(R.id.view_personal_details_firm_title);
        userFirmAddCompany = findViewById(R.id.view_personal_details_firm_btn);
        userFirmNameTitleTextView = (TextView) findViewById(R.id.view_personal_details_firm_name_title);
        userFirmNameTextView = (TextView) findViewById(R.id.view_personal_details_firm_name_text_view);
        userFirmAddressTitleTextView = (TextView) findViewById(R.id.view_personal_details_firm_address_title);
        userFirmAddressTextView = (TextView) findViewById(R.id.view_personal_details_firm_address_text_view);
        userEditFirmDetailsTextView = (TextView) findViewById(R.id.view_personal_details_edit_firm_details);

        previewAadharBtn = findViewById(R.id.view_personal_details_preview_aadhar_card);
        previewPANBtn = findViewById(R.id.view_personal_details_preview_pan_card);
        previewProfileBtn = findViewById(R.id.view_personal_details_preview_profile);

        previewDialogPan = new Dialog(ViewPersonalDetailsActivity.this);
        previewDialogPan.setContentView(R.layout.dialog_preview_images);
        previewDialogPan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        previewDialogAadhar = new Dialog(ViewPersonalDetailsActivity.this);
        previewDialogAadhar.setContentView(R.layout.dialog_preview_images);
        previewDialogAadhar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        previewDialogProfile = new Dialog(ViewPersonalDetailsActivity.this);
        previewDialogProfile.setContentView(R.layout.dialog_preview_images);
        previewDialogProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getUserDetails();
        getImageURL();

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

                        userNameAPI = obj.getString("name");
                        userMobileNumberAPI = obj.getString("phone_number");
                        userAddressAPI = obj.getString("address");
                        userCityAPI = obj.getString("preferred_location");
                        userPinCodeAPI = obj.getString("pin_code");
                        userRoleAPI = obj.getString("user_type");
                        isPersonalDetailsDone = obj.getString("isPersonal_dt_added");
                        isProfilePicAdded = obj.getString("isProfile_pic_added");

                        if (isProfilePicAdded.equals("1")){
                            uploadProfilebtn.setVisibility(View.GONE);
                            previewProfileBtn.setVisibility(View.GONE);
                        } else {
                            uploadProfilebtn.setVisibility(View.GONE);
                            previewProfileBtn.setVisibility(View.GONE);
                        }

                        if (isPersonalDetailsDone.equals("1")) {
                            previewAadharBtn.setVisibility(View.VISIBLE);
                            previewPANBtn.setVisibility(View.VISIBLE);
                            uploadPanAAdharBtn.setVisibility(View.GONE);
                            uploadPanAAdharBtnTitle.setVisibility(View.GONE);
                        } else {
                            previewAadharBtn.setVisibility(View.INVISIBLE);
                            previewPANBtn.setVisibility(View.INVISIBLE);
                            uploadPanAAdharBtn.setVisibility(View.VISIBLE);
                            uploadPanAAdharBtnTitle.setVisibility(View.VISIBLE);
                        }

                        userEmailIdAPI = obj.getString("email_id");

                        isPersonalDetailsDoneAPI = obj.getString("isPersonal_dt_added");
                        isFirmDetailsDoneAPI = obj.getString("isCompany_added");
                        isBankDetailsDoneAPI = obj.getString("isBankDetails_given");
                        isTruckDetailsDoneAPI = obj.getString("isTruck_added");
                        isDriverDetailsDoneAPI = obj.getString("isDriver_added");

                        userNameTextView.setText(userNameAPI);

                        String s1 = userMobileNumberAPI.substring(2, 12);

                        userPhoneNumberTextView.setText("+91 " + s1);

                        userEmailTextView.setText(userEmailIdAPI);

                        userAddressTextView.setText(userAddressAPI + ", " + userCityAPI + " " + userPinCodeAPI);

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

    private void getImageURL() {
        String url = getString(R.string.baseURL) + "/imgbucket/Images/" + userId;
        Log.i("Image URL", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray imageList = response.getJSONArray("data");
                    for (int i = 0; i < imageList.length(); i++) {
                        JSONObject obj = imageList.getJSONObject(i);
                        String imageType = obj.getString("image_type");

                        String panImageURL, aadharImageURL, profileImgUrl;

                        if (imageType.equals("aadhar")) {
                            aadharImageURL = obj.getString("image_url");
                            new DownloadImageTask((ImageView) previewDialogAadhar.findViewById(R.id.dialog_preview_image_view)).execute(aadharImageURL);
                            Log.i("IMAGE AADHAR URL", aadharImageURL);
                        }

                        if (imageType.equals("pan")) {
                            panImageURL = obj.getString("image_url");
                            Log.i("IMAGE PAN URL", panImageURL);
                            new DownloadImageTask((ImageView) previewDialogPan.findViewById(R.id.dialog_preview_image_view)).execute(panImageURL);
                        }

                        if (imageType.equals("profile")) {
                            profileImgUrl = obj.getString("image_url");
                            Log.i("IMAGE PAN URL", profileImgUrl);
                            new DownloadImageTask((ImageView) previewDialogProfile.findViewById(R.id.dialog_preview_image_view)).execute(profileImgUrl);
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

    public void onClickPreviewAadharCard(View view) {
        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(previewDialogAadhar.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.gravity = Gravity.CENTER;

        previewDialogAadhar.show();
        previewDialogAadhar.getWindow().setAttributes(lp2);
    }

    public void onClickPreviewPanCard(View view) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(previewDialogPan.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        previewDialogPan.show();
        previewDialogPan.getWindow().setAttributes(lp);
    }

    public void onClickPreviewProfile(View view) {
        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(previewDialogProfile.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.gravity = Gravity.CENTER;

        previewDialogProfile.show();
        previewDialogProfile.getWindow().setAttributes(lp2);
    }

    public void onClickEditPersonalDetailsView(View view) {
        JumpTo.personalDetailsAndIdProofActivity(ViewPersonalDetailsActivity.this, userId, false);
    }

    public void onClickAddPersonalDetails(View view) {
        JumpTo.personalDetailsActivity(ViewPersonalDetailsActivity.this, userId, false);
    }
}
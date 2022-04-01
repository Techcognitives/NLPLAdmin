package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.UpdateMethods.UpdateUserDetails;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.spec.IvParameterSpec;

public class ViewUserDetailsActivity extends AppCompatActivity {

    String userId, isProfileActive;
    CheckBox VSPersonalCheckBox, VSBankCheckBox, VSTruckCheckBox, VSDriverCheckBox, PCPersonalCheckBox, PCBankCheckBox, PCTruckCheckBox, PCDriverCheckBox;
    private RequestQueue mQueue;
    View verification_status_view, profile_completeness_view, user_list_view;
    TextView deactivateProfile, actionBarTitle, userName, userRole, userNumber, VSTick, PCTick, VSKyc, VSBank, VSTruck, VSDriver, PCPersonal, PCBank, PCTruck, PCDriver, VSTitle, PCTitle, VSViewBtn1, VSViewBtn2, VSViewBtn3, VSViewBtn4, PCViewBtn1, PCViewBtn2, PCViewBtn3, PCViewBtn4;
    ImageView profilePic, actionBarBackButton, actionBarMenuButton;
    Dialog previewDialogProfile;
    boolean isUserVerified;
    View actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        actionBar = findViewById(R.id.customer_setting_and_preferences_action_bar);
        actionBarTitle = (TextView) actionBar.findViewById(R.id.action_bar_title_text);
        actionBarBackButton = (ImageView) actionBar.findViewById(R.id.action_bar_back_button);
        actionBarMenuButton = (ImageView) actionBar.findViewById(R.id.action_bar_menu_button);

        actionBarTitle.setText("User Details");
        actionBarMenuButton.setVisibility(View.GONE);
        actionBarBackButton.setVisibility(View.VISIBLE);
        actionBarBackButton.setOnClickListener(view -> {
            ViewUserDetailsActivity.this.finish();
        });

        verification_status_view = findViewById(R.id.customer_setting_and_preferences_verification_status);
        profile_completeness_view = findViewById(R.id.customer_setting_and_preferences_profile_completeness);
        user_list_view = findViewById(R.id.customer_setting_and_preferences_user_list);
        deactivateProfile = findViewById(R.id.user_details_deactivate_profile_btn);

        userName = user_list_view.findViewById(R.id.users_list_name);
        userRole = user_list_view.findViewById(R.id.users_list_role);
        userNumber = user_list_view.findViewById(R.id.user_list_number);
        profilePic = user_list_view.findViewById(R.id.users_list_profilePhto);

        previewDialogProfile = new Dialog(ViewUserDetailsActivity.this);
        previewDialogProfile.setContentView(R.layout.dialog_preview_images);
        previewDialogProfile.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

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

        PCViewBtn1.setOnClickListener(View -> onClickViewAndVerifyKyc());
        PCViewBtn2.setOnClickListener(View -> onClickViewBankDetails());
        PCViewBtn3.setOnClickListener(View -> onClickViewTruckDetails());
        PCViewBtn4.setOnClickListener(View -> onClickViewDriverDetails());

        VSViewBtn1.setOnClickListener(View -> onClickViewAndVerifyKyc());
        VSViewBtn2.setOnClickListener(View -> onClickViewBankDetails());
        VSViewBtn3.setOnClickListener(View -> onClickViewTruckDetails());
        VSViewBtn4.setOnClickListener(View -> onClickViewDriverDetails());

        VSPersonalCheckBox.setOnClickListener(View -> onClickPersonalCheckbox());
        VSBankCheckBox.setOnClickListener(View -> onClickBankCheckbox());
        VSTruckCheckBox.setOnClickListener(View -> onClickTruckCheckbox());
        VSDriverCheckBox.setOnClickListener(View -> onClickDriverCheckbox());

    }

    private void onClickPersonalCheckbox() {
        if (isUserVerified) {
            UpdateUserDetails.updateIsUserVerified(userId, "0");
        } else {
            UpdateUserDetails.updateIsUserVerified(userId, "1");
        }
    }

    private void onClickBankCheckbox() {

    }

    private void onClickTruckCheckbox() {

    }

    private void onClickDriverCheckbox() {

    }

    public void viewProfile(View view) {
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

    private void onClickDeactivateProfile(String status) {
        UpdateUserDetails.updateUserIsProfileActive(userId, status);
        alertForActivation(status);
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

                        if (obj.getString("is_user_verfied").equals("1")){
                            isUserVerified = true;
                            VSPersonalCheckBox.setChecked(true);
                        } else {
                            isUserVerified = false;
                            VSPersonalCheckBox.setChecked(false);
                        }

                        if (obj.getString("isProfile_pic_added").equals("1")) {
                            getProfilePic();
                        }

                        if (obj.getString("user_type").equals("Customer")) {
                            userRole.setText("Load Poster");
                        } else {
                            userRole.setText(obj.getString("user_type"));
                        }

                        String phone = obj.getString("phone_number");
                        userName.setText(obj.getString("name"));
                        userNumber.setText("+" + obj.getString("phone_number"));
                        isProfileActive = obj.getString("is_account_active");

                        userNumber.setOnClickListener(View -> onClickOpenDialer(phone));

                        if (isProfileActive.equals("null")) {
                            deactivateProfile.setBackground(getResources().getDrawable(R.drawable.red_color));
                            deactivateProfile.setText("Deactivate Profile");
                            deactivateProfile.setOnClickListener(View -> onClickDeactivateProfile("0"));
                        } else if (isProfileActive.equals("1")) {
                            deactivateProfile.setBackground(getResources().getDrawable(R.drawable.button_active));
                            deactivateProfile.setText("Activate Profile");
                            deactivateProfile.setOnClickListener(View -> onClickDeactivateProfile("0"));
                        } else if (isProfileActive.equals("0")) {
                            deactivateProfile.setBackground(getResources().getDrawable(R.drawable.red_color));
                            deactivateProfile.setText("Deactivate Profile");
                            deactivateProfile.setOnClickListener(View -> onClickDeactivateProfile("1"));
                        }

                        if (obj.getString("user_type").equals("Owner") || obj.getString("user_type").equals("Driver") || obj.getString("user_type").equals("Broker")) {
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
                            VSViewBtn1.setEnabled(true);
                            VSPersonalCheckBox.setEnabled(true);
                            PCPersonal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            VSPersonalCheckBox.setEnabled(false);
                            VSViewBtn1.setEnabled(false);
                            PCPersonal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isBankDetails_given").equals("1")) {
                            VSBankCheckBox.setEnabled(true);
                            VSViewBtn2.setEnabled(true);
                            PCBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            VSBankCheckBox.setEnabled(false);
                            VSViewBtn2.setEnabled(false);
                            PCBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isTruck_added").equals("1")) {
                            VSTruckCheckBox.setEnabled(true);
                            VSViewBtn3.setEnabled(true);
                            PCTruck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            VSTruckCheckBox.setEnabled(false);
                            VSViewBtn3.setEnabled(false);
                            PCTruck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_success_small, 0, 0, 0);
                        }

                        if (obj.getString("isDriver_added").equals("1")) {
                            VSDriverCheckBox.setEnabled(true);
                            VSViewBtn4.setEnabled(true);
                            PCDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_small, 0, 0, 0);
                        } else {
                            VSDriverCheckBox.setEnabled(false);
                            VSViewBtn4.setEnabled(false);
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

    private void onClickOpenDialer(String phone_number) {
        Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone_number));
        startActivity(i1);
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

    public void alertForActivation(String status) {

        //----------------------- Alert Dialog -------------------------------------------------
        Dialog alert = new Dialog(ViewUserDetailsActivity.this);
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

        if (status.equals("0")) {
            alertTitle.setText("Account Deactivation");
            alertMessage.setText("User account deactivated successfully");
        } else {
            alertTitle.setText("Account Activation");
            alertMessage.setText("User account activated successfully");
        }

        alertPositiveButton.setVisibility(View.GONE);
        alertNegativeButton.setText("OK");
        alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
        alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

        alertNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                JumpTo.viewUserDetailsActivity(ViewUserDetailsActivity.this, userId);
            }
        });
        //------------------------------------------------------------------------------------------

    }

    public void onClickViewAndVerifyKyc() {
        JumpTo.viewPersonalDetailsActivity(ViewUserDetailsActivity.this, userId);
    }

    public void onClickViewBankDetails() {
        JumpTo.viewBankDetailsActivity(ViewUserDetailsActivity.this, userId, false, false);
    }

    public void onClickViewTruckDetails() {
        JumpTo.viewTruckDetailsActivity(ViewUserDetailsActivity.this, userId, false, false);
    }

    public void onClickViewDriverDetails() {
        JumpTo.viewDriverDetailsActivity(ViewUserDetailsActivity.this, userId, false, false);
    }
}
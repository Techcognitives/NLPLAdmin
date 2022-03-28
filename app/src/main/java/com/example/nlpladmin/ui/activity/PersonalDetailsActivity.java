package com.example.nlpladmin.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.Requests.ImageRequest;
import com.example.nlpladmin.model.Responses.ImageResponse;
import com.example.nlpladmin.model.Responses.UploadImageResponse;
import com.example.nlpladmin.model.UpdateMethods.UpdateUserDetails;
import com.example.nlpladmin.utils.ApiClient;
import com.example.nlpladmin.utils.FileUtils;
import com.example.nlpladmin.utils.JumpTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailsActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    View action_bar;
    TextView actionBarTitle;
    ImageView actionBarBackButton;
    Dialog chooseDialog;

    TextView panCardText, editPAN, editFront, frontText, profileText, editProfile;
    Button uploadPAN, uploadF, uploadProfile, okPersonalDetails;
    ImageView imgPAN, imgF, imgProfile, previewProfile, previewPan, previewAadhar;
    private int GET_FROM_GALLERY = 0;
    private int GET_FROM_GALLERY1 = 1;
    private int CAMERA_PIC_REQUEST = 3;
    private int CAMERA_PIC_REQUEST1 = 2;
    private int CAMERA_PIC_REQUEST2 = 4;
    private int GET_FROM_GALLERY2 = 5;


    View panAndAadharView;
    ConstraintLayout aadharConstrain, panConstrain, profileConstrain;
    TextView uploadAadharTitle, uploadPanTitle, uploadProfileTitle;

    String userRoleAPI, userId, mobile;
    Boolean profilePic, isPanUploaded = false, isFrontUploaded = false, isProfileUploaded = false;
    String img_type;

    Dialog previewDialogPan, previewDialogAadhar, previewDialogProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
        mQueue = Volley.newRequestQueue(PersonalDetailsActivity.this);

        panAndAadharView = (View) findViewById(R.id.personal_details_pan_and_aadhar);
        panCardText = panAndAadharView.findViewById(R.id.pancard1);
        frontText = panAndAadharView.findViewById(R.id.frontText);
        uploadPAN = panAndAadharView.findViewById(R.id.uploadPan);
        uploadF = panAndAadharView.findViewById(R.id.uploadF);
        imgPAN = panAndAadharView.findViewById(R.id.imagePan);
        imgF = panAndAadharView.findViewById(R.id.imageF);
        editPAN = panAndAadharView.findViewById(R.id.edit1);
        editFront = panAndAadharView.findViewById(R.id.editFront);
        okPersonalDetails = findViewById(R.id.okPersonalDetails);
        previewPan = (ImageView) panAndAadharView.findViewById(R.id.pan_aadhar_preview_pan);
        previewAadhar = (ImageView) panAndAadharView.findViewById(R.id.pan_aadhar_preview_aadhar);
        previewProfile = (ImageView) panAndAadharView.findViewById(R.id.preview_profile);
        editProfile = panAndAadharView.findViewById(R.id.editProfile);
        uploadProfile = panAndAadharView.findViewById(R.id.uploadProfile);
        profileText = panAndAadharView.findViewById(R.id.ProfileText);
        imgProfile = panAndAadharView.findViewById(R.id.imageProfile);

        aadharConstrain = panAndAadharView.findViewById(R.id.aadhar_constrain);
        panConstrain = panAndAadharView.findViewById(R.id.pan_card_constrain);
        profileConstrain = panAndAadharView.findViewById(R.id.upload_profile_constrain);
        uploadPanTitle = panAndAadharView.findViewById(R.id.upload_pan_text);
        uploadAadharTitle = panAndAadharView.findViewById(R.id.upload_aadhar_text);
        uploadProfileTitle = panAndAadharView.findViewById(R.id.upload_profile_text);

        uploadProfileTitle.setVisibility(View.GONE);
        profileConstrain.setVisibility(View.GONE);
        uploadAadharTitle.setVisibility(View.VISIBLE);
        aadharConstrain.setVisibility(View.VISIBLE);
        uploadPanTitle.setVisibility(View.VISIBLE);
        panConstrain.setVisibility(View.VISIBLE);

        previewDialogPan = new Dialog(PersonalDetailsActivity.this);
        previewDialogPan.setContentView(R.layout.dialog_preview_images);
        previewDialogPan.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        previewDialogAadhar = new Dialog(PersonalDetailsActivity.this);
        previewDialogAadhar.setContentView(R.layout.dialog_preview_images);
        previewDialogAadhar.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        previewDialogProfile = new Dialog(PersonalDetailsActivity.this);
        previewDialogProfile.setContentView(R.layout.dialog_preview_images);
        previewDialogProfile.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        getUserDetails();
        previewPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(previewDialogPan.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;

                previewDialogPan.show();
                previewDialogPan.getWindow().setAttributes(lp);
            }
        });

        previewAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(previewDialogAadhar.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.gravity = Gravity.CENTER;

                previewDialogAadhar.show();
                previewDialogAadhar.getWindow().setAttributes(lp2);
            }
        });

        previewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(previewDialogProfile.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.gravity = Gravity.CENTER;

                previewDialogProfile.show();
                previewDialogProfile.getWindow().setAttributes(lp2);
            }
        });

        uploadF.setVisibility(View.VISIBLE);
        editFront.setVisibility(View.INVISIBLE);

        uploadPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPanDialogChoose();
            }
        });

        editPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPanDialogChoose();
            }
        });

        uploadF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAadharDialogChoose();
            }
        });

        editFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAadharDialogChoose();
            }
        });

        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileDialogChoose();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileDialogChoose();
            }
        });
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
                        userRoleAPI = obj.getString("user_type");
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


    //-----------------------------------------------upload Image------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request code for PAN
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("PAN Card Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });
            //------------------------------------------------------------------------------------------
            panCardText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadPAN.setVisibility(View.INVISIBLE);
            editPAN.setVisibility(View.VISIBLE);
            previewPan.setVisibility(View.VISIBLE);
            isPanUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            saveImage(imageRequest());
            uploadImage(picturePath);
            ImageView editedAadhar = (ImageView) previewDialogAadhar.findViewById(R.id.dialog_preview_image_view);
            ImageView editedPan = (ImageView) previewDialogPan.findViewById(R.id.dialog_preview_image_view);
            editedPan.setImageURI(selectedImage);
            imgPAN.setImageURI(selectedImage);

        } else if (requestCode == GET_FROM_GALLERY1 && resultCode == Activity.RESULT_OK) {
//----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("Aadhar Card Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();

                }
            });
            //------------------------------------------------------------------------------------------
            frontText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadF.setVisibility(View.INVISIBLE);
            editFront.setVisibility(View.VISIBLE);
            previewAadhar.setVisibility(View.VISIBLE);
            isFrontUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            saveImage(imageRequest());
            uploadImage(picturePath);
            ImageView editedAadhar = (ImageView) previewDialogAadhar.findViewById(R.id.dialog_preview_image_view);
            ImageView editedPan = (ImageView) previewDialogPan.findViewById(R.id.dialog_preview_image_view);
            editedAadhar.setImageURI(selectedImage);
            imgF.setImageURI(selectedImage);

        } else if (requestCode == CAMERA_PIC_REQUEST) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("PAN Card Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();

                }
            });
            //------------------------------------------------------------------------------------------

            panCardText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadPAN.setVisibility(View.INVISIBLE);
            editPAN.setVisibility(View.VISIBLE);
            previewPan.setVisibility(View.VISIBLE);
            isPanUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            imgPAN.setImageBitmap(BitmapFactory.decodeFile(path));
            saveImage(imageRequest());
            uploadImage(path);

        } else if (requestCode == CAMERA_PIC_REQUEST1) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("Aadhar Card Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();

                }
            });
            //------------------------------------------------------------------------------------------

            frontText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadF.setVisibility(View.INVISIBLE);
            editFront.setVisibility(View.VISIBLE);
            previewAadhar.setVisibility(View.VISIBLE);
            isFrontUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            imgF.setImageBitmap(BitmapFactory.decodeFile(path));
            saveImage(imageRequest());
            uploadImage(path);
        } else if (requestCode == GET_FROM_GALLERY2 && resultCode == Activity.RESULT_OK) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("Profile Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });
            //------------------------------------------------------------------------------------------
            profileText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadProfile.setVisibility(View.INVISIBLE);
            editProfile.setVisibility(View.VISIBLE);
            previewProfile.setVisibility(View.VISIBLE);
            isProfileUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            saveImage(imageRequest());
            uploadImage(picturePath);
            ImageView editedAadhar = (ImageView) previewDialogAadhar.findViewById(R.id.dialog_preview_image_view);
            ImageView editedPan = (ImageView) previewDialogPan.findViewById(R.id.dialog_preview_image_view);
            editedPan.setImageURI(selectedImage);
            imgProfile.setImageURI(selectedImage);

        } else if (requestCode == CAMERA_PIC_REQUEST2) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

            alertTitle.setText("Personal Details");
            alertMessage.setText("Profile Uploaded Successfully");
            alertPositiveButton.setVisibility(View.GONE);
            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();

                }
            });
            //------------------------------------------------------------------------------------------

            profileText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadProfile.setVisibility(View.INVISIBLE);
            editProfile.setVisibility(View.VISIBLE);
            previewProfile.setVisibility(View.VISIBLE);
            isProfileUploaded = true;

//            if (profilePic) {
//                if (isProfileUploaded) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            } else {
//                if (isPanUploaded && isFrontUploaded ) {
//                    okPersonalDetails.setBackgroundResource((R.drawable.button_active));
//                }
//            }

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            imgProfile.setImageBitmap(BitmapFactory.decodeFile(path));
            saveImage(imageRequest());
            uploadImage(path);

        }
    }
    //-------------------------------------------------------------------------------------------------------------------
    public void onClickOKPersonal(View view) {
//        if (profilePic){
//            if (isProfileUploaded){
//                UpdateUserDetails.updateUserIsProfileAdded(userId,"1");
//
//                Dialog alert = new Dialog(PersonalDetailsActivity.this);
//                alert.setContentView(R.layout.dialog_alert);
//                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(alert.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.gravity = Gravity.CENTER;
//
//                alert.show();
//                alert.getWindow().setAttributes(lp);
//                alert.setCancelable(false);
//
//                TextView alertTitle = (TextView) alert.findViewById(R.id.dialog_alert_title);
//                TextView alertMessage = (TextView) alert.findViewById(R.id.dialog_alert_message);
//                TextView alertPositiveButton = (TextView) alert.findViewById(R.id.dialog_alert_positive_button);
//                TextView alertNegativeButton = (TextView) alert.findViewById(R.id.dialog_alert_negative_button);
//
//                alertTitle.setText("Profile Picture");
//                alertMessage.setText("Profile Picture added successfully");
//                alertPositiveButton.setVisibility(View.GONE);
//                alertNegativeButton.setText("OK");
//                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
//                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
//
//                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alert.dismiss();
//                        JumpTo.viewPersonalDetailsActivity(PersonalDetailsActivity.this, userId);
//                    }
//                });
//            }
//        } else {
            if (isPanUploaded && isFrontUploaded) {
                UpdateUserDetails.updateUserIsPersonalDetailsAdded(userId, "1");
                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(PersonalDetailsActivity.this);
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

                alertTitle.setText("Personal Details");
                alertMessage.setText("Personal Details added successfully");
                alertPositiveButton.setVisibility(View.GONE);
                alertNegativeButton.setText("OK");
                alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

                alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        JumpTo.viewPersonalDetailsActivity(PersonalDetailsActivity.this, userId);
                    }
                });
            }
//        }
    }
    //--------------------------------------create image in API -------------------------------------
    public ImageRequest imageRequest() {
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setUser_id(userId);
        imageRequest.setImage_type(img_type);
        return imageRequest;
    }

    public void saveImage(ImageRequest imageRequest) {
        Call<ImageResponse> imageResponseCall = ApiClient.getImageService().saveImage(imageRequest);
        imageResponseCall.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {

            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        Log.i("file uri: ", String.valueOf(fileUri));
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    private void uploadImage(String picPath) {

        File file = new File(picPath);
//        File file = new File(getExternalFilesDir("/").getAbsolutePath(), file);

        MultipartBody.Part body = prepareFilePart("file", Uri.fromFile(file));

        Call<UploadImageResponse> call = ApiClient.getImageUploadService().uploadImage(userId, img_type, body);
        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                Log.i("successful:", "success");
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                t.printStackTrace();
                Log.i("failed:", "failed");
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadPanDialogChoose() {
        requestPermissionsForCamera();
        requestPermissionsForGalleryWRITE();
        requestPermissionsForGalleryREAD();
        img_type = "pan";

        Dialog chooseDialog;
        chooseDialog = new Dialog(PersonalDetailsActivity.this);
        chooseDialog.setContentView(R.layout.dialog_choose);
        chooseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(chooseDialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.BOTTOM;

        chooseDialog.show();
        chooseDialog.getWindow().setAttributes(lp2);

        ImageView camera = chooseDialog.findViewById(R.id.dialog_choose_camera_image);
        ImageView gallery = chooseDialog.findViewById(R.id.dialog__choose_photo_lirary_image);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                chooseDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                chooseDialog.dismiss();
            }
        });

    }

    private void uploadAadharDialogChoose() {
        requestPermissionsForCamera();
        requestPermissionsForGalleryWRITE();
        requestPermissionsForGalleryREAD();
        img_type = "aadhar";

        Dialog chooseDialog;
        chooseDialog = new Dialog(PersonalDetailsActivity.this);
        chooseDialog.setContentView(R.layout.dialog_choose);
        chooseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(chooseDialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.BOTTOM;

        chooseDialog.show();
        chooseDialog.getWindow().setAttributes(lp2);

        ImageView camera = chooseDialog.findViewById(R.id.dialog_choose_camera_image);
        ImageView gallery = chooseDialog.findViewById(R.id.dialog__choose_photo_lirary_image);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
                chooseDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY1);
                chooseDialog.dismiss();
            }
        });
    }

    private void uploadProfileDialogChoose() {
        requestPermissionsForCamera();
        requestPermissionsForGalleryWRITE();
        requestPermissionsForGalleryREAD();
        img_type = "profile";

        Dialog chooseDialog;
        chooseDialog = new Dialog(PersonalDetailsActivity.this);
        chooseDialog.setContentView(R.layout.dialog_choose);
        chooseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(chooseDialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.BOTTOM;

        chooseDialog.show();
        chooseDialog.getWindow().setAttributes(lp2);

        ImageView camera = chooseDialog.findViewById(R.id.dialog_choose_camera_image);
        ImageView gallery = chooseDialog.findViewById(R.id.dialog__choose_photo_lirary_image);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST2);
                chooseDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY2);
                chooseDialog.dismiss();
            }
        });
    }

    private void requestPermissionsForCamera() {
        if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalDetailsActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void requestPermissionsForGalleryWRITE() {
        if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalDetailsActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
    }

    private void requestPermissionsForGalleryREAD() {
        if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalDetailsActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JumpTo.viewPersonalDetailsActivity(PersonalDetailsActivity.this, userId);
    }

}
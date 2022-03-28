package com.example.nlpladmin.ui.activity;

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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nlpladmin.R;
import com.example.nlpladmin.model.Requests.AddDriverRequest;
import com.example.nlpladmin.model.Requests.UserRequest;
import com.example.nlpladmin.model.Responses.AddDriverResponse;
import com.example.nlpladmin.model.Responses.UploadDriverLicenseResponse;
import com.example.nlpladmin.model.Responses.UploadDriverSelfieResponse;
import com.example.nlpladmin.model.Responses.UserResponse;
import com.example.nlpladmin.model.UpdateMethods.UpdateDriverDetails;
import com.example.nlpladmin.model.UpdateMethods.UpdateTruckDetails;
import com.example.nlpladmin.model.UpdateMethods.UpdateUserDetails;
import com.example.nlpladmin.utils.ApiClient;
import com.example.nlpladmin.utils.DownloadImageTask;
import com.example.nlpladmin.utils.FileUtils;
import com.example.nlpladmin.utils.GetCurrentLocation;
import com.example.nlpladmin.utils.JumpTo;
import com.example.nlpladmin.utils.SelectCity;
import com.example.nlpladmin.utils.SelectState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetailsActivity extends AppCompatActivity {

    View action_bar;
    TextView actionBarTitle;
    ImageView actionBarMenuButton;

    EditText driverName, driverMobile, driverEmailId;
    ImageView actionBarBackButton;
    Intent data;
    int requestCode;
    int resultCode;
    String isDriverDetailsDoneAPI;
    GetCurrentLocation getCurrentLocation;

    Button uploadDL, okDriverDetails, uploadSelfie;
    TextView textDL, editDL, series, textDS, editDS;
    int GET_FROM_GALLERY = 0;
    int GET_FROM_GALLERY1 = 1;
    int CAMERA_PIC_REQUEST = 1;
    int CAMERA_PIC_REQUEST1 = 3;
    ImageView driverLicenseImage, driverSelfieImg;

    private RequestQueue mQueue;

    String driverUserId, driverUserIdGet;

    String pathForDL, pathForSelfie, userId, driverId, driverNameAPI, driverNumberAPI, driverEmailAPI, mobile;
    Boolean fromBidNow = false, isDLUploaded = false, isEdit, isSelfieUploded = false, idDLEdited = false;
    ImageView previewDrivingLicense, previewSelfie, previewDLImageView, previewSelfieImageView;
    Dialog previewDialogDL, previewDialogSelfie;
    View personalAndAddress;

    TextView selectStateText, selectDistrictText, setCurrentLocation;
    String selectedState;
    EditText pinCode, address;

    String userIdAPI, nameAPI, stateAPI, pinCodeAPI, addressAPI, mobileNoAPI, cityAPI, roleAPI;
    ArrayList<String> arrayUserId, arrayMobileNo, arrayPinCode, arrayName, arrayRole, arrayCity, arrayAddress, arrayState;
    Boolean alreadyDriver = true, isSelfieEdited = false;
    String truckIdPass, driverIdPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
            isEdit = bundle.getBoolean("isEdit");
            driverId = bundle.getString("driverId");
            fromBidNow = bundle.getBoolean("fromBidNow");
            mobile = bundle.getString("mobile");
            truckIdPass = bundle.getString("truckIdPass");
        }
        mQueue = Volley.newRequestQueue(DriverDetailsActivity.this);

        personalAndAddress = (View) findViewById(R.id.driver_details_personal_and_address);
        action_bar = (View) findViewById(R.id.driver_details_action_bar);

        actionBarTitle = (TextView) action_bar.findViewById(R.id.action_bar_title_text);
        actionBarBackButton = (ImageView) action_bar.findViewById(R.id.action_bar_back_button);
        actionBarMenuButton = (ImageView) action_bar.findViewById(R.id.action_bar_menu_button);
        actionBarMenuButton.setVisibility(View.GONE);

        driverMobile = (EditText) personalAndAddress.findViewById(R.id.registration_mobile_no_edit);
        driverMobile.setEnabled(false);
        driverName = personalAndAddress.findViewById(R.id.registration_edit_name);
        okDriverDetails = findViewById(R.id.driver_details_ok_button);
        series = (TextView) personalAndAddress.findViewById(R.id.registration_prefix);
        driverEmailId = (EditText) personalAndAddress.findViewById(R.id.registration_email_id_edit);
        setCurrentLocation = (TextView) personalAndAddress.findViewById(R.id.personal_and_address_get_current_location);
        setCurrentLocation.setVisibility(View.VISIBLE);

        previewDLImageView = (ImageView) findViewById(R.id.driver_details_preview_driving_license);
        previewSelfieImageView = (ImageView) findViewById(R.id.driver_details_preview_selfie);
        ConstraintLayout personalConstrain = (ConstraintLayout) personalAndAddress.findViewById(R.id.personal_registration_sp_constrain);
        personalConstrain.setVisibility(View.GONE);

        pinCode = (EditText) personalAndAddress.findViewById(R.id.registration_pin_code_edit);
        address = (EditText) personalAndAddress.findViewById(R.id.registration_address_edit);
        selectStateText = (TextView) personalAndAddress.findViewById(R.id.registration_select_state);
        selectDistrictText = (TextView) personalAndAddress.findViewById(R.id.registration_select_city);

        driverName.setHint("Enter Driver Name");
        driverMobile.setHint("Enter 10 digit Driver Number");
        driverEmailId.setHint("Enter Driver Email Id");

        driverName.addTextChangedListener(driverNameWatcher);
        driverMobile.addTextChangedListener(driverMobileWatcher);
        pinCode.addTextChangedListener(pinCodeWatcher);
        address.addTextChangedListener(driverNameWatcher);

        address.setFilters(new InputFilter[]{filter});
        getCurrentLocation = new GetCurrentLocation();


        driverEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String email = driverEmailId.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (email.matches(emailPattern)) {
                    driverEmailId.setBackground(getResources().getDrawable(R.drawable.edit_text_border));
                } else {
                    driverEmailId.setBackground(getResources().getDrawable(R.drawable.edit_text_border_red));
                }

            }
        });

        previewDialogDL = new Dialog(DriverDetailsActivity.this);
        previewDialogDL.setContentView(R.layout.dialog_preview_images);
        previewDialogDL.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        previewDialogSelfie = new Dialog(DriverDetailsActivity.this);
        previewDialogSelfie.setContentView(R.layout.dialog_preview_images);
        previewDialogSelfie.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        driverName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        driverName.setFilters(new InputFilter[]{filter});

        actionBarTitle.setText("Driver Details");
        actionBarBackButton.setVisibility(View.VISIBLE);
        actionBarBackButton.setOnClickListener(view -> {
            DriverDetailsActivity.this.finish();
        });

        uploadDL = findViewById(R.id.driver_details_upload_driver_license);
        uploadSelfie = findViewById(R.id.upload_driver_selfie);
        editDL = findViewById(R.id.driver_details_edit_driver_license);
        textDL = findViewById(R.id.driver_details_driver_license_text_image);
        driverLicenseImage = (ImageView) findViewById(R.id.driver_details_driver_license_image);
        driverSelfieImg = findViewById(R.id.driver_selfie_img);
        textDS = findViewById(R.id.driver_selfie_text);
        editDS = findViewById(R.id.driver_details_edit_selfie_text);
        previewDrivingLicense = (ImageView) previewDialogDL.findViewById(R.id.dialog_preview_image_view);
        previewSelfie = (ImageView) previewDialogSelfie.findViewById(R.id.dialog_preview_image_view);

        String mobileNoWatcher = driverMobile.getText().toString().trim();
        String nameWatcher = driverName.getText().toString().trim();

//        if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
//            okDriverDetails.setEnabled(true);
//            okDriverDetails.setBackgroundResource(R.drawable.button_active);
//        }


        if (userId != null) {
            getUserDetails();
        }

        arrayUserId = new ArrayList<>();
        arrayMobileNo = new ArrayList<>();
        arrayAddress = new ArrayList<>();
        arrayCity = new ArrayList<>();
        arrayPinCode = new ArrayList<>();
        arrayName = new ArrayList<>();
        arrayRole = new ArrayList<>();
        arrayState = new ArrayList<>();

        if (isEdit) {
            isDLUploaded = true;
            isSelfieUploded = true;
            okDriverDetails.setEnabled(false);
            okDriverDetails.setBackgroundResource(R.drawable.button_de_active);
            uploadDL.setVisibility(View.INVISIBLE);
            uploadSelfie.setVisibility(View.INVISIBLE);
            editDS.setVisibility(View.VISIBLE);
            editDL.setVisibility(View.VISIBLE);
            previewDLImageView.setVisibility(View.VISIBLE);
            previewSelfieImageView.setVisibility(View.VISIBLE);
            textDS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            textDL.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            getDriverDetails();
        }

        uploadSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogChooseForDriverSelfie();
            }
        });

        editDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelfieEdited = true;
                DialogChooseForDriverSelfie();
            }
        });


        uploadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogChoose();
            }
        });

        editDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idDLEdited = true;
                DialogChoose();
            }
        });

        selectStateText.setOnClickListener(view -> SelectState.selectState(DriverDetailsActivity.this, selectStateText, selectDistrictText));

        selectDistrictText.setOnClickListener(view -> {
            if (!selectStateText.getText().toString().isEmpty()) {
                selectedState = selectStateText.getText().toString();
                SelectCity.selectCity(DriverDetailsActivity.this, selectedState, selectDistrictText);
            }
        });

        if (isEdit) {
            if (mobile.equals("91" + driverMobile.getText().toString())) {
                setCurrentLocation.setVisibility(View.INVISIBLE);
                driverName.setCursorVisible(false);
                driverName.setEnabled(false);
                driverMobile.setCursorVisible(false);
                driverMobile.setEnabled(false);
                address.setCursorVisible(false);
                address.setEnabled(false);
                pinCode.setCursorVisible(false);
                pinCode.setEnabled(false);
                driverEmailId.setCursorVisible(false);
                driverEmailId.setEnabled(false);
                selectDistrictText.setEnabled(false);
                selectStateText.setEnabled(false);
            } else {
                setCurrentLocation.setVisibility(View.INVISIBLE);
                driverName.setCursorVisible(true);
                driverName.setEnabled(true);
                driverMobile.setCursorVisible(true);
                driverMobile.setEnabled(true);
                address.setCursorVisible(true);
                address.setEnabled(true);
                pinCode.setCursorVisible(true);
                pinCode.setEnabled(true);
                driverEmailId.setCursorVisible(true);
                driverEmailId.setEnabled(true);
                selectDistrictText.setEnabled(true);
                selectStateText.setEnabled(true);
            }
        }

    }

    private TextWatcher pinCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pinCodeWatcher = pinCode.getText().toString().trim();

            if (pinCodeWatcher.length() != 6) {
                selectStateText.setText("");
                selectDistrictText.setText("");
                pinCode.setBackground(getResources().getDrawable(R.drawable.edit_text_border_red));
            } else {
                String enteredPinCode = pinCode.getText().toString();
                getStateAndDistrict(enteredPinCode);
                pinCode.setBackground(getResources().getDrawable(R.drawable.edit_text_border));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void getStateAndDistrict(String enteredPin) {

        Log.i("Entered PIN", enteredPin);

        String url = "http://13.234.163.179:3000/user/locationData/" + enteredPin;
        Log.i("url for truckByTruckId", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    String stateByPinCode = obj.getString("stateCode");
                    String distByPinCode = obj.getString("district");

                    selectStateText.setText(stateByPinCode);
                    selectDistrictText.setText(distByPinCode);

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
        this.resultCode = resultCode;
        this.requestCode = requestCode;
        this.data = data;

        getCurrentLocation.setAddressAndPin(DriverDetailsActivity.this, data, address, pinCode);
        DLimagePicker();
        DLimagePickerWithoutAlert();
        selfieImagePicker();
        selfieImagePickerWithoutAlert();

    }

    private String DLimagePickerWithoutAlert() {

        //Detects request code for PAN
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            textDL.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadDL.setVisibility(View.INVISIBLE);
            editDL.setVisibility(View.VISIBLE);
            previewDLImageView.setVisibility(View.VISIBLE);
            previewSelfieImageView.setVisibility(View.VISIBLE);

            isDLUploaded = true;
            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            driverLicenseImage.setImageURI(selectedImage);
            previewDrivingLicense.setImageURI(selectedImage);
            pathForDL = picturePath;
            return picturePath;

        } else if (requestCode == CAMERA_PIC_REQUEST1) {

            textDL.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadDL.setVisibility(View.INVISIBLE);
            editDL.setVisibility(View.VISIBLE);
            previewDLImageView.setVisibility(View.VISIBLE);
            previewSelfieImageView.setVisibility(View.VISIBLE);

            isDLUploaded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            driverLicenseImage.setImageBitmap(BitmapFactory.decodeFile(path));
            previewDrivingLicense.setImageBitmap(BitmapFactory.decodeFile(path));
            pathForDL = path;
            return path;
        }
        return "";
    }

    private String selfieImagePickerWithoutAlert() {

        if (requestCode == CAMERA_PIC_REQUEST) {

            isSelfieUploded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            textDS.setText("Selfie Uploaded");
            textDS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadSelfie.setVisibility(View.INVISIBLE);
            editDS.setVisibility(View.VISIBLE);

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            driverSelfieImg.setImageBitmap(BitmapFactory.decodeFile(path));
            previewSelfie.setImageBitmap(BitmapFactory.decodeFile(path));
            pathForSelfie = path;
            return path;

        } else if (requestCode == GET_FROM_GALLERY1 && resultCode == Activity.RESULT_OK) {

            isSelfieUploded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            textDS.setText("Selfie Uploaded");
            textDS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadSelfie.setVisibility(View.INVISIBLE);
            editDS.setVisibility(View.VISIBLE);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            driverSelfieImg.setImageURI(selectedImage);
            previewSelfie.setImageURI(selectedImage);
            pathForSelfie = picturePath;
            return picturePath;


        }
        return "";
    }

    private String DLimagePicker() {

        //Detects request code for PAN
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(DriverDetailsActivity.this);
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

            alertTitle.setText("Driver Details");
            alertMessage.setText("Driving License uploaded successfully");
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
            textDL.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadDL.setVisibility(View.INVISIBLE);
            editDL.setVisibility(View.VISIBLE);
            previewDLImageView.setVisibility(View.VISIBLE);
            previewSelfieImageView.setVisibility(View.VISIBLE);

            isDLUploaded = true;
            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            driverLicenseImage.setImageURI(selectedImage);
            previewDrivingLicense.setImageURI(selectedImage);
            pathForDL = picturePath;
            return picturePath;

        } else if (requestCode == CAMERA_PIC_REQUEST1) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(DriverDetailsActivity.this);
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

            alertTitle.setText("Driver Details");
            alertMessage.setText("Driving License uploaded successfully");
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

            textDL.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadDL.setVisibility(View.INVISIBLE);
            editDL.setVisibility(View.VISIBLE);
            previewDLImageView.setVisibility(View.VISIBLE);
            previewSelfieImageView.setVisibility(View.VISIBLE);

            isDLUploaded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            driverLicenseImage.setImageBitmap(BitmapFactory.decodeFile(path));
            previewDrivingLicense.setImageBitmap(BitmapFactory.decodeFile(path));

            pathForDL = path;
            return path;
        }
        return "";
    }


    private String selfieImagePicker() {

        if (requestCode == CAMERA_PIC_REQUEST) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(DriverDetailsActivity.this);
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

            alertTitle.setText("Driver Details");
            alertMessage.setText("Driver Selfie uploaded successfully");
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

            isSelfieUploded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            textDS.setText("Selfie Uploaded");
            textDS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadSelfie.setVisibility(View.INVISIBLE);
            editDS.setVisibility(View.VISIBLE);

            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(this, image));
            driverSelfieImg.setImageBitmap(BitmapFactory.decodeFile(path));
            previewSelfie.setImageBitmap(BitmapFactory.decodeFile(path));
            pathForSelfie = path;
            return path;

        } else if (requestCode == GET_FROM_GALLERY1 && resultCode == Activity.RESULT_OK) {
//----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(DriverDetailsActivity.this);
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

            alertTitle.setText("Driver Details");
            alertMessage.setText("Driver Selfie uploaded successfully");
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

            isSelfieUploded = true;

            String mobileNoWatcher = driverMobile.getText().toString();
            String nameWatcher = driverName.getText().toString();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            }

            textDS.setText("Selfie Uploaded");
            textDS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.success, 0);
            uploadSelfie.setVisibility(View.INVISIBLE);
            editDS.setVisibility(View.VISIBLE);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            driverSelfieImg.setImageURI(selectedImage);
            previewSelfie.setImageURI(selectedImage);
            pathForSelfie = picturePath;
            return picturePath;

        }
        return "";
    }

    public void onClickDriverDetailsOk(View view) {
        String driverMobileText = driverMobile.getText().toString();
        String driverNameText = driverName.getText().toString();

        if (!driverNameText.isEmpty() && !driverMobileText.isEmpty() && isDLUploaded && isSelfieUploded) {
            if (driverMobileText.length() != 10) {
                //----------------------- Alert Dialog -------------------------------------------------
                Dialog alert = new Dialog(DriverDetailsActivity.this);
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

                alertTitle.setText("Invalid Mobile Number");
                alertMessage.setText("Please enter a 10 digit valid mobile number.");
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
            } else {
                //update Driver as a user (IsDriverAdded)
                UpdateUserDetails.updateUserIsDriverAdded(userId, "1");


                if (isEdit) {

                    if (idDLEdited) {
                        uploadDriverLicense(driverId, pathForDL);
                    }

                    if (isSelfieEdited) {
                        uploadDriverSelfie(driverId, pathForSelfie);
                    }

                    if (driverName.getText().toString() != null) {
                        UpdateDriverDetails.updateDriverName(driverId, driverName.getText().toString());
                        //update Driver as a user (Name)
                        UpdateUserDetails.updateUserName(driverUserIdGet, driverName.getText().toString());
                    }
                    if (driverEmailId.getText().toString() != null) {
                        UpdateDriverDetails.updateDriverEmailId(driverId, driverEmailId.getText().toString());
                        //update Driver as a user (Email)
                        UpdateUserDetails.updateUserEmailId(driverUserIdGet, driverEmailId.getText().toString());
                    }

                    if (driverMobile.getText().toString() != null && !driverNumberAPI.equals("91" + driverMobile.getText().toString())) {
                        UpdateDriverDetails.updateDriverNumber(driverId, "91" + driverMobile.getText().toString());
                        //update Driver as a user (Phone)
                        UpdateUserDetails.updateUserPhoneNumber(driverUserIdGet, "91" + driverMobile.getText().toString());
                    }

                    if (address.getText().toString() != null) {
                        //update Driver as a user (Address)
                        UpdateUserDetails.updateUserAddress(driverUserIdGet, address.getText().toString());
                    }

                    if (pinCode.getText().toString() != null) {
                        //update Driver as a user (PinCode)
                        UpdateUserDetails.updateUserPinCode(driverUserIdGet, pinCode.getText().toString());
                    }

                    if (selectStateText.getText().toString() != null) {
                        //update Driver as a user (PinCode)
                        UpdateUserDetails.updateUserState(driverUserIdGet, selectStateText.getText().toString());
                    }

                    if (selectDistrictText.getText().toString() != null) {
                        //update Driver as a user (City)
                        UpdateUserDetails.updateUserCity(driverUserIdGet, selectDistrictText.getText().toString());
                    }
                    DriverDetailsActivity.this.finish();
                } else {

                    if (alreadyDriver) {
                        saveDriver(createDriver());
                    } else {
                        saveDriver(createDriver());
                        saveDriverUser(createDriverUser());
                    }

                    //----------------------- Alert Dialog -------------------------------------------------
                    Dialog alert = new Dialog(DriverDetailsActivity.this);
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

                    alertTitle.setText("Driver Details added successfully");
                    alertMessage.setText("Would you like to add Driver's Bank Details?");

                    alertPositiveButton.setText("Add Driver Bank Details");
                    alertPositiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                            if (alreadyDriver) {
                                JumpTo.goToBankDetailsActivity(DriverDetailsActivity.this, userId, false, true, null);
                            } else {
                                JumpTo.goToBankDetailsActivity(DriverDetailsActivity.this, driverUserId, false, true, null);
                            }
                        }
                    });

                    alertNegativeButton.setText("May be Later");
                    alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
                    alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
                    alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                            if (fromBidNow) {
                                DriverDetailsActivity.this.finish();
                            } else {
                                DriverDetailsActivity.this.finish();
                            }
                        }
                    });
                    //------------------------------------------------------------------------------------------
                }
            }
        }
    }

    //--------------------------------------create Driver Details in API -------------------------------------
    public AddDriverRequest createDriver() {
        AddDriverRequest addDriverRequest = new AddDriverRequest();
        addDriverRequest.setUser_id(userId);
        addDriverRequest.setDriver_name(driverName.getText().toString());
        addDriverRequest.setDriver_number("91" + driverMobile.getText().toString());
        addDriverRequest.setDriver_emailId(driverEmailId.getText().toString());
        addDriverRequest.setTruck_id(truckIdPass);
        return addDriverRequest;
    }

    public void saveDriver(AddDriverRequest addDriverRequest) {
        Call<AddDriverResponse> addDriverResponseCall = ApiClient.addDriverService().saveDriver(addDriverRequest);
        addDriverResponseCall.enqueue(new Callback<AddDriverResponse>() {
            @Override
            public void onResponse(Call<AddDriverResponse> call, Response<AddDriverResponse> response) {
                AddDriverResponse driverResponse = response.body();
                driverIdPass = driverResponse.getData().getDriver_id();
                UpdateTruckDetails.updateTruckDriverId(truckIdPass, driverIdPass);

                uploadDriverLicense(driverIdPass, pathForDL);
                uploadDriverSelfie(driverIdPass, pathForSelfie);

            }

            @Override
            public void onFailure(Call<AddDriverResponse> call, Throwable t) {

            }
        });
    }

    private TextWatcher driverNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mobileNoWatcher = driverMobile.getText().toString().trim();
            String nameWatcher = driverName.getText().toString().trim();

            if (!nameWatcher.isEmpty() && !mobileNoWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                okDriverDetails.setEnabled(true);
                okDriverDetails.setBackgroundResource(R.drawable.button_active);
            } else {
//                okDriverDetails.setEnabled(false);
                okDriverDetails.setBackgroundResource(R.drawable.button_de_active);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            for (int i = s.length() - 1; i >= 0; i--) {
                if (s.charAt(i) == '\n') {
                    s.delete(i, i + 1);
                    return;
                }
            }
        }
    };


    private TextWatcher driverMobileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mobileNoWatcher = driverMobile.getText().toString().trim();
            String nameWatcher = driverName.getText().toString().trim();

            if (mobileNoWatcher.length() == 10) {
                checkPhoneInAPI("91" + driverMobile.getText().toString());
                if (!nameWatcher.isEmpty() && isDLUploaded && isSelfieUploded) {
                    okDriverDetails.setEnabled(true);
                    okDriverDetails.setBackgroundResource(R.drawable.button_active);
                } else {
//                    okDriverDetails.setEnabled(false);
                    okDriverDetails.setBackgroundResource(R.drawable.button_de_active);
                }


                driverMobile.setBackground(getResources().getDrawable(R.drawable.mobile_number_right));
                series.setBackground(getResources().getDrawable(R.drawable.mobile_number_left));
            } else {
//                okDriverDetails.setEnabled(false);
                okDriverDetails.setBackgroundResource(R.drawable.button_de_active);
                driverMobile.setBackground(getResources().getDrawable(R.drawable.mobile_number_right_red));
                series.setBackground(getResources().getDrawable(R.drawable.mobile_number_left_red));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private String blockCharacterSet = "~#^|$%&*!+@₹_-()':;?/={}";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {


            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private void getDriverDetails() {

        String url = getString(R.string.baseURL) + "/driver/driverId/" + driverId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        driverNameAPI = obj.getString("driver_name");
                        driverNumberAPI = obj.getString("driver_number");
                        driverEmailAPI = obj.getString("driver_emailId");

                        driverName.setText(driverNameAPI);

                        String drivingLicenseURL = obj.getString("upload_dl");
                        new DownloadImageTask(previewDrivingLicense).execute(drivingLicenseURL);
                        new DownloadImageTask(driverLicenseImage).execute(drivingLicenseURL);

                        String selfieURL = obj.getString("driver_selfie");
                        new DownloadImageTask(previewSelfie).execute(selfieURL);
                        new DownloadImageTask(driverSelfieImg).execute(selfieURL);

                        if (driverNumberAPI != null) {
                            String s1 = driverNumberAPI.substring(2, 12);
                            driverMobile.setText(s1);
                            checkPhoneForDriverDetails(driverNumberAPI);
                        }

                        if (driverEmailAPI == null) {
                            driverEmailId.setText("");
                        } else {
                            driverEmailId.setText(driverEmailAPI);
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

    private void uploadDriverLicense(String driverId1, String picPath) {

        File file = new File(picPath);

        MultipartBody.Part body = prepareFilePart("dl", Uri.fromFile(file));

        Call<UploadDriverLicenseResponse> call = ApiClient.getUploadDriverLicenseService().uploadDriverLicense(driverId1, body);
        call.enqueue(new Callback<UploadDriverLicenseResponse>() {
            @Override
            public void onResponse(Call<UploadDriverLicenseResponse> call, Response<UploadDriverLicenseResponse> response) {

            }

            @Override
            public void onFailure(Call<UploadDriverLicenseResponse> call, Throwable t) {

            }
        });
    }

    private void uploadDriverSelfie(String driverId1, String picPath) {

        File file = new File(picPath);

        MultipartBody.Part body = prepareFilePart("selfie", Uri.fromFile(file));

        Call<UploadDriverSelfieResponse> call = ApiClient.getUploadDriverSelfieService().uploadDriverSelfie(driverId1, body);
        call.enqueue(new Callback<UploadDriverSelfieResponse>() {
            @Override
            public void onResponse(Call<UploadDriverSelfieResponse> call, Response<UploadDriverSelfieResponse> response) {

            }

            @Override
            public void onFailure(Call<UploadDriverSelfieResponse> call, Throwable t) {

            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    public void onClickPreviewDrivingLicense(View view) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(previewDialogDL.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        previewDialogDL.show();
        previewDialogDL.getWindow().setAttributes(lp);
    }

    public void onClickPreviewSelfie(View view) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(previewDialogSelfie.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        previewDialogSelfie.show();
        previewDialogSelfie.getWindow().setAttributes(lp);
    }

    //--------------------------------------create User in API -------------------------------------
    public UserRequest createDriverUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(driverName.getText().toString());
        userRequest.setPhone_number("91" + driverMobile.getText().toString());
        userRequest.setAddress(address.getText().toString());
        userRequest.setUser_type("Driver");
        userRequest.setEmail_id(driverEmailId.getText().toString());
        userRequest.setIsRegistration_done(1);
        userRequest.setPin_code(pinCode.getText().toString());
        userRequest.setPreferred_location(selectDistrictText.getText().toString());
        userRequest.setState_code(selectStateText.getText().toString());
        return userRequest;
    }

    public void saveDriverUser(UserRequest userRequest) {
        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                Log.i("Message UserCreated:", userResponse.getData().getPhone_number());
                UserResponse userResponse = response.body();
                Log.i("Msg Success", String.valueOf(userResponse.getData().getUser_id()));
                driverUserId = String.valueOf(userResponse.getData().getUser_id());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void checkPhoneInAPI(String getMobile) {
        String receivedMobile = getMobile;
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
                        userIdAPI = data.getString("user_id");
                        mobileNoAPI = data.getString("phone_number");
                        pinCodeAPI = data.getString("pin_code");
                        nameAPI = data.getString("name");
                        roleAPI = data.getString("user_type");
                        cityAPI = data.getString("preferred_location");
                        addressAPI = data.getString("address");
                        stateAPI = data.getString("isRegistration_done");

                        arrayUserId.add(userIdAPI);
                        arrayMobileNo.add(mobileNoAPI);
                        arrayAddress.add(addressAPI);
                        arrayState.add(stateAPI);
                        arrayName.add(nameAPI);
                        arrayRole.add(roleAPI);
                        arrayCity.add(cityAPI);
                        arrayPinCode.add(pinCodeAPI);
                    }

                    for (int j = 0; j < arrayMobileNo.size(); j++) {
                        if (arrayMobileNo.get(j).equals(receivedMobile)) {
//                            driverUserIdGet = arrayUserId.get(j);
//                            String nameGet = arrayName.get(j);
//                            String phoneGet = arrayMobileNo.get(j);
//                            String addressGet = arrayAddress.get(j);
//                            String pinCodeGet = arrayPinCode.get(j);
//                            String cityGet = arrayCity.get(j);
//                            String roleGet = arrayRole.get(j);
//                            String isRegistrationDoneGet = arrayRegDone.get(j);

                            alreadyDriver = true;
                            Log.i("Already", "Driver");

                            break;
                        } else {
                            alreadyDriver = false;
                        }
                    }

                    Log.i("Driver Status", String.valueOf(alreadyDriver));
//
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

    private void checkPhoneForDriverDetails(String getMobile) {
        String receivedMobile = getMobile;
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
                        userIdAPI = data.getString("user_id");
                        mobileNoAPI = data.getString("phone_number");
                        pinCodeAPI = data.getString("pin_code");
                        nameAPI = data.getString("name");
                        roleAPI = data.getString("user_type");
                        cityAPI = data.getString("preferred_location");
                        addressAPI = data.getString("address");
                        stateAPI = data.getString("state_code");

                        arrayUserId.add(userIdAPI);
                        arrayMobileNo.add(mobileNoAPI);
                        arrayAddress.add(addressAPI);
                        arrayState.add(stateAPI);
                        arrayName.add(nameAPI);
                        arrayRole.add(roleAPI);
                        arrayCity.add(cityAPI);
                        arrayPinCode.add(pinCodeAPI);
                    }

                    for (int j = 0; j < arrayMobileNo.size(); j++) {
                        if (arrayMobileNo.get(j).equals(receivedMobile)) {
                            driverUserIdGet = arrayUserId.get(j);
                            Log.i("DriverUserId", driverUserIdGet);
                            String nameGet = arrayName.get(j);
                            String phoneGet = arrayMobileNo.get(j);
                            String addressGet = arrayAddress.get(j);
                            String pinCodeGet = arrayPinCode.get(j);
                            String cityGet = arrayCity.get(j);
                            String roleGet = arrayRole.get(j);
                            String stateGet = arrayState.get(j);

                            address.setText(addressGet);
                            Log.i("Address Driver", addressGet);
                            pinCode.setText(pinCodeGet);
                            selectStateText.setText(stateGet);
                            selectDistrictText.setText(cityGet);

                        }
                    }

                    Log.i("Driver Status", String.valueOf(alreadyDriver));
//
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

    private void requestPermissionsForCamera() {
        if (ContextCompat.checkSelfPermission(DriverDetailsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverDetailsActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void requestPermissionsForGalleryWRITE() {
        if (ContextCompat.checkSelfPermission(DriverDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverDetailsActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
    }

    private void requestPermissionsForGalleryREAD() {
        if (ContextCompat.checkSelfPermission(DriverDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverDetailsActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }
    }

    public void getUserDetails() {

        String url = getString(R.string.baseURL) + "/user/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray truckLists = response.getJSONArray("data");
                    for (int i = 0; i < truckLists.length(); i++) {
                        JSONObject obj = truckLists.getJSONObject(i);
                        String userNameAPI = obj.getString("name");
                        String userMobileAPI = obj.getString("phone_number");
                        String emailIdAPI = obj.getString("email_id");
                        String userAddressAPI = obj.getString("address");
                        String userStateAPI = obj.getString("state_code");
                        String userCityAPI = obj.getString("preferred_location");
                        String userPinCodeAPI = obj.getString("pin_code");
                        String userRole = obj.getString("user_type");
                        isDriverDetailsDoneAPI = obj.getString("isDriver_added");

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

    public void onClickGetCurrentLocation(View view) {
        getCurrentLocation.getCurrentLocationMaps(DriverDetailsActivity.this, address, pinCode);
    }

    private void DialogChooseForDriverSelfie() {
        try {
            requestPermissionsForCamera();
            requestPermissionsForGalleryWRITE();
            requestPermissionsForGalleryREAD();

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        } catch (Exception e) {
            //----------------------- Alert Dialog -------------------------------------------------
            Dialog alert = new Dialog(DriverDetailsActivity.this);
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

            alertTitle.setText("Please Upload From Gallery");
            alertMessage.setText("Choose from Gallery");
            alertPositiveButton.setVisibility(View.GONE);

            alertNegativeButton.setText("OK");
            alertNegativeButton.setBackground(getResources().getDrawable(R.drawable.button_active));
            alertNegativeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));

            alertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY1);
                    alert.dismiss();
                }
            });
            //------------------------------------------------------------------------------------------
        }
    }

    private void DialogChoose() {
        requestPermissionsForGalleryWRITE();
        requestPermissionsForGalleryREAD();
        requestPermissionsForCamera();
        Dialog chooseDialog;

        chooseDialog = new Dialog(DriverDetailsActivity.this);
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
                chooseDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                chooseDialog.dismiss();
            }
        });
    }
}
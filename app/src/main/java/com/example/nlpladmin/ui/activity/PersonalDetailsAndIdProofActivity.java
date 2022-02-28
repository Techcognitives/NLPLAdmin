package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.nlpladmin.R;

public class PersonalDetailsAndIdProofActivity extends AppCompatActivity {

    View action_bar;
    TextView actionBarTitle;
    ImageView actionBarBackButton;

    View personalAndAddressView;
    Button personalAddressButton;
    View personalView;
    RadioButton ownerButton, driverButton, brokerButton, customerButton;
    TextView selectStateText, selectDistrictText, series;
    String isPersonalDetailsDone, stateByPinCode, distByPinCode, selectedDistrict, selectedState, role, img_type;

    EditText name, pinCode, address, mobileEdit, emailIdEdit;
    Button okButton;
    View panAndAadharView;
    Button panAndAadharButton;
    View panView;

    Button uploadPAN, uploadF, uploadProfile;
    Dialog previewDialogPan, previewDialogAadhar, previewDialogProfile;
    Boolean isPanAdded = false, isAadharAdded = false, noChange = true, isProfileAdded = false;

    TextView panCardText, editPAN, editFront, frontText, backText, profileText, editProfile, setCurrentLocation;
    ImageView imgPAN, imgF, previewPan, previewAadhar , imgProfile, previewProfile;

    ConstraintLayout aadharConstrain, panConstrain;
    TextView uploadAadharTitle, uploadPanTitle;
    String nameAPI, mobileAPI, addressAPI, pinCodeAPI, roleAPI, cityAPI, stateAPI, emailAPI;
    private int GET_FROM_GALLERY = 0;
    private int GET_FROM_GALLERY1 = 1;
    int CAMERA_PIC_REQUEST1 = 5;
    int CAMERA_PIC_REQUEST2 = 15;
    private int GET_FROM_GALLERY2 = 125;
    int CAMERA_PIC_REQUEST3 = 54;

    private RequestQueue mQueue;
    String userId, mobileString, panImageURL, aadharImageURL, profileImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details_and_id_proof);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
    }
}
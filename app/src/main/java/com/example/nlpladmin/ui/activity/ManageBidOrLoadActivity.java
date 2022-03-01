package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityManageBidOrLoadBinding;

public class ManageBidOrLoadActivity extends AppCompatActivity {

    ActivityManageBidOrLoadBinding activityManageBidOrLoadBinding;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageBidOrLoadBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_bid_or_load);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
        }
    }
}
package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityLogInBinding;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLogInBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);
    }
}
package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivitySplashScreenBinding;
import com.example.nlpladmin.utils.JumpTo;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 2000; //Delay for Animation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //------------------------------------- Handler for Animation --------------------------------------
        new Handler().postDelayed(() -> JumpTo.logInActivity(SplashScreenActivity.this), SPLASH_SCREEN);
    }
}
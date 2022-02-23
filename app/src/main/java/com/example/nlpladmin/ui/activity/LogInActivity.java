package com.example.nlpladmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.nlpladmin.R;
import com.example.nlpladmin.databinding.ActivityLogInBinding;
import com.example.nlpladmin.utils.JumpTo;
import com.example.nlpladmin.utils.ShowAlert;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);
        binding.setHandlers(LogInActivity.this);

        binding.logInMobileNo.addTextChangedListener(textWatcher);
        binding.logInPassword.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i2==1 && binding.logInPassword.getText().toString().trim().length()>0){
                binding.logInGetOtpButton.setBackgroundTintList(getResources().getColorStateList(R.color.active_button));
            }else{
                binding.logInGetOtpButton.setBackgroundTintList(getResources().getColorStateList(R.color.de_active_button));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void onClickLogIn(View view){
        if (binding.logInMobileNo.getText().toString().equals("8796543114") && binding.logInPassword.getText().toString().equals("admin")){
            JumpTo.dashboardActivity(LogInActivity.this);
        }else{
            ShowAlert.showAlert(LogInActivity.this, "Log In Failed", "Please try again", true, false, "ok", null);
        }
    }
}
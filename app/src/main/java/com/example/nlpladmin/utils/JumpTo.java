package com.example.nlpladmin.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.nlpladmin.ui.activity.LogInActivity;

public class JumpTo {

    public static void logInActivity(Activity activity){
        Intent intent = new Intent(activity, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }
}

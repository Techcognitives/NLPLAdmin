package com.example.nlpladmin.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.nlpladmin.model.UserResponses;
import com.example.nlpladmin.ui.activity.BankDetailsActivity;
import com.example.nlpladmin.ui.activity.DashboardActivity;
import com.example.nlpladmin.ui.activity.LogInActivity;
import com.example.nlpladmin.ui.activity.PersonalDetailsActivity;
import com.example.nlpladmin.ui.activity.PersonalDetailsAndIdProofActivity;
import com.example.nlpladmin.ui.activity.ViewBankDetailsActivity;
import com.example.nlpladmin.ui.activity.ViewDriverDetailsActivity;
import com.example.nlpladmin.ui.activity.ViewPersonalDetailsActivity;
import com.example.nlpladmin.ui.activity.ViewTruckDetailsActivity;
import com.example.nlpladmin.ui.activity.ViewUserDetailsActivity;

public class JumpTo {

    public static void logInActivity(Activity activity) {
        Intent intent = new Intent(activity, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    public static void dashboardActivity(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    public static void viewUserDetailsActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, ViewUserDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
//        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    public static void viewPersonalDetailsActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, ViewPersonalDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    public static void personalDetailsAndIdProofActivity(Activity activity, String userId,  Boolean isFinish){
        Intent intent = new Intent(activity, PersonalDetailsAndIdProofActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void personalDetailsActivity(Activity activity, String userId, boolean isFinish){
        Intent intent = new Intent(activity, PersonalDetailsActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0,0);
    }


    public static void viewBankDetailsActivity(Activity activity, String userId, Boolean isFinish){
        Intent intent = new Intent(activity, ViewBankDetailsActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void viewTruckDetailsActivity(Activity activity, String userId, Boolean isFinish){
        Intent intent = new Intent(activity, ViewTruckDetailsActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void viewDriverDetailsActivity(Activity activity, String userId, Boolean isFinish){
        Intent intent = new Intent(activity, ViewDriverDetailsActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void goToBankDetailsActivity(Activity activity, String userId, Boolean isEdit, Boolean isFinish, String bankId){
        Intent intent = new Intent(activity, BankDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("bankDetailsID", bankId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }
}

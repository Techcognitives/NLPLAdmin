package com.example.nlpladmin.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.nlpladmin.ui.activity.BankDetailsActivity;
import com.example.nlpladmin.ui.activity.DriverDetailsActivity;
import com.example.nlpladmin.ui.activity.ManageLoadActivity;
import com.example.nlpladmin.ui.activity.DashboardActivity;
import com.example.nlpladmin.ui.activity.LogInActivity;
import com.example.nlpladmin.ui.activity.PersonalDetailsActivity;
import com.example.nlpladmin.ui.activity.PersonalDetailsAndIdProofActivity;
import com.example.nlpladmin.ui.activity.PostALoadActivity;
import com.example.nlpladmin.ui.activity.ManageBidsActivity;
import com.example.nlpladmin.ui.activity.VehicleDetailsActivity;
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

    public static void personalDetailsActivity(Activity activity, String userId, Boolean isFinish){
        Intent intent = new Intent(activity, PersonalDetailsActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0,0);
    }


    public static void viewBankDetailsActivity(Activity activity, String userId, Boolean isFinish, Boolean showAllBanks){
        Intent intent = new Intent(activity, ViewBankDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("allBanks", showAllBanks);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void viewTruckDetailsActivity(Activity activity, String userId, Boolean isFinish, Boolean showAllTrucks){
        Intent intent = new Intent(activity, ViewTruckDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("allTrucks", showAllTrucks);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void viewDriverDetailsActivity(Activity activity, String userId, Boolean isFinish, Boolean showAllDrivers){
        Intent intent = new Intent(activity, ViewDriverDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("allDrivers", showAllDrivers);
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

    public static void goToCustomerDashboard(Activity activity, String mobileNumber, Boolean isBidsReceived, Boolean isFinish){
        Intent intent = new Intent(activity, ManageLoadActivity.class);
        intent.putExtra("mobile", mobileNumber);
        intent.putExtra("bidsReceived", isBidsReceived);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void goToPostALoad(Activity activity, String userId, String mobileNumber, Boolean reActivate, Boolean isEdit, String loadId, Boolean isFinish){
        Intent intent = new Intent(activity, PostALoadActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("mobile", mobileNumber);
        intent.putExtra("reActivate", reActivate);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("loadId", loadId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void goToServiceProviderDashboard(Activity activity, String mobileNumber, Boolean isFromLoadNotification, Boolean isFinish){
        Intent intent = new Intent(activity, ManageBidsActivity.class);
        intent.putExtra("mobile2", mobileNumber);
        intent.putExtra("loadNotification", isFromLoadNotification);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void goToVehicleDetailsActivity(Activity activity, String userId, String mobileNumber, Boolean isEdit, Boolean isFromBidNow, Boolean isFromAssignTruck, Boolean isFinish, String driverId, String truckId){
        Intent intent = new Intent(activity, VehicleDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("mobile", mobileNumber);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("fromBidNow", isFromBidNow);
        intent.putExtra("assignTruck", isFromAssignTruck);
        intent.putExtra("driverId", driverId);
        intent.putExtra("truckId", truckId);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }

    public static void goToDriverDetailsActivity(Activity activity, String userId, String mobileNumber, Boolean isEdit, Boolean isFromBidNow, Boolean isFinish, String truckId, String driverId){
        Intent intent = new Intent(activity, DriverDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("mobile", mobileNumber);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("fromBidNow", isFromBidNow);
        intent.putExtra("truckIdPass", truckId);
        intent.putExtra("driverId", driverId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        if (isFinish){
            activity.finish();
        }
        activity.overridePendingTransition(0, 0);
    }
}

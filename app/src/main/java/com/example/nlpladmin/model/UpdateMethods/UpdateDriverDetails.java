package com.example.nlpladmin.model.UpdateMethods;

import android.util.Log;

import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverEmailId;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverName;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverNumber;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverTruckId;
import com.example.nlpladmin.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDriverDetails {

    //-------------------------------- Update Driver Name ------------------------------------------
    public static void updateDriverName(String driverId, String driverName) {
        UpdateDriverName updateDriverName = new UpdateDriverName(driverName);
        Call<UpdateDriverName> call = ApiClient.addDriverService().updateDriverName("" + driverId, updateDriverName);
        call.enqueue(new Callback<UpdateDriverName>() {

            @Override
            public void onResponse(Call<UpdateDriverName> call, Response<UpdateDriverName> response) {
                if (response.isSuccessful()) {
                    Log.i("Successful", "User is Driver Added");
                }
            }

            @Override
            public void onFailure(Call<UpdateDriverName> call, Throwable t) {
                Log.i("Not Successful", "User is Driver Added");

            }
        });
    }

    //-------------------------------- Update Driver Mobile Number ---------------------------------
    public static void updateDriverNumber(String driverId, String driverMobile) {
        UpdateDriverNumber updateDriverNumber = new UpdateDriverNumber(driverMobile);
        Call<UpdateDriverNumber> call = ApiClient.addDriverService().updateDriverNumber("" + driverId, updateDriverNumber);
        call.enqueue(new Callback<UpdateDriverNumber>() {
            @Override
            public void onResponse(Call<UpdateDriverNumber> call, Response<UpdateDriverNumber> response) {
                if (response.isSuccessful()) {
                    Log.i("Successful", "User is Driver Number");
                }
            }

            @Override
            public void onFailure(Call<UpdateDriverNumber> call, Throwable t) {
                Log.i("Not Successful", "User is Driver Added");

            }
        });
    }

    //-------------------------------- Update Driver Email Id --------------------------------------
    public static void updateDriverEmailId(String driverId, String driverEmailId) {
        UpdateDriverEmailId updateDriverEmailId = new UpdateDriverEmailId(driverEmailId);
        Call<UpdateDriverEmailId> call = ApiClient.addDriverService().updateDriverEmailId("" + driverId, updateDriverEmailId);
        call.enqueue(new Callback<UpdateDriverEmailId>() {
            @Override
            public void onResponse(Call<UpdateDriverEmailId> call, Response<UpdateDriverEmailId> response) {
                if (response.isSuccessful()) {
                    Log.i("Successful", "User is Driver Email");
                }
            }

            @Override
            public void onFailure(Call<UpdateDriverEmailId> call, Throwable t) {
                Log.i("Not Successful", "User is Driver Added");

            }
        });
    }

    //-------------------------------- Update Driver TruckId ---------------------------------------
    public static void updateDriverTruckId(String driverId, String truckId) {
        UpdateDriverTruckId updateDriverTruckId = new UpdateDriverTruckId(truckId);
        Call<UpdateDriverTruckId> call = ApiClient.addDriverService().updateDriverTruckId("" + driverId, updateDriverTruckId);
        call.enqueue(new Callback<UpdateDriverTruckId>() {
            @Override
            public void onResponse(Call<UpdateDriverTruckId> call, Response<UpdateDriverTruckId> response) {
                if (response.isSuccessful()) {
                    Log.i("Successful", "User is Driver Truck Id");
                }
            }

            @Override
            public void onFailure(Call<UpdateDriverTruckId> call, Throwable t) {
                Log.i("Not Successful", "User is Driver Added");

            }
        });
    }
}

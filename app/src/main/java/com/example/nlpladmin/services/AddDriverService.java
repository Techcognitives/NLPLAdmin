package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverEmailId;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverName;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverNumber;
import com.example.nlpladmin.model.Models.UpdateDriverDetails.UpdateDriverTruckId;
import com.example.nlpladmin.model.Requests.AddDriverRequest;
import com.example.nlpladmin.model.Responses.AddDriverResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddDriverService {
    @POST("driver/addDriver")
    Call<AddDriverResponse> saveDriver(@Body AddDriverRequest addDriverRequest);

    @PUT("/driver/udateDr/{driverId}")
    Call<UpdateDriverName> updateDriverName(@Path("driverId") String driverId, @Body UpdateDriverName updateDriverName);

    @PUT("/driver/udateDr/{driverId}")
    Call<UpdateDriverNumber> updateDriverNumber(@Path("driverId") String driverId, @Body UpdateDriverNumber updateDriverNumber);

    @PUT("/driver/udateDr/{driverId}")
    Call<UpdateDriverEmailId> updateDriverEmailId(@Path("driverId") String driverId, @Body UpdateDriverEmailId updateDriverEmailId);

    @PUT("/driver/udateDr/{driverId}")
    Call<UpdateDriverTruckId> updateDriverTruckId(@Path("driverId") String driverId, @Body UpdateDriverTruckId updateDriverTruckId);
}

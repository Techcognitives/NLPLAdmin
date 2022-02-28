package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckCarryingCapacity;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckDriverId;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckFeet;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckRcBook;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckType;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckVehicleInsurance;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateTruckVehicleNumber;
import com.example.nlpladmin.model.Models.UpdateTruckDetails.UpdateVehicleType;
import com.example.nlpladmin.model.Requests.AddTruckRequest;
import com.example.nlpladmin.model.Responses.AddTruckResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddTruckService {
    @POST("/truck/addtruck")
    Call<AddTruckResponse> saveTruck(@Body AddTruckRequest addTruckRequest);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckVehicleNumber> updateTruckVehicleNumber(@Path("truckId") String truckId, @Body UpdateTruckVehicleNumber updateTruckVehicleNumber);

    @PUT("/truck/{truckId}")
    Call<UpdateVehicleType> updateVehicleType(@Path("truckId") String truckId, @Body UpdateVehicleType updateVehicleType);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckFeet> updateTruckFeet(@Path("truckId") String truckId, @Body UpdateTruckFeet updateTruckFeet);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckCarryingCapacity> updateTruckCarryingCapacity(@Path("truckId") String truckId, @Body UpdateTruckCarryingCapacity updateTruckCarryingCapacity);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckRcBook> updateTruckRcBook(@Path("truckId") String truckId, @Body UpdateTruckRcBook updateTruckRcBook);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckVehicleInsurance> updateTruckVehicleInsurance(@Path("truckId") String truckId, @Body UpdateTruckVehicleInsurance updateTruckVehicleInsurance);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckType> updateTruckType(@Path("truckId") String truckId, @Body UpdateTruckType updateTruckType);

    @PUT("/truck/{truckId}")
    Call<UpdateTruckDriverId> updateTruckDriverId(@Path("truckId") String truckId, @Body UpdateTruckDriverId updateTruckDriverId);
}


















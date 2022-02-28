package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateCount;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateCustomerBudget;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateCustomerNoteForSP;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadBodyType;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadCapacity;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadDropAdd;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadDropCity;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadDropCountry;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadDropPinCode;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadDropState;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadFeet;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadKmApprox;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPickAdd;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPickCity;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPickCountry;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPickPinCode;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPickState;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPostPickUpDate;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadPostPickUpTime;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadStatusSubmitted;
import com.example.nlpladmin.model.Models.UpdateLoadPost.UpdateLoadVehicleModel;
import com.example.nlpladmin.model.Requests.PostLoadRequest;
import com.example.nlpladmin.model.Responses.PostLoadResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostLoadService {
    @POST("/loadpost/postAload")
    Call<PostLoadResponse> saveLoad(@Body PostLoadRequest postLoadRequest);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadStatusSubmitted> updateBidStatusSubmitted(@Path("loadId") String loadId, @Body UpdateLoadStatusSubmitted updateLoadStatusSubmitted);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPostPickUpDate> updateLoadPostPickUpDate(@Path("loadId") String loadId, @Body UpdateLoadPostPickUpDate updateLoadPost);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPostPickUpTime> updateLoadPostPickUpTime(@Path("loadId") String loadId, @Body UpdateLoadPostPickUpTime updateLoadPostPickUpTime);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateCustomerBudget> updateCustomerBudget(@Path("loadId") String loadId, @Body UpdateCustomerBudget updateCustomerBudget);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadVehicleModel> updateLoadVehicleModel(@Path("loadId") String loadId, @Body UpdateLoadVehicleModel updateLoadVehicleModel);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadFeet> updateLoadFeet(@Path("loadId") String loadId, @Body UpdateLoadFeet updateLoadFeet);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadCapacity> updateLoadCapacity(@Path("loadId") String loadId, @Body UpdateLoadCapacity updateLoadCapacity);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadBodyType> updateLoadBodyType(@Path("loadId") String loadId, @Body UpdateLoadBodyType updateLoadBodyType);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPickAdd> updateLoadPickAdd(@Path("loadId") String loadId, @Body UpdateLoadPickAdd updateLoadPickAdd);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPickPinCode> updateLoadPickPinCode(@Path("loadId") String loadId, @Body UpdateLoadPickPinCode updateLoadPickPinCode);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPickState> updateLoadPickState(@Path("loadId") String loadId, @Body UpdateLoadPickState updateLoadPickState);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPickCity> updateLoadPickCity(@Path("loadId") String loadId, @Body UpdateLoadPickCity updateLoadPickCity);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadDropAdd> updateLoadDropAdd(@Path("loadId") String loadId, @Body UpdateLoadDropAdd updateLoadDropAdd);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadDropPinCode> updateLoadDropPinCode(@Path("loadId") String loadId, @Body UpdateLoadDropPinCode updateLoadDropPinCode);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadDropState> updateLoadDropState(@Path("loadId") String loadId, @Body UpdateLoadDropState updateLoadDropState);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadDropCity> updateLoadDropCity(@Path("loadId") String loadId, @Body UpdateLoadDropCity updateLoadDropCity);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadKmApprox> updateLoadKmApprox(@Path("loadId") String loadId, @Body UpdateLoadKmApprox updateLoadKmApprox);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadDropCountry> updateLoadDropCountry(@Path("loadId") String loadId, @Body UpdateLoadDropCountry updateLoadDropCountry);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateLoadPickCountry> updateLoadPickCountry(@Path("loadId") String loadId, @Body UpdateLoadPickCountry updateLoadPickCountry);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateCustomerNoteForSP> updateCustomerNoteForSP(@Path("loadId") String bidId, @Body UpdateCustomerNoteForSP updateCustomerNoteForSP);

    @PUT("/loadpost/updatePostByPID/{loadId}")
    Call<UpdateCount> updateCount(@Path("loadId") String bidId, @Body UpdateCount updateCount);

}

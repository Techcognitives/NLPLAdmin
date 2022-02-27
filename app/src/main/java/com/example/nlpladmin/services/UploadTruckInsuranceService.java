package com.example.nlpladmin.services;


import com.example.nlpladmin.model.Responses.UploadTruckInsuranceResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UploadTruckInsuranceService {
    @Multipart
    @PUT("truck/uploadRCAndInsurence/{truckId}")
    Call<UploadTruckInsuranceResponse> uploadTruckInsurance(@Path("truckId") String truckId, @Part MultipartBody.Part insurance);
}

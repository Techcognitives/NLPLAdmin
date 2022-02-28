package com.example.nlpladmin.services;


import com.example.nlpladmin.model.Responses.UploadDriverSelfieResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UploadDriverSelfieService {
    @Multipart
    @PUT("/driver/uploadDrDlAndSelfie/{driverId}")
    Call<UploadDriverSelfieResponse> uploadDriverSelfie(@Path("driverId") String driverId, @Part MultipartBody.Part file);
}

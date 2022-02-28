package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Requests.ImageRequest;
import com.example.nlpladmin.model.Responses.ImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageService {
    @POST("imgbucket/creatImg")
    Call<ImageResponse> saveImage(@Body ImageRequest imageRequest);
}

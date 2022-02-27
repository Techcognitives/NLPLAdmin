package com.example.nlpladmin.utils;

import com.example.nlpladmin.services.AddDriverService;
import com.example.nlpladmin.services.AddTruckService;
import com.example.nlpladmin.services.BankService;
import com.example.nlpladmin.services.BidLoadService;
import com.example.nlpladmin.services.CompanyService;
import com.example.nlpladmin.services.ImageService;
import com.example.nlpladmin.services.ImageUploadService;
import com.example.nlpladmin.services.PostLoadService;
import com.example.nlpladmin.services.UploadChequeService;
import com.example.nlpladmin.services.UploadDriverLicenseService;
import com.example.nlpladmin.services.UploadDriverSelfieService;
import com.example.nlpladmin.services.UploadTruckInsuranceService;
import com.example.nlpladmin.services.UploadTruckRCBookService;
import com.example.nlpladmin.services.UserService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static String BASE_URL = "http://13.234.163.179:3000";

    private static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static UserService getUserService() {
        UserService userService = getRetrofit().create(UserService.class);
        return userService;
    }
    public static BankService getBankService() {
        BankService bankService = getRetrofit().create(BankService.class);
        return bankService;
    }
    public static AddTruckService addTruckService() {
        AddTruckService addTruckService = getRetrofit().create(AddTruckService.class);
        return addTruckService;
    }
    public static AddDriverService addDriverService() {
        AddDriverService addDriverService = getRetrofit().create(AddDriverService.class);
        return addDriverService;
    }
    public static CompanyService getCompanyService() {
        CompanyService companyService = getRetrofit().create(CompanyService.class);
        return companyService;
    }

    public static ImageService getImageService() {
        ImageService imageService = getRetrofit().create(ImageService.class);
        return imageService;
    }
    public static ImageUploadService getImageUploadService() {
        ImageUploadService imageUploadService = getRetrofit().create(ImageUploadService.class);
        return imageUploadService;
    }

    public static UploadChequeService getUploadChequeService() {
        UploadChequeService uploadChequeService = getRetrofit().create(UploadChequeService.class);
        return uploadChequeService;
    }

    public static UploadDriverLicenseService getUploadDriverLicenseService() {
        UploadDriverLicenseService uploadDriverLicenseService = getRetrofit().create(UploadDriverLicenseService.class);
        return uploadDriverLicenseService;
    }

    public static UploadDriverSelfieService getUploadDriverSelfieService() {
        UploadDriverSelfieService uploadDriverSelfieService = getRetrofit().create(UploadDriverSelfieService.class);
        return uploadDriverSelfieService;
    }

    public static UploadTruckInsuranceService getTuckInsuranceService() {
        UploadTruckInsuranceService uploadTruckInsuranceService = getRetrofit().create(UploadTruckInsuranceService.class);
        return uploadTruckInsuranceService;
    }

    public static UploadTruckRCBookService getUploadTruckRCBookService() {
        UploadTruckRCBookService uploadTruckRCBookService = getRetrofit().create(UploadTruckRCBookService.class);
        return uploadTruckRCBookService;
    }

    public static PostLoadService getPostLoadService() {
        PostLoadService postLoadService = getRetrofit().create(PostLoadService.class);
        return postLoadService;
    }
    public static BidLoadService getBidLoadService() {
        BidLoadService bidLoadService = getRetrofit().create(BidLoadService.class);
        return bidLoadService;
    }
}

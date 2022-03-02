package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserAddress;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserEmailId;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsBankDetailsGiven;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsCompanyAdded;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsDriverAdded;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsPersonalDetailsAdded;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsProfileActive;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsProfileAdded;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsRegistrationDone;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserIsTruckAdded;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserName;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserPhoneNumber;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserPinCode;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserPreferredLanguage;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserPreferredLocation;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserRating;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserStateCode;
import com.example.nlpladmin.model.Models.UpdateUserDetails.UpdateUserType;
import com.example.nlpladmin.model.Requests.UserRequest;
import com.example.nlpladmin.model.Responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("/user/create")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

    @PUT("/user/{userId}")
    Call<UpdateUserName> updateUserName(@Path("userId") String userId, @Body UpdateUserName updateUserName);

    @PUT("/user/{userId}")
    Call<UpdateUserPhoneNumber> updateUserPhoneNumber(@Path("userId") String userId, @Body UpdateUserPhoneNumber updateUserPhoneNumber);

    @PUT("/user/{userId}")
    Call<UpdateUserType> updateUserType(@Path("userId") String userId, @Body UpdateUserType updateUserType);

    @PUT("/user/{userId}")
    Call<UpdateUserPreferredLocation> updateUserPreferredLocation(@Path("userId") String userId, @Body UpdateUserPreferredLocation updateUserPreferredLocation);

    @PUT("/user/{userId}")
    Call<UpdateUserIsRegistrationDone> updateUserIsRegistrationDone(@Path("userId") String userId, @Body UpdateUserIsRegistrationDone updateUserIsRegistrationDone);

    @PUT("/user/{userId}")
    Call<UpdateUserPreferredLanguage> updateUserPreferredLanguage(@Path("userId") String userId, @Body UpdateUserPreferredLanguage updateUserPreferredLanguage);

    @PUT("/user/{userId}")
    Call<UpdateUserAddress> updateUserAddress(@Path("userId") String userId, @Body UpdateUserAddress updateUserAddress);

    @PUT("/user/{userId}")
    Call<UpdateUserStateCode> updateUserStateCode(@Path("userId") String userId, @Body UpdateUserStateCode updateUserStateCode);

    @PUT("/user/{userId}")
    Call<UpdateUserPinCode> updateUserPinCode(@Path("userId") String userId, @Body UpdateUserPinCode updateUserPinCode);

    @PUT("/user/{userId}")
    Call<UpdateUserIsTruckAdded> updateUserIsTruckAdded(@Path("userId") String userId, @Body UpdateUserIsTruckAdded updateUserIsTruckAdded);

    @PUT("/user/{userId}")
    Call<UpdateUserIsDriverAdded> updateUserIsDriverAdded(@Path("userId") String userId, @Body UpdateUserIsDriverAdded updateUserIsDriverAdded);

    @PUT("/user/{userId}")
    Call<UpdateUserIsBankDetailsGiven> updateUserIsBankDetailsGiven(@Path("userId") String userId, @Body UpdateUserIsBankDetailsGiven updateUserIsBankDetailsGiven);

    @PUT("/user/{userId}")
    Call<UpdateUserIsCompanyAdded> updateUserIsCompanyAdded(@Path("userId") String userId, @Body UpdateUserIsCompanyAdded updateUserIsCompanyAdded);

    @PUT("/user/{userId}")
    Call<UpdateUserIsPersonalDetailsAdded> updateUserIsPersonalDetailsAdded(@Path("userId") String userId, @Body UpdateUserIsPersonalDetailsAdded updateUserIsPersonalDetailsAdded);

    @PUT("/user/{userId}")
    Call<UpdateUserIsProfileAdded> updateUserIsProfileAdded(@Path("userId") String userId, @Body UpdateUserIsProfileAdded updateUserIsProfileAdded);

    @PUT("/user/{userId}")
    Call<UpdateUserIsProfileActive> updateUserIsProfileActive(@Path("userId") String userId, @Body UpdateUserIsProfileActive updateUserIsProfileActive);

    @PUT("/user/{userId}")
    Call<UpdateUserEmailId> updateUserEmailId(@Path("userId") String userId, @Body UpdateUserEmailId updateUserEmailId);

    @PUT("/user/{userId}")
    Call<UpdateUserRating> updateUserRating(@Path("userId") String userId, @Body UpdateUserRating updateUserRating);
}

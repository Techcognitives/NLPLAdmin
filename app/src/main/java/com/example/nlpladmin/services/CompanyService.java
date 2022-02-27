package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyAddress;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyCity;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyGSTNumber;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyName;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyPAN;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyState;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyType;
import com.example.nlpladmin.model.Models.UpdateCompanyDetails.UpdateCompanyZip;
import com.example.nlpladmin.model.Requests.CompanyRequest;
import com.example.nlpladmin.model.Responses.CompanyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompanyService {

    @POST("/company/create")
    Call<CompanyResponse> saveCompany(@Body CompanyRequest companyRequest);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyName> updateCompanyName(@Path ("companyId") String companyId, @Body UpdateCompanyName updateCompanyName);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyGSTNumber> updateCompanyGSTNumber(@Path ("companyId") String companyId, @Body UpdateCompanyGSTNumber updateCompanyGSTNumber);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyPAN> updateCompanyPAN(@Path ("companyId") String companyId, @Body UpdateCompanyPAN updateCompanyPAN);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyState> updateCompanyState(@Path ("companyId") String companyId, @Body UpdateCompanyState updateCompanyState);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyCity> updateCompanyCity(@Path ("companyId") String companyId, @Body UpdateCompanyCity updateCompanyCity);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyZip> updateCompanyZip(@Path ("companyId") String companyId, @Body UpdateCompanyZip updateCompanyZip);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyAddress> updateCompanyAddress(@Path ("companyId") String companyId, @Body UpdateCompanyAddress updateCompanyAddress);

    @PUT("/company/{companyId}")
    Call<UpdateCompanyType> updateCompanyType(@Path ("companyId") String companyId, @Body UpdateCompanyType updateCompanyType);
}

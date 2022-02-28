package com.example.nlpladmin.services;


import com.example.nlpladmin.model.Models.UpdateBankDetails.UpdateBankAccountHolderName;
import com.example.nlpladmin.model.Models.UpdateBankDetails.UpdateBankAccountNumber;
import com.example.nlpladmin.model.Models.UpdateBankDetails.UpdateBankIFSICode;
import com.example.nlpladmin.model.Models.UpdateBankDetails.UpdateBankName;
import com.example.nlpladmin.model.Models.UpdateBankDetails.UpdateBankReEnterAccountNumber;
import com.example.nlpladmin.model.Requests.BankRequest;
import com.example.nlpladmin.model.Responses.BankResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BankService {
    @POST("/bank/createBkAcc")
    Call<BankResponse> saveBank(@Body BankRequest bankRequest);

    @PUT("/bank/updateBkByBkId/{bankId}")
    Call<UpdateBankAccountHolderName> updateBankAccountHolderName(@Path("bankId") String bankId, @Body UpdateBankAccountHolderName updateBankAccountHolderName);

    @PUT("/bank/updateBkByBkId/{bankId}")
    Call<UpdateBankAccountNumber> updateBankAccountNumber(@Path("bankId") String bankId, @Body UpdateBankAccountNumber updateBankAccountNumber);

    @PUT("/bank/updateBkByBkId/{bankId}")
    Call<UpdateBankReEnterAccountNumber> updateBankReEnterAccountNumber(@Path("bankId") String bankId, @Body UpdateBankReEnterAccountNumber updateBankReEnterAccountNumber);

    @PUT("/bank/updateBkByBkId/{bankId}")
    Call<UpdateBankIFSICode> updateBankIFSICode(@Path("bankId") String bankId, @Body UpdateBankIFSICode updateBankIFSICode);

    @PUT("/bank/updateBkByBkId/{bankId}")
    Call<UpdateBankName> updateBankName(@Path("bankId") String bankId, @Body UpdateBankName updateBankName);
}

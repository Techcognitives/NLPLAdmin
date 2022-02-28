package com.example.nlpladmin.services;

import com.example.nlpladmin.model.Models.UpdateBids.UpdateAssignedDriverId;
import com.example.nlpladmin.model.Models.UpdateBids.UpdateAssignedTruckIdToBid;
import com.example.nlpladmin.model.Models.UpdateBids.UpdateBidStatus;
import com.example.nlpladmin.model.Models.UpdateBids.UpdateBudgetCustomerForSP;
import com.example.nlpladmin.model.Models.UpdateBids.UpdateSPQuoteFinal;
import com.example.nlpladmin.model.Models.UpdateBids.UpdateSpNoteForCustomer;
import com.example.nlpladmin.model.Requests.BidLoadRequest;
import com.example.nlpladmin.model.Responses.BidLadResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BidLoadService {
    @POST("/spbid/bidapost")
    Call<BidLadResponse> saveBid(@Body BidLoadRequest bidLoadRequest);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateBidStatus> updateBidStatusAccepted(@Path("bidId") String bidId, @Body UpdateBidStatus updateBidStatus);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateSPQuoteFinal> updateSPQuoteFinal(@Path("bidId") String bidId, @Body UpdateSPQuoteFinal updateSPQuoteFinal);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateBudgetCustomerForSP> updateBudgetCustomerForSP(@Path("bidId") String bidId, @Body UpdateBudgetCustomerForSP updateBudgetCustomerForSP);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateAssignedTruckIdToBid> updateAssignedTruckId(@Path("bidId") String bidId, @Body UpdateAssignedTruckIdToBid updateAssignedTruckIdToBid);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateAssignedDriverId> updateAssignedDriverId(@Path("bidId") String bidId, @Body UpdateAssignedDriverId updateAssignedDriverId);

    @PUT("/spbid/updateBidByBID/{bidId}")
    Call<UpdateSpNoteForCustomer> updateSPNoteForCustomer(@Path("bidId") String bidId, @Body UpdateSpNoteForCustomer updateSpNoteForCustomer);

}

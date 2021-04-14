package com.jumbotail.cashflow.data.network.api;

import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse.PostResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransactionApi {

  @GET("transaction")
  Call<TransactionResponse> getTransactions();

  @POST("transaction")
  Call<PostResponse> saveTransaction(@Body Transaction transaction);

  @POST("transaction/{id}")
  Call<TransactionResponse> getTransactionByEntityId(@Path("id") String id);
}

package com.jumbotail.cashflow.data.network.api;

import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.model.responses.EntityResponse.PostResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EntityApi {
  @GET("entity")
  Call<EntityResponse> getEntityList();

  @POST("entity")
  Call<PostResponse> saveEntity(@Body Entity entity);
}

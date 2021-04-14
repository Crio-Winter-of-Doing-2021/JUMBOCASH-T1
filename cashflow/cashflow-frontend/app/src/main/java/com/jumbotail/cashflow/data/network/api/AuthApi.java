package com.jumbotail.cashflow.data.network.api;

import com.jumbotail.cashflow.data.model.requests.AuthRequest;
import com.jumbotail.cashflow.data.model.requests.SignupRequest;
import com.jumbotail.cashflow.data.model.responses.LoginResponse;
import com.jumbotail.cashflow.data.model.responses.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

  @POST("authenticate")
  Call<LoginResponse> authenticateUser(@Body AuthRequest authRequest);

  @POST("register")
  Call<SignupResponse> registerUser(@Body SignupRequest signupRequest);

}

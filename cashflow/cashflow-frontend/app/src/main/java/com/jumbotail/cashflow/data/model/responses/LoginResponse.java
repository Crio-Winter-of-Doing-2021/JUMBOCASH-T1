package com.jumbotail.cashflow.data.model.responses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
  @SerializedName("jwt")
  @Nullable private final String token;
  @SerializedName("message")
  @Nullable private final String message;

  public LoginResponse(@Nullable String token, @NonNull String message){
    this.token = token;
    this.message = message;
  }

  @Nullable
  public String getToken() {
    return token;
  }


  @Nullable
  public String getMessage() {
    return message;
  }

}


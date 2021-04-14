package com.jumbotail.cashflow.data.model.responses;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {

  @SerializedName("userName")
  private final String username;
  private final String email;
  private final String roles;
  private final String message;

  public SignupResponse(String username, String email, String roles, String message) {
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.message = message;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getRoles() {
    return roles;
  }

  public String getMessage() {
    return message;
  }
}

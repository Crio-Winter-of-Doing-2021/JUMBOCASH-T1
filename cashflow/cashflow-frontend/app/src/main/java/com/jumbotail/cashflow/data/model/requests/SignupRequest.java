package com.jumbotail.cashflow.data.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupRequest {

  @Expose
  @SerializedName("userName")
  private String username;

  @Expose
  @SerializedName("password")
  private String password;

  @Expose
  @SerializedName("email")
  private String email;

  @Expose
  @SerializedName("roles")
  private String roles;

  public SignupRequest(String username, String password, String email, String roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "SignupRequest{" +
        "username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", roles='" + roles + '\'' +
        '}';
  }
}

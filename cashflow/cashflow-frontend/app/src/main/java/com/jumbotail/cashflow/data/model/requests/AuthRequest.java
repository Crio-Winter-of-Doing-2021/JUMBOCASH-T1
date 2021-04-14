package com.jumbotail.cashflow.data.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthRequest {

  @Expose
  @SerializedName("username")
  private String username;

  @Expose
  @SerializedName("password")
  private String password;

  public AuthRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

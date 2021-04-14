package com.jumbotail.cashflow.data.local;

public interface PreferencesHelper {
  String getAccessToken();

  void setAccessToken(String accessToken);

  String getCurrentUserEmail();

  void setCurrentUserEmail(String email);

  String getCurrentUserName();

  void setCurrentUserName(String userName);


}

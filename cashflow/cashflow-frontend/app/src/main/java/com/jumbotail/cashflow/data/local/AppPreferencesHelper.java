package com.jumbotail.cashflow.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper {


  private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

  private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";

  private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";

  

  private static AppPreferencesHelper instance;

  private final SharedPreferences sharedPreferences;

  public AppPreferencesHelper(Context context, String prefFile){
    sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
  }

  public static AppPreferencesHelper getInstance(Context context, String prefFile){
    if(instance == null){
      instance = new AppPreferencesHelper(context, prefFile);
    }
    return instance;
  }

  @Override
  public String getAccessToken() {
    return sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null);
  }

  @Override
  public void setAccessToken(String accessToken) {
    sharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
  }

  @Override
  public String getCurrentUserEmail() {
    return sharedPreferences.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
  }

  @Override
  public void setCurrentUserEmail(String email) {
    sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
  }

  @Override
  public String getCurrentUserName() {
    return sharedPreferences.getString(PREF_KEY_CURRENT_USER_NAME, null);
  }

  @Override
  public void setCurrentUserName(String userName) {
    sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
  }

  @Override
  public void setCashIn(int cashIn) {

  }

  @Override
  public int getCashIn() {
    return 0;
  }

  @Override
  public void setCashOut(int cashOut) {

  }

  @Override
  public int getCashOut() {
    return 0;
  }
}

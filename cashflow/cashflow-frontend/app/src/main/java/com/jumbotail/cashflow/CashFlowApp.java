package com.jumbotail.cashflow;

import android.app.Application;

public class CashFlowApp extends Application {


  private static CashFlowApp sInstance;

  private static synchronized void setInstance(CashFlowApp app) {
    sInstance = app;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    setInstance(this);
  }

  public static CashFlowApp getAppContext() {
    return sInstance;
  }
}

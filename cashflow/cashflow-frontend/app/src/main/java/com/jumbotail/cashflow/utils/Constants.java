package com.jumbotail.cashflow.utils;

public class Constants {

  public static final String APP_PACKAGE = "com.jumbotail.cashflow";

  public static final String BASE_URL = "http://localhost:8081/";
  public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
  public static final int READ_TIMEOUT = 2; // 2 seconds
  public static final int WRITE_TIMEOUT = 2; // 2 seconds

  // fragments
  public static final int FRAGMENT_DASHBOARD = 0;
  public static final int FRAGMENT_ENTITY = 1;
  public static final int FRAGMENT_PROFILE = 2;

  // transaction type
  public static final String TRANSACTION_CREDIT = "CREDIT";
  public static final String TRANSACTION_DEBIT = "DEBIT";

  // entity type
  public static final int ENTITY_CUSTOMER = 0;
  public static final int ENTITY_VENDOR = 1;
}

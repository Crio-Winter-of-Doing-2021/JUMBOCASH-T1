package com.jumbotail.cashflow.utils;

import android.util.Pair;
import com.jumbotail.cashflow.data.model.Transaction;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HelperFunctions {

  public static List<Transaction>  filterTransactionOnMonth(int m, List<Transaction> transactionList){
    double cashIn = 0, cashOut = 0;
    List<Transaction> filteredList = new ArrayList<>();
    for(Transaction transaction : transactionList){
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(transaction.getTimestamp());
      int month = calendar.get(Calendar.MONTH);
      if(month == m ){
        filteredList.add(transaction);
      }
    }


    return filteredList;
  }

  public static List<Transaction> filterTransactionOnDate(int d, int m, List<Transaction> list){
    List<Transaction> filteredList = new ArrayList<>();
    for(Transaction transaction : list){
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(transaction.getTimestamp());
      int date = calendar.get(Calendar.DATE);
      int month = calendar.get(Calendar.MONTH);
      if(date == d && month == m ){
        filteredList.add(transaction);
      }
    }

    return filteredList;
  }

  public static long endOfDay(int y, int m, int d){
    Calendar calendar = Calendar.getInstance();
    calendar.set(y, m, d);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTimeInMillis();
  }

  public static long startOfDay(int y, int m, int d){
    Calendar calendar = Calendar.getInstance();
    calendar.set(y, m, d);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }

  public static long startDayOfMonth(Date date){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();

  }

  public static long endDayOfMonth(Date date){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, 31);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();

  }

  public static String toDate(long timestamp){
    Date date = new Date(timestamp);
    Format format = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    return format.format(date);
  }

  public static String extractMonth(long timestamp){
    Date date = new Date(timestamp);
    Format format = new SimpleDateFormat("MMMM", Locale.getDefault());
    return format.format(date);
  }
}

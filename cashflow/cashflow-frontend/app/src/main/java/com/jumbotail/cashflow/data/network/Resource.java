package com.jumbotail.cashflow.data.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
  @Nullable public final Status status;
  @Nullable public final T data;
  @Nullable public final String message;

  public Resource(@Nullable Status status, @Nullable T data, @Nullable String message){
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public static <T> Resource<T> success(@Nullable T data, @Nullable String message){
    return new Resource<>(Status.SUCCESS, data, message);
  }

  public static <T> Resource<T> error(@Nullable T data, @NonNull String message){
    return new Resource<>(Status.ERROR, data, message);
  }

  public static <T> Resource<T> loading(@Nullable T data,@Nullable String message){
    return new Resource<>(Status.LOADING, data, message);
  }


  public enum Status{SUCCESS, ERROR, LOADING}
}

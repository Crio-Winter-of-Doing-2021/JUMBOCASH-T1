package com.jumbotail.cashflow.data.network;

import static com.jumbotail.cashflow.utils.Constants.CONNECTION_TIMEOUT;
import static com.jumbotail.cashflow.utils.Constants.READ_TIMEOUT;
import static com.jumbotail.cashflow.utils.Constants.WRITE_TIMEOUT;

import androidx.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {

  public static final String BASE_URL = "https://cashflow-jumbotail.herokuapp.com/";
  public static String authToken;

  private static final Builder client = new Builder()
      .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
      .retryOnConnectionFailure(false);


  private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create());

  //private  static final Retrofit retrofit = retrofitBuilder.build();
  // returns the instance of auth api
  public static Retrofit getApi(@Nullable String authToken){
    if(authToken != null){
      client.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          Request newRequest  = chain.request().newBuilder()
              .addHeader("Authorization", "Bearer " + authToken)
              .build();
          return chain.proceed(newRequest);
        }
      });
    }

    return retrofitBuilder.client(client.build()).build();
  }
}

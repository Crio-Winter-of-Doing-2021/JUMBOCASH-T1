package com.jumbotail.cashflow.data.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.jumbotail.cashflow.data.model.requests.AuthRequest;
import com.jumbotail.cashflow.data.model.requests.SignupRequest;
import com.jumbotail.cashflow.data.model.responses.SignupResponse;
import com.jumbotail.cashflow.data.network.api.AuthApi;
import com.jumbotail.cashflow.data.network.RemoteDataSource;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.model.responses.LoginResponse;
import com.jumbotail.cashflow.data.network.RetrofitCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthRepository {

  private static final String TAG = "AuthRepository";
  private final AuthApi authApi;
  private final MutableLiveData<Resource<LoginResponse>> authLiveData;
  private final MutableLiveData<Resource<SignupResponse>> signupLiveData;

  public AuthRepository(){
    authApi = RemoteDataSource.getApi(null).create(AuthApi.class);
    authLiveData = new MutableLiveData();
    signupLiveData = new MutableLiveData<>();
  }

  // api call to authenticate the user
  public void authenticateUser(AuthRequest authRequest){

    authLiveData.postValue(Resource.loading(null, "Logging in"));

    authApi.authenticateUser(authRequest)
        .enqueue(new RetrofitCallback<LoginResponse>(authLiveData));

    Log.d(TAG, "authenticateUser: "+authLiveData);

  }

  // api call to register the user in the app
  public void registerUser(SignupRequest request){
    signupLiveData.postValue(Resource.loading(null,"Registering you"));
    authApi.registerUser(request)
        .enqueue(new RetrofitCallback<SignupResponse>(signupLiveData));
  }

  public MutableLiveData<Resource<LoginResponse>> authLiveData(){return authLiveData; }
  public MutableLiveData<Resource<SignupResponse>> signupLiveData(){ return signupLiveData; }
}

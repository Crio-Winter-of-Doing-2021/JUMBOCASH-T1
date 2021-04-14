package com.jumbotail.cashflow.ui.auth;

import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.model.requests.AuthRequest;
import com.jumbotail.cashflow.data.model.requests.SignupRequest;
import com.jumbotail.cashflow.data.model.responses.SignupResponse;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.repository.AuthRepository;
import com.jumbotail.cashflow.data.model.responses.LoginResponse;
import okhttp3.ResponseBody;

public class AuthViewModel extends ViewModel {

  private static final String TAG = "AuthViewModel" ;
  private final AuthRepository authRepository;
  public LiveData<Resource<LoginResponse>> authMutableLiveData;
  public MutableLiveData<Resource<SignupResponse>> signupLiveData;

  public AuthViewModel(){
    authRepository = new AuthRepository();
    authMutableLiveData = new MutableLiveData<>();
    signupLiveData = new MutableLiveData<>();
  }

  public boolean validateUser(AuthRequest authRequest){
    if(TextUtils.isEmpty(authRequest.getUsername())){
      return false;
    }
    if(TextUtils.isEmpty(authRequest.getPassword())){
      return false;
    }
    return true;
  }

  public void registerUser(SignupRequest request){
    authRepository.registerUser(request);
  }

  public void authenticateUser(AuthRequest authRequest){
     authRepository.authenticateUser(authRequest);
  }

  public void getAuthLiveData(){
    authMutableLiveData = authRepository.authLiveData();
  }

  public void getSignupLiveData(){
    signupLiveData = authRepository.signupLiveData();
  }
}

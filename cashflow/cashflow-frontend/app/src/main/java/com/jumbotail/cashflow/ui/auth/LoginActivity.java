package com.jumbotail.cashflow.ui.auth;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.ViewUtils.intent;
import static com.jumbotail.cashflow.utils.ViewUtils.progressBar;
import static com.jumbotail.cashflow.utils.ViewUtils.toast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.model.requests.AuthRequest;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.data.model.responses.LoginResponse;
import com.jumbotail.cashflow.databinding.ActivityLoginBinding;
import com.jumbotail.cashflow.ui.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

  private static final String TAG = "LoginActivity";
  private ActivityLoginBinding loginBinding;
  private String mUsername, mPassword;
  private AuthViewModel authViewModel;
  private ProgressDialog dialog;
  private AppPreferencesHelper appPreferencesHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
    View view = loginBinding.getRoot();
    setContentView(view);
    initializations();
    initListeners();
    setSharedPrefData();
    authViewModel.getAuthLiveData();

    authViewModel.authMutableLiveData.observe(this, new Observer<Resource<LoginResponse>>() {
      @Override
      public void onChanged(Resource<LoginResponse> loginResponseResource) {
        if(loginResponseResource.status == Status.LOADING){
          dialog = progressBar(LoginActivity.this,
              loginResponseResource.message,
              "Please wait"
          );
          dialog.show();
          Log.d(TAG, "onChanged: "+loginResponseResource.message);
        }

        else if(loginResponseResource.status == Status.SUCCESS){
          dialog.dismiss();
          toast(getApplicationContext(), "Login Successful");
          // save token in shared pref
          appPreferencesHelper.setAccessToken(loginResponseResource.data.getToken());
          // move to dashboard
          intent(LoginActivity.this, DashboardActivity.class);
          finish();
        }

        else if(loginResponseResource.status == Status.ERROR){
          dialog.dismiss();
          toast(getApplicationContext(), "Login failed, "
              + "Access : "+loginResponseResource.message);
        }
      }
    });



  }

  private void initializations() {
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    appPreferencesHelper = new AppPreferencesHelper(this, APP_PACKAGE);
  }

  private void initListeners() {
    loginBinding.btnLogin.setOnClickListener(this);
  }

  private void setSharedPrefData(){
    // get data from pref
    String _username = appPreferencesHelper.getCurrentUserName();
    loginBinding.edTextUsername.setText(_username);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnLogin:

        mUsername = loginBinding.edTextUsername.getText().toString();
        mPassword = loginBinding.edTextPassword.getText().toString();
        AuthRequest authRequest = new AuthRequest(mUsername, mPassword);

        if (authViewModel.validateUser(authRequest)) {
          authViewModel.authenticateUser(new AuthRequest(mUsername, mPassword));
        } else {
          toast(this,
              "Please provide username or password"
          );
        }
        break;

    }
  }
}
package com.jumbotail.cashflow.ui.auth;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.ViewUtils.intent;
import static com.jumbotail.cashflow.utils.ViewUtils.progressBar;
import static com.jumbotail.cashflow.utils.ViewUtils.toast;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.model.requests.SignupRequest;
import com.jumbotail.cashflow.data.model.responses.SignupResponse;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

  private static final String TAG = "SignupActivity";
  private ActivitySignupBinding viewBinding;
  private String mUsername, mEmail, mPassword;
  private ProgressDialog dialog;
  private AuthViewModel authViewModel;
  private AppPreferencesHelper appPreferencesHelper;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewBinding = ActivitySignupBinding.inflate(getLayoutInflater());
    View view = viewBinding.getRoot();
    setContentView(view);
    initializations();

    // button listener
    viewBinding.btnSignup.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mUsername = viewBinding.edTextUsername.getText().toString();
        mEmail = viewBinding.edTextEmail.getText().toString();
        mPassword = viewBinding.edTextPassword.getText().toString();
        SignupRequest signupRequest = new SignupRequest(mUsername, mPassword, mEmail, "USER");
        Log.d(TAG, "onClick: "+signupRequest);
        authViewModel.registerUser(signupRequest);
      }
    });

    // get live data
    authViewModel.getSignupLiveData();
    // attaching observer to get the data change
    authViewModel.signupLiveData.observe(this, new Observer<Resource<SignupResponse>>() {
      @Override
      public void onChanged(Resource<SignupResponse> signupResponseResource) {
        if(signupResponseResource.status == Status.LOADING){
          Log.d(TAG, "onChanged: "+signupResponseResource.message);
          dialog = progressBar(SignupActivity.this,
              "Registering you",
              "Please wait"
          );
          dialog.show();
        }
        else if(signupResponseResource.status == Status.SUCCESS){
          Log.d(TAG, "onChanged: "+signupResponseResource.data.getMessage());
          dialog.dismiss();
          toast(getApplicationContext(), signupResponseResource.data.getMessage());
          // save signup response to shared pref
          appPreferencesHelper.setCurrentUserEmail(signupResponseResource.data.getEmail());
          appPreferencesHelper.setCurrentUserName(signupResponseResource.data.getUsername());
          // move to login activity
          intent(SignupActivity.this, LoginActivity.class);
          finish();
        }
        else{
          Log.d(TAG, "onChanged: "+signupResponseResource.message);
          dialog.dismiss();
          toast(getApplicationContext(), signupResponseResource.message);
        }

      }
    });


  }

  private void initializations() {
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    appPreferencesHelper = new AppPreferencesHelper(this, APP_PACKAGE);
  }
}
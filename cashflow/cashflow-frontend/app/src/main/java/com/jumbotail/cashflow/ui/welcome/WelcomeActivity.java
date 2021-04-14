package com.jumbotail.cashflow.ui.welcome;

import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.databinding.ActivityWelcomeBinding;
import com.jumbotail.cashflow.ui.auth.LoginActivity;
import com.jumbotail.cashflow.ui.auth.SignupActivity;

public class WelcomeActivity extends AppCompatActivity {
  private ActivityWelcomeBinding binding;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    binding.btnLogin.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        callLoginActivity();
      }
    });

    binding.btnSignup.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        callSignupActivity();
      }
    });
  }

  private void callLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    Pair[] pairs = new Pair[1];
    pairs[0] = new Pair<View, String>(binding.btnLogin, "login_transition");

    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
        this,
        pairs
    );
    startActivity(intent, activityOptions.toBundle());
  }

  private void callSignupActivity(){
    Intent intent = new Intent(this, SignupActivity.class);
    Pair[] pairs = new Pair[1];
    pairs[0] = new Pair<View, String>(binding.btnSignup, "signup_transition");

    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
        this,
        pairs
    );

    startActivity(intent, activityOptions.toBundle());
  }
}
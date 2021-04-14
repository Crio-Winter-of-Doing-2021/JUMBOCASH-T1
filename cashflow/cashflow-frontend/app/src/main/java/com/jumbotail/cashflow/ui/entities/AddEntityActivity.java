package com.jumbotail.cashflow.ui.entities;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.ViewUtils.progressBar;
import static com.jumbotail.cashflow.utils.ViewUtils.toast;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Supplier;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.ViewModelProviderFactory;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.responses.EntityResponse.PostResponse;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityAddEntityBinding;
import com.jumbotail.cashflow.ui.BaseActivity;

public class AddEntityActivity extends BaseActivity<ActivityAddEntityBinding, EntityViewModel>
    implements OnClickListener, RadioGroup.OnCheckedChangeListener {

  private static final String TAG = "AddEntityActivity";

  private ProgressDialog dialog;

  private  Entity entity = new Entity();



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    reqPermissions();


    viewModel.getEntityPostLiveData().observe(this, postResource -> {

      if(postResource.status == Status.LOADING){
        Log.d(TAG, "onCreate: "+postResource.message);

        dialog = progressBar(AddEntityActivity.this,
            "Saving Entity",
            "Please wait"
        );
        dialog.show();
      }

      if(postResource.status == Status.SUCCESS){
        Log.d(TAG, "onCreate: "+postResource.message);
        toast(getApplicationContext(), "Entity saved successfully");
        dialog.dismiss();
      }

      if(postResource.status == Status.ERROR){
        Log.d(TAG, "onCreate: "+postResource.message);
        toast(getApplicationContext(), "Something went wrong. Try again");
        dialog.dismiss();
      }

    });

  }

  @Override
  protected Class<EntityViewModel> getViewModel() {
    return EntityViewModel.class;
  }

  @Override
  public ActivityAddEntityBinding getBinding() {
    return ActivityAddEntityBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {
    viewBinding.btnEntitySave.setOnClickListener(this);
    viewBinding.radioGroup1.setOnCheckedChangeListener(this);
    viewBinding.btnAddFromContact.setOnClickListener(this);
  }


  private void reqPermissions(){
    ActivityCompat.requestPermissions(this, new String[]{permission.READ_CONTACTS},
        PackageManager.PERMISSION_GRANTED);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btnEntitySave: {

        entity.setEntityName(viewBinding.edTextName.getText().toString());
        entity.setEntityPhone(viewBinding.edTextPhone.getText().toString());
        entity.setEntityAddress(viewBinding.edTextAddress.getText().toString());

        if (viewModel.validateEntity(entity)) {
          viewModel.saveEntityData(entity);
        } else {
          toast(getApplicationContext(), "Please fill all details");
        }
        Log.d(TAG, "onClick: " + entity);
        break;
      }

      case R.id.btnAddFromContact: {

        Intent intent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
        startActivityForResult(intent, 1);
      }
    }
  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    int id = group.getCheckedRadioButtonId();

    if(id == R.id.radioBtnCustomer){
      Log.d(TAG, "onCheckedChanged: "+checkedId);
      entity.setEntityType(viewBinding.radioBtnCustomer.getText().toString());
    }
    else if(id == R.id.radioBtnVendor){
      Log.d(TAG, "onCheckedChanged: "+checkedId);
      entity.setEntityType(viewBinding.radioBtnVendor.getText().toString());
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(resultCode != RESULT_CANCELED && data != null) {
      Uri contactData = data.getData();
      Cursor c = getContentResolver().query(
          contactData, null, null, null, null
      );
      if (c.moveToFirst()) {

        String phoneNumber = "";
        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        phoneNumber = c.getString(c.getColumnIndex(Phone.NUMBER));
        Log.d(TAG, "onActivityResult: " + name + " " + phoneNumber);
        viewBinding.edTextName.setText(name);
        viewBinding.edTextPhone.setText(phoneNumber);

      }
    }


  }
}
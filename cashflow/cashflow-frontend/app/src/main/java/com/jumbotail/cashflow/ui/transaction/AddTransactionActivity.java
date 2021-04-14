package com.jumbotail.cashflow.ui.transaction;

import static com.jumbotail.cashflow.utils.ViewUtils.bringDateTimePicker;
import static com.jumbotail.cashflow.utils.ViewUtils.progressBar;
import static com.jumbotail.cashflow.utils.ViewUtils.toast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.os.Bundle;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog.Listener;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityAddTransactionBinding;
import com.jumbotail.cashflow.ui.BaseActivity;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;

public class AddTransactionActivity extends BaseActivity<ActivityAddTransactionBinding,
    TransactionViewModel> implements OnClickListener, RadioGroup.OnCheckedChangeListener {

  private static final String TAG = "AddTransactionActivity";

  private static Transaction transaction;
  private EntityResponse entityResponse;
  private ProgressDialog dialog;
  String[] test;

  @Override
  protected Class<TransactionViewModel> getViewModel() {
    return TransactionViewModel.class;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    transaction = new Transaction();

    viewModel.getEntityListLiveData();
    // observe entity list and after getting it set it to spinner
    viewModel.entityList.observe(this, entityResource ->{
      if(entityResource.status == Status.SUCCESS){
        entityResponse = new EntityResponse(entityResource.data);
        initSpinner(entityResource.data);
      }
    });

    viewModel.getTransactionPostResponse();
    // observe transaction post response
    viewModel.transactionPostResponse.observe(this, postResponseResource -> {
      if(postResponseResource.status == Status.LOADING){
        dialog = progressBar(AddTransactionActivity.this, "Saving transaction","");
        dialog.show();
      }
      if(postResponseResource.status == Status.SUCCESS){
        Log.d(TAG, "onCreate: "+postResponseResource.data);
        dialog.dismiss();
        toast(getApplicationContext(), "Transaction saved successfully");
      }
      if(postResponseResource.status == Status.ERROR){
        Log.d(TAG, "onCreate: "+postResponseResource.message);
        dialog.dismiss();
        toast(getApplicationContext(), "Something went wrong. Try again");
      }
    });

  }

  @Override
  public ActivityAddTransactionBinding getBinding() {
    return ActivityAddTransactionBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {

    viewBinding.btnTransactionSave.setOnClickListener(this);
    viewBinding.radioGroup1.setOnCheckedChangeListener(this);
    viewBinding.radioGroup2.setOnCheckedChangeListener(this);
    viewBinding.edTextDateTime.setOnClickListener(this);
  }


  private void initSpinner(List<Entity> list){
    List<String> temp = new ArrayList<>();
    // extracting entity type, name and phone and saving it to list
    for(Entity entity : list){
      temp.add(
          entity.getId()+" - "+
          entity.getEntityType().toUpperCase()+": "+
          entity.getEntityName()+" "+
          entity.getEntityPhone()
      );
    }
    viewBinding.spinnerEntity.setItems(temp);
    viewBinding.spinnerEntity.
        setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {

          @Override
          public void onItemSelected(int i, @Nullable String s, int i1, String t1) {
            String[] t = t1.split(" ");
            Entity entity = entityResponse.getEntityById(t[0]);
            transaction.setEntityId(entity.getId());
            transaction.setEntityName(entity.getEntityName());
          }
        });
  }


  private void bringTimeStampPicker(){
    bringDateTimePicker(this).setListener(new Listener() {
      @Override
      public void onDateSelected(Date date) {
        long timestamp = date.getTime();
        viewBinding.edTextDateTime.setText(toDate(timestamp));
        transaction.setTimestamp(timestamp);
      }
    }).display();
  }
  private String toDate(long timestamp){
    Date date = new Date(timestamp);
    Format format = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    return format.format(date);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btnTransactionSave:
        if(!viewBinding.edTextAmount.getText().toString().isEmpty()) {
          transaction.setAmount(Double.parseDouble(viewBinding.edTextAmount.getText().toString()));
        }
        transaction.setModeOfPayment(viewBinding.edTextPaymentMode.getText().toString());
        transaction.setRemarks(viewBinding.edTextRemarks.getText().toString());

        if(transaction.checkValidation(viewBinding).first){
          Log.d(TAG, "onClick: "+transaction);
          viewModel.saveTransaction(transaction);
        }
        else{
          toast(getApplicationContext(), transaction.checkValidation(viewBinding).second);
        }

        break;

      case R.id.edTextDateTime:
        bringTimeStampPicker();
    }
  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    int id = group.getCheckedRadioButtonId();

    if(id == R.id.radioBtnCredit){
      transaction.setType("CREDIT");
    }
    if(id == R.id.radioBtnDebit){
      transaction.setType("DEBIT");
    }
    if(id == R.id.radioBtnPending){
      transaction.setStatus("PENDING");
    }
    if(id == R.id.radioBtnDone){
      transaction.setStatus("DONE");
    }


  }
}
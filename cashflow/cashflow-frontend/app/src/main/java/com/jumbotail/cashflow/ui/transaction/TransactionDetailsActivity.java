package com.jumbotail.cashflow.ui.transaction;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.HelperFunctions.toDate;
import static com.jumbotail.cashflow.utils.ViewUtils.getIntentData;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.databinding.ActivityTransactionDetailsBinding;
import com.jumbotail.cashflow.ui.BaseActivity;

public class TransactionDetailsActivity extends BaseActivity<ActivityTransactionDetailsBinding,
    TransactionViewModel> {

  private static final String TAG = "TransactionDetailsActiv";

  @Override
  protected Class<TransactionViewModel> getViewModel() {
    return null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // get intent data from transaction history
    Transaction transaction = getIntentData(this, APP_PACKAGE);
    Log.d(TAG, "onCreate: "+transaction);

    // set views
    viewBinding.toolbarTransactionStatus.setText("Transaction "+transaction.getStatus());
    viewBinding.toolbarTransactionDate.setText("Done on "+toDate(transaction.getTimestamp()));
    viewBinding.txtViewTransactionId.setText(""+transaction.getId());

    if(transaction.getType().equals("CREDIT")){
      viewBinding.txtViewTransactionType.setText("Received from");
    }else{
      viewBinding.txtViewTransactionType.setText("Paid to");
    }
    viewBinding.txtViewEntityName.setText(transaction.getEntityName());
    viewBinding.txtViewTransactionMode.setText("Via "+transaction.getModeOfPayment().toUpperCase());
    viewBinding.txtViewAmount.setText("₹ "+transaction.getAmount());
    if(transaction.getRemarks().isEmpty()){
      viewBinding.txtViewTransactionRemarks.setText("No transaction remarks found");
    }else {
      viewBinding.txtViewTransactionRemarks.setText(transaction.getRemarks());
    }

  }

  @Override
  public ActivityTransactionDetailsBinding getBinding() {
    return ActivityTransactionDetailsBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {

  }
}
package com.jumbotail.cashflow.ui.transaction;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.HelperFunctions.endOfDay;
import static com.jumbotail.cashflow.utils.HelperFunctions.startOfDay;
import static com.jumbotail.cashflow.utils.ViewUtils.getIntentData;
import static com.jumbotail.cashflow.utils.ViewUtils.intent;

import android.app.DatePickerDialog.OnDateSetListener;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.util.Supplier;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityTransactionHistoryBinding;
import com.jumbotail.cashflow.ui.BaseActivity;
import com.jumbotail.cashflow.ui.transaction.TransactionAdapter.RecyclerViewItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionHistoryActivity extends BaseActivity<ActivityTransactionHistoryBinding,
    TransactionViewModel> implements DatePickerDialog.OnDateSetListener, RecyclerViewItemListener {

  private static final String TAG = "TransactionHistoryActiv";

  private TransactionAdapter transactionAdapter;

  private List<Transaction> list ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // get intent data from EntityActivity
    Integer id = getIntentData(this, APP_PACKAGE);
    if(id != null){
      viewModel.getTransactionByEntityId(id);
      subscribeToTransactionData();

    }
    else{
      viewModel.getTransactionListLiveData();
    }

    viewBinding.btnFilterDate.setOnClickListener(v ->{
      bringDatePickerDialog();
    });


    // observer transaction list
    subscribeToTransactionData();

    viewBinding.btnGeneratePdf.setOnClickListener(view ->{
      intent(TransactionHistoryActivity.this, TransactionPdfReportActivity.class,
          APP_PACKAGE, list);
    });

  }

  @Override
  public ActivityTransactionHistoryBinding getBinding() {
    return ActivityTransactionHistoryBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {
    transactionAdapter = new TransactionAdapter(this, new ArrayList<>());
    viewBinding.recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
    viewBinding.recyclerTransactions.setAdapter(transactionAdapter);
  }

  @Override
  protected Class<TransactionViewModel> getViewModel() {
    return TransactionViewModel.class;
  }

  private void bringDatePickerDialog(){
    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
        TransactionHistoryActivity.this,
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH)
    );
    dpd.setAutoHighlight(true);
    dpd.show(getFragmentManager(), "Datepickerdialog");
  }

  private void subscribeToTransactionData(){
    viewModel.transactionList.observe(this, listResource -> {

      if(listResource.status == Status.LOADING){
        viewBinding.recyclerTransactions.showShimmerAdapter();
      }

      if(listResource.status == Status.SUCCESS || listResource.status == Status.ERROR){
        viewBinding.recyclerTransactions.hideShimmerAdapter();
        list = listResource.data;
        transactionAdapter.filterList(listResource.data);
        Log.d(TAG, "onCreate: "+listResource.data);
      }
    });
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
      int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
    viewBinding.btnFilterDate.setText(dayOfMonth+"-"+monthOfYear+"-"+year+""+dayOfMonthEnd);

    long st = startOfDay(year, monthOfYear, dayOfMonth);
    long end;
    if(dayOfMonth != dayOfMonthEnd) {
      end = endOfDay(yearEnd, monthOfYearEnd, dayOfMonthEnd);
    }
    else{
      end = endOfDay(year, monthOfYear, dayOfMonth);
      Log.d(TAG, "onDateSet: date matches");
    }

    Log.d(TAG, "onDateSet: range "+st/1000+" to "+end/1000);
    // check if filterable eid is coming from intent
    if(getIntentData(this, APP_PACKAGE) == null ) {
      viewModel.getTransactionsByRange(st, end);
    }
    else{
      viewModel.getTransactionsByRangeAndEid(st, end, getIntentData(this, APP_PACKAGE));
    }
    subscribeToTransactionData();

  }

  @Override
  public void onResume() {
    super.onResume();
    DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
    if(dpd != null) dpd.setOnDateSetListener(this);
  }

  @Override
  public void onRecyclerItemClick(View v, int position, Transaction transaction) {
    intent(this, TransactionDetailsActivity.class, APP_PACKAGE, transaction);
  }
}
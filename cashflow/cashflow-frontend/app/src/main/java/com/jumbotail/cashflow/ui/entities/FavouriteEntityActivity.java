package com.jumbotail.cashflow.ui.entities;

import static com.jumbotail.cashflow.utils.HelperFunctions.endDayOfMonth;
import static com.jumbotail.cashflow.utils.HelperFunctions.startDayOfMonth;
import static com.jumbotail.cashflow.utils.ViewUtils.bringDateTimePicker;
import static com.jumbotail.cashflow.utils.ViewUtils.bringMonthPicker;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog.Listener;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityFavouriteEntityBinding;
import com.jumbotail.cashflow.ui.BaseActivity;
import com.jumbotail.cashflow.utils.ViewUtils;
import java.util.ArrayList;
import java.util.Date;

public class FavouriteEntityActivity extends BaseActivity<ActivityFavouriteEntityBinding,
    EntityViewModel>  {


  private static final String TAG = "FavouriteEntityActivity";

  private FavEntityAdapter adapter;

  @Override
  protected Class<EntityViewModel> getViewModel() {
    return EntityViewModel.class;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initializations();

    // get current date
    Date date = new Date();
    Log.d(TAG, "onCreate: "+startDayOfMonth(date)+" "+endDayOfMonth(date));


    viewModel.getFavouriteEntities(startDayOfMonth(date), endDayOfMonth(date),1);
    subscribeToFavouriteList();



    viewBinding.btnFilterDate.setOnClickListener(view ->{
      bringMonthPickerDialog();
    });
  }

  private void initializations() {
    adapter = new FavEntityAdapter(new ArrayList<>());
    viewBinding.recyclerViewFavEntity.setLayoutManager(new LinearLayoutManager(this));
    viewBinding.recyclerViewFavEntity.setAdapter(adapter);
  }

  private void bringMonthPickerDialog(){
    bringMonthPicker(this).setListener(new Listener() {
      @Override
      public void onDateSelected(Date date) {
        Log.d(TAG, "onDateSelected: "+startDayOfMonth(date)+" to "+endDayOfMonth(date));
        viewModel.getFavouriteEntities(startDayOfMonth(date), endDayOfMonth(date),1);
        subscribeToFavouriteList();

      }
    }).display();
  }

  private void subscribeToFavouriteList(){
    viewModel.favouriteEntityList.observe(this, listResource -> {
      if(listResource.status == Status.SUCCESS){
        adapter.updateList(listResource.data);
        Log.d(TAG, "onCreate: "+listResource.data);
      }
    });
  }

  @Override
  public ActivityFavouriteEntityBinding getBinding() {
    return ActivityFavouriteEntityBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {

  }
}
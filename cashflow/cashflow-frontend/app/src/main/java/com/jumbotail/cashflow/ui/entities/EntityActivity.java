package com.jumbotail.cashflow.ui.entities;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.Constants.ENTITY_CUSTOMER;
import static com.jumbotail.cashflow.utils.Constants.ENTITY_VENDOR;
import static com.jumbotail.cashflow.utils.ViewUtils.intent;
import static com.jumbotail.cashflow.utils.ViewUtils.toast;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import androidx.core.util.Supplier;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.local.dao.EntityDao_Impl;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityEntityBinding;
import com.jumbotail.cashflow.ui.BaseActivity;
import com.jumbotail.cashflow.ui.entities.EntityAdapter.RecyclerViewItemListener;
import com.jumbotail.cashflow.ui.transaction.TransactionHistoryActivity;
import com.tiper.MaterialSpinner;
import com.tiper.MaterialSpinner.OnItemSelectedListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityActivity extends BaseActivity<ActivityEntityBinding, EntityViewModel>
    implements OnItemSelectedListener, RecyclerViewItemListener {

  private static final String TAG = "EntityActivity";

  private EntityAdapter entityAdapter;

  private EntityResponse entityResponse;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initRecyclerView();
    initSpinner();

    viewModel.entityLiveData.observe(this, responseResource -> {
      if(responseResource.status == Status.LOADING){
        viewBinding.recyclerViewEntity.showShimmerAdapter();
        Log.d(TAG, "onCreate: "+responseResource.message);
      }
      else if(responseResource.status == Status.SUCCESS){
        Log.d(TAG, "onCreate: Success "+responseResource.data);
        viewBinding.recyclerViewEntity.hideShimmerAdapter();
        entityResponse = new EntityResponse(responseResource.data);
        entityAdapter.updateList(responseResource.data);
      }
      else{
        Log.d(TAG, "onCreate: "+responseResource.message);
        viewBinding.recyclerViewEntity.hideShimmerAdapter();
        entityResponse = new EntityResponse(responseResource.data);
        entityAdapter.updateList(responseResource.data);
      }
    });



  }

  private void initRecyclerView() {
    entityAdapter = new EntityAdapter(this, new ArrayList<>());
    viewBinding.recyclerViewEntity.setLayoutManager(
        new LinearLayoutManager(EntityActivity.this)
    );
    viewBinding.recyclerViewEntity.setAdapter(entityAdapter);
  }

  private void initSpinner(){
    String[] ITEMS = {"CUSTOMER", "VENDOR"};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item,
        ITEMS
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    viewBinding.materialSpinner.setAdapter(adapter);
    viewBinding.materialSpinner.getEditText().setText("Filter Entity");
  }

  @Override
  public ActivityEntityBinding getBinding() {
    return ActivityEntityBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {
    viewBinding.materialSpinner.setOnItemSelectedListener(this);
  }

  @Override
  protected Class<EntityViewModel> getViewModel() {
    return EntityViewModel.class;
  }

  @Override
  public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i,
      long l) {

    if(i == ENTITY_CUSTOMER && entityResponse != null){
      entityAdapter.updateList(entityResponse.getEntityByType("CUSTOMER"));
    }
    else if(i == ENTITY_VENDOR && entityResponse != null){
      entityAdapter.updateList(entityResponse.getEntityByType("VENDOR"));
    }

  }

  @Override
  public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

  }

  @Override
  public void onRecyclerItemClick(View v, int position, int entityId) {
    Log.d(TAG, "onRecyclerItemClick: "+entityId);
    intent(EntityActivity.this, TransactionHistoryActivity.class, APP_PACKAGE, entityId);
  }
}
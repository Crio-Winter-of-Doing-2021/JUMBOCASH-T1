package com.jumbotail.cashflow.ui;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;

import android.os.Bundle;
import android.util.Pair;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.jumbotail.cashflow.ViewModelProviderFactory;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.ui.entities.EntityAdapter.ViewHolder;
import com.jumbotail.cashflow.ui.entities.EntityViewModel;
import com.jumbotail.cashflow.ui.transaction.TransactionViewModel;

public abstract class BaseActivity<Binding extends ViewBinding, VM extends ViewModel>
    extends AppCompatActivity {

  protected Binding viewBinding;
  protected VM viewModel;
  private ViewModelProviderFactory<VM> factory;

  protected abstract Class<VM> getViewModel();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.initBinding();
    this.initListeners();
    setContentView(viewBinding.getRoot());
    String authToken = AppPreferencesHelper.getInstance(getApplicationContext(), APP_PACKAGE)
        .getAccessToken();

    //view model
    if(getViewModel() != null) {
      if(getViewModel() == EntityViewModel.class){
        Supplier<EntityViewModel> supplier = () -> new EntityViewModel(getApplication(), authToken);
        factory = new ViewModelProviderFactory<VM>(getViewModel(), (Supplier<VM>) supplier);
      }

      if(getViewModel() == TransactionViewModel.class){
        Supplier<TransactionViewModel> supplier = () ->
            new TransactionViewModel(getApplication(), authToken);

        factory = new ViewModelProviderFactory<VM>(getViewModel(), (Supplier<VM>) supplier);
      }

      viewModel = new ViewModelProvider(this, factory).get(getViewModel());
    }

  }

  public abstract Binding getBinding();

  public abstract void initListeners();

  void initBinding(){
    viewBinding = getBinding();
  }
}

package com.jumbotail.cashflow.data.repository;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jumbotail.cashflow.data.local.CashFlowDatabase;
import com.jumbotail.cashflow.data.local.dao.EntityDao;
import com.jumbotail.cashflow.data.local.dao.TransactionDao;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse.PostResponse;
import com.jumbotail.cashflow.data.network.NetworkBoundResource;
import com.jumbotail.cashflow.data.network.RemoteDataSource;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.network.RetrofitCallback;
import com.jumbotail.cashflow.data.network.api.TransactionApi;
import java.util.List;
import retrofit2.Call;

public class TransactionRepository {

  private final TransactionApi transactionApi;
  private EntityDao entityDao;
  private TransactionDao transactionDao;

  private MutableLiveData<Resource<PostResponse>> transactionPostResponse;

  public TransactionRepository(Application application, String authToken){

    transactionApi = RemoteDataSource.getApi(authToken).create(TransactionApi.class);
    entityDao = CashFlowDatabase.getInstance(application).entityDao();
    transactionDao = CashFlowDatabase.getInstance(application).transactionDao();

  }

  // fetch entity list from local db
  public LiveData<Resource<List<Entity>>> loadEntityFromCache(){
    return new NetworkBoundResource<List<Entity>, EntityResponse>(){

      @Override
      protected void saveCallResult(EntityResponse item) {}

      @Override
      protected boolean shouldFetch() { return false; }

      @NonNull
      @Override
      protected LiveData<List<Entity>> loadFromDb() { return entityDao.loadEntities(); }

      @NonNull
      @Override
      protected Call<EntityResponse> createCall() { return null; }
    }.getAsLiveData();
  }

  //fetch transaction list from local db
  public LiveData<Resource<List<Transaction>>> loadTransactionsFromCache(){
    return new NetworkBoundResource<List<Transaction>, TransactionResponse>(){

      @Override
      protected void saveCallResult(TransactionResponse item) {
        transactionDao.saveTransactions(item.getTransactionList());
      }

      @Override
      protected boolean shouldFetch() {
        return true;
      }

      @NonNull
      @Override
      protected LiveData<List<Transaction>> loadFromDb() {
        return transactionDao.getTransactionList();
      }

      @NonNull
      @Override
      protected Call<TransactionResponse> createCall() {
        return transactionApi.getTransactions();
      }
    }.getAsLiveData();
  }

  // get transactions y range from cache
  public LiveData<Resource<List<Transaction>>> getTransactionsByRange(long st, long end){
    return new NetworkBoundResource<List<Transaction>, TransactionResponse>(){

      @Override
      protected void saveCallResult(TransactionResponse item) {

      }

      @Override
      protected boolean shouldFetch() {
        return false;
      }

      @NonNull
      @Override
      protected LiveData<List<Transaction>> loadFromDb() {
        return transactionDao.getTransactionByRange(st, end);
      }

      @NonNull
      @Override
      protected Call<TransactionResponse> createCall() {
        return null;
      }
    }.getAsLiveData();
  }

  // get transactions from cache by entity id
  public LiveData<Resource<List<Transaction>>> getTransactionsByEntityId(int id){
    return new NetworkBoundResource<List<Transaction>, TransactionResponse>(){

      @Override
      protected void saveCallResult(TransactionResponse item) { }

      @Override
      protected boolean shouldFetch() { return false; }

      @NonNull
      @Override
      protected LiveData<List<Transaction>> loadFromDb() {
        return transactionDao.getTransactionsByEntityId(id);
      }

      @NonNull
      @Override
      protected Call<TransactionResponse> createCall() { return null; }
    }.getAsLiveData();
  }

  // getTransactions by range and id
  public LiveData<Resource<List<Transaction>>> getTransactionsByRangeAndEid(long st, long end, int id){
    return new NetworkBoundResource<List<Transaction>, TransactionResponse>(){

      @Override
      protected void saveCallResult(TransactionResponse item) {}

      @Override
      protected boolean shouldFetch() { return false;}

      @NonNull
      @Override
      protected LiveData<List<Transaction>> loadFromDb() {
        return transactionDao.getTransactionByRangeAndEid(st, end, id);
      }

      @NonNull
      @Override
      protected Call<TransactionResponse> createCall() { return null;}
    }.getAsLiveData();
  }



  // save transaction in server
  public void saveTransaction(Transaction transaction){

    transactionPostResponse.postValue(Resource.loading(null, "Saving transaction"));

    transactionApi.saveTransaction(transaction).enqueue(
        new RetrofitCallback<>(transactionPostResponse)
    );
  }

  public MutableLiveData<Resource<PostResponse>> getMutableTransactionPostResponse(){
    if(transactionPostResponse == null ){
      transactionPostResponse = new MutableLiveData<>();
    }
    return transactionPostResponse;
  }
}

package com.jumbotail.cashflow.ui.transaction;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse;
import com.jumbotail.cashflow.data.model.responses.TransactionResponse.PostResponse;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.repository.TransactionRepository;
import java.util.List;

public class TransactionViewModel extends ViewModel {

  private TransactionRepository transactionRepository;

  public LiveData<Resource<List<Entity>>> entityList;

  public LiveData<Resource<TransactionResponse.PostResponse>> transactionPostResponse;

  public LiveData<Resource<List<Transaction>>> transactionList;

  public TransactionViewModel(Application application, String authToken){
    transactionRepository = new TransactionRepository(application, authToken);
  }

  public void getEntityListLiveData(){
    if(entityList == null){
      entityList = new MutableLiveData<>();
    }
    entityList = transactionRepository.loadEntityFromCache();
  }

  public void getTransactionListLiveData(){
    if(transactionList == null){
      transactionList = new MutableLiveData<>();
    }
    transactionList = transactionRepository.loadTransactionsFromCache();
  }

  public void saveTransaction(Transaction transaction){

    transactionRepository.saveTransaction(transaction);

  }

  public void getTransactionPostResponse(){
    if(transactionPostResponse == null){
      transactionPostResponse = new MutableLiveData<>();
    }
    transactionPostResponse = transactionRepository.getMutableTransactionPostResponse();
  }

  public void getTransactionsByRange(long start, long end){
    if(transactionList == null){
      transactionList = new MutableLiveData<>();
    }
    transactionList = transactionRepository.getTransactionsByRange(start, end);
  }

  public void getTransactionByEntityId(int id){
    if(transactionList == null){
      transactionList = new MutableLiveData<>();
    }
    transactionList = transactionRepository.getTransactionsByEntityId(id);
  }

  public void getTransactionsByRangeAndEid(long st, long end, int id){
    if(transactionList == null){
      transactionList = new MutableLiveData<>();
    }
    transactionList = transactionRepository.getTransactionsByRangeAndEid(st,end,id);
  }




}

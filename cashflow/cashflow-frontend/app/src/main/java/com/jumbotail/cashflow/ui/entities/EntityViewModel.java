package com.jumbotail.cashflow.ui.entities;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.FavouriteEntity;
import com.jumbotail.cashflow.data.model.TopDebitEntities;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.model.responses.EntityResponse.PostResponse;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.repository.EntityRepository;
import java.util.List;

public class EntityViewModel extends ViewModel {

  private static final String TAG = "EntityViewModel";
  private  EntityRepository entityRepository;
  public LiveData<Resource<List<Entity>>> entityLiveData;
  public LiveData<Resource<PostResponse>> postLiveData;

  public LiveData<Resource<List<FavouriteEntity>>> favouriteEntityList;

  public LiveData<Resource<List<TopDebitEntities>>> topDebitEntityList;

  public LiveData<Resource<List<TopDebitEntities>>> topCreditEntityList;

  public EntityViewModel(Application application, String authToken){
    entityRepository = new EntityRepository(application,authToken);
    entityLiveData = entityRepository.loadEntities();
    Log.d(TAG, "EntityViewModel: ");
  }

  public LiveData<Resource<List<Entity>>> getEntityLiveData(){
    return entityLiveData;
  }

  public void saveEntityData(Entity entity){
    entityRepository.saveEntityToServer(entity);
  }

  public LiveData<Resource<PostResponse>> getEntityPostLiveData(){
    if(postLiveData == null){
      postLiveData = new MutableLiveData<>();
    }
    postLiveData = entityRepository.getPostResponseMutableData();
    return postLiveData;
  }

  public LiveData<Resource<List<FavouriteEntity>>> getFavouriteEntities(long st, long end, int c){
    if(favouriteEntityList == null){
      favouriteEntityList = new MutableLiveData<>();
    }
    favouriteEntityList = entityRepository.getFavouriteEntityByRange(st, end, c);
    return favouriteEntityList;
  }

  public void getTopDebitEntitiesByRange(long st, long end){
    if(topDebitEntityList == null){
      topDebitEntityList = new MutableLiveData<>();
    }
    topDebitEntityList = entityRepository.getTopDebitEntities(st, end);

  }

  public void getTopCreditEntitiesByRange(long st, long end){
    if(topCreditEntityList == null){
      topCreditEntityList = new MutableLiveData<>();
    }
    topCreditEntityList = entityRepository.getTopCreditEntities(st, end);

  }

  public boolean validateEntity(Entity entity) {
    if(TextUtils.isEmpty(entity.getEntityName())){
      return false;
    }
    else if(TextUtils.isEmpty(entity.getEntityType())){
      return false;
    }
    else if(TextUtils.isEmpty(entity.getEntityAddress())){
      return false;
    }
    else if(TextUtils.isEmpty(entity.getEntityPhone())){
      return false;
    }
    return true;
  }
}

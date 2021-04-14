package com.jumbotail.cashflow.data.repository;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jumbotail.cashflow.data.local.CashFlowDatabase;
import com.jumbotail.cashflow.data.local.dao.EntityDao;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.FavouriteEntity;
import com.jumbotail.cashflow.data.model.TopDebitEntities;
import com.jumbotail.cashflow.data.model.responses.EntityResponse;
import com.jumbotail.cashflow.data.model.responses.EntityResponse.PostResponse;
import com.jumbotail.cashflow.data.network.NetworkBoundResource;
import com.jumbotail.cashflow.data.network.RemoteDataSource;
import com.jumbotail.cashflow.data.network.Resource;
import com.jumbotail.cashflow.data.network.RetrofitCallback;
import com.jumbotail.cashflow.data.network.api.EntityApi;
import java.util.List;
import retrofit2.Call;

public class EntityRepository {

  private static final String TAG = "EntityRepository";
  private final EntityApi entityApi;
  private EntityDao entityDao;
  private MutableLiveData<Resource<EntityResponse.PostResponse>> postResponse;

  public EntityRepository(Context context, String authToken){
    entityApi = RemoteDataSource.getApi(authToken).create(EntityApi.class);
    entityDao = CashFlowDatabase.getInstance(context).entityDao();
    postResponse = new MutableLiveData<>();
  }


  public LiveData<Resource<List<Entity>>> loadEntities(){
    return new NetworkBoundResource<List<Entity>, EntityResponse>() {

      @Override
      protected void saveCallResult(EntityResponse item) {
        if(item != null){
          entityDao.saveEntities(item.getEntityList());
        }
      }

      @Override
      protected boolean shouldFetch() {
       return true;
      }

      @NonNull
      @Override
      protected LiveData<List<Entity>> loadFromDb() {
        return entityDao.loadEntities();
      }

      @NonNull
      @Override
      protected Call<EntityResponse> createCall() {
        return entityApi.getEntityList();
      }
    }.getAsLiveData();
  }

  public LiveData<Resource<List<FavouriteEntity>>> getFavouriteEntityByRange(long st, long end, int c){
    return new NetworkBoundResource<List<FavouriteEntity>, EntityResponse>(){

      @Override
      protected void saveCallResult(EntityResponse item) {}

      @Override
      protected boolean shouldFetch() { return false; }

      @NonNull
      @Override
      protected LiveData<List<FavouriteEntity>> loadFromDb() {
        Log.d(TAG, "loadFromDb: "+entityDao.getFavouriteEntitiesByMonth(st, end, c).getValue());
        return entityDao.getFavouriteEntitiesByMonth(st, end, c);
      }

      @NonNull
      @Override
      protected Call<EntityResponse> createCall() {
        return null;
      }
    }.getAsLiveData();
  }

  // fetch top debit entities
  public LiveData<Resource<List<TopDebitEntities>>> getTopDebitEntities(long st, long end){
    return new NetworkBoundResource<List<TopDebitEntities>, EntityResponse>(){

      @Override
      protected void saveCallResult(EntityResponse item) {

      }

      @Override
      protected boolean shouldFetch() {
        return false;
      }

      @NonNull
      @Override
      protected LiveData<List<TopDebitEntities>> loadFromDb() {
        return entityDao.getTopDebitEntitiesByRange(st, end);
      }

      @NonNull
      @Override
      protected Call<EntityResponse> createCall() {
        return null;
      }
    }.getAsLiveData();
  }

  // get top credit entities
  public LiveData<Resource<List<TopDebitEntities>>> getTopCreditEntities(long st, long end){
    return new NetworkBoundResource<List<TopDebitEntities>, EntityResponse>(){

      @Override
      protected void saveCallResult(EntityResponse item) {

      }

      @Override
      protected boolean shouldFetch() {
        return false;
      }

      @NonNull
      @Override
      protected LiveData<List<TopDebitEntities>> loadFromDb() {
        return entityDao.getTopCreditEntitiesByRange(st, end);
      }

      @NonNull
      @Override
      protected Call<EntityResponse> createCall() {
        return null;
      }
    }.getAsLiveData();
  }


  public void saveEntityToServer(Entity entity){
    postResponse.postValue(Resource.loading(null, "Saving entity"));
    entityApi.saveEntity(entity).enqueue(new RetrofitCallback<>(postResponse));
  }

  public MutableLiveData<Resource<PostResponse>> getPostResponseMutableData(){
    return postResponse;
  }


}

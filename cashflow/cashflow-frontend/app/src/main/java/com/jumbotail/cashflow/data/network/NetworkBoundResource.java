package com.jumbotail.cashflow.data.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import com.google.gson.stream.MalformedJsonException;
import com.jumbotail.cashflow.CashFlowApp;
import com.jumbotail.cashflow.R;
import java.io.IOException;
import java.net.SocketTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class NetworkBoundResource<CacheObj, NetworkObj> {

  private static final String TAG = "NetworkBoundResource";
  private final MediatorLiveData<Resource<CacheObj>> result = new MediatorLiveData<>();


  protected NetworkBoundResource(){
    init();
  }

  private void init(){

    result.setValue(Resource.loading(null, "Loading"));
    // always load the data from the cache first
    LiveData<CacheObj> dbSource = loadFromDb();

    result.addSource(dbSource, cacheObj -> {
      result.removeSource(dbSource);
      if(shouldFetch()){
        fetchDataFromNetwork(dbSource);
      }
      else{
        result.addSource(dbSource, newData->{
          result.setValue(Resource.success(newData, "Data loaded"));
        });
      }
    });
  }

  private void fetchDataFromNetwork(LiveData<CacheObj> dbSource){
    Log.d(TAG, "fetchDataFromNetwork: ");
    result.addSource(dbSource, cacheObj -> result.setValue(Resource.loading(cacheObj,"")));
    createCall().enqueue(new Callback<NetworkObj>() {
      @Override
      public void onResponse(Call<NetworkObj> call, Response<NetworkObj> response) {
        result.removeSource(dbSource);
        saveResultInBackground(response.body());
      }

      @Override
      public void onFailure(Call<NetworkObj> call, Throwable t) {
        result.removeSource(dbSource);

        result.addSource(dbSource, newData-> result.setValue(Resource.error(newData,
            getCustomErrorMessage(t)))
        );
      }
    });
  }

  private String getCustomErrorMessage(Throwable error){

    if (error instanceof SocketTimeoutException) {
      return "Oooops! We couldn’t capture your request in time. Please try again.";
    } else if (error instanceof MalformedJsonException) {
      return  "Oops we hit an error try again";
    } else if (error instanceof IOException) {
      return  "Network error happened";
    } else if (error instanceof HttpException) {
      return error.getMessage();
    } else {
      return "Something went wrong";
    }

  }

  @SuppressLint("StaticFieldLeak")
  @MainThread
  private void saveResultInBackground(NetworkObj body){
    new AsyncTask<Void, Void, Void>() {

      @Override
      protected Void doInBackground(Void... voids) {
        saveCallResult(body);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        result.addSource(loadFromDb(), newData -> {
          if (null != newData)
            result.setValue(Resource.success(newData, "Success"));
        });
      }
    }.execute();
  }


  @WorkerThread
  protected abstract void saveCallResult(NetworkObj item);

  @MainThread
  protected abstract boolean shouldFetch();

  @NonNull
  @MainThread
  protected abstract LiveData<CacheObj> loadFromDb();

  @NonNull
  @MainThread
  protected abstract Call<NetworkObj> createCall();

  public final LiveData<Resource<CacheObj>> getAsLiveData() {
    return result;
  }
}

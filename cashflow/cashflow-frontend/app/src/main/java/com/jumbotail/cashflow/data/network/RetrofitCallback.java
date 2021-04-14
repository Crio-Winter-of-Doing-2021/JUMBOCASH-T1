package com.jumbotail.cashflow.data.network;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback<T> implements Callback<T> {

  private static final String TAG = "RetrofitCallback";
  private MutableLiveData<Resource<T>> mutableLiveData;
  public RetrofitCallback(MutableLiveData<Resource<T>> mutableLiveData){
    this.mutableLiveData = mutableLiveData;
  }

  @Override
  public void onResponse(Call<T> call, Response<T> response) {
    Log.d(TAG, "onResponse: "+response.body()+" "+response.raw());
    if(response.isSuccessful()){
      mutableLiveData.postValue(Resource.success(response.body(), "SUCCESS"));
    }
    else{
      mutableLiveData.postValue(Resource.error(null, "Error occured, Please try again" ));
    }
  }

  @Override
  public void onFailure(Call<T> call, Throwable t) {
    Log.d(TAG, "onFailure: "+t.getMessage());
    mutableLiveData.postValue(Resource.error(null, t.getMessage()));
  }
}

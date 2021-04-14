package com.jumbotail.cashflow.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.FavouriteEntity;
import com.jumbotail.cashflow.data.model.TopDebitEntities;
import com.jumbotail.cashflow.data.model.Transaction;
import java.util.List;

@Dao
public interface EntityDao {
//select entityId, entityName, count(*) as count , sum(amount) from transactions where type='DEBIT' and timestamp between 1617215400*1000 and 1622485799*1000 group by entityId having count(*)>=1
  @Query("select * from entity order by id DESC")
  LiveData<List<Entity>> loadEntities();

  @Query("select entityId, entityName, timestamp, count(*) as count from transactions where timestamp between :st and :end group by entityId having count(*)>=:c order by count DESC")
  LiveData<List<FavouriteEntity>> getFavouriteEntitiesByMonth(long st, long end, int c);

  @Query("select entityId, entityName, sum(amount) as amount from transactions where timestamp between :st*1000 and :end*1000 and type='DEBIT' group by entityId order by amount desc limit 5")
  LiveData<List<TopDebitEntities>> getTopDebitEntitiesByRange(long st, long end);

  @Query("select entityId, entityName, sum(amount) as amount from transactions where timestamp between :st*1000 and :end*1000 and type='CREDIT' group by entityId order by amount desc limit 5")
  LiveData<List<TopDebitEntities>> getTopCreditEntitiesByRange(long st, long end);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void saveEntities(List<Entity> entityList);


}

package com.jumbotail.cashflow.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jumbotail.cashflow.data.model.Transaction;
import java.util.List;

@Dao
public interface TransactionDao {

  @Query("select * from transactions order by timestamp ASC")
  LiveData<List<Transaction>> getTransactionList();

  @Query("select * from transactions where timestamp between :start and :end order by timestamp asc")
  LiveData<List<Transaction>> getTransactionByRange(long start, long end);

  @Query("select * from transactions where entityId = :id")
  LiveData<List<Transaction>> getTransactionsByEntityId(int id);

  @Query("select * from transactions where timestamp between :start and :end and entityId = :id "
      + "order by timestamp asc")
  LiveData<List<Transaction>> getTransactionByRangeAndEid(long start, long end, int id);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void saveTransactions(List<Transaction> transactions);
}

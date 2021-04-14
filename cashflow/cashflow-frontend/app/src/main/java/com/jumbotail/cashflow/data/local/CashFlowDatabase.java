package com.jumbotail.cashflow.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.jumbotail.cashflow.data.local.dao.EntityDao;
import com.jumbotail.cashflow.data.local.dao.TransactionDao;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.data.model.FavouriteEntity;
import com.jumbotail.cashflow.data.model.TopDebitEntities;
import com.jumbotail.cashflow.data.model.Transaction;

@Database(entities = {Entity.class, Transaction.class, FavouriteEntity.class, TopDebitEntities.class}, version = 5)
public abstract class CashFlowDatabase extends RoomDatabase {
  public static final String DATABASE_NAME = "cashflow_db";

  private static CashFlowDatabase instance;

  public static CashFlowDatabase getInstance(final Context context){
    if(instance == null){
      instance = Room.databaseBuilder(
          context.getApplicationContext(),
          CashFlowDatabase.class,
          DATABASE_NAME
      ).build();
    }
    return instance;
  }
  public abstract EntityDao entityDao();

  public abstract TransactionDao transactionDao();
}

package com.jumbotail.cashflow.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "top_debit")
public class TopDebitEntities {

  @PrimaryKey
  private int entityId;

  private String entityName;

  private int amount;

  public TopDebitEntities(int entityId, String entityName, int amount) {
    this.entityId = entityId;
    this.entityName = entityName;
    this.amount = amount;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  @Override
  public String toString() {
    return "TopDebitEntities{" +
        "id=" + entityId +
        ", entityName='" + entityName + '\'' +
        ", amount=" + amount +
        '}';
  }
}

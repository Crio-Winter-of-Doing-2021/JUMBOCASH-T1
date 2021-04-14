package com.jumbotail.cashflow.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_entity")
public class FavouriteEntity {
  @PrimaryKey
  private int entityId;

  private String entityName;

  private long timestamp;

  private int count;

  public FavouriteEntity(int entityId, String entityName, long timestamp, int count) {
    this.entityId = entityId;
    this.entityName = entityName;
    this.timestamp = timestamp;
    this.count = count;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "FavouriteEntity{" +
        "entityId=" + entityId +
        ", entityName='" + entityName + '\'' +
        ", timestamp=" + timestamp +
        ", count=" + count +
        '}';
  }
}

package com.jumbotail.cashflow.data.model;

import androidx.annotation.Nullable;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;


@androidx.room.Entity
public class Entity {
  @PrimaryKey
  @SerializedName("id")
  private int id;
  @SerializedName("type")
  private String entityType;
  @SerializedName("name")
  private String entityName;
  @SerializedName("contactNo")
  private String entityPhone;
  @SerializedName("address")
  private String entityAddress;

  public Entity(String entityType, String entityName, String entityPhone,
       String entityAddress) {
    this.entityType = entityType;
    this.entityName = entityName;
    this.entityPhone = entityPhone;
    this.entityAddress = entityAddress;
  }

  public Entity() {

  }


  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }


  public String getEntityPhone() {
    return entityPhone;
  }

  public void setEntityPhone( String entityPhone) {
    this.entityPhone = entityPhone;
  }

  public String getEntityAddress() {
    return entityAddress;
  }

  public void setEntityAddress(@Nullable String entityAddress) {
    this.entityAddress = entityAddress;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Entity{" +
        "id=" + id +
        ", entityType='" + entityType + '\'' +
        ", entityName='" + entityName + '\'' +
        ", entityPhone='" + entityPhone + '\'' +
        ", entityAddress='" + entityAddress + '\'' +
        '}';
  }
}

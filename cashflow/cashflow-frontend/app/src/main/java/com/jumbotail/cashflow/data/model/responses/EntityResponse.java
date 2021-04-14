package com.jumbotail.cashflow.data.model.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jumbotail.cashflow.data.model.Entity;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EntityResponse {
  @SerializedName("entityDtoList")
  @Expose
  private List<Entity> entityList = null;

  public EntityResponse(List<Entity> entityList) {
    this.entityList = entityList;
  }

  public List<Entity> getEntityList() {
    return entityList;
  }

  public void setEntityList(List<Entity> entityList) {
    this.entityList = entityList;
  }



  public List<Entity> getEntityByType(String type){
    if(type.equals("ALL")){
      return entityList;
    }
    List<Entity> temp = new ArrayList<>();
    for(Entity entity : entityList){
      if(entity.getEntityType().toUpperCase().equals(type)){
        temp.add(entity);
      }
    }
    return temp;
  }

  public Entity getEntityById(String id){

    for(Entity entity : entityList){
      if(entity.getId() == Integer.parseInt(id)){
        return entity;
      }

    }
    return null;
  }

  @Override
  public String toString() {
    return "EntityResponse{" +
        "entityList=" + entityList +
        '}';
  }
  public static class PostResponse {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      return "PostResponse{" +
          "message='" + message + '\'' +
          '}';
    }
  }
}

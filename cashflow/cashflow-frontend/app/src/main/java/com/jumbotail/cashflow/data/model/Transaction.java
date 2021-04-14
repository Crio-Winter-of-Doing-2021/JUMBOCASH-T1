package com.jumbotail.cashflow.data.model;

import android.text.TextUtils;
import android.util.Pair;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jumbotail.cashflow.databinding.ActivityAddTransactionBinding;
import java.io.Serializable;

@Entity(tableName = "transactions")
public class Transaction implements Serializable {

  @PrimaryKey
  private int id;

  @SerializedName("amount")
  @Expose
  private Double amount;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("modeOfPayment")
  @Expose
  private String modeOfPayment;
  @SerializedName("timestamp")
  @Expose
  private Long timestamp;
  @SerializedName("entityId")
  @Expose
  private long entityId;
  @SerializedName("remarks")
  @Expose
  private String remarks;

  private String entityName;

  @SerializedName("status")
  @Expose
  private String status;

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getModeOfPayment() {
    return modeOfPayment;
  }

  public void setModeOfPayment(String modeOfPayment) {
    this.modeOfPayment = modeOfPayment;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public long getEntityId() {
    return entityId;
  }

  public void setEntityId(long entityId) {
    this.entityId = entityId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "id=" + id +
        ", amount=" + amount +
        ", type='" + type + '\'' +
        ", modeOfPayment='" + modeOfPayment + '\'' +
        ", timestamp=" + timestamp +
        ", entityId=" + entityId +
        ", remarks='" + remarks + '\'' +
        ", entityName='" + entityName + '\'' +
        ", status='" + status + '\'' +
        '}';
  }

  public Pair<Boolean, String> checkValidation(ActivityAddTransactionBinding binding) {

    binding.edTextDateTime.setError(null);
    binding.edTextAmount.setError(null);
    binding.spinnerEntity.setError(null);
    binding.edTextPaymentMode.setError(null);

    if(amount == null){
      binding.edTextAmount.setError("Please enter amount");
      return new Pair<>(false, "Amount can't be empty");
    }

    else if(modeOfPayment.isEmpty()){
      binding.edTextPaymentMode.setError("Please enter mode of payment");
      return new Pair<>(false, "Mode of payment can't be empty");
    }

    else if(timestamp == null){
      binding.edTextDateTime.setError("Please specify date time");
      return new Pair<>(false, "Please specify date");
    }

    else if(entityId == 0){
      binding.spinnerEntity.setError("Please select an entity");
      return new Pair<>(false, "Please select an entity");
    }
    else if(entityName == null){
      return new Pair<>(false, "Please select an entity");
    }

    else if(type == null){
      return new Pair<>(false, "Please enter transaction type");
    }

    else if(status == null){
      return new Pair<>(false, "Please specify transaction status");
    }
    return new Pair<>(true, "");
  }

}

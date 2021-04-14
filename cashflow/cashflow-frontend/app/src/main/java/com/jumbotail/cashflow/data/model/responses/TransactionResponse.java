package com.jumbotail.cashflow.data.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jumbotail.cashflow.data.model.Transaction;
import java.util.List;

public class TransactionResponse {
  @SerializedName("transactionDtoList")
  @Expose
  private List<Transaction> transactionList = null;
  @SerializedName("cashIn")
  @Expose
  private Integer cashIn;
  @SerializedName("cashOut")
  @Expose
  private Integer cashOut;

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  public void setTransactionList(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }

  public Integer getCashIn() {
    return cashIn;
  }

  public void setCashIn(Integer cashIn) {
    this.cashIn = cashIn;
  }

  public Integer getCashOut() {
    return cashOut;
  }

  public void setCashOut(Integer cashOut) {
    this.cashOut = cashOut;
  }

  @Override
  public String toString() {
    return "TransactionResponse{" +
        "transactionList=" + transactionList +
        ", cashIn=" + cashIn +
        ", cashOut=" + cashOut +
        '}';
  }

  public static class PostResponse{

    @SerializedName("cashIn")
    @Expose
    private int cashIn;

    @SerializedName("cashOut")
    @Expose
    private int cashOut;

    public int getCashIn() {
      return cashIn;
    }

    public int getCashOut() {
      return cashOut;
    }

    @Override
    public String toString() {
      return "PostResponse{" +
          "cashIn=" + cashIn +
          ", cashOut=" + cashOut +
          '}';
    }
  }
}

package com.jumbotail.cashflow.ui.transaction;

import static com.jumbotail.cashflow.utils.Constants.TRANSACTION_CREDIT;
import static com.jumbotail.cashflow.utils.Constants.TRANSACTION_DEBIT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.model.Transaction;
import com.jumbotail.cashflow.databinding.ListTransactionBinding;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

  private List<Transaction> transactionList;
  private RecyclerViewItemListener listener;
  public TransactionAdapter(RecyclerViewItemListener listener, List<Transaction> transactionList) {

    this.listener = listener;
    this.transactionList = transactionList;

  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ListTransactionBinding listBinding = ListTransactionBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );

    return new ViewHolder(listBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Transaction transaction = transactionList.get(position);
    if(transaction.getType().equals(TRANSACTION_CREDIT)){
      holder.tViewTransactionType.setText("Received from");
      holder.imgTransactionType.setBackgroundResource(R.drawable.ic_credit);
    }
    if(transaction.getType().equals(TRANSACTION_DEBIT)){
      holder.tViewTransactionType.setText("Paid to");
      holder.imgTransactionType.setBackgroundResource(R.drawable.ic_debit);
    }

    holder.tViewEntity.setText(transaction.getEntityName());
    holder.tViewAmount.setText("₹ "+transaction.getAmount().toString());
    String timestamp = toDate(transaction.getTimestamp());
    holder.tViewDate.setText(timestamp);
    holder.tViewPaymentMode.setText(transaction.getModeOfPayment().toUpperCase()+" TRANSFER");

    holder.itemView.setOnClickListener(view ->{
      listener.onRecyclerItemClick(view, position, transaction);
    });
  }

  private String toDate(long timestamp){
    Date date = new Date(timestamp);
    Format format = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    return format.format(date);
  }


  @Override
  public int getItemCount() {
    return transactionList.size();
  }

  public void filterList(List<Transaction> list){
    transactionList = list;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView tViewTransactionType, tViewAmount, tViewPaymentMode, tViewEntity, tViewDate;
    ImageView imgTransactionType;
    public ViewHolder(@NonNull ListTransactionBinding itemView) {
      super(itemView.getRoot());
      tViewTransactionType = itemView.tViewTransactionType;
      tViewEntity = itemView.txtViewEntityName;
      tViewAmount = itemView.txtViewTransactionAmount;
      tViewDate = itemView.txtViewTransactionDate;
      tViewPaymentMode = itemView.txtViewTransactionMode;

      imgTransactionType = itemView.imgTransactionType;
    }
  }
  public interface RecyclerViewItemListener {
    void onRecyclerItemClick(View v, int position, Transaction transaction);
  }
}

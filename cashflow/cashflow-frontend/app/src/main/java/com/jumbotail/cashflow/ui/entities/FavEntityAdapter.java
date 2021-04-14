package com.jumbotail.cashflow.ui.entities;

import static com.jumbotail.cashflow.utils.HelperFunctions.extractMonth;
import static com.jumbotail.cashflow.utils.HelperFunctions.toDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.jumbotail.cashflow.data.model.FavouriteEntity;
import com.jumbotail.cashflow.databinding.ListEntityBinding;
import com.jumbotail.cashflow.databinding.ListFavEntityBinding;
import java.util.List;

public class FavEntityAdapter extends RecyclerView.Adapter<FavEntityAdapter.ViewHolder> {

  private List<FavouriteEntity> list;

  public FavEntityAdapter(List<FavouriteEntity> list){
    this.list = list;
  }


  @NonNull
  @Override
  public FavEntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ListFavEntityBinding binding = ListFavEntityBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull FavEntityAdapter.ViewHolder holder, int position) {

    FavouriteEntity entity = list.get(position);

    holder.tViewEntityName.setText(entity.getEntityName());
    holder.tViewTransactionCount.setText("Total transaction done - "+entity.getCount());
    holder.tViewTimestamp.setText("Favourite in month of "+extractMonth(entity.getTimestamp()));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public void updateList(List<FavouriteEntity> l){
    list = l;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView tViewEntityName, tViewTransactionCount, tViewTimestamp;
    public ViewHolder(ListFavEntityBinding itemView) {
      super(itemView.getRoot());
      tViewEntityName = itemView.txtViewEntityName;
      tViewTransactionCount = itemView.txtViewTransactionCount;
      tViewTimestamp = itemView.txtViewMonth;
    }
  }
}

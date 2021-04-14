package com.jumbotail.cashflow.ui.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.jumbotail.cashflow.data.model.Entity;
import com.jumbotail.cashflow.databinding.ListEntityBinding;
import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.ViewHolder> {

  private List<Entity> entityList;
  private RecyclerViewItemListener listener;

  public EntityAdapter(RecyclerViewItemListener listener, List<Entity> entityList){
    this.listener = listener;
    this.entityList = entityList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ListEntityBinding listEntityBinding = ListEntityBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );

    return new ViewHolder(listEntityBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // set data to text views
    holder.tViewEntityName.setText(entityList.get(position).getEntityName().toUpperCase());
    holder.btnEntityType.setText(entityList.get(position).getEntityType());
    holder.tViewEntityPhone.setText(String.valueOf(entityList.get(position).getEntityPhone()));
    holder.tViewEntityAddress.setText(entityList.get(position).getEntityAddress());

    holder.itemView.setOnClickListener(view ->{
      listener.onRecyclerItemClick(view, position, entityList.get(position).getId());
    });
  }

  @Override
  public int getItemCount() {
    return entityList.size();
  }

  public void updateList(List<Entity> list){
    entityList = list;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tViewEntityName, tViewEntityPhone,tViewEntityAddress;
    Button btnEntityType;
    public ViewHolder(@NonNull ListEntityBinding itemView) {
      super(itemView.getRoot());
      tViewEntityName = itemView.tViewEntityName;
      btnEntityType = itemView.btnEntityType;
      tViewEntityPhone = itemView.tViewEntityPhone;
      tViewEntityAddress = itemView.tViewEntityAddress;
    }
  }
  public interface RecyclerViewItemListener {
    void onRecyclerItemClick(View v, int position, int entityId);
  }
}

package com.example.to_dolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.R;
import com.example.to_dolist.model.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private final List<NotificationItem> list;

    public NotificationAdapter(List<NotificationItem> list){

        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){

        NotificationItem item = list.get(position);

        holder.txtTitle.setText(item.getTitle());

        holder.txtMessage.setText(item.getMessage());

    }

    @Override
    public int getItemCount(){

        return list.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtMessage;

        ViewHolder(View itemView){

            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);

            txtMessage=itemView.findViewById(R.id.txtMessage);

        }

    }

}
package com.example.to_dolist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.R;
import com.example.to_dolist.entity.Task;

import java.util.List;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private List<Task> fullList;
    private OnTaskClickListener listener;
    private final int[] pastelColors = {
            R.color.pastel1,
            R.color.pastel2,
            R.color.pastel3,
            R.color.pastel4,
            R.color.pastel5
    };

    public interface OnTaskClickListener{
        void onClick(Task task);
        void onDelete(Task task);
        void onComplete(Task task);
    }

    public TaskAdapter(Context context,List<Task> taskList){

        this.context=context;
        this.taskList=taskList;
        this.fullList = new ArrayList<>(taskList);

    }

    public void setOnTaskClickListener(OnTaskClickListener listener){

        this.listener=listener;

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){

        View view= LayoutInflater.from(context)
                .inflate(R.layout.item_task,parent,false);

        return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder,int position){

        Task task=taskList.get(position);

        holder.txtTitle.setText(task.getTitle());

        holder.txtDeadline.setText("📅 " + task.getDeadline());

        holder.txtActivity.setText("🏷 " + task.getActivityType());

        if(task.isCompleted()){

            holder.txtStatus.setText("✓ Selesai");

            holder.txtStatus.setBackgroundResource(R.drawable.status_background);

            holder.txtTitle.setPaintFlags(
                    holder.txtTitle.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);

        }else{

            holder.txtStatus.setText("⏳ Belum Selesai");

            holder.txtStatus.setBackgroundResource(
                    R.drawable.status_pending_background);

            holder.txtTitle.setPaintFlags(
                    holder.txtTitle.getPaintFlags()
                            & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }

        holder.itemView.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(
                                context,
                                pastelColors[position % pastelColors.length]
                        )
                )
        );

        holder.itemView.setOnClickListener(v -> {

            if(listener!=null){

                listener.onClick(task);

            }

        });

        holder.itemView.setOnLongClickListener(v -> {

            showPopup(v,task);

            return true;

        });

    }

    @Override
    public int getItemCount(){

        return taskList.size();

    }

    private void showPopup(View view,Task task){

        PopupMenu popupMenu=new PopupMenu(context,view);

        popupMenu.inflate(R.menu.menu_task);

        popupMenu.setOnMenuItemClickListener(item -> {

            int id=item.getItemId();

            if(id==R.id.menu_complete){

                confirmComplete(task);

                return true;

            }

            if(id==R.id.menu_delete){

                if(listener!=null){

                    confirmDelete(task);

                }

                return true;

            }

            return false;

        });

        popupMenu.show();

    }

    private void confirmDelete(Task task){

        new AlertDialog.Builder(context)

                .setTitle(R.string.delete_title)

                .setMessage(R.string.delete_message)

                .setIcon(R.drawable.ic_delete)

                .setPositiveButton(R.string.yes,(dialog,which)->{

                    if(listener!=null){

                        listener.onDelete(task);

                    }

                    Toast.makeText(
                            context,
                            "To-Do berhasil dihapus",
                            Toast.LENGTH_SHORT
                    ).show();

                })

                .setNegativeButton(R.string.cancel,null)

                .show();

    }

    private void confirmComplete(Task task){

        new AlertDialog.Builder(context)

                .setTitle(R.string.complete_title)

                .setMessage(R.string.complete_message)

                .setIcon(R.drawable.ic_check)

                .setPositiveButton(R.string.yes,(dialog,which)->{

                    if(listener!=null){

                        listener.onComplete(task);

                    }

                    Toast.makeText(
                            context,
                            "Selamat! Tugas selesai 🎉",
                            Toast.LENGTH_SHORT
                    ).show();

                })

                .setNegativeButton(R.string.cancel,null)

                .show();

    }

    public void updateData(List<Task> list){

        taskList.clear();

        taskList.addAll(list);

        fullList.clear();

        fullList.addAll(list);

        notifyDataSetChanged();

    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;

        TextView txtActivity;

        TextView txtDeadline;

        TextView txtStatus;

        public TaskViewHolder(@NonNull View itemView){

            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);

            txtActivity=itemView.findViewById(R.id.txtActivity);

            txtDeadline=itemView.findViewById(R.id.txtDeadline);

            txtStatus = itemView.findViewById(R.id.txtStatus);

        }

    }

    public void filter(String text){

        taskList.clear();

        if(text.isEmpty()){

            taskList.addAll(fullList);

        }else{

            text = text.toLowerCase();

            for(Task task : fullList){

                if(task.getTitle()
                        .toLowerCase()
                        .contains(text)){

                    taskList.add(task);

                }

            }

        }

        notifyDataSetChanged();

    }

}
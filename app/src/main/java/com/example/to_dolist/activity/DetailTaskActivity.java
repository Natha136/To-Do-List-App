package com.example.to_dolist.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.R;
import com.example.to_dolist.database.DatabaseClient;
import com.example.to_dolist.entity.Task;

public class DetailTaskActivity extends AppCompatActivity {

    private TextView txtTitle,txtType,txtDeadline,txtDescription,txtStatus,btnEdit;

    private ImageView btnBack;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_task);

        txtTitle=findViewById(R.id.txtTitle);
        txtType=findViewById(R.id.txtType);
        txtDeadline=findViewById(R.id.txtDeadline);
        txtDescription=findViewById(R.id.txtDescription);
        txtStatus=findViewById(R.id.txtStatus);

        btnEdit=findViewById(R.id.btnEdit);

        btnBack=findViewById(R.id.btnBack);

        int id=getIntent().getIntExtra("taskId",-1);

        if(id!=-1){

            task = DatabaseClient.getInstance(this)
                    .taskDao()
                    .getTaskById(id);

            if(task != null){

                txtTitle.setText(task.getTitle());
                txtType.setText(task.getActivityType());
                txtDeadline.setText(task.getDeadline());
                txtDescription.setText(task.getDescription());

                txtStatus.setText(
                        task.isCompleted()
                                ? "Selesai"
                                : "Belum Selesai");

            }

        }

        btnBack.setOnClickListener(v->finish());

        btnEdit.setOnClickListener(v->{

            Intent intent =
                    new Intent(
                            DetailTaskActivity.this,
                            EditTaskActivity.class);

            intent.putExtra("taskId",task.getId());

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out);

        });

    }

}
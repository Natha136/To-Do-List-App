package com.example.to_dolist.activity;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.R;
import com.example.to_dolist.adapter.NotificationAdapter;
import com.example.to_dolist.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        ImageView btnBack=findViewById(R.id.btnBack);

        RecyclerView recycler=findViewById(R.id.recyclerNotification);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        List<NotificationItem> list=new ArrayList<>();

        list.add(new NotificationItem(
                "Deadline Hari Ini",
                "Belajar Android - Deadline 25 Juli 2026"));

        list.add(new NotificationItem(
                "Deadline Besok",
                "Laporan Basis Data - Deadline 26 Juli 2026"));

        recycler.setAdapter(new NotificationAdapter(list));

        btnBack.setOnClickListener(v->finish());

    }

}
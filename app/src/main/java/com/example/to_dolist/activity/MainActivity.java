package com.example.to_dolist.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.PopupMenu;
import android.widget.Button;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.ExistingPeriodicWorkPolicy;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.to_dolist.R;
import com.example.to_dolist.adapter.TaskAdapter;
import com.example.to_dolist.database.DatabaseClient;
import com.example.to_dolist.entity.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.to_dolist.helper.NotificationHelper;
import com.example.to_dolist.worker.ReminderWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView txtEmpty;

    private RecyclerView recyclerTask;

    private FloatingActionButton fabAdd;

    private TaskAdapter adapter;

    private List<Task> taskList;

    private ImageView btnNotification;

    private EditText edtSearch;

    private ImageView btnSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= 33) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        100);

            }

        }

        initView();

        fabAdd.animate()
                .rotation(360)
                .setDuration(700)
                .start();

        loadData();

        NotificationHelper.createChannel(this);

        PeriodicWorkRequest reminderWork =
                new PeriodicWorkRequest.Builder(
                        ReminderWorker.class,
                        1,
                        TimeUnit.DAYS)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "dailyReminder",
                        ExistingPeriodicWorkPolicy.KEEP,
                        reminderWork
                );

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                if(adapter!=null){

                    adapter.filter(s.toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        fabAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddTaskActivity.class);

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out);

        });

        btnNotification.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    NotificationActivity.class);

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out);


        });

        btnSort.setOnClickListener(v -> showSortMenu());

    }

    private void initView(){

        recyclerTask=findViewById(R.id.recyclerTask);

        fabAdd=findViewById(R.id.fabAdd);

        recyclerTask.setLayoutManager(new LinearLayoutManager(this));

        recyclerTask.setHasFixedSize(true);

        btnNotification=findViewById(R.id.btnNotification);

        edtSearch=findViewById(R.id.edtSearch);

        btnSort = findViewById(R.id.btnSort);

        txtEmpty = findViewById(R.id.txtEmpty);

    }

    private void loadData(){

        taskList = DatabaseClient.getInstance(this)
                .taskDao()
                .getAllTasks();

        if(taskList.isEmpty()){

            txtEmpty.setVisibility(View.VISIBLE);

            recyclerTask.setVisibility(View.GONE);

        }else{

            txtEmpty.setVisibility(View.GONE);

            recyclerTask.setVisibility(View.VISIBLE);

        }

        if(adapter == null){

            adapter = new TaskAdapter(this, taskList);

            adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {

                @Override
                public void onClick(Task task) {

                    Intent intent = new Intent(MainActivity.this,
                            DetailTaskActivity.class);

                    intent.putExtra("taskId", task.getId());

                    startActivity(intent);

                }

                @Override
                public void onDelete(Task task) {

                    DatabaseClient.getInstance(MainActivity.this)
                            .taskDao()
                            .delete(task);

                    loadData();

                }

                @Override
                public void onComplete(Task task) {

                    task.setCompleted(true);

                    DatabaseClient.getInstance(MainActivity.this)
                            .taskDao()
                            .update(task);

                    loadData();

                }

            });

            recyclerTask.setAdapter(adapter);

            recyclerTask.scheduleLayoutAnimation();

        }else{

            adapter.updateData(taskList);

            recyclerTask.scheduleLayoutAnimation();

        }

    }

    private void showSortMenu(){

        PopupMenu popupMenu =
                new PopupMenu(this, btnSort);

        popupMenu.inflate(R.menu.sort_menu);

        popupMenu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menuTitle) {

                adapter.updateData(
                        DatabaseClient.getInstance(this)
                                .taskDao()
                                .sortByTitle());

                return true;

            } else if (id == R.id.menuDeadline) {

                adapter.updateData(
                        DatabaseClient.getInstance(this)
                                .taskDao()
                                .sortByDeadline());

                return true;

            } else if (id == R.id.menuCreated) {

                adapter.updateData(
                        DatabaseClient.getInstance(this)
                                .taskDao()
                                .sortByCreatedAt());

                return true;

            }

            return false;

        });

        popupMenu.show();

    }

    @Override
    protected void onResume() {

        super.onResume();

        loadData();

    }

}
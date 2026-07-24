package com.example.to_dolist.worker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.to_dolist.R;
import com.example.to_dolist.activity.MainActivity;
import com.example.to_dolist.database.DatabaseClient;
import com.example.to_dolist.entity.Task;
import com.example.to_dolist.helper.NotificationHelper;

import java.util.List;

public class ReminderWorker extends Worker {

    public ReminderWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {

        super(context, params);

    }

    @NonNull
    @Override
    public Result doWork() {

        Intent intent =
                new Intent(getApplicationContext(),
                        MainActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        1,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(),
                        NotificationHelper.CHANNEL_ID)

                        .setSmallIcon(R.drawable.ic_notifications)

                        .setContentTitle("To-Do Reminder")

                        .setContentText("Ada tugas yang mendekati deadline.")

                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH)

                        .setContentIntent(pendingIntent)

                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager)
                        getApplicationContext()
                                .getSystemService(Context.NOTIFICATION_SERVICE);

        List<Task> tasks =
                DatabaseClient.getInstance(getApplicationContext())
                        .taskDao()
                        .getAllTasks();

        boolean adaBelumSelesai = false;

        for(Task task : tasks){

            if(!task.isCompleted()){

                adaBelumSelesai = true;
                break;

            }

        }

        if(!adaBelumSelesai){

            return Result.success();

        }

        manager.notify(1, builder.build());

        return Result.success();

    }

}
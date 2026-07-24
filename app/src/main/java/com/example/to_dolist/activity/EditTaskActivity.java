package com.example.to_dolist.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.R;
import com.example.to_dolist.database.DatabaseClient;
import com.example.to_dolist.entity.Task;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtDeadline;
    private EditText edtDescription;

    private Spinner spinnerActivity;

    private Button btnUpdate;

    private ImageView btnBack;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_task);

        edtTitle=findViewById(R.id.edtTitle);
        edtDeadline=findViewById(R.id.edtDeadline);
        edtDescription=findViewById(R.id.edtDescription);

        spinnerActivity=findViewById(R.id.spinnerActivity);

        btnUpdate=findViewById(R.id.btnUpdate);

        btnBack=findViewById(R.id.btnBack);

        ArrayAdapter<CharSequence> adapter=
                ArrayAdapter.createFromResource(
                        this,
                        R.array.activity_type,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerActivity.setAdapter(adapter);

        int id=getIntent().getIntExtra("taskId",-1);

        task=DatabaseClient.getInstance(this)
                .taskDao()
                .getTaskById(id);

        if(task!=null){

            edtTitle.setText(task.getTitle());

            edtDeadline.setText(task.getDeadline());

            edtDescription.setText(task.getDescription());

            spinnerActivity.setSelection(
                    adapter.getPosition(task.getActivityType()));

        }

        edtDeadline.setOnClickListener(v->showDatePicker());

        btnBack.setOnClickListener(v->finish());

        btnUpdate.setOnClickListener(v->updateTask());

    }

    private void showDatePicker(){

        Calendar calendar=Calendar.getInstance();

        DatePickerDialog dialog=new DatePickerDialog(
                this,
                (view,year,month,day)->{

                    edtDeadline.setText(
                            day+"/"+(month+1)+"/"+year);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();

    }

    private void updateTask(){

        task.setTitle(
                edtTitle.getText().toString());

        task.setActivityType(
                spinnerActivity.getSelectedItem().toString());

        task.setDeadline(
                edtDeadline.getText().toString());

        task.setDescription(
                edtDescription.getText().toString());

        DatabaseClient.getInstance(this)
                .taskDao()
                .update(task);

        finish();

    }

}
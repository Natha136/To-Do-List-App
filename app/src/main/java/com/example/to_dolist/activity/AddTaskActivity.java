package com.example.to_dolist.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.R;
import com.example.to_dolist.database.DatabaseClient;
import com.example.to_dolist.entity.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtDeadline;
    private EditText edtDescription;

    private Spinner spinnerActivity;

    private Button btnSave;

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);

        edtTitle = findViewById(R.id.edtTitle);
        edtDeadline = findViewById(R.id.edtDeadline);
        edtDescription = findViewById(R.id.edtDescription);

        spinnerActivity = findViewById(R.id.spinnerActivity);

        btnSave = findViewById(R.id.btnSave);

        btnBack = findViewById(R.id.btnBack);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.activity_type,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerActivity.setAdapter(adapter);

        edtDeadline.setOnClickListener(v -> showDatePicker());

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveTask());

    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) ->
                        edtDeadline.setText(day + "/" + (month + 1) + "/" + year),

                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();

    }

    private void saveTask() {

        String title = edtTitle.getText().toString().trim();
        String deadline = edtDeadline.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String activity = spinnerActivity.getSelectedItem().toString();

        if (title.isEmpty()) {

            edtTitle.setError("Judul wajib diisi");
            return;

        }

        if (deadline.isEmpty()) {

            edtDeadline.setError("Deadline wajib diisi");
            return;

        }

        Task task = new Task();

        task.setTitle(title);
        task.setActivityType(activity);
        task.setDeadline(deadline);
        task.setDescription(description);

        String createdDate = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        task.setCreatedAt(createdDate);

        task.setCompleted(false);

        DatabaseClient.getInstance(this)
                .taskDao()
                .insert(task);

        Toast.makeText(
                this,
                "To-Do berhasil ditambahkan",
                Toast.LENGTH_SHORT
        ).show();

        finish();

    }

}
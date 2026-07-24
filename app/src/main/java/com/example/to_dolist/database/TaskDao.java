package com.example.to_dolist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.to_dolist.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :keyword || '%'")
    List<Task> search(String keyword);

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    List<Task> sortByTitle();

    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    List<Task> sortByDeadline();

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> sortByCreated();

    @Query("SELECT * FROM tasks WHERE id=:id")
    Task getTaskById(int id);

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> sortByCreatedAt();

}
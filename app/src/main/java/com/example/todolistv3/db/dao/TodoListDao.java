package com.example.todolistv3.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todolistv3.db.ToDoEntity;

import java.util.List;

/**
 * To Do List Data Access layer pulls/queries or bundles all to do items in the database
 */
@Dao
public interface TodoListDao {

    @Query("SELECT * FROM TodoEntity")
    List<ToDoEntity> getAll();

    @Query("SELECT * FROM ToDoEntity WHERE id IN (:id)")
    List<ToDoEntity> loadAllByIds(String[] id);

    @Query("SELECT * FROM ToDoEntity WHERE todo LIKE :search")
    ToDoEntity findToDoWithToDoString(String search);

    @Query("UPDATE ToDoEntity SET todo = :newTodoValue WHERE todo = :previousTodoValue")
    void updateTodo(String previousTodoValue, String newTodoValue);

    @Insert
    void insertAll(ToDoEntity... toDoEntities);

    @Delete
    void delete(ToDoEntity toDoEntity);

}

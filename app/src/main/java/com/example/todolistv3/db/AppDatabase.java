package com.example.todolistv3.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todolistv3.db.dao.TodoListDao;

@Database(entities = {ToDoEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoListDao todoListDao();
}

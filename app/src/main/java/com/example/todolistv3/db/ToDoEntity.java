package com.example.todolistv3.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This is a database entity object that represents a single to do item.
 *
 */
@Entity
public class ToDoEntity {

    @PrimaryKey
    public double id;

    @ColumnInfo(name = "todo")
    public String todo;
}

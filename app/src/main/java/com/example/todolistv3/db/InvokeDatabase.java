package com.example.todolistv3.db;

import android.content.Context;

import androidx.room.Room;

/**
 * Construct the instance of this class, constructor will initialize the application context
 * do that {@link InvokeDatabase#getAppDatabase()} can be invoked to get an instance of the AppDatabase.
 * This class should be called only once upon start up.
 *
 *
 */
public class InvokeDatabase {

    private AppDatabase appDatabase;
    private final Context applicationContext;

    public InvokeDatabase(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Create a database instance only once, store in appDatabase property
     *
     * @return
     */
    public AppDatabase getAppDatabase(){
        AppDatabase db = Room.databaseBuilder(applicationContext,
                AppDatabase.class, "db-todo")
                .fallbackToDestructiveMigration()
                .build();
        return db;
    }
}

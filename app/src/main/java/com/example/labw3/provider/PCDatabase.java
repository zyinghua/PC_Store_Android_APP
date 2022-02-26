package com.example.labw3.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PC.class}, version = 1, exportSchema = false)
public abstract class PCDatabase extends RoomDatabase {
    public static final String PC_DATABASE_NAME = "PC_database";

    public abstract PCDao pcDao();

    private static volatile PCDatabase INSTANCE;
    private static final int NUM_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    static PCDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (PCDatabase.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PCDatabase.class, PC_DATABASE_NAME).build();
                }
            }
        }

        return INSTANCE;
    }
}

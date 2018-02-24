package com.alex.devops.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.alex.devops.utils.Constants;

@Database(entities = {Client.class}, version = 1, exportSchema = false)
public abstract class ClientsDataBase extends RoomDatabase {
    private static ClientsDataBase INSTANCE;

    public abstract ClientDao clientDao();

    public static ClientsDataBase getDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ClientsDataBase.class, Constants.DB_NAME).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

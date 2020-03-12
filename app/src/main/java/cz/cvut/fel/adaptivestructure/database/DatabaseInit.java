package cz.cvut.fel.adaptivestructure.database;

import android.content.Context;

import java.util.Map;

import androidx.room.Room;

public class DatabaseInit {
    private static ASDatabase database;

    public static ASDatabase getASDatabase(Context context){
        if(database == null) {
                database = Room.databaseBuilder(context, ASDatabase.class, "as_db").build();
        }
        return database;
    }
}

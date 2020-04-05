package cz.cvut.fel.adaptivestructure.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import cz.cvut.fel.adaptivestructure.dao.NodeDao;
import cz.cvut.fel.adaptivestructure.entity.Node;

@Database(entities = {Node.class}, version = 4)
public abstract class ASDatabase extends RoomDatabase {
    public abstract NodeDao nodeDao();
}

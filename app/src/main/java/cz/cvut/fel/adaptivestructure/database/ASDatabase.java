package cz.cvut.fel.adaptivestructure.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import cz.cvut.fel.adaptivestructure.dao.NodeDao;
import cz.cvut.fel.adaptivestructure.dao.StructureDao;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.entity.Structure;

@Database(entities = {Node.class, Structure.class}, version = 9)
public abstract class ASDatabase extends RoomDatabase {
    public abstract NodeDao nodeDao();
    public abstract StructureDao structureDao();
    public void cleanDatabase(){
        nodeDao().deleteAll();
        structureDao().deleteAll();
    }
}

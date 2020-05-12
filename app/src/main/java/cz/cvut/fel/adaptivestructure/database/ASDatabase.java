package cz.cvut.fel.adaptivestructure.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import cz.cvut.fel.adaptivestructure.dao.AppInfoDao;
import cz.cvut.fel.adaptivestructure.dao.NodeDao;
import cz.cvut.fel.adaptivestructure.dao.StructureDao;
import cz.cvut.fel.adaptivestructure.entity.AppInfo;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.entity.Structure;

/**
 * Get tables
 *
 * @author Marek Klement
 */
@Database(entities = {Node.class, Structure.class, AppInfo.class}, version = 12)
public abstract class ASDatabase extends RoomDatabase {
    public abstract NodeDao nodeDao();

    public abstract StructureDao structureDao();

    public abstract AppInfoDao appInfoDao();

    public void cleanDatabase() {
        nodeDao().deleteAll();
        structureDao().deleteAll();
    }
}

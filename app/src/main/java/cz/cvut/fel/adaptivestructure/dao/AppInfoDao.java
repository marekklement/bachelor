package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import cz.cvut.fel.adaptivestructure.entity.AppInfo;


@Dao
public interface AppInfoDao extends BaseDao<AppInfo> {

    @Query("SELECT * FROM appinfo")
    List<AppInfo> getAll();

    @Query("SELECT * FROM appinfo WHERE id = (:id)")
    AppInfo getById(long id);

    @Query("DELETE FROM appinfo")
    void deleteAll();
}

package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fel.adaptivestructure.entity.AppInfo;


@Dao
public interface AppInfoDao {

    @Query("SELECT * FROM appinfo")
    List<AppInfo> getAll();

    @Query("SELECT * FROM appinfo WHERE id IN (:appInfoIds)")
    List<AppInfo> loadAllByIds(long[] appInfoIds);

    @Query("SELECT * FROM appinfo WHERE id = (:id)")
    AppInfo getById(long id);

    @Query("SELECT * FROM appinfo WHERE gender LIKE :gender")
    List<AppInfo> getByGender(String gender);

    @Query("SELECT uid FROM node ORDER BY uid DESC LIMIT 1")
    int findHighestId();

    @Query("DELETE FROM appinfo")
    void deleteAll();

    @Insert
    void insertAll(AppInfo... appInfos);

    @Insert
    void insert(AppInfo appInfo);

    @Delete
    void delete(AppInfo appInfo);

    @Update
    void update(AppInfo appInfo);
}

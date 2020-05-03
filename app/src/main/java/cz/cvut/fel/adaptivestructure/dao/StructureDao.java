package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fel.adaptivestructure.entity.Structure;

@Dao
public interface StructureDao {
    @Query("SELECT * FROM structure")
    List<Structure> getAll();

    @Query("SELECT * FROM structure WHERE version IN (:structureIds)")
    List<Structure> loadAllByIds(long[] structureIds);

    @Query("SELECT * FROM structure WHERE version = (:version)")
    Structure getById(int version);

    @Query("SELECT * FROM structure ORDER BY version DESC LIMIT 1")
    Structure getHighestVersion();

    @Query("DELETE FROM structure")
    void deleteAll();

    @Insert
    void insertAll(Structure... structures);

    @Insert
    void insert(Structure structure);

    @Delete
    void delete(Structure structure);

    @Update
    void update(Structure structure);
}

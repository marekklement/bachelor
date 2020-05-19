package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import cz.cvut.fel.adaptivestructure.entity.Structure;

/**
 * DAO for querying structures
 *
 * @author Marek Klement
 */
@Dao
public interface StructureDao extends BaseDao<Structure> {
    @Query("SELECT * FROM structure")
    List<Structure> getAll();

    @Query("SELECT * FROM structure ORDER BY version DESC LIMIT 1")
    Structure getHighestVersion();

    @Query("DELETE FROM structure")
    void deleteAll();
}

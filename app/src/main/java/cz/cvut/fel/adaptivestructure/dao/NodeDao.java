package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fel.adaptivestructure.entity.Node;

/**
 * DAO for querying nodes
 *
 * @author Marek Klement
 */
@Dao
public interface NodeDao {
    @Query("SELECT * FROM node")
    List<Node> getAll();

    @Query("SELECT * FROM node WHERE uid IN (:nodeIds)")
    List<Node> loadAllByIds(long[] nodeIds);

    @Query("SELECT * FROM node WHERE uid = (:id)")
    Node getById(long id);

    @Query("SELECT * FROM node WHERE name LIKE :name")
    List<Node> getByName(String name);

    @Query("SELECT uid FROM node ORDER BY uid DESC LIMIT 1")
    int findHighestId();

    @Query("DELETE FROM node")
    void deleteAll();

    @Insert
    void insertAll(Node... nodes);

    @Insert
    void insert(Node node);

    @Delete
    void delete(Node node);

    @Update
    void update(Node node);
}

package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import cz.cvut.fel.adaptivestructure.entity.Node;

/**
 * DAO for querying nodes
 *
 * @author Marek Klement
 */
@Dao
public interface NodeDao extends BaseDao<Node> {
    @Query("SELECT * FROM node")
    List<Node> getAll();

    @Query("SELECT * FROM node WHERE id IN (:nodeIds)")
    List<Node> loadAllByIds(long[] nodeIds);

    @Query("SELECT * FROM node WHERE id = (:id)")
    Node getById(long id);

    @Query("SELECT * FROM node WHERE name LIKE :name")
    List<Node> getByName(String name);

    @Query("SELECT id FROM node ORDER BY id DESC LIMIT 1")
    int findHighestId();

    @Query("DELETE FROM node")
    void deleteAll();
}

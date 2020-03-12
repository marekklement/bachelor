package cz.cvut.fel.adaptivestructure.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import cz.cvut.fel.adaptivestructure.entity.Node;

@Dao
public interface NodeDao {
    @Query("SELECT * FROM node")
    List<Node> getAll();

    @Query("SELECT * FROM node WHERE uid IN (:nodeIds)")
    List<Node> loadAllByIds(long[] nodeIds);

    @Query("SELECT * FROM node WHERE name LIKE :name")
    Node findByName(String name);

    @Insert
    void insertAll(Node... nodes);

    @Delete
    void delete(Node node);

    @Update
    void update(Node node);
}

package cz.cvut.fel.adaptivestructure.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    void insertAll(T... objects);

    @Insert
    void insert(T object);

    @Delete
    void delete(T object);

    @Update
    void update(T object);
}

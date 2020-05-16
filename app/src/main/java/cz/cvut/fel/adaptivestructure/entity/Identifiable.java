package cz.cvut.fel.adaptivestructure.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Identifiable {

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

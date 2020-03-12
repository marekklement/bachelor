package cz.cvut.fel.adaptivestructure.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Node {
    @PrimaryKey
    public long uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "parent")
    public Node parent;

    // node info
    @ColumnInfo(name = "visits")
    public long visits;
    @ColumnInfo(name = "disgust")
    public float disgust;
    @ColumnInfo(name = "joy")
    public float joy;
    @ColumnInfo(name = "neutral")
    public float neutral;
    @ColumnInfo(name = "sadness")
    public float sadness;
    @ColumnInfo(name = "anger")
    public float anger;
}

package cz.cvut.fel.adaptivestructure.entity;

import android.util.Pair;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import cz.cvut.fel.adaptivestructure.converter.StructureConverter;

@Entity
public class Structure {

    @ColumnInfo(name = "version")
    private int version;
    @PrimaryKey
    private int id;
    @TypeConverters(StructureConverter.class)
    List<Pair<String, List<String>>> pages;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Pair<String, List<String>>> getPages() {
        return pages;
    }

    public void setPages(List<Pair<String, List<String>>> pages) {
        this.pages = pages;
    }
}

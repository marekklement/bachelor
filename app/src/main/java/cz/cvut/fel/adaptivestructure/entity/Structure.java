package cz.cvut.fel.adaptivestructure.entity;

import java.util.HashMap;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import cz.cvut.fel.adaptivestructure.converter.StructureConverter;

/**
 * Structure represent the way user sees the app - like structure tree
 *
 * @author Marek Klement
 */
@Entity
public class Structure {

    @TypeConverters(StructureConverter.class)
    HashMap<String, List<String>> pages;
    @PrimaryKey
    @ColumnInfo(name = "version")
    private int version;
    //List<Pair<String, List<String>>> pages;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public HashMap<String, List<String>> getPages() {
        return pages;
    }

    public void setPages(HashMap<String, List<String>> pages) {
        this.pages = pages;
    }
}

package cz.cvut.fel.adaptivestructure.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AppInfo {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "age", defaultValue = "0")
    private int age;

    @ColumnInfo(name = "gender")
    private String gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

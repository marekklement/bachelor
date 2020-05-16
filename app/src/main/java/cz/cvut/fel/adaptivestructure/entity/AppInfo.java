package cz.cvut.fel.adaptivestructure.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class AppInfo extends Identifiable {

    @ColumnInfo(name = "age", defaultValue = "0")
    private int age;

    @ColumnInfo(name = "gender")
    private String gender;

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

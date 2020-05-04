package cz.cvut.fel.adaptivestructure.converter;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import androidx.room.TypeConverter;

public class StructureConverter {

    @TypeConverter
    public String fromStructure(HashMap<String, List<String>> structure) {
        if (structure == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {}.getType();
        return gson.toJson(structure, type);
    }

    @TypeConverter
    public HashMap<String, List<String>> toStructure(String structure) {
        if (structure == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {}.getType();
        return gson.fromJson(structure, type);
    }
}

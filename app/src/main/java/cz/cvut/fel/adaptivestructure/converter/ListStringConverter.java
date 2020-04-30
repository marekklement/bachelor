package cz.cvut.fel.adaptivestructure.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class ListStringConverter {

    @TypeConverter
    public String fromMovedViewList(List<String> movedViews) {
        if (movedViews == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(movedViews, type);
    }

    @TypeConverter
    public List<String> toMovedViewList(String movedViewsString) {
        if (movedViewsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(movedViewsString, type);
    }
}

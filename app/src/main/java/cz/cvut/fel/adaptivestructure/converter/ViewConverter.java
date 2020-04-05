package cz.cvut.fel.adaptivestructure.converter;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class ViewConverter {

    @TypeConverter
    public String fromMovedViewList(List<View> movedViews) {
        if (movedViews == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<View>>() {}.getType();
        return gson.toJson(movedViews, type);
    }

    @TypeConverter
    public List<View> toMovedViewList(String movedViewsString) {
        if (movedViewsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<View>>() {}.getType();
        return gson.fromJson(movedViewsString, type);
    }
}

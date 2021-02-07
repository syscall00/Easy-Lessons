package com.unito.models;

import androidx.room.TypeConverter;

import com.unito.models.entity.Course;
import com.unito.models.entity.Tutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ObjectConverter {

    @TypeConverter
    public String fromCourseList(List<Course> value) {
        if (value == null)
            return null;
        Gson gson = new Gson();
        return gson.toJson(value);
    }

    @TypeConverter
    public List<Course> toCourseList(String value) {
        if(value == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Course>>() {}.getType();
        return gson.fromJson(value, type);

    }


}

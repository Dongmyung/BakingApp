package com.dongmyungahn.android.bakingapp.utilities;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils {

    public static <T> T convertToObject(String jsonString, Type type) {
        if(jsonString == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }

    public static String convertToJsonString(Object object, Type type) {
        if(object == null) return null;
        Gson gson = new Gson();
        return gson.toJson(object, type);
    }

}

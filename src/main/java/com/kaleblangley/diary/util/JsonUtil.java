package com.kaleblangley.diary.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
    public static String tryGetString(JsonObject jsonObject, String name) {
        return tryGetString(jsonObject, name, null);
    }

    public static String tryGetString(JsonObject jsonObject, String name, String defaultValue) {
        return jsonObject.get(name) == null ? defaultValue : jsonObject.get(name).getAsString();
    }

    public static JsonElement tryGet(JsonObject jsonObject, String name) {
        if (jsonObject.has(name)) {
            return jsonObject.get(name);
        }
        return null;
    }
}
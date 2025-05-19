package com.kaleblangley.diary.diary.data;

import com.kaleblangley.diary.diary.Diary;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DiaryManager {
    private final static Map<String, Diary> diaryMap = new LinkedHashMap<>();
    private final static Map<String, List<Diary>> typeMap = new LinkedHashMap<>();
    private final static Set<String> diaryViewMap = Collections.unmodifiableSet(diaryMap.keySet());
    private final static Set<String> typeViewMap = Collections.unmodifiableSet(typeMap.keySet());

    static void dataRegister(String id, Diary diary) {
        if (diaryMap.containsKey(id)) {
            throw new IllegalArgumentException("ID conflict with code-defined dialogue: " + id);
        }
        diaryMap.put(id, diary);
        String type = diary.type;
        if (type != null) {
            typeMap.computeIfAbsent(type, string -> new ArrayList<>()).add(diary);
        }
    }

    static void clearData() {
        diaryMap.clear();
        typeMap.clear();
    }

    public static Set<String> getDiaryMapView() {
        return diaryViewMap;
    }
    public static Set<String> getTypeMapView() {
        return typeViewMap;
    }

    public static Diary getDiaryValue(@Nullable String id) {
        return diaryMap.getOrDefault(id, null);
    }

    public static List<Diary> getTypeValue(@Nullable String id) {
        return typeMap.getOrDefault(id, List.of());
    }
}

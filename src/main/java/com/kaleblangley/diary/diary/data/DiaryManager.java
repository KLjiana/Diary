package com.kaleblangley.diary.diary.data;

import com.kaleblangley.diary.diary.Diary;
import com.kaleblangley.diary.diary.DiaryPaper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DiaryManager {
    private final static Map<String, DiaryPaper> paperMap = new LinkedHashMap<>();
    private final static Map<String, Diary> bookMap = new LinkedHashMap<>();
    private final static Set<String> diaryViewMap = Collections.unmodifiableSet(paperMap.keySet());
    private final static Set<String> typeViewMap = Collections.unmodifiableSet(bookMap.keySet());

    static void paperRegister(String id, DiaryPaper diaryPaper) {
        if (paperMap.containsKey(id)) {
            throw new IllegalArgumentException("ID conflict with code-defined dialogue: " + id);
        }
        paperMap.put(id, diaryPaper);
    }

    static void bookRegister(String id, Diary diaryPaper) {
        if (bookMap.containsKey(id)) {
            throw new IllegalArgumentException("ID conflict with code-defined dialogue: " + id);
        }
        bookMap.put(id, diaryPaper);
    }

    static void clearPaperData() {
        paperMap.clear();
    }
    static void clearBookData() {
        bookMap.clear();
    }

    public static Set<String> getDiaryMapView() {
        return diaryViewMap;
    }
    public static Set<String> getTypeMapView() {
        return typeViewMap;
    }

    public static DiaryPaper getDiaryValue(@Nullable String id) {
        return paperMap.getOrDefault(id, null);
    }

    public static Diary getTypeValue(@Nullable String id) {
        return bookMap.getOrDefault(id, null);
    }
}

package com.kaleblangley.diary.diary;

public class Diary {
    public final String title;
    public final String type;
    public final String[] texts;

    public Diary(String title, String type, String[] texts) {
        this.title = title;
        this.type = type;
        this.texts = texts;
    }
}

package com.kaleblangley.diary.diary;

import net.minecraft.network.FriendlyByteBuf;

public record Diary(String title, DiaryPaper[] diaryPapers) {
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(title);
        buf.writeVarInt(diaryPapers.length);
        for (DiaryPaper paper : diaryPapers) {
            paper.writeToBuffer(buf);
        }
    }

    public static Diary readFromBuffer(FriendlyByteBuf buf) {
        String title = buf.readUtf();
        int length = buf.readVarInt();
        DiaryPaper[] papers = new DiaryPaper[length];
        for (int i = 0; i < length; i++) {
            papers[i] = DiaryPaper.readFromBuffer(buf);
        }
        return new Diary(title, papers);
    }
}

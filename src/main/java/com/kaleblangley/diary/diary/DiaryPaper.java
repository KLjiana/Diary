package com.kaleblangley.diary.diary;

import net.minecraft.network.FriendlyByteBuf;

public record DiaryPaper(String title, String[] texts) {
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(title);
        buf.writeVarInt(texts.length);
        for (String text : texts) {
            buf.writeUtf(text);
        }
    }

    public static DiaryPaper readFromBuffer(FriendlyByteBuf buf) {
        String title = buf.readUtf();
        int length = buf.readVarInt();
        String[] texts = new String[length];
        for (int i = 0; i < length; i++) {
            texts[i] = buf.readUtf();
        }
        return new DiaryPaper(title, texts);
    }
}

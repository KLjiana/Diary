package com.kaleblangley.diary.network.s2c;

import com.kaleblangley.diary.diary.Diary;
import com.kaleblangley.diary.diary.DiaryPaper;
import com.kaleblangley.diary.diary.data.DiaryManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncDiaryDataPacket {
    private final Map<String, DiaryPaper> papers;
    private final Map<String, Diary> books;

    public SyncDiaryDataPacket(Map<String, DiaryPaper> papers, Map<String, Diary> books) {
        this.papers = papers;
        this.books = books;
    }

    public static void encode(SyncDiaryDataPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.papers.size());
        for (var entry : msg.papers.entrySet()) {
            buf.writeUtf(entry.getKey());
            entry.getValue().writeToBuffer(buf);
        }

        buf.writeVarInt(msg.books.size());
        for (var entry : msg.books.entrySet()) {
            buf.writeUtf(entry.getKey());
            entry.getValue().writeToBuffer(buf);
        }
    }

    public static SyncDiaryDataPacket decode(FriendlyByteBuf buf) {
        Map<String, DiaryPaper> papers = new HashMap<>();
        int paperSize = buf.readVarInt();
        for (int i = 0; i < paperSize; i++) {
            String id = buf.readUtf();
            papers.put(id, DiaryPaper.readFromBuffer(buf));
        }

        Map<String, Diary> books = new HashMap<>();
        int bookSize = buf.readVarInt();
        for (int i = 0; i < bookSize; i++) {
            String id = buf.readUtf();
            books.put(id, Diary.readFromBuffer(buf));
        }

        return new SyncDiaryDataPacket(papers, books);
    }

    public static void handle(SyncDiaryDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DiaryManager.clearPaperData();
            DiaryManager.clearBookData();
            msg.papers.forEach(DiaryManager::paperRegister);
            msg.books.forEach(DiaryManager::bookRegister);
        });
        ctx.get().setPacketHandled(true);
    }
}
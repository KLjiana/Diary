package com.kaleblangley.diary.diary.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.Diary;
import com.kaleblangley.diary.diary.DiaryPaper;
import com.kaleblangley.diary.util.JsonUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiaryLoader extends SimpleJsonResourceReloadListener {
    public DiaryLoader() {
        super(new Gson(), "diary/book");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        DiaryManager.clearBookData();
        for (var entry : object.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();

            try {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = JsonUtil.tryGetString(jsonObject, "id");
                String title = JsonUtil.tryGetString(jsonObject, "title");
                JsonArray textsArray = jsonObject.getAsJsonArray("papers");

                List<DiaryPaper> diaryPaperList = new ArrayList<>();
                textsArray.forEach(jsonElement -> diaryPaperList.add(DiaryManager.getDiaryValue(jsonElement.getAsString())));

                DiaryManager.bookRegister(id == null ? key.toString() : id, new Diary(title, diaryPaperList.toArray(DiaryPaper[]::new)));
            } catch (NullPointerException e) {
                DiaryMod.LOGGER.error("[DiaryReloadListener] Failed to load diary: {} - {}", key, e.getMessage(), e);
            }
        }
    }
}

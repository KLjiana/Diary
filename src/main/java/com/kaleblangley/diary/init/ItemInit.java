package com.kaleblangley.diary.init;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.item.DiaryItem;
import com.kaleblangley.diary.item.DiaryPaperItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, DiaryMod.MODID);
    public static final RegistryObject<Item> DIARY_PAPER = ITEM.register("diary_paper", DiaryPaperItem::new);
    public static final RegistryObject<Item> DIARY = ITEM.register("diary", DiaryItem::new);
}

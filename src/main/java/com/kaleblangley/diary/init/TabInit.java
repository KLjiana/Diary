package com.kaleblangley.diary.init;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.data.DiaryManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class TabInit {
    public static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DiaryMod.MODID);
    public static final RegistryObject<CreativeModeTab> DIARY_TAB = TAB.register("diary", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.diary"))
            .icon(()-> new ItemStack(ItemInit.DIARY.get()))
            .displayItems((parameters, output) -> {
                List<ItemStack> itemStacks = new ArrayList<>();
                output.accept(ItemInit.DIARY_PAPER.get().getDefaultInstance());
                for (String id : DiaryManager.getDiaryMapView()) {
                    ItemStack itemStack = ItemInit.DIARY_PAPER.get().getDefaultInstance();
                    itemStack.addTagElement("diary_id", StringTag.valueOf(id));
                    itemStacks.add(itemStack);
                }
                output.accept(ItemInit.DIARY.get().getDefaultInstance());
                for (String type : DiaryManager.getTypeMapView()) {
                    ItemStack itemStack = ItemInit.DIARY.get().getDefaultInstance();
                    itemStack.addTagElement("diary_type", StringTag.valueOf(type));
                    itemStacks.add(itemStack);
                }
                output.accept(ItemInit.COVER.get().getDefaultInstance());
                output.acceptAll(itemStacks);
            })
            .build()
    );
}

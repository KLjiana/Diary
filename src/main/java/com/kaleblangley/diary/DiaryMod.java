package com.kaleblangley.diary;

import com.kaleblangley.diary.diary.data.DiaryManager;
import com.kaleblangley.diary.init.ItemInit;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Mod(DiaryMod.MODID)
public class DiaryMod {
    public static final String MODID = "diary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public DiaryMod(){
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEM.register(modBusEvent);
    }
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemInit.DIARY.get());
        }

        @Override
        public void fillItemList(@NotNull NonNullList<ItemStack> itemStackNonNullList) {
            List<ItemStack> itemStacks = new ArrayList<>();
            itemStackNonNullList.add(ItemInit.DIARY_PAPER.get().getDefaultInstance());
            for (String id : DiaryManager.getDiaryMapView()) {
                ItemStack itemStack = ItemInit.DIARY_PAPER.get().getDefaultInstance();
                itemStack.addTagElement("diary_id", StringTag.valueOf(id));
                itemStacks.add(itemStack);
            }
            itemStackNonNullList.add(ItemInit.DIARY.get().getDefaultInstance());
            for (String type : DiaryManager.getTypeMapView()) {
                ItemStack itemStack = ItemInit.DIARY.get().getDefaultInstance();
                itemStack.addTagElement("diary_type", StringTag.valueOf(type));
                itemStacks.add(itemStack);
            }
            itemStackNonNullList.addAll(itemStacks);
        }
    };
}

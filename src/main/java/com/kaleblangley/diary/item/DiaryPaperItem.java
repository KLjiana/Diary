package com.kaleblangley.diary.item;

import com.kaleblangley.diary.client.screen.DiaryScreen;
import com.kaleblangley.diary.diary.Diary;
import com.kaleblangley.diary.diary.data.DiaryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DiaryPaperItem extends Item {
    public DiaryPaperItem() {
        super(new Item.Properties());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(usedHand);
            Diary diary = getDiary(itemStack);
            if (diary != null) {
                Minecraft.getInstance().setScreen(new DiaryScreen(diary));
                return InteractionResultHolder.success(itemStack);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Diary diary = getDiary(stack);
        if (diary!=null){
            return new TranslatableComponent(diary.title);
        }
        return super.getName(stack);
    }

    public Diary getDiary(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            return DiaryManager.getDiaryValue(tag.getString("diary_id"));
        }
        return null;
    }
}

package com.kaleblangley.diary.item;

import com.kaleblangley.diary.client.screen.DiaryScreen;
import com.kaleblangley.diary.diary.DiaryPaper;
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
            DiaryPaper diaryPaper = getDiary(itemStack);
            if (diaryPaper != null) {
                Minecraft.getInstance().setScreen(new DiaryScreen(diaryPaper));
                return InteractionResultHolder.success(itemStack);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        DiaryPaper diaryPaper = getDiary(stack);
        if (diaryPaper !=null){
            return new TranslatableComponent(diaryPaper.title());
        }
        return super.getName(stack);
    }

    public DiaryPaper getDiary(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            return DiaryManager.getDiaryValue(tag.getString("diary_id"));
        }
        return null;
    }
}

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DiaryItem extends Item {
    public DiaryItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(usedHand);
            return openScreen(itemStack, player, usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @OnlyIn(Dist.CLIENT)
    private InteractionResultHolder<ItemStack> openScreen(ItemStack itemStack, Player player, InteractionHand usedHand) {
        Diary diaries = getDiaries(itemStack);
        if (diaries!=null) {
            String[] texts = Arrays.stream(diaries.diaryPapers())
                    .flatMap(diary -> Arrays.stream(diary.texts()))
                    .toArray(String[]::new);
            Minecraft.getInstance().setScreen(new DiaryScreen(texts));
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Diary diaryPaper = getDiaries(stack);
        if (diaryPaper !=null) {
            return new TranslatableComponent(diaryPaper.title());
        }
        return super.getName(stack);
    }

    public String getType(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            return tag.getString("diary_type");
        }
        return "";
    }

    public Diary getDiaries(ItemStack itemStack) {
        return DiaryManager.getTypeValue(getType(itemStack));
    }
}

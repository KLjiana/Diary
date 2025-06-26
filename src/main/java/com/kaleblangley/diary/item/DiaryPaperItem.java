package com.kaleblangley.diary.item;

import com.kaleblangley.diary.client.screen.DiaryScreen;
import com.kaleblangley.diary.diary.DiaryPaper;
import com.kaleblangley.diary.diary.data.DiaryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DiaryPaperItem extends Item {
    public DiaryPaperItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            return openScreen(player.getItemInHand(usedHand), player, usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @OnlyIn(Dist.CLIENT)
    private InteractionResultHolder<ItemStack> openScreen(ItemStack itemStack, Player player, InteractionHand usedHand) {
        DiaryPaper diaryPaper = getDiary(itemStack);
        if (diaryPaper != null) {
            Minecraft.getInstance().setScreen(new DiaryScreen(diaryPaper));
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        DiaryPaper diaryPaper = getDiary(stack);
        if (diaryPaper !=null){
            return Component.translatable(diaryPaper.title());
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

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.contains("CustomModelData", Tag.TAG_INT)) {
                int variant = level.random.nextInt(3) + 1;
                tag.putInt("CustomModelData", variant);
            }
        }
    }
}

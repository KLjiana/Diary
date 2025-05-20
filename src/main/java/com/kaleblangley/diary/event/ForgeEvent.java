package com.kaleblangley.diary.event;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.data.DiaryLoader;
import com.kaleblangley.diary.diary.data.DiaryPaperLoader;
import com.kaleblangley.diary.util.StructureLocator;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = DiaryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void dataListener(AddReloadListenerEvent event) {
        event.addListener(new DiaryPaperLoader());
        event.addListener(new DiaryLoader());
    }

    @SubscribeEvent
    public static void chatReplace(ServerChatEvent event) {
        String message = event.getMessage();
        ServerPlayer serverPlayer = event.getPlayer();
        if (message.contains("<structure>") && message.contains("</structure>")) {
            String structure = message.replaceAll(".*<structure>(.*?)</structure>.*", "$1");
            BlockPos center = serverPlayer.blockPosition();
            Optional<Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> holderPair = StructureLocator.findNearest(
                    serverPlayer.getLevel(),
                    center,
                    new ResourceLocation(structure),
                    100,
                    false
            );
            AtomicReference<String> replace = new AtomicReference<>();
            holderPair.ifPresentOrElse(pair -> {
                Component component = StructureLocator.formatLocateResult(structure, center, pair, "commands.locate.success");
                replace.set(component.getString());
            }, () -> {
                replace.set(new TranslatableComponent("commands.locate.failed", structure).getString());
            });
            Style style = event.getComponent().getStyle();
            MutableComponent mutableComponent = new TextComponent(message.replaceAll("<structure>(.*?)</structure>", "§a%s§r".formatted(replace.get()))).withStyle(style);
            event.setComponent(mutableComponent);
        }
    }
}

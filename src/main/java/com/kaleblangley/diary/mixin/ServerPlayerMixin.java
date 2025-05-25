package com.kaleblangley.diary.mixin;

import com.kaleblangley.diary.util.StructureLocator;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @ModifyArg(method = "sendMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundChatPacket;<init>(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"), index = 0)
    public Component modifyMessage(Component component){
        MutableComponent mutableComponent = component.copy();
        String message = mutableComponent.getString();
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
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
                Component component1 = StructureLocator.formatLocateResult(structure, center, pair, "message.diary.locate");
                replace.set(component1.getString());
            }, () -> {
                replace.set(new TranslatableComponent("commands.locate.failed", structure).getString());
            });
            Style style = component.getStyle();
            mutableComponent = new TextComponent(message.replaceAll("<structure>(.*?)</structure>", "§a%s§r".formatted(replace.get()))).withStyle(style);
        }
        return mutableComponent;
    }
}

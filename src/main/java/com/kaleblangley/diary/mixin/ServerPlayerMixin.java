package com.kaleblangley.diary.mixin;

import com.kaleblangley.diary.util.StructureLocator;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @ModifyArg(method = "sendSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;<init>(Lnet/minecraft/network/chat/Component;Z)V"), index = 0)
    public Component modifyMessage(Component component){
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        return StructureLocator.modifyMessage(component, serverPlayer);
    }
}

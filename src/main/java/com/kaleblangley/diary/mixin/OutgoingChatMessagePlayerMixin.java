package com.kaleblangley.diary.mixin;

import com.kaleblangley.diary.mixin.accessor.PlayerChatMessageAccessor;
import com.kaleblangley.diary.mixin.accessor.SignedMessageBodyAccessor;
import com.kaleblangley.diary.util.StructureLocator;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OutgoingChatMessage.Player.class)
public class OutgoingChatMessagePlayerMixin {
    @ModifyArg(method = "sendToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;sendPlayerChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/ChatType$Bound;)V"), index = 0)
    public PlayerChatMessage modifyMessage(PlayerChatMessage chatMessage, @Local(argsOnly = true) ServerPlayer serverPlayer){
        if (chatMessage.unsignedContent() != null) {
            ((PlayerChatMessageAccessor) (Object) chatMessage).setUnsignedContent(StructureLocator.modifyMessage(chatMessage.unsignedContent(), serverPlayer));
        } else {
            ((SignedMessageBodyAccessor) (Object) chatMessage.signedBody()).setContent(StructureLocator.modifyMessage(Component.literal(chatMessage.signedBody().content()), serverPlayer).getString());
        }
        return chatMessage;
    }
}

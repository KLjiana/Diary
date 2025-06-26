package com.kaleblangley.diary.mixin.accessor;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerChatMessage.class)
public interface PlayerChatMessageAccessor {
    @Mutable
    @Accessor("unsignedContent")
    void setUnsignedContent(Component component);
}

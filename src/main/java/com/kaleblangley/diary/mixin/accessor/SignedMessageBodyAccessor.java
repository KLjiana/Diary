package com.kaleblangley.diary.mixin.accessor;

import net.minecraft.network.chat.SignedMessageBody;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SignedMessageBody.class)
public interface SignedMessageBodyAccessor {
    @Mutable
    @Accessor("content")
    void setContent(String content);
}

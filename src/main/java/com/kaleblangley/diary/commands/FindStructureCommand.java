package com.kaleblangley.diary.commands;

import com.kaleblangley.diary.util.StructureLocator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class FindStructureCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("findstructure").requires(stack -> stack.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entity())
                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                .executes(commandContext -> {
                                    var entity = EntityArgument.getEntity(commandContext, "targets");
                                    var id = ResourceLocationArgument.getId(commandContext, "id");
                                    return tellStructure(entity, id, "%s");
                                })
                                .then(Commands.argument("text", StringArgumentType.string()).executes(commandContext -> {
                                            var entity = EntityArgument.getEntity(commandContext, "targets");
                                            var id = ResourceLocationArgument.getId(commandContext, "id");
                                            var string = StringArgumentType.getString(commandContext, "text");
                                            return tellStructure(entity, id, string);
                                        })
                                )
                        )
                )
        );
    }

    private static int tellStructure(Entity entity, ResourceLocation id, String appendText) {
        if (entity instanceof ServerPlayer serverPlayer) {
            BlockPos center = serverPlayer.blockPosition();
            Optional<Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> holderPair = StructureLocator.findNearest(
                    serverPlayer.getLevel(),
                    center,
                    id,
                    100,
                    false
            );
            AtomicReference<Component> replace = new AtomicReference<>();
            holderPair.ifPresentOrElse(pair -> {
                replace.set(StructureLocator.formatLocateResult(center, pair, "message.diary.locate"));
            }, () -> {
                replace.set(new TranslatableComponent("commands.locate.failed", id.toString()));
            });
            serverPlayer.sendMessage(new TranslatableComponent(appendText, replace.get()), Util.NIL_UUID);
        }
        return 0;
    }
}

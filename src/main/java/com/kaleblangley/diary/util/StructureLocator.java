package com.kaleblangley.diary.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class StructureLocator {
    private static final ResourceKey<Registry<Structure>> STRUCTURE_REGISTRY_KEY =
            Registries.STRUCTURE;

    /**
     * 根据单个结构 ResourceLocation（如 "minecraft:village" 或者自定义 mod:id）查找最近的那个点。
     *
     * @param level     当前维度
     * @param center    中心搜索点
     * @param id        结构的 ResourceLocation
     * @param radius    搜索半径（方块数）
     * @param skipKnown 是否跳过已探索过的结构
     * @return 如果找到，返回 Pair(结构坐标, Holder<该结构>); 找不到则 empty()
     */
    public static Optional<Pair<BlockPos, Holder<Structure>>> findNearest(
            ServerLevel level,
            BlockPos center,
            ResourceLocation id,
            int radius,
            boolean skipKnown
    ) {
        Registry<Structure> registry =
                level.registryAccess().registryOrThrow(STRUCTURE_REGISTRY_KEY);

        ResourceKey<Structure> key =
                ResourceKey.create(STRUCTURE_REGISTRY_KEY, id);

        Holder<Structure> holder =
                registry.getHolder(key).orElse(null);
        if (holder == null) return Optional.empty();

        HolderSet<Structure> holderSet = HolderSet.direct(holder);
        Pair<BlockPos, Holder<Structure>> result =
                level.getChunkSource()
                        .getGenerator()
                        .findNearestMapStructure(level, holderSet, center, radius, skipKnown);

        return Optional.ofNullable(result);
    }

    /**
     * 根据 TagKey（像 "#minecraft:village"）来查找最近的结构。
     *
     * @param level     当前维度
     * @param center    中心搜索点
     * @param tagKey    结构 TagKey（Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY 下的 Tag）
     * @param radius    搜索半径
     * @param skipKnown 是否跳过已探索结构
     * @return 如果找到，返回 Pair(结构坐标, Holder<该结构>); 找不到则 empty()
     */
    public static Optional<Pair<BlockPos, Holder<Structure>>> findNearestByTag(
            ServerLevel level,
            BlockPos center,
            TagKey<Structure> tagKey,
            int radius,
            boolean skipKnown
    ) {
        Registry<Structure> registry =
                level.registryAccess().registryOrThrow(STRUCTURE_REGISTRY_KEY);

        HolderSet<Structure> holderSet =
                registry.getOrCreateTag(tagKey);
        Pair<BlockPos, Holder<Structure>> result =
                level.getChunkSource()
                        .getGenerator()
                        .findNearestMapStructure(level, holderSet, center, radius, skipKnown);

        return Optional.ofNullable(result);
    }

    /**
     * 把查到的结果格式化成一个聊天用的组件（绿色坐标 + 距离）。
     *
     * @param structureName 你想显示的结构名称（如 "minecraft:village" 或 "#minecraft:village"）
     * @param origin        搜索中心
     * @param pair          findNearest 返回的 Pair
     * @param translateKey  翻译 key，通常用 "commands.locate.success"
     */
    public static Component formatLocateResult(
            String structureName,
            BlockPos origin,
            Pair<BlockPos, Holder<Structure>> pair,
            String translateKey
    ) {
        BlockPos found = pair.getFirst();
        int distance = Mth.floor(
                dist(origin.getX(), origin.getZ(), found.getX(), found.getZ())
        );

        MutableComponent coords = ComponentUtils.wrapInSquareBrackets(
                        Component.translatable("chat.coordinates", found.getX(), "~", found.getZ())
                );

        return Component.translatable(
                translateKey, coords, distance
        );
    }

    private static float dist(int x1, int z1, int x2, int z2) {
        int dx = x2 - x1, dz = z2 - z1;
        return Mth.sqrt((float) (dx * dx + dz * dz));
    }

    public static Component modifyMessage(Component component, ServerPlayer serverPlayer){
        MutableComponent mutableComponent = component.copy();
        String message = mutableComponent.getString();
        if (message.contains("<structure>") && message.contains("</structure>")) {
            String structure = message.replaceAll(".*<structure>(.*?)</structure>.*", "$1");
            BlockPos center = serverPlayer.blockPosition();
            Optional<Pair<BlockPos, Holder<Structure>>> holderPair = StructureLocator.findNearest(
                    serverPlayer.serverLevel(),
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
                replace.set(Component.translatable("commands.locate.failed", structure).getString());
            });
            Style style = component.getStyle();
            mutableComponent = Component.literal(message.replaceAll("<structure>(.*?)</structure>", "§a%s§r".formatted(replace.get()))).withStyle(style);
        }
        return mutableComponent;
    }
}
package com.kaleblangley.diary.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import java.util.Optional;

public class StructureLocator {
    private static final ResourceKey<Registry<ConfiguredStructureFeature<?, ?>>> STRUCTURE_REGISTRY_KEY =
            Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY;

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
    public static Optional<Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> findNearest(
            ServerLevel level,
            BlockPos center,
            ResourceLocation id,
            int radius,
            boolean skipKnown
    ) {
        Registry<ConfiguredStructureFeature<?, ?>> registry =
                level.registryAccess().registryOrThrow(STRUCTURE_REGISTRY_KEY);

        ResourceKey<ConfiguredStructureFeature<?, ?>> key =
                ResourceKey.create(STRUCTURE_REGISTRY_KEY, id);

        Holder<ConfiguredStructureFeature<?, ?>> holder =
                registry.getHolder(key).orElse(null);
        if (holder == null) return Optional.empty();

        HolderSet<ConfiguredStructureFeature<?, ?>> holderSet = HolderSet.direct(holder);
        Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> result =
                level.getChunkSource()
                        .getGenerator()
                        .findNearestMapFeature(level, holderSet, center, radius, skipKnown);

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
    public static Optional<Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> findNearestByTag(
            ServerLevel level,
            BlockPos center,
            TagKey<ConfiguredStructureFeature<?, ?>> tagKey,
            int radius,
            boolean skipKnown
    ) {
        Registry<ConfiguredStructureFeature<?, ?>> registry =
                level.registryAccess().registryOrThrow(STRUCTURE_REGISTRY_KEY);

        HolderSet<ConfiguredStructureFeature<?, ?>> holderSet =
                registry.getOrCreateTag(tagKey);
        Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> result =
                level.getChunkSource()
                        .getGenerator()
                        .findNearestMapFeature(level, holderSet, center, radius, skipKnown);

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
            Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair,
            String translateKey
    ) {
        BlockPos found = pair.getFirst();
        int distance = Mth.floor(
                dist(origin.getX(), origin.getZ(), found.getX(), found.getZ())
        );

        MutableComponent coords = ComponentUtils.wrapInSquareBrackets(
                        new TranslatableComponent("chat.coordinates", found.getX(), "~", found.getZ())
                );

        return new TranslatableComponent(
                translateKey, coords, distance
        );
    }

    private static float dist(int x1, int z1, int x2, int z2) {
        int dx = x2 - x1, dz = z2 - z1;
        return Mth.sqrt((float) (dx * dx + dz * dz));
    }
}
package com.kaleblangley.diary.event;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.data.DiaryLoader;
import com.kaleblangley.diary.diary.data.DiaryManager;
import com.kaleblangley.diary.diary.data.DiaryPaperLoader;
import com.kaleblangley.diary.network.Network;
import com.kaleblangley.diary.network.s2c.SyncDiaryDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DiaryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void dataListener(AddReloadListenerEvent event) {
        event.addListener(new DiaryPaperLoader());
        event.addListener(new DiaryLoader());
    }

    @SubscribeEvent
    public static void syncData(OnDatapackSyncEvent event){
        if (event.getPlayer() == null) {
            PlayerList playerList = event.getPlayerList();
            for (ServerPlayer serverPlayer : playerList.getPlayers()) {
                Network.CHANNEL.send(PacketDistributor.PLAYER.with(()->serverPlayer),
                        new SyncDiaryDataPacket(DiaryManager.getPaperMap(), DiaryManager.getBookMap()));
            }
        } else {
            Network.CHANNEL.send(PacketDistributor.PLAYER.with(event::getPlayer),
                    new SyncDiaryDataPacket(DiaryManager.getPaperMap(), DiaryManager.getBookMap()));
        }
    }
}

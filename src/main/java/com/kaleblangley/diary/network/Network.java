package com.kaleblangley.diary.network;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.network.s2c.SyncDiaryDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DiaryMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++, SyncDiaryDataPacket.class, SyncDiaryDataPacket::encode, SyncDiaryDataPacket::decode, SyncDiaryDataPacket::handle);
    }
}

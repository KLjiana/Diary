package com.kaleblangley.diary.event;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.data.DiaryLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DiaryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void dataListener(AddReloadListenerEvent event){
        event.addListener(new DiaryLoader());
    }
}

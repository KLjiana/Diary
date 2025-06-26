package com.kaleblangley.diary;

import com.kaleblangley.diary.init.ItemInit;
import com.kaleblangley.diary.init.TabInit;
import com.kaleblangley.diary.network.Network;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(DiaryMod.MODID)
public class DiaryMod {
    public static final String MODID = "diary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public DiaryMod(){
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEM.register(modBusEvent);
        TabInit.TAB.register(modBusEvent);
        Network.register();
    }
}

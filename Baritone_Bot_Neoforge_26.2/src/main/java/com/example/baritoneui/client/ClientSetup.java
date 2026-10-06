package com.example.baritoneui.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.api.distmarker.Dist;
import com.example.baritoneui.BaritoneUIMod;

public class ClientSetup {

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::registerKeyMappings);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_UI);
    }

    @EventBusSubscriber(modid = BaritoneUIMod.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            KeyBindings.tick();
        }
    }
}

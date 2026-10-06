package com.example.baritoneui;

import com.example.baritoneui.client.KeyBindings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = BaritoneUIMod.MOD_ID, dist = Dist.CLIENT)
public class BaritoneUIMod {
    public static final String MOD_ID = "baritoneui";

    public BaritoneUIMod(IEventBus modEventBus) {
        modEventBus.addListener(BaritoneUIMod::onRegisterKeys);
        NeoForge.EVENT_BUS.addListener(BaritoneUIMod::onClientTick);
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.registerCategory(KeyBindings.CATEGORY);
        event.register(KeyBindings.OPEN_UI);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        KeyBindings.tick();
    }
}

package com.example.baritoneui;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;
import com.example.baritoneui.client.ClientSetup;

@Mod(value = BaritoneUIMod.MOD_ID, dist = Dist.CLIENT)
public class BaritoneUIMod {

    public static final String MOD_ID = "baritoneui";

    public BaritoneUIMod(IEventBus modEventBus) {
        ClientSetup.init(modEventBus);
    }
}

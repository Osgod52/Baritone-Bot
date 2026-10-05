package com.example.baritoneui;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;
import com.example.baritoneui.client.ClientSetup;

/**
 * Entry point for the Baritone UI addon.
 * <p>
 * This mod does NOT depend on Baritone's code at compile time. It only opens
 * a Screen full of buttons that, when clicked, send the same chat messages
 * you would otherwise type by hand (e.g. "#goto 100 64 200"). Baritone reads
 * chat messages prefixed with its command prefix (default '#') client-side,
 * so this works purely by simulating that chat input.
 */
@Mod(value = BaritoneUIMod.MOD_ID, dist = Dist.CLIENT)
public class BaritoneUIMod {

    public static final String MOD_ID = "baritoneui";

    public BaritoneUIMod(IEventBus modEventBus) {
        ClientSetup.init(modEventBus);
    }
}

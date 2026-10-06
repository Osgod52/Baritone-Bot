package com.example.baritoneui;

import com.example.baritoneui.client.KeyBindings;
import net.fabricmc.api.ClientModInitializer;


public class BaritoneUIMod implements ClientModInitializer {
    public static final String MOD_ID = "baritoneui";

    @Override
    public void onInitializeClient() {
        KeyBindings.register();
    }
}

package com.example.baritoneui.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import com.example.baritoneui.BaritoneUIMod;
import com.example.baritoneui.client.gui.BaritoneUIScreen;

public final class KeyBindings {

    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(BaritoneUIMod.MOD_ID, "main")
    );

    
    public static final KeyMapping OPEN_UI = new KeyMapping(
            "key.baritoneui.open",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_B,
            CATEGORY
    );

    private KeyBindings() {}

    
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        while (OPEN_UI.consumeClick()) {
            if (mc.gui.screen() == null) {
                mc.gui.setScreen(new BaritoneUIScreen());
            }
        }
    }
}

package com.example.baritoneui.client;

import com.example.baritoneui.BaritoneUIMod;
import com.example.baritoneui.client.gui.BaritoneUIScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    private KeyBindings() {}

        public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath(BaritoneUIMod.MOD_ID, "main")
    );

    public static final KeyMapping OPEN_UI = new KeyMapping(
            "key.baritoneui.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY
    );

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        while (OPEN_UI.consumeClick()) {
            if (mc.screen == null && mc.player != null) {
                mc.setScreen(new BaritoneUIScreen());
            }
        }
    }
}

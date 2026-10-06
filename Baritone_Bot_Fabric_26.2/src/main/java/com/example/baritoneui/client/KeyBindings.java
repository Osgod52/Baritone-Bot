package com.example.baritoneui.client;

import com.example.baritoneui.client.gui.BaritoneUIScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("baritoneui", "baritoneui")
    );

    public static final KeyMapping OPEN_UI = new KeyMapping(
            "key.baritoneui.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
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

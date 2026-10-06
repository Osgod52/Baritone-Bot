package com.example.baritoneui.client;

import com.example.baritoneui.BaritoneUIMod;
import com.example.baritoneui.client.gui.BaritoneUIScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    private KeyBindings() {}

    
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(BaritoneUIMod.MOD_ID, "main")
    );

    public static KeyMapping OPEN_UI;

    public static void register() {
        OPEN_UI = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.baritoneui.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(KeyBindings::tick);
    }

    private static void tick(Minecraft mc) {
        while (OPEN_UI.consumeClick()) {
            if (mc.screen == null && mc.player != null) {
                mc.setScreen(new BaritoneUIScreen());
            }
        }
    }
}

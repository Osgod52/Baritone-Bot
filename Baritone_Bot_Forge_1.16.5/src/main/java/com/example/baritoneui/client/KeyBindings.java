package com.example.baritoneui.client;

import com.example.baritoneui.client.gui.BaritoneUIScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String CATEGORY = "key.categories.baritoneui";

    public static final KeyBinding OPEN_UI = new KeyBinding(
            "key.baritoneui.open",
            GLFW.GLFW_KEY_B,
            CATEGORY
    );

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        if (OPEN_UI.consumeClick() && mc.screen == null) {
            mc.setScreen(new BaritoneUIScreen());
        }
    }
}
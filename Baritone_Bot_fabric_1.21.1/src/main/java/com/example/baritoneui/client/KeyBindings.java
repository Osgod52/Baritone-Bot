package com.example.baritoneui.client;

import com.example.baritoneui.client.gui.BaritoneUIScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    private static final String CATEGORY = "key.categories.baritoneui";

    public static final KeyBinding OPEN_UI = new KeyBinding(
            "key.baritoneui.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY
    );

    private KeyBindings() {}

    public static void tick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        while (OPEN_UI.wasPressed()) {
            if (mc.currentScreen == null) {
                mc.setScreen(new BaritoneUIScreen());
            }
        }
    }
}

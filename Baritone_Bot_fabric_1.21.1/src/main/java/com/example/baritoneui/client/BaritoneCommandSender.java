package com.example.baritoneui.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;


public final class BaritoneCommandSender {
    private static final String PREFIX = "#";

    private BaritoneCommandSender() {}

    public static void send(String command) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        String fullMessage = PREFIX + command.trim();
        player.networkHandler.sendChatMessage(fullMessage);
    }

    public static void send(String command, String... args) {
        StringBuilder sb = new StringBuilder(command);
        for (String arg : args) {
            if (arg == null || arg.isBlank()) continue;
            sb.append(' ').append(arg.trim());
        }
        send(sb.toString());
    }
}

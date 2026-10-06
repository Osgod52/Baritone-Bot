package com.example.baritoneui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class BaritoneCommandSender {
    private static final String PREFIX = "#";

    private BaritoneCommandSender() {}

    public static void send(String command) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || player.connection == null) {
            return;
        }

        String fullMessage = PREFIX + command.trim();

        
        
        player.connection.sendChat(fullMessage);
    }

    public static void send(String command, String... args) {
        StringBuilder sb = new StringBuilder(command);

        for (String arg : args) {
            if (arg == null || arg.trim().isEmpty()) {
                continue;
            }

            sb.append(' ').append(arg.trim());
        }

        send(sb.toString());
    }
}

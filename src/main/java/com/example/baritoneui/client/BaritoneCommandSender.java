package com.example.baritoneui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Sends a raw chat line the same way pressing Enter in the chat box would.
 * Baritone (like most client-side command mods) hooks the outgoing chat
 * pipeline and intercepts any message starting with its configured prefix
 * (default '#'), so it never actually reaches the server as a real chat
 * message. This class doesn't know or care whether Baritone is installed -
 * it just reproduces exactly what typing the command by hand would send.
 */
public final class BaritoneCommandSender {

    /** Baritone's default chat control prefix. Change here if you've changed
     *  Baritone's "prefix" setting away from the default. */
    private static final String PREFIX = "#";

    private BaritoneCommandSender() {}

    /**
     * Sends "#{command}" through the normal chat pipeline.
     * @param command everything after the '#', e.g. "goto 100 64 200"
     */
    public static void send(String command) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        String fullMessage = PREFIX + command.trim();

        if (player.connection != null) {
            player.connection.sendChat(fullMessage);
        }
    }

    /** Convenience for commands with no arguments, e.g. send("cancel"). */
    public static void send(String command, String... args) {
        StringBuilder sb = new StringBuilder(command);
        for (String arg : args) {
            if (arg == null || arg.isBlank()) continue;
            sb.append(' ').append(arg.trim());
        }
        send(sb.toString());
    }
}

package com.example.baritoneui.client.gui;

import com.example.baritoneui.client.BaritoneCommandSender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.util.LinkedList;

public class BaritoneUIScreen extends Screen {

    private enum Tab {
        MOVEMENT("Movement"),
        MINING("Mining"),
        BUILDING("Building"),
        WAYPOINTS("Waypoints"),
        SETTINGS("Settings"),
        CONSOLE("Console");

        final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    private static final LinkedList<String> HISTORY = new LinkedList<>();
    private static final int HISTORY_MAX = 4;

    private static final int BASE_PANEL_WIDTH = 360;
    private static final int BASE_PANEL_HEIGHT = 300;

    private Tab currentTab = Tab.MOVEMENT;

    private float scale = 1f;
    private int panelWidth;
    private int panelHeight;
    private int panelX;
    private int panelY;

    private TextFieldWidget gotoX, gotoY, gotoZ;
    private TextFieldWidget followEntity;
    private TextFieldWidget thiswayDistance;
    private TextFieldWidget mineBlocks;
    private TextFieldWidget farmRadius;
    private TextFieldWidget buildSchematic;
    private TextFieldWidget tunnelH, tunnelW, tunnelD;
    private TextFieldWidget selFillBlock;
    private TextFieldWidget waypointName;
    private TextFieldWidget consoleInput;

    public BaritoneUIScreen() {
        super(StringTextComponent.EMPTY);
    }

    @Override
    protected void init() {
        int margin = 20;
        float scaleW = (this.width - margin) / (float) BASE_PANEL_WIDTH;
        float scaleH = (this.height - margin) / (float) BASE_PANEL_HEIGHT;
        scale = Math.min(1f, Math.min(scaleW, scaleH));
        scale = Math.max(scale, 0.5f);

        panelWidth = Math.round(BASE_PANEL_WIDTH * scale);
        panelHeight = Math.round(BASE_PANEL_HEIGHT * scale);
        panelX = (this.width - panelWidth) / 2;
        panelY = (this.height - panelHeight) / 2;

        int tabCount = Tab.values().length;
        int tabWidthBase = BASE_PANEL_WIDTH / tabCount;
        int txBase = 0;

        for (final Tab tab : Tab.values()) {
            int fxBase = txBase;
            this.addButton(new Button(
                    x(fxBase), y(0), s(tabWidthBase - 2), s(20),
                    new StringTextComponent(tab.label),
                    b -> switchTab(tab)
            ));
            txBase += tabWidthBase;
        }

        int bottomYBase = BASE_PANEL_HEIGHT - 24;

        this.addButton(new Button(
                x(0), y(bottomYBase), s(80), s(20),
                new StringTextComponent("Pause"), b -> send("pause")
        ));

        this.addButton(new Button(
                x(84), y(bottomYBase), s(80), s(20),
                new StringTextComponent("Resume"), b -> send("resume")
        ));

        this.addButton(new Button(
                x(168), y(bottomYBase), s(100), s(20),
                new StringTextComponent("Cancel/Stop"), b -> send("cancel")
        ));

        this.addButton(new Button(
                x(BASE_PANEL_WIDTH - 60), y(bottomYBase), s(60), s(20),
                new StringTextComponent("Close"), b -> this.minecraft.setScreen(null)
        ));

        buildTabContent();
    }

    private int x(int baseOffsetFromPanel) {
        return panelX + Math.round(baseOffsetFromPanel * scale);
    }

    private int y(int baseOffsetFromPanel) {
        return panelY + Math.round(baseOffsetFromPanel * scale);
    }

    private int s(int baseSize) {
        return Math.max(1, Math.round(baseSize * scale));
    }

    private void switchTab(Tab tab) {
        this.currentTab = tab;
        this.init(this.minecraft, this.width, this.height);
    }

    private TextFieldWidget field(int xBase, int yBase, int wBase, String hint) {
        TextFieldWidget box = new TextFieldWidget(
                this.font,
                x(xBase),
                y(yBase),
                s(wBase),
                s(18),
                new StringTextComponent("")
        );

        box.setMaxLength(64);
        box.setSuggestion(hint);

        this.addButton(box);
        return box;
    }

    private Button btn(String label, Runnable action, int xBase, int yBase, int wBase, int hBase) {
        return this.addButton(new Button(
                x(xBase),
                y(yBase),
                s(wBase),
                s(hBase),
                new StringTextComponent(label),
                b -> action.run()
        ));
    }

    private void buildTabContent() {
        int contentY = 26;
        int col1 = 8;

        switch (currentTab) {
            case MOVEMENT:
                gotoX = field(col1, contentY + 18, 50, "x");
                gotoY = field(col1 + 54, contentY + 18, 50, "y");
                gotoZ = field(col1 + 108, contentY + 18, 50, "z");

                btn(
                        "Goto XYZ",
                        () -> send("goto " + gotoX.getValue() + " " + gotoY.getValue() + " " + gotoZ.getValue()),
                        col1 + 164, contentY + 18, 100, 18
                );

                btn("Path (to cursor)", () -> send("path"),
                        col1, contentY + 42, 150, 18);

                btn("Come to me", () -> send("come"),
                        col1 + 154, contentY + 42, 110, 18);

                followEntity = field(col1, contentY + 68, 120, "player/entity");

                btn("Follow", () -> send("follow " + followEntity.getValue()),
                        col1 + 124, contentY + 68, 80, 18);

                btn("Follow players", () -> send("follow players"),
                        col1 + 208, contentY + 68, 130, 18);

                thiswayDistance = field(col1, contentY + 94, 80, "blocks");

                btn("This way", () -> send("thisway " + thiswayDistance.getValue()),
                        col1 + 84, contentY + 94, 90, 18);

                btn("Explore", () -> send("explore"),
                        col1 + 178, contentY + 94, 90, 18);

                btn("Top (surface up)", () -> send("top"),
                        col1, contentY + 120, 150, 18);

                btn("Surface", () -> send("surface"),
                        col1 + 154, contentY + 120, 110, 18);

                btn("Invert goal", () -> send("invert"),
                        col1, contentY + 146, 264, 18);
                break;

            case MINING:
                mineBlocks = field(col1, contentY + 18, 180, "blocks (space sep)");

                btn("Mine", () -> send("mine " + mineBlocks.getValue()),
                        col1 + 184, contentY + 18, 80, 18);

                farmRadius = field(col1, contentY + 44, 80, "radius");

                btn("Farm", () -> {
                    String r = farmRadius.getValue();
                    send(r.trim().isEmpty() ? "farm" : "farm " + r);
                }, col1 + 84, contentY + 44, 90, 18);

                btn("Stop farming", () -> send("cancel"),
                        col1 + 178, contentY + 44, 90, 18);

                btn("Blacklist current mine target", () -> send("blacklist"),
                        col1, contentY + 70, 264, 18);

                btn("Click block to select mine target", () -> send("click"),
                        col1, contentY + 96, 264, 18);
                break;

            case BUILDING:
                buildSchematic = field(col1, contentY + 18, 180, "schematic file name");

                btn("Build", () -> send("build " + buildSchematic.getValue()),
                        col1 + 184, contentY + 18, 80, 18);

                tunnelH = field(col1, contentY + 44, 50, "height");
                tunnelW = field(col1 + 54, contentY + 44, 50, "width");
                tunnelD = field(col1 + 108, contentY + 44, 50, "depth");

                btn("Tunnel", () -> {
                    String h = tunnelH.getValue();
                    String w = tunnelW.getValue();
                    String d = tunnelD.getValue();

                    StringBuilder cmd = new StringBuilder("tunnel");

                    if (!h.trim().isEmpty()) cmd.append(' ').append(h);
                    if (!w.trim().isEmpty()) cmd.append(' ').append(w);
                    if (!d.trim().isEmpty()) cmd.append(' ').append(d);

                    send(cmd.toString());
                }, col1 + 164, contentY + 44, 100, 18);

                btn("Sel Pos1", () -> send("sel pos1"),
                        col1, contentY + 70, 84, 18);

                btn("Sel Pos2", () -> send("sel pos2"),
                        col1 + 88, contentY + 70, 84, 18);

                btn("Sel Clear", () -> send("sel clear"),
                        col1 + 176, contentY + 70, 88, 18);

                selFillBlock = field(col1, contentY + 96, 150, "block to fill with");

                btn("Sel Fill", () -> send("sel fill " + selFillBlock.getValue()),
                        col1 + 154, contentY + 96, 110, 18);

                btn("Sel Copy", () -> send("sel copy"),
                        col1, contentY + 122, 130, 18);

                btn("Sel Paste", () -> send("sel paste"),
                        col1 + 134, contentY + 122, 130, 18);
                break;

            case WAYPOINTS:
                btn("Set Home Here", () -> send("sethome"),
                        col1, contentY + 18, 130, 18);

                btn("Goto Home", () -> send("home"),
                        col1 + 134, contentY + 18, 130, 18);

                waypointName = field(col1, contentY + 44, 150, "waypoint name");

                btn("Save", () -> send("waypoints save " + waypointName.getValue()),
                        col1 + 154, contentY + 44, 55, 18);

                btn("Goto", () -> send("waypoints goto " + waypointName.getValue()),
                        col1 + 211, contentY + 44, 53, 18);

                btn("List waypoints", () -> send("waypoints list"),
                        col1, contentY + 70, 130, 18);

                btn("Clear all waypoints", () -> send("waypoints clear"),
                        col1 + 134, contentY + 70, 130, 18);
                break;

            case SETTINGS:
                String[] settings = {
                        "allowBreak",
                        "allowPlace",
                        "allowSprint",
                        "allowInventory",
                        "allowParkour",
                        "allowJumpAt",
                        "freeLook",
                        "legitMine"
                };

                int i = 0;

                for (final String setting : settings) {
                    int row = i / 2;
                    int colOffset = (i % 2) * 134;

                    btn("Toggle: " + setting,
                            () -> send("set " + setting),
                            col1 + colOffset,
                            contentY + 18 + row * 22,
                            130,
                            18);

                    i++;
                }

                btn("Show all settings (#set)",
                        () -> send("set"),
                        col1,
                        contentY + 18 + ((settings.length / 2) + 1) * 22,
                        264,
                        18);
                break;

            case CONSOLE:
                consoleInput = field(
                        col1,
                        contentY + 18,
                        200,
                        "raw command, no # needed"
                );

                btn("Send", () -> {
                    String value = consoleInput.getValue();

                    if (!value.trim().isEmpty()) {
                        send(value);
                        consoleInput.setValue("");
                    }
                }, col1 + 204, contentY + 18, 60, 18);

                btn("Help", () -> send("help"),
                        col1, contentY + 44, 84, 18);

                btn("Version", () -> send("version"),
                        col1 + 88, contentY + 44, 84, 18);

                btn("Render", () -> send("render"),
                        col1 + 176, contentY + 44, 88, 18);
                break;
        }
    }

    private void send(String command) {
        if (command == null || command.trim().isEmpty()) return;

        BaritoneCommandSender.send(command);

        HISTORY.addFirst("#" + command.trim());

        while (HISTORY.size() > HISTORY_MAX) {
            HISTORY.removeLast();
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        fill(
                matrixStack,
                panelX - 4,
                panelY - 4,
                panelX + panelWidth + 4,
                panelY + panelHeight + 4,
                0xB0101010
        );

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int histYBase = BASE_PANEL_HEIGHT - 70;
        int lineGapBase = 10;
        int cutoffYBase = BASE_PANEL_HEIGHT - 32;

        this.font.draw(
                matrixStack,
                "Last sent",
                x(4),
                y(histYBase - 12),
                0xAAAAAA
        );

        int lineBase = histYBase;

        for (String line : HISTORY) {
            this.font.draw(
                    matrixStack,
                    line,
                    x(4),
                    y(lineBase),
                    0x55FF55
            );

            lineBase += lineGapBase;

            if (lineBase > cutoffYBase) break;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

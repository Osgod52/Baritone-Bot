package com.example.baritoneui.client.gui;

import com.example.baritoneui.client.BaritoneCommandSender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

public class BaritoneUIScreen extends Screen {

    private enum Tab {
        MOVEMENT("Movement"),
        MINING("Mining"),
        BUILDING("Building"),
        WAYPOINTS("Waypoints"),
        SETTINGS("Settings"),
        CONSOLE("Console");

        final String label;
        Tab(String label) { this.label = label; }
    }

    private static final LinkedList<String> HISTORY = new LinkedList<>();
    private static final int HISTORY_MAX = 4;


    private static final int BASE_PANEL_WIDTH = 360;
    private static final int BASE_PANEL_HEIGHT = 300;

    private Tab currentTab = Tab.MOVEMENT;

    private float scale = 1f;
    private int panelWidth, panelHeight;
    private int panelX, panelY;

    // Text fields kept as fields so we can read them across renders.
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

    private final List<TextFieldWidget> activeFields = new ArrayList<>();
    // Widgets created per-tab that need to be torn down on tab switch.
    private final List<Element> rebuildable = new ArrayList<>();

    public BaritoneUIScreen() {
        super(Text.literal("Baritone UI"));
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
        for (Tab tab : Tab.values()) {
            int fxBase = txBase;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tab.label), b -> switchTab(tab))
                    .dimensions(x(fxBase), y(0), s(tabWidthBase - 2), s(20))
                    .build());
            txBase += tabWidthBase;
        }

        int bottomYBase = BASE_PANEL_HEIGHT - 24;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Pause"), b -> send("pause"))
                .dimensions(x(0), y(bottomYBase), s(80), s(20)).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Resume"), b -> send("resume"))
                .dimensions(x(84), y(bottomYBase), s(80), s(20)).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel/Stop"), b -> send("cancel"))
                .dimensions(x(168), y(bottomYBase), s(100), s(20)).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> this.close())
                .dimensions(x(BASE_PANEL_WIDTH - 60), y(bottomYBase), s(60), s(20)).build());

        buildTabContent();
    }

    private int x(int baseOffsetFromPanel) { return panelX + Math.round(baseOffsetFromPanel * scale); }
    private int y(int baseOffsetFromPanel) { return panelY + Math.round(baseOffsetFromPanel * scale); }
    private int s(int baseSize) { return Math.max(1, Math.round(baseSize * scale)); }

    private void switchTab(Tab tab) {
        this.currentTab = tab;
        for (TextFieldWidget box : activeFields) this.remove(box);
        activeFields.clear();
        rebuildable.forEach(this::remove);
        rebuildable.clear();
        buildTabContent();
    }

    private <T extends Element & Drawable & Selectable> T addTabWidget(T widget) {
        this.addDrawableChild(widget);
        rebuildable.add(widget);
        return widget;
    }

    private TextFieldWidget field(int xBase, int yBase, int wBase, String hint) {
        TextFieldWidget box = new TextFieldWidget(this.textRenderer, x(xBase), y(yBase), s(wBase), s(18), Text.literal(hint));
        box.setPlaceholder(Text.literal(hint));
        box.setMaxLength(64);
        addTabWidget(box);
        activeFields.add(box);
        return box;
    }

    private ButtonWidget.Builder btn(String label, ButtonWidget.PressAction onPress, int xBase, int yBase, int wBase, int hBase) {
        return ButtonWidget.builder(Text.literal(label), onPress)
                .dimensions(x(xBase), y(yBase), s(wBase), s(hBase));
    }

    private void buildTabContent() {
        int contentY = 26; // base-unit offset from panel top
        int col1 = 8;      // base-unit offset from panel left

        switch (currentTab) {
            case MOVEMENT -> {
                gotoX = field(col1, contentY + 18, 50, "x");
                gotoY = field(col1 + 54, contentY + 18, 50, "y");
                gotoZ = field(col1 + 108, contentY + 18, 50, "z");
                addTabWidget(btn("Goto XYZ", b ->
                                send("goto " + gotoX.getText() + " " + gotoY.getText() + " " + gotoZ.getText()),
                        col1 + 164, contentY + 18, 100, 18).build());

                addTabWidget(btn("Path (to cursor)", b -> send("path"), col1, contentY + 42, 150, 18).build());
                addTabWidget(btn("Come to me", b -> send("come"), col1 + 154, contentY + 42, 110, 18).build());

                followEntity = field(col1, contentY + 68, 120, "player/entity");
                addTabWidget(btn("Follow", b -> send("follow " + followEntity.getText()),
                        col1 + 124, contentY + 68, 80, 18).build());
                addTabWidget(btn("Follow players", b -> send("follow players"),
                        col1 + 208, contentY + 68, 130, 18).build());

                thiswayDistance = field(col1, contentY + 94, 80, "blocks");
                addTabWidget(btn("This way", b -> send("thisway " + thiswayDistance.getText()),
                        col1 + 84, contentY + 94, 90, 18).build());
                addTabWidget(btn("Explore", b -> send("explore"), col1 + 178, contentY + 94, 90, 18).build());

                addTabWidget(btn("Top (surface up)", b -> send("top"), col1, contentY + 120, 150, 18).build());
                addTabWidget(btn("Surface", b -> send("surface"), col1 + 154, contentY + 120, 110, 18).build());
                addTabWidget(btn("Invert goal", b -> send("invert"), col1, contentY + 146, 264, 18).build());
            }
            case MINING -> {
                mineBlocks = field(col1, contentY + 18, 180, "blocks (space sep)");
                addTabWidget(btn("Mine", b -> send("mine " + mineBlocks.getText()),
                        col1 + 184, contentY + 18, 80, 18).build());

                farmRadius = field(col1, contentY + 44, 80, "radius");
                addTabWidget(btn("Farm", b -> {
                            String r = farmRadius.getText();
                            send(r.isBlank() ? "farm" : "farm " + r);
                        }, col1 + 84, contentY + 44, 90, 18).build());
                addTabWidget(btn("Stop farming", b -> send("cancel"), col1 + 178, contentY + 44, 90, 18).build());

                addTabWidget(btn("Blacklist current mine target", b -> send("blacklist"),
                        col1, contentY + 70, 264, 18).build());
                addTabWidget(btn("Click block to select mine target", b -> send("click"),
                        col1, contentY + 96, 264, 18).build());
            }
            case BUILDING -> {
                buildSchematic = field(col1, contentY + 18, 180, "schematic file name");
                addTabWidget(btn("Build", b -> send("build " + buildSchematic.getText()),
                        col1 + 184, contentY + 18, 80, 18).build());

                tunnelH = field(col1, contentY + 44, 50, "height");
                tunnelW = field(col1 + 54, contentY + 44, 50, "width");
                tunnelD = field(col1 + 108, contentY + 44, 50, "depth");
                addTabWidget(btn("Tunnel", b -> {
                            String h = tunnelH.getText(), w = tunnelW.getText(), d = tunnelD.getText();
                            StringBuilder cmd = new StringBuilder("tunnel");
                            if (!h.isBlank()) cmd.append(' ').append(h);
                            if (!w.isBlank()) cmd.append(' ').append(w);
                            if (!d.isBlank()) cmd.append(' ').append(d);
                            send(cmd.toString());
                        }, col1 + 164, contentY + 44, 100, 18).build());

                addTabWidget(btn("Sel Pos1", b -> send("sel pos1"), col1, contentY + 70, 84, 18).build());
                addTabWidget(btn("Sel Pos2", b -> send("sel pos2"), col1 + 88, contentY + 70, 84, 18).build());
                addTabWidget(btn("Sel Clear", b -> send("sel clear"), col1 + 176, contentY + 70, 88, 18).build());

                selFillBlock = field(col1, contentY + 96, 150, "block to fill with");
                addTabWidget(btn("Sel Fill", b -> send("sel fill " + selFillBlock.getText()),
                        col1 + 154, contentY + 96, 110, 18).build());

                addTabWidget(btn("Sel Copy", b -> send("sel copy"), col1, contentY + 122, 130, 18).build());
                addTabWidget(btn("Sel Paste", b -> send("sel paste"), col1 + 134, contentY + 122, 130, 18).build());
            }
            case WAYPOINTS -> {
                addTabWidget(btn("Set Home Here", b -> send("sethome"), col1, contentY + 18, 130, 18).build());
                addTabWidget(btn("Goto Home", b -> send("home"), col1 + 134, contentY + 18, 130, 18).build());

                waypointName = field(col1, contentY + 44, 150, "waypoint name");
                addTabWidget(btn("Save", b -> send("waypoints save " + waypointName.getText()),
                        col1 + 154, contentY + 44, 55, 18).build());
                addTabWidget(btn("Goto", b -> send("waypoints goto " + waypointName.getText()),
                        col1 + 211, contentY + 44, 53, 18).build());

                addTabWidget(btn("List waypoints", b -> send("waypoints list"), col1, contentY + 70, 130, 18).build());
                addTabWidget(btn("Clear all waypoints", b -> send("waypoints clear"),
                        col1 + 134, contentY + 70, 130, 18).build());
            }
            case SETTINGS -> {
                String[] settings = {
                        "allowBreak", "allowPlace", "allowSprint", "allowInventory",
                        "allowParkour", "allowJumpAt", "freeLook", "legitMine"
                };
                int i = 0;
                for (String setting : settings) {
                    int row = i / 2;
                    int colOffset = (i % 2) * 134;
                    addTabWidget(btn("Toggle: " + setting, b -> send("set " + setting),
                            col1 + colOffset, contentY + 18 + row * 22, 130, 18).build());
                    i++;
                }
                addTabWidget(btn("Show all settings (#set)", b -> send("set"),
                        col1, contentY + 18 + ((settings.length / 2) + 1) * 22, 264, 18).build());
            }
            case CONSOLE -> {
                consoleInput = field(col1, contentY + 18, 200, "raw command, no # needed");
                addTabWidget(btn("Send", b -> {
                            String v = consoleInput.getText();
                            if (!v.isBlank()) {
                                send(v);
                                consoleInput.setText("");
                            }
                        }, col1 + 204, contentY + 18, 60, 18).build());
                addTabWidget(btn("Help", b -> send("help"), col1, contentY + 44, 84, 18).build());
                addTabWidget(btn("Version", b -> send("version"), col1 + 88, contentY + 44, 84, 18).build());
                addTabWidget(btn("Render", b -> send("render"), col1 + 176, contentY + 44, 88, 18).build());
            }
        }
    }

    private void send(String command) {
        if (command == null || command.isBlank()) return;
        BaritoneCommandSender.send(command);
        HISTORY.addFirst("#" + command.trim());
        while (HISTORY.size() > HISTORY_MAX) HISTORY.removeLast();
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        graphics.fill(panelX - 4, panelY - 4, panelX + panelWidth + 4, panelY + panelHeight + 4, 0xB0101010);

        super.render(graphics, mouseX, mouseY, delta);

        // History log sits in its own reserved strip between the tallest
        // tab's content and the bottom button bar.
        int histYBase = BASE_PANEL_HEIGHT - 70;
        int lineGapBase = 10;
        int cutoffYBase = BASE_PANEL_HEIGHT - 32;

        graphics.drawText(this.textRenderer, "Last sent:", x(4), y(histYBase - 12), 0xAAAAAA, false);
        int lineBase = histYBase;
        for (String line : HISTORY) {
            graphics.drawText(this.textRenderer, line, x(4), y(lineBase), 0x55FF55, false);
            lineBase += lineGapBase;
            if (lineBase > cutoffYBase) break;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

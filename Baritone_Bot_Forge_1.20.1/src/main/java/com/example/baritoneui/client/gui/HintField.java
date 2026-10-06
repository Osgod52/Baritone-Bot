package com.example.baritoneui.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

class HintField extends EditBox {

    private final String hint;
    private final int fx, fy, fw, fh;

    HintField(Font font, int x, int y, int width, int height, String hint) {
        super(font, x, y, width, height, Component.empty());
        this.hint = hint;
        this.fx = x;
        this.fy = y;
        this.fw = width;
        this.fh = height;
        this.setMaxLength(64);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        if (this.getValue().isEmpty() && !this.isFocused()) {
            Font fr = Minecraft.getInstance().font;
            String trimmed = fr.plainSubstrByWidth(hint, Math.max(0, fw - 8));
            graphics.drawString(fr, trimmed, fx + 4, fy + (fh - 8) / 2, 0xFF707070, false);
        }
    }
}

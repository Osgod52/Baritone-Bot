package com.example.baritoneui.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;


class HintField extends TextFieldWidget {

    private final String hint;
    private final int fx, fy, fw, fh;

    HintField(FontRenderer font, int x, int y, int width, int height, String hint) {
        super(font, x, y, width, height, net.minecraft.util.text.StringTextComponent.EMPTY);
        this.hint = hint;
        this.fx = x;
        this.fy = y;
        this.fw = width;
        this.fh = height;
        this.setMaxLength(64);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (this.getValue().isEmpty() && !this.isFocused()) {
            FontRenderer fr = Minecraft.getInstance().font;
            String trimmed = fr.plainSubstrByWidth(hint, Math.max(0, fw - 8));
            fr.draw(matrixStack, trimmed, fx + 4, fy + (fh - 8) / 2f, 0x707070);
        }
    }
}

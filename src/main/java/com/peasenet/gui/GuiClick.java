package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Mods;
import com.peasenet.util.color.Colors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class GuiClick {

    private static final int X_INCREMENT = 90;
    private static final int Y_INCREMENT = 12;

    static final int START_X1 = 10;
    static final int START_Y1 = 10;
    static final int START_X2 = START_X1 + X_INCREMENT;
    static final int START_Y2 = 21;

    public void render(MatrixStack matrixStack, TextRenderer tr, Text title) {
        int xt1 = START_X1;
        int yt1 = START_Y1;
        int xt2 = START_X2;
        int yt2 = START_Y2;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        // Draw title

        drawBox(Colors.BLACK.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
        tr.draw(matrixStack, title, xt1 + 2, yt1 + 2, 0xFFFFFF);
        drawOutline(Colors.WHITE.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);

        yt1 += Y_INCREMENT;
        yt2 += Y_INCREMENT;

        // Draw each of the mod categories
        for (Mods.Category category : Mods.Category.values()) {
            // skip the GUI category
            if (category == Mods.Category.GUI) continue;

            // Draw the category name
            drawBox(Colors.DARK_RED.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
            tr.draw(matrixStack, new TranslatableText(category.getTranslationKey()), xt1 + 2, yt1 + 2, 0xFFFFFF);
            drawOutline(Colors.BLACK.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);

            // Draw the checkboxes for each mod in the category
            drawModsInCategory(matrixStack, tr, xt1, yt1, xt2, yt2, category);
            xt1 += X_INCREMENT + 5;
            xt2 += X_INCREMENT + 5;
        }
    }

    private void drawModsInCategory(MatrixStack matrixStack, TextRenderer tr, int xt1, int yt1, int xt2, int yt2, Mods.Category category) {
        for (Mod mod : GavinsMod.getModsInCategory(category)) {
            yt1 += Y_INCREMENT;
            yt2 += Y_INCREMENT;

            drawBox(mod.isActive() ? Colors.GREEN.getAsFloatArray() : Colors.DARK_GRAY.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
            tr.draw(matrixStack, new TranslatableText(mod.getTranslationKey()), xt1 + 2, yt1 + 2, 0xffffff);
            drawOutline(Colors.BLACK.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
        }
    }

    private void drawBox(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 0.5f);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }

    private void drawOutline(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 1.0F);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP,
                VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }


    public void mouseClicked(double mouseX, double mouseY, int button) {
        // Get the x and y coordinates of the mod
        if (button != 0) return;
        int xt1 = START_X1;
        int yt1 = START_Y1 + Y_INCREMENT;
        int xt2 = START_X2;
        int yt2 = START_Y2 + Y_INCREMENT;
        // Get all mods per category
        for (Mods.Category category : Mods.Category.values()) {
            // skip over gui category
            if (category == Mods.Category.GUI) {
                continue;
            }
            // Get the mods in the category
            for (Mod mod : GavinsMod.getModsInCategory(category)) {
                // Check if the mouse is within the mod's box
                if (mouseX >= xt1 && mouseX <= xt2 && mouseY - 12 >= yt1 && mouseY - 12 <= yt2) {
                    // Toggle the mod
                    mod.toggle();
                    return;
                }
                yt1 += Y_INCREMENT;
                yt2 += Y_INCREMENT;
            }
            yt1 = 22;
            yt2 = 32;
            xt1 += X_INCREMENT + 5;
            xt2 += X_INCREMENT + 5;
        }
    }
}

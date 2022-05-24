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

    public void render(MatrixStack matrixStack, TextRenderer tr, Text title, int xt1, int yt1, int xt2, int yt2) {
        var acColor = Colors.DARK_RED.getAsFloatArray();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        // box
        drawBox(acColor, xt1, yt1, xt2, yt2, matrixStack);
        tr.draw(matrixStack, title, xt1 + 2, yt1 + 2, 0xFFFFFF);
        drawOutline(acColor, xt1, yt1, xt2, yt2, matrixStack);
        yt1 += 12;
        yt2 += 12;
        // Draw each of the mod categories
        for (Mods.Category category : Mods.Category.values()) {
            // skip the GUI category
            if (category == Mods.Category.GUI) continue;
            // Draw the category name
            drawBox(Colors.DARK_CYAN.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
            tr.draw(matrixStack, new TranslatableText(category.getTranslationKey()), xt1 + 2, yt1 + 2, 0xFFFFFF);
            drawOutline(Colors.YELLOW.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
            // Draw the checkboxes for each mod in the category
            drawModsInCategory(matrixStack, tr, xt1, yt1, xt2, yt2, category);
            xt1 += 90;
            xt2 += 90;
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        // Get the x and y coordinates of the mod
        if (button != 0) return;
        int xt1 = 10;
        int yt1 = 22;
        int xt2 = 95;
        int yt2 = 32;
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
                yt1 += 12;
                yt2 += 12;
            }
            yt1 = 22;
            yt2 = 32;
            xt1 += 90;
            xt2 += 90;
        }
    }

    private void drawModsInCategory(MatrixStack matrixStack, TextRenderer tr, int xt1, int yt1, int xt2, int yt2, Mods.Category category) {
        for (Mod mod : GavinsMod.getModsInCategory(category)) {
            yt1 += 12;
            yt2 += 12;

            drawBox(mod.isActive() ? Colors.DARK_GREEN.getAsFloatArray() : Colors.DARK_RED.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
            tr.draw(matrixStack, new TranslatableText(mod.getTranslationKey()), xt1 + 2, yt1 + 2, mod.isActive() ? 0x75ffae : 0xfc665b);
            drawOutline(mod.isActive() ? Colors.GREEN.getAsFloatArray() : Colors.RED.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
        }
    }

    private void drawBox(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 0.75f);
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
}

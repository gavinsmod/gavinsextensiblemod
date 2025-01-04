/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.gavui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.gavui.GavUI;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * @author gt3ch1
 * @version 01/07/2023
 * A utility class for drawing gui elements.
 */
public class GuiUtil {
    /**
     * Draws a box around the given box, with an alpha of 1f.
     *
     * @param c           - The color to draw the box with.
     * @param box         - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     */
    public static void drawBox(Color c, BoxF box, MatrixStack matrixStack) {
        drawBox(c, box, matrixStack, 1f);
    }

    /**
     * @param c           - The color to draw the box with.
     * @param box         - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    public static void drawBox(Color c, BoxF box, MatrixStack matrixStack, float alpha) {
        alpha = Math.max(0, Math.min(1, alpha));
        var acColor = c.getAsFloatArray();
//        var shader = RenderSystem.getShader();
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        
//        RenderSystem.setShader(ShaderProgramKeys.POSITION);
        RenderSystem.setShader(ShaderProgramKeys.POSITION);
        RenderSystem.enableBlend();
        var shaderColors = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], alpha);
        var tesselator = RenderSystem.renderThreadTesselator();
        var bufferBuilder = tesselator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        var matrix = matrixStack.peek().getPositionMatrix();
        drawBox(box, matrix, bufferBuilder);
        var e = bufferBuilder.end();
        BufferRenderer.drawWithGlobalProgram(e);
//        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    /**
     * Draws an outline of the given box with the given color, with an alpha of 1f.
     *
     * @param c           - The color to draw the outline with.
     * @param box         - The outline of a box to draw.
     * @param matrixStack - The matrix stack to draw with.
     */
    public static void drawOutline(Color c, BoxF box, MatrixStack matrixStack) {
        drawOutline(c, box, matrixStack, 1.0f);
    }

    /**
     * Draws an outline of the given box with the given color, with an alpha of 1f.
     *
     * @param boxF        - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     */
    public static void drawOutline(BoxF boxF, MatrixStack matrixStack) {
        drawOutline(GavUI.borderColor(), boxF, matrixStack);
    }

    /**
     * Draws an outline of the given box with the given color.
     *
     * @param c           - The color to draw the outline with.
     * @param box         - The outline of a box to draw.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    public static void drawOutline(Color c, BoxF box, MatrixStack matrixStack, float alpha) {
        alpha = Math.max(0, Math.min(1, alpha));
        var acColor = c.getAsFloatArray();
        RenderSystem.setShader(ShaderProgramKeys.POSITION);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], alpha);

        var matrix = matrixStack.peek().getPositionMatrix();
        var tess = RenderSystem.renderThreadTesselator();
        var bb = tess.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        drawBox(box, matrix, bb);
        var e = bb.end();
        BufferRenderer.drawWithGlobalProgram(e);
//        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    /**
     * Draws a box around the given box.
     *
     * @param box           - The box to draw.
     * @param matrix        - The matrix to draw with.
     * @param bufferBuilder - The buffer builder to draw with.
     */
    private static void drawBox(BoxF box, Matrix4f matrix, BufferBuilder bufferBuilder) {
        var xt1 = box.getTopLeft().x();
        var yt1 = box.getTopLeft().y();
        var xt2 = box.getBottomRight().x();
        var yt2 = box.getBottomRight().y();
        bufferBuilder.vertex(matrix, xt1, yt1, 0);
        bufferBuilder.vertex(matrix, xt1, yt2, 0);
        bufferBuilder.vertex(matrix, xt2, yt2, 0);
        bufferBuilder.vertex(matrix, xt2, yt1, 0);
        bufferBuilder.vertex(matrix, xt1, yt1, 0);

    }

    /**
     * Draws a single line to the given coordinates.
     *
     * @param color       - The color to draw the line with.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    public static void renderSingleLine(Color color, PointF p1, PointF p2, MatrixStack matrixStack, float alpha) {
        alpha = Math.max(0, Math.min(1, alpha));
        var accColor = color.getAsFloatArray();
        RenderSystem.setShader(ShaderProgramKeys.POSITION);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var tessellator = RenderSystem.renderThreadTesselator();
        RenderSystem.setShaderColor(accColor[0], accColor[1], accColor[2], alpha);
        var bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, p1.x(), p1.y(), 0);
        bufferBuilder.vertex(matrix, p2.x(), p2.y(), 0);
        var e = bufferBuilder.end();
        BufferRenderer.drawWithGlobalProgram(e);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
}

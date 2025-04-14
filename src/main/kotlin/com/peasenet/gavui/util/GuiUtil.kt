/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
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
package com.peasenet.gavui.util

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.GavUI.borderColor
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.util.GemRenderLayers
import com.peasenet.util.RenderUtils
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack

/**
 * A utility class for drawing gui elements.
 * @author GT3CH1
 * @version 02-02-2025
 * @since 01/07/2023
 */

//TODO: Fix this class!
object GuiUtil {
    /**
     * @param c           - The color to draw the box with.
     * @param box         - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    @JvmOverloads
    fun drawBox(c: Color, box: BoxF, matrixStack: MatrixStack, alpha: Float = 1f) {
        drawBox(box, matrixStack, c, alpha, GemRenderLayers.QUADS)
    }

    /**
     * Draws an outline of the given box with the given color, with an alpha of 1f.
     *
     * @param boxF        - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     */
    fun drawOutline(boxF: BoxF, matrixStack: MatrixStack) {
        drawOutline(borderColor(), boxF, matrixStack)
    }


    /**
     * Draws an outline of the given box with the given color
     *
     * @param c           - The color to draw the outline with.
     * @param box         - The outline of a box to draw.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    @JvmOverloads
    fun drawOutline(c: Color, box: BoxF, matrixStack: MatrixStack, alpha: Float = 1.0f) {
        drawBox(box, matrixStack, c, alpha)
    }

    /**
     * Draws a box around the given box.
     *
     * @param box           - The box to draw.
     * @param matrix        - The matrix to draw with.
     * @param bufferBuilder - The buffer builder to draw with.
     */
    private fun drawBox(box: BoxF, matrix: MatrixStack, color: Color, alpha: Float, targetLayer: RenderLayer? = null) {
        // todo: move to gavui render utils?
        val vcp = RenderUtils.getVertexConsumerProvider()
        val layer = targetLayer ?: GemRenderLayers.LINES
        val bufferBuilder = vcp.getBuffer(layer)

        val matrix4f = matrix.peek()

        val xt1 = box.topLeft.x
        val yt1 = box.topLeft.y
        val xt2 = box.bottomRight.x
        val yt2 = box.bottomRight.y
        bufferBuilder.vertex(matrix4f, xt1, yt1, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix4f, xt1, yt2, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix4f, xt2, yt2, 0f)
            .normal(0f, 0f, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        bufferBuilder.vertex(matrix4f, xt2, yt1, 0f)
            .normal(0f, 0f, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        bufferBuilder.vertex(matrix4f, xt1, yt1, 0f)
            .normal(0f, 0f, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        vcp.draw(layer)
    }

    /**
     * Draws a single line to the given coordinates.
     *
     * @param color       - The color to draw the line with.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    fun renderSingleLine(color: Color, p1: PointF, p2: PointF, matrixStack: MatrixStack, alpha: Float) {
        val newAlpha = alpha.coerceIn(0.0f, 1.0f)
        val accColor = color.asFloatArray
//        RenderSystem.setShader(ShaderProgramKeys.POSITION)
//        RenderSystem.enableBlend()
        val matrix = matrixStack.peek().positionMatrix
//        val tessellator = RenderSystem.renderThreadTesselator()
        RenderSystem.setShaderColor(accColor[0], accColor[1], accColor[2], newAlpha)
//        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION)
//        bufferBuilder.vertex(matrix, p1.x, p1.y, 0f)
//        bufferBuilder.vertex(matrix, p2.x, p2.y, 0f)
//        val e = bufferBuilder.end()
//        BufferRenderer.drawWithGlobalProgram(e)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
//        RenderSystem.disableBlend()
    }

    fun fill(box: BoxF, matrixStack: MatrixStack, color: Color, alpha: Float) {

        val vcp = RenderUtils.getVertexConsumerProvider()
        val layer = RenderLayer.getGui()
        val bufferBuilder = vcp.getBuffer(layer)
        bufferBuilder.vertex(matrixStack.peek(), box.topLeft.x, box.topLeft.y, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        bufferBuilder.vertex(matrixStack.peek(), box.topLeft.x, box.bottomRight.y, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        bufferBuilder.vertex(matrixStack.peek(), box.bottomRight.x, box.bottomRight.y, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
        bufferBuilder.vertex(matrixStack.peek(), box.bottomRight.x, box.topLeft.y, 0f)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)

    }
}

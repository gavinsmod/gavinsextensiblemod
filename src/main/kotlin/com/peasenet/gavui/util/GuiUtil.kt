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

import com.peasenet.gavui.GavUI.borderColor
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.math.BoxF
import com.peasenet.util.GemRenderLayers
import com.peasenet.util.RenderUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix3x2fStack

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
    @Deprecated("Use fill with drawContext", ReplaceWith("fill(box, drawContext, color)"))
    @JvmOverloads
    fun drawBox(c: Color, box: BoxF, matrixStack: Matrix3x2fStack, alpha: Float = 1f) {
        drawBox(box, matrixStack, c, GemRenderLayers.QUADS)
    }

    /**
     * Draws an outline of the given box with the given color, with an alpha of 1f.
     *
     * @param boxF        - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     */
    @Deprecated("Use drawOutline with drawContext", ReplaceWith("drawOutline(boxF, drawContext,color)"))
    fun drawOutline(boxF: BoxF, matrixStack: Matrix3x2fStack) {
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
    fun drawOutline(c: Color, box: BoxF, matrixStack: Matrix3x2fStack) {
        drawBox(box, matrixStack, c)
    }

    fun drawOutline(box: BoxF, drawContext: DrawContext, color: Color) {
        drawContext.drawVerticalLine(box.x1.toInt(), box.y1.toInt()-1, box.y2.toInt(), color.asInt)
        drawContext.drawVerticalLine(box.x2.toInt()-1, box.y1.toInt()-1, box.y2.toInt(), color.asInt)
        drawContext.drawHorizontalLine(box.x1.toInt()+1, box.x2.toInt()-2, box.y1.toInt(), color.asInt)
        drawContext.drawHorizontalLine(box.x1.toInt()+1, box.x2.toInt()-2, box.y2.toInt()-1, color.asInt)
    }

    /**
     * Draws a box around the given box.
     *
     * @param box           - The box to draw.
     * @param matrix        - The matrix to draw with.
     * @param bufferBuilder - The buffer builder to draw with.
     */
    private fun drawBox(
        box: BoxF,
        matrix: Matrix3x2fStack,
        color: Color,
        targetLayer: RenderLayer? = null,
    ) {
        val vcp = RenderUtils.getVertexConsumerProvider()
        val layer = targetLayer ?: GemRenderLayers.QUADS
        val bufferBuilder = vcp.getBuffer(layer)
        val xt1 = (box.topLeft.x).toInt()
        val yt1 = (box.topLeft.y).toInt()
        val xt2 = box.bottomRight.x.toInt()
        val yt2 = box.bottomRight.y.toInt()
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt1.toFloat())
            .color(color.asInt)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt2.toFloat())
            .color(color.asInt)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt2.toFloat())
            .color(color.asInt)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt1.toFloat())
            .color(color.asInt)
            .normal(0f, 0f, 0f)
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt1.toFloat())
            .color(color.asInt)
            .normal(0f, 0f, 0f)
        vcp.draw(layer)
    }

    fun fill(box: BoxF, drawContext: DrawContext, color: Color) {
        drawContext.fill(box.x1.toInt(), box.y1.toInt(), box.x2.toInt(), box.y2.toInt(), color.asInt)
    }

}

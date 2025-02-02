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
import net.minecraft.client.gl.ShaderProgramKeys
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix4f

/**
 * A utility class for drawing gui elements.
 * @author GT3CH1
 * @version 02-02-2025
 * @since 01/07/2023
 */
object GuiUtil {
    /**
     * @param c           - The color to draw the box with.
     * @param box         - The box to draw.
     * @param matrixStack - The matrix stack to draw with.
     * @param alpha       - The alpha value to draw with.
     */
    @JvmOverloads
    fun drawBox(c: Color, box: BoxF, matrixStack: MatrixStack, alpha: Float = 1f) {
        val newAlpha = alpha.coerceIn(0f, 1f)
        val acColor = c.asFloatArray
        RenderSystem.setShader(ShaderProgramKeys.POSITION)
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], newAlpha)
        val tesselator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tesselator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
        val matrix = matrixStack.peek().positionMatrix
        drawBox(box, matrix, bufferBuilder)
        val e = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(e)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.disableBlend()
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
        val newAlpha = alpha.coerceIn(0.0f, 1.0f)
        val acColor = c.asFloatArray
        RenderSystem.setShader(ShaderProgramKeys.POSITION)
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], newAlpha)

        val matrix = matrixStack.peek().positionMatrix
        val tess = RenderSystem.renderThreadTesselator()
        val bb = tess.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION)
        drawBox(box, matrix, bb)
        val e = bb.end()
        BufferRenderer.drawWithGlobalProgram(e)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.disableBlend()
    }

    /**
     * Draws a box around the given box.
     *
     * @param box           - The box to draw.
     * @param matrix        - The matrix to draw with.
     * @param bufferBuilder - The buffer builder to draw with.
     */
    private fun drawBox(box: BoxF, matrix: Matrix4f, bufferBuilder: BufferBuilder) {
        val xt1 = box.topLeft.x
        val yt1 = box.topLeft.y
        val xt2 = box.bottomRight.x
        val yt2 = box.bottomRight.y
        bufferBuilder.vertex(matrix, xt1, yt1, 0f)
        bufferBuilder.vertex(matrix, xt1, yt2, 0f)
        bufferBuilder.vertex(matrix, xt2, yt2, 0f)
        bufferBuilder.vertex(matrix, xt2, yt1, 0f)
        bufferBuilder.vertex(matrix, xt1, yt1, 0f)
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
        RenderSystem.setShader(ShaderProgramKeys.POSITION)
        RenderSystem.enableBlend()
        val matrix = matrixStack.peek().positionMatrix
        val tessellator = RenderSystem.renderThreadTesselator()
        RenderSystem.setShaderColor(accColor[0], accColor[1], accColor[2], newAlpha)
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION)
        bufferBuilder.vertex(matrix, p1.x, p1.y, 0f)
        bufferBuilder.vertex(matrix, p2.x, p2.y, 0f)
        val e = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(e)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.disableBlend()
    }
}

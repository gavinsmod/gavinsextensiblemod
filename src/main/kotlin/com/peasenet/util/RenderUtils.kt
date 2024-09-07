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
package com.peasenet.util

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.GavinsModClient
import com.peasenet.mixinterface.ISimpleOption
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.chunk.Chunk
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11


/**
 * @author gt3ch1
 * @version 07-18-2023
 * A utility class for rendering tracers and esp's.
 */
object RenderUtils {
    /**
     * How many chunks away to render things.
     */
    var CHUNK_RADIUS: Int = GavinsModClient.minecraftClient.options.viewDistance.value

    /**
     * The last player configured gamma.
     */
    private var LAST_GAMMA = 0.0

    /**
     * Resets the render system to the default state.
     */
    fun resetRenderSystem() {
        RenderSystem.applyModelViewMatrix()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.disableBlend()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
    }

    /**
     * Gets the bounding box of an entity.
     *
     * @param delta The delta time.
     * @param e     The entity.
     * @return The bounding box of the entity.
     */
    @Deprecated("Use the entity's bounding box instead.", ReplaceWith("e.boundingBox"))
    fun getEntityBox(delta: Float, e: Entity): Box {
        return e.boundingBox
    }

    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    fun setHighGamma() {
        gamma = 16.0
    }

    /**
     * Resets the gamma to the players last configured value.
     */
    fun setLowGamma() {
        gamma = LAST_GAMMA
    }

    var gamma: Double
        /**
         * Gets the current game gamma.
         *
         * @return The current game gamma.
         */
        get() = GavinsModClient.minecraftClient.options.gamma.value
        /**
         * Sets the gamma to the given value.
         *
         * @param gamma The value to set the gamma to.
         */
        set(gamma) {
            var newValue = gamma
            val maxGamma = 16f
            if (newValue < 0.0) newValue = 0.0
            if (newValue > maxGamma) newValue = maxGamma.toDouble()
            val newGamma = GavinsModClient.minecraftClient.options.gamma
            if (newGamma.value != newValue) {
                val newGamma2 = (newGamma as ISimpleOption<Double>)
                newGamma2.forceSetValue(newValue)
            }
        }

    /**
     * Whether the gamma is set to the full bright value, 16.0.
     */
    val isHighGamma: Boolean
        get() = gamma == 16.0

    /**
     * Whether the gamma is set to the last gamma value as defined by the player.
     */
    val isLastGamma: Boolean
        get() = gamma <= LAST_GAMMA

    /**
     * Returns the last gamma value before the gamma was set to full bright.
     */
    private fun getLastGamma(): Double {
        return LAST_GAMMA
    }

    /**
     * Draws a box on screen.
     *
     * @param acColor     The color of the box as a 4 point float array.
     * @param xt1         The x coordinate of the top left corner of the box.
     * @param yt1         The y coordinate of the top left corner of the box.
     * @param xt2         The x coordinate of the bottom right corner of the box.
     * @param yt2         The y coordinate of the bottom right corner of the box.
     * @param matrixStack The matrix stack used to draw boxes on screen.
     * @param alpha       The alpha value of the box.
     */
    fun drawBox(acColor: FloatArray, xt1: Int, yt1: Int, xt2: Int, yt2: Int, matrixStack: MatrixStack, alpha: Float) {
        // set alpha to be between 0 and 1
        var newAlpha = alpha
        newAlpha = 0f.coerceAtLeast(1f.coerceAtMost(newAlpha))
        RenderSystem.setShader { GameRenderer.getPositionProgram() }
        RenderSystem.enableBlend()
        val matrix = matrixStack.peek().positionMatrix
        val tessellator = Tessellator.getInstance()
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], newAlpha)
        var bb = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
        drawBox(xt1, yt1, xt2, yt2, matrix, bb)
        BufferRenderer.drawWithGlobalProgram(bb.end())
    }

    /**
     * Draws a box from the given points
     *
     * @param xt1           - The x coordinate of the top left corner of the box.
     * @param yt1           - The y coordinate of the top left corner of the box.
     * @param xt2           - The x coordinate of the bottom right corner of the box.
     * @param yt2           - The y coordinate of the bottom right corner of the box.
     * @param matrix        - The matrix stack used to draw boxes on screen.
     * @param bufferBuilder - The buffer builder used to draw boxes on screen.
     */
    private fun drawBox(xt1: Int, yt1: Int, xt2: Int, yt2: Int, matrix: Matrix4f, bufferBuilder: BufferBuilder) {
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt1.toFloat(), 0f)
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt2.toFloat(), 0f)
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt2.toFloat(), 0f)
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt1.toFloat(), 0f)
    }

    fun getRenderDistance(): Int {
        val client = GavinsModClient.minecraftClient.options.viewDistance.value + 1
        val networkView = GavinsModClient.minecraftClient.getWorld().simulationDistance + 1
        return maxOf(client, networkView)
    }

    fun getVisibleChunks(): List<Chunk> {
        val chunks = ArrayList<Chunk>()
        val player = GavinsModClient.minecraftClient.getPlayer()
        val chunkX = player.chunkPos.x
        val chunkZ = player.chunkPos.z
        val level = GavinsModClient.minecraftClient.getWorld()
        for (x in -(getRenderDistance() + 1) until (getRenderDistance())) {
            for (z in -(getRenderDistance() + 1) until (getRenderDistance())) {
                val chunkX1 = chunkX + x
                val chunkZ1 = chunkZ + z
                chunks.add(level.getChunk(chunkX1, chunkZ1))
            }
        }
        return chunks
    }

    fun getCameraPos(): Vec3d {
        val camera = MinecraftClient.getInstance().blockEntityRenderDispatcher.camera
        return camera.pos
    }

    private fun getCameraBlockPos(): BlockPos {
        val camera = GavinsModClient.minecraftClient.entityRenderDispatcher.camera
        return camera.blockPos
    }

    fun getCameraRegionPos(): RegionPos {
        return RegionPos.fromBlockPos(getCameraBlockPos())
    }

    private fun applyRegionalRenderOffset(stack: MatrixStack, region: RegionPos) {
        val offset = region.toVec3d().subtract(getCameraPos())
        stack.translate(offset.x, offset.y, offset.z)
    }

    fun drawOutlinedBox(bb: Box, vertexBuffer: VertexBuffer, color: Color = Colors.WHITE, alpha: Float = 1f) {
        val tessellator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        drawOutlinedBox(bb, bufferBuilder)
        val buffer = bufferBuilder.end()
        vertexBuffer.bind()
        vertexBuffer.upload(buffer)
        VertexBuffer.unbind()
    }

    fun drawOutlinedBox(
        bb: Box, vertexBuffer: VertexBuffer, matrixStack: MatrixStack, color: Color = Colors.WHITE, alpha: Float = 1f
    ) {
        val viewMatrix = matrixStack.peek().positionMatrix
        val projMatrix = RenderSystem.getProjectionMatrix()
        val shader: ShaderProgram? = RenderSystem.getShader()
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor(color.red, color.green, color.blue, alpha)
        drawOutlinedBox(bb, vertexBuffer)
        vertexBuffer.bind()
        vertexBuffer.draw(viewMatrix, projMatrix, shader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        VertexBuffer.unbind()
    }

    fun drawOutlinedBox(
        bb: Box, buffer: BufferBuilder, matrix4f: Matrix4f, color: Color = Colors.WHITE, alpha: Float = 1f
    ) {
        val minX = bb.minX.toFloat()
        val minY = bb.minY.toFloat()
        val minZ = bb.minZ.toFloat()
        val maxX = bb.maxX.toFloat()
        val maxY = bb.maxY.toFloat()
        val maxZ = bb.maxZ.toFloat()
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
    }

    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        start: Vec3d,
        end: Vec3d,
        color: Color = Colors.WHITE,
        alpha: Float = 1f
    ) {
        drawSingleLine(buffer, matrix4f, start.toVector3f(), end.toVector3f(), color, alpha)
    }

    fun drawSingleLine(
        bufferBuilder: BufferBuilder,
        matrix4f: Matrix4f,
        start: Vector3f,
        end: Vector3f,
        color: Color = Colors.WHITE,
        alpha: Float = 1f
    ) {
        bufferBuilder.vertex(
            matrix4f, start.x, start.y, start.z
        ).color(color.red, color.green, color.blue, alpha)
        bufferBuilder.vertex(
            matrix4f, end.x, end.y, end.z
        ).color(color.red, color.green, color.blue, alpha)
    }

    fun drawOutlinedBox(bb: Box, buffer: BufferBuilder) {
        val minX = bb.minX.toFloat()
        val minY = bb.minY.toFloat()
        val minZ = bb.minZ.toFloat()
        val maxX = bb.maxX.toFloat()
        val maxY = bb.maxY.toFloat()
        val maxZ = bb.maxZ.toFloat()
        buffer.vertex(minX, minY, minZ)
        buffer.vertex(maxX, minY, minZ)
        buffer.vertex(maxX, minY, minZ)
        buffer.vertex(maxX, minY, maxZ)
        buffer.vertex(maxX, minY, maxZ)
        buffer.vertex(minX, minY, maxZ)
        buffer.vertex(minX, minY, maxZ)
        buffer.vertex(minX, minY, minZ)
        buffer.vertex(minX, minY, minZ)
        buffer.vertex(minX, maxY, minZ)
        buffer.vertex(maxX, minY, minZ)
        buffer.vertex(maxX, maxY, minZ)
        buffer.vertex(maxX, minY, maxZ)
        buffer.vertex(maxX, maxY, maxZ)
        buffer.vertex(minX, minY, maxZ)
        buffer.vertex(minX, maxY, maxZ)
        buffer.vertex(minX, maxY, minZ)
        buffer.vertex(maxX, maxY, minZ)
        buffer.vertex(maxX, maxY, minZ)
        buffer.vertex(maxX, maxY, maxZ)
        buffer.vertex(maxX, maxY, maxZ)
        buffer.vertex(minX, maxY, maxZ)
        buffer.vertex(minX, maxY, maxZ)
        buffer.vertex(minX, maxY, minZ)
    }

    fun cleanupRender(matrixStack: MatrixStack) {
        matrixStack.pop()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    fun getLookVec(delta: Float): Vec3d {
        val mc = MinecraftClient.getInstance()
        val pitch = mc.player!!.getPitch(delta)
        val yaw = mc.player!!.getYaw(delta)
        return Rotation(pitch, yaw).asLookVec()

    }

    fun setupRender(matrixStack: MatrixStack) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        matrixStack.push()
        val region = getCameraRegionPos()
        applyRegionalRenderOffset(matrixStack, region);
    }
}


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
@file:Suppress("KotlinConstantConditions", "UNCHECKED_CAST")

package com.peasenet.util

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.GavinsModClient
import com.peasenet.mixinterface.ISimpleOption
import com.peasenet.util.math.MathUtils
import net.minecraft.client.Minecraft
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.level.chunk.ChunkAccess
import org.joml.Matrix3x2fStack
import org.joml.Vector3f
import org.lwjgl.opengl.GL11


/**
 * A utility class for rendering tracers and esp's.
 * @author GT3CH1
 * @version 09-08-2024
 * @since 07-18-2023
 */
object RenderUtils {
    /**
     * How many chunks away to render things.
     */
    var CHUNK_RADIUS: Int = GavinsModClient.minecraftClient.options.renderDistance().get()

    /**
     * The last player configured gamma.
     */
    private var LAST_GAMMA = 0.0

    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    fun setHighGamma() {
        LAST_GAMMA = gamma
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
        get() = GavinsModClient.minecraftClient.options.gamma().get()
        /**
         * Sets the gamma to the given value.
         *
         * @param gamma The value to set the gamma to.
         */
        set(gamma) {
            val newValue = gamma
            newValue.coerceAtLeast(0.0).coerceAtMost(16.0)
            val newGamma = GavinsModClient.minecraftClient.options.gamma()
            if (newGamma.get() != newValue) {
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
     * Gets the current render distance based off of the maximum of the client and simulation render distances.
     * @return The current render distance.
     */
    fun getRenderDistance(): Int {
        val client = GavinsModClient.minecraftClient.options.renderDistance().get() + 1
        val networkView = GavinsModClient.minecraftClient.getWorld().serverSimulationDistance + 1
        return maxOf(client, networkView)
    }

    /**
     * Gets the visible chunks around the player based off of the render distance.
     * @return The visible chunks around the player.
     *
     * @see getRenderDistance
     */
    fun getVisibleChunks(renderDistance: Int = getRenderDistance()): List<ChunkAccess> {
        val chunks = mutableSetOf<ChunkAccess>()
        val player = GavinsModClient.minecraftClient.getPlayer()
        val chunkX = player.chunkPosition().x
        val chunkZ = player.chunkPosition().z
        val level = GavinsModClient.minecraftClient.getWorld()
        for (x in -(renderDistance + 1) until (renderDistance)) {
            for (z in -(renderDistance + 1) until (renderDistance)) {
                val chunkX1 = chunkX + x
                val chunkZ1 = chunkZ + z
                chunks.add(level.getChunk(chunkX1, chunkZ1))
            }
        }
        return chunks.toList()
    }

    /**
     * Gets the camera position.
     * @return The camera position.
     */
    fun getCameraPos(): Vec3 {
        val camera = Minecraft.getInstance().gameRenderer.mainCamera.position()
        return camera!!
    }

    fun drawOutlinedBoxOptimized(
        bb: AABB, matrixStack: PoseStack, color: Color = Colors.WHITE, alpha: Float = 1f, buffer: VertexConsumer,
    ) {
        val bb2 = bb.move((getCameraPos().reverse()))

        val minX = bb2.minX.toFloat()
        val minY = bb2.minY.toFloat()
        val minZ = bb2.minZ.toFloat()
        val maxX = bb2.maxX.toFloat()
        val maxY = bb2.maxY.toFloat()
        val maxZ = bb2.maxZ.toFloat()

        val matrix4f = matrixStack.last()
        val colorWithAlpha = color.withAlpha(alpha).asInt
        try {
            // draw lines connecting the corners of the box
            buffer.addVertex(matrix4f, minX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)
            buffer.addVertex(matrix4f, maxX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)

            buffer.addVertex(matrix4f, minX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, minX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)

            buffer.addVertex(matrix4f, minX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, maxX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)

            buffer.addVertex(matrix4f, maxX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)
            buffer.addVertex(matrix4f, maxX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)

            // top
            buffer.addVertex(matrix4f, minX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)
            buffer.addVertex(matrix4f, maxX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)

            buffer.addVertex(matrix4f, minX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, minX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, minX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, maxX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 0f, 1f)
            buffer.addVertex(matrix4f, maxX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)
            buffer.addVertex(matrix4f, maxX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 1f, 0f, 0f)
            // corners
            buffer.addVertex(matrix4f, minX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)
            buffer.addVertex(matrix4f, minX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)

            buffer.addVertex(matrix4f, maxX, minY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)
            buffer.addVertex(matrix4f, maxX, maxY, minZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)

            buffer.addVertex(matrix4f, minX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)
            buffer.addVertex(matrix4f, minX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)

            buffer.addVertex(matrix4f, maxX, minY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)
            buffer.addVertex(matrix4f, maxX, maxY, maxZ)
                .setColor(colorWithAlpha)
                .setLineWidth(2.0f)
                .setNormal(matrix4f, 0f, 1f, 0f)
        } catch (_: IllegalStateException) {
            // ignore
        }
    }

    /**
     * Draws an outlined box.
     * @param bb The box to draw.
     * @param matrixStack The matrix to draw with.
     * @param color The color of the box.
     * @param alpha The alpha of the box.
     */
    fun drawOutlinedBox(
        bb: AABB, matrixStack: PoseStack, color: Color = Colors.WHITE, alpha: Float = 1f,
    ) {

        GL11.glDisable(GL11.GL_DEPTH_TEST)
        val vcp = getVertexConsumerProvider()
        val layer = GemRenderLayers.LINES
        val buffer = vcp.getBuffer(layer)
        val bb2 = bb.move((getCameraPos().reverse()))

        val minX = bb2.minX.toFloat()
        val minY = bb2.minY.toFloat()
        val minZ = bb2.minZ.toFloat()
        val maxX = bb2.maxX.toFloat()
        val maxY = bb2.maxY.toFloat()
        val maxZ = bb2.maxZ.toFloat()

        val matrix4f = matrixStack.last()

        // draw lines connecting the corners of the box
        buffer.addVertex(matrix4f, minX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 0f, 1f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 1f, 0f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, minZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, minX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, minY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)
        buffer.addVertex(matrix4f, maxX, maxY, maxZ)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(matrix4f, 0f, 1f, 0f)
            .setLineWidth(2.0f)

        vcp.endBatch(layer)

        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }


    fun drawSingleLineOptimized(

        matrixStack: PoseStack,
        start: Vec3,
        end: Vec3,
        color: Color,
        alpha: Float = 1f,
        buffer: VertexConsumer,
    ) {
        val posMatrix = matrixStack.last()
        val x1 = start.x.toFloat()
        val y1 = start.y.toFloat()
        val z1 = start.z.toFloat()
        val x2 = end.x.toFloat()
        val y2 = end.y.toFloat()
        val z2 = end.z.toFloat()
        val normal = Vector3f(x2, y2, z2).sub(Vector3f(x1, y1, z1)).normalize()
        buffer.addVertex(posMatrix, x1, y1, z1)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(2.0f)
        buffer.addVertex(posMatrix, x2, y2, z2)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(2.0f)

    }

    fun drawSingleLine(
        matrixStack: PoseStack,
        start: Vec3,
        end: Vec3,
        color: Color,
        alpha: Float = 1f,
        withOffset: Boolean = true,
        depthTest: Boolean = false,
        buffer: VertexConsumer? = null,
    ) {

        val vcp = getVertexConsumerProvider()

        val layer = if (depthTest) GemRenderLayers.LINES else GemRenderLayers.ESP_LINES
        val bufferBuilder = buffer ?: vcp.getBuffer(layer)
        val posMatrix = matrixStack.last()
        var bb2 = end
        if (withOffset)
            bb2 = end.add((getCameraPos().reverse()))
        val x1 = start.x.toFloat()
        val y1 = start.y.toFloat()
        val z1 = start.z.toFloat()
        val x2 = bb2.x.toFloat()
        val y2 = bb2.y.toFloat()
        val z2 = bb2.z.toFloat()
        val normal = Vector3f(x2, y2, z2).sub(Vector3f(x1, y1, z1)).normalize()
        bufferBuilder.addVertex(posMatrix, x1, y1, z1)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(2.0f)
        bufferBuilder.addVertex(posMatrix, x2, y2, z2)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(2.0f)
        if (buffer == null)
            vcp.endBatch(layer)

    }

    fun drawSingleLine(
        matrixStack: Matrix3x2fStack,
        start: Vec3,
        end: Vec3,
        color: Color,
        alpha: Float = 1f,
        withOffset: Boolean = true,
        depthTest: Boolean = false,
    ) {

        val vcp = getVertexConsumerProvider()

        val layer = if (depthTest) GemRenderLayers.LINES else GemRenderLayers.ESP_LINES
        val bufferBuilder = vcp.getBuffer(layer)
        val posMatrix = matrixStack
        var bb2 = end
        if (withOffset)
            bb2 = end.add((getCameraPos().reverse()))
        val x1 = start.x.toFloat()
        val y1 = start.y.toFloat()
        val z1 = start.z.toFloat()
        val x2 = bb2.x.toFloat()
        val y2 = bb2.y.toFloat()
        val z2 = bb2.z.toFloat()
        val normal = Vector3f(x2, y2, z2).sub(Vector3f(x1, y1, z1)).normalize()

        bufferBuilder.addVertexWith2DPose(posMatrix, x1, y1)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(1f)
        bufferBuilder.addVertexWith2DPose(posMatrix, x2, y2)
            .setColor(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setLineWidth(1f)
        vcp.endBatch(layer)

    }

    /**
     * Gets the look vector of the player.
     * @param delta The delta time.
     * @return The look vector of the player.
     */
    fun getLookVec(delta: Float): Vec3 {
        val mc = Minecraft.getInstance()
        val pitch = mc.player!!.getViewXRot(delta)
        val yaw = mc.player!!.getViewYRot(delta)
        return Rotation(pitch, yaw).asLookVec()

    }

    fun getLerpedBox(e: Entity, delta: Float): AABB {
        val offset =
            MathUtils.lerp(
                delta, e.position(),
                Vec3(
                    e.xOld,
                    e.yOld,
                    e.zOld
                )
            ).subtract(e.position())
        return e.boundingBox.move(offset)
    }

    fun renderEntityEsp(
        matrixStack: PoseStack,
        box: AABB,
        color: Color,
        alpha: Float,
    ) {
        drawOutlinedBox(box, matrixStack, color, alpha)
    }


    fun getVertexConsumerProvider(): MultiBufferSource.BufferSource {
        return GavinsModClient.minecraftClient.getBufferBuilderStorage().bufferSource()
    }
}


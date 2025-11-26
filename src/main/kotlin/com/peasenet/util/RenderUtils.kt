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

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.GavinsModClient
import com.peasenet.mixinterface.ISimpleOption
import com.peasenet.util.math.MathUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.chunk.Chunk
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
    var CHUNK_RADIUS: Int = GavinsModClient.minecraftClient.options.viewDistance.value

    /**
     * The last player configured gamma.
     */
    private var LAST_GAMMA = 0.0

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
            val newValue = gamma
            newValue.coerceAtLeast(0.0).coerceAtMost(16.0)
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
     * Gets the current render distance based off of the maximum of the client and simulation render distances.
     * @return The current render distance.
     */
    fun getRenderDistance(): Int {
        val client = GavinsModClient.minecraftClient.options.viewDistance.value + 1
        val networkView = GavinsModClient.minecraftClient.getWorld().simulationDistance + 1
        return maxOf(client, networkView)
    }

    /**
     * Gets the visible chunks around the player based off of the render distance.
     * @return The visible chunks around the player.
     *
     * @see getRenderDistance
     */
    fun getVisibleChunks(renderDistance: Int = getRenderDistance()): List<Chunk> {
        val chunks = mutableSetOf<Chunk>()
        val player = GavinsModClient.minecraftClient.getPlayer()
        val chunkX = player.chunkPos.x
        val chunkZ = player.chunkPos.z
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
    fun getCameraPos(): Vec3d {
        val camera = MinecraftClient.getInstance().gameRenderer.camera.cameraPos
        return camera!!
    }

    /**
     * Gets the camera block position.
     * @return The camera block position.
     */
    private fun getCameraBlockPos(): BlockPos {
        val camera = MinecraftClient.getInstance().gameRenderer.camera.blockPos
        return camera!!
    }

    /**
     * Gets the camera region position.
     * @return The camera region position.
     */
    fun getCameraRegionPos(): RegionPos {
        return RegionPos.fromBlockPos(getCameraBlockPos())
    }

    /**
     * Applies the regional render offset to the given matrix stack.
     * @param stack The matrix stack to apply the offset to.
     * @param region The region to apply the offset from.
     */
    fun applyRegionalRenderOffset(stack: MatrixStack, region: RegionPos) {
        val offset = region.toVec3d().subtract(getCameraPos())
        stack.translate(offset.x, offset.y, offset.z)
    }

    /**
     * Draws an outlined box.
     * @param bb The box to draw.
     * @param buffer The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param color The color of the box.
     * @param alpha The alpha of the box.
     */
    fun drawOutlinedBox(
        bb: Box, matrixStack: MatrixStack, color: Color = Colors.WHITE, alpha: Float = 1f,
        withOffset: Boolean = true,
    ) {

        GL11.glDisable(GL11.GL_DEPTH_TEST)
        val vcp = getVertexConsumerProvider()
        val layer = GemRenderLayers.LINES
        val buffer = vcp.getBuffer(layer)
        val bb2 = bb.offset((getCameraPos().negate()))

        var minX = bb2.minX.toFloat()
        var minY = bb2.minY.toFloat()
        var minZ = bb2.minZ.toFloat()
        var maxX = bb2.maxX.toFloat()
        var maxY = bb2.maxY.toFloat()
        var maxZ = bb2.maxZ.toFloat()
//
//        var max = Vec3d(maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())
//        var min = Vec3d(minX.toDouble(), minY.toDouble(), minZ.toDouble())
//        val newBB = Box(min, max)
//        minX = newBB.minX.toFloat()
//        minY = newBB.minY.toFloat()
//        minZ = newBB.minZ.toFloat()
//        maxX = newBB.maxX.toFloat()
//        maxY = newBB.maxY.toFloat()
//        maxZ = newBB.maxZ.toFloat()
        // TODO: MC 1.21.10 migration
        val matrix4f = matrixStack.peek()

        // draw lines connecting the corners of the box
        buffer.vertex(matrix4f, minX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)
        buffer.vertex(matrix4f, maxX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)

        buffer.vertex(matrix4f, minX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, minX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)

        buffer.vertex(matrix4f, minX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, maxX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)

        buffer.vertex(matrix4f, maxX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)
        buffer.vertex(matrix4f, maxX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)

        // top
        buffer.vertex(matrix4f, minX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)
        buffer.vertex(matrix4f, maxX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)

        buffer.vertex(matrix4f, minX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, minX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, minX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, maxX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 0f, 1f)
        buffer.vertex(matrix4f, maxX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)
        buffer.vertex(matrix4f, maxX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 1f, 0f, 0f)
        // corners
        buffer.vertex(matrix4f, minX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)
        buffer.vertex(matrix4f, minX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)

        buffer.vertex(matrix4f, maxX, minY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)
        buffer.vertex(matrix4f, maxX, maxY, minZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)

        buffer.vertex(matrix4f, minX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)
        buffer.vertex(matrix4f, minX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)

        buffer.vertex(matrix4f, maxX, minY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)
        buffer.vertex(matrix4f, maxX, maxY, maxZ)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(matrix4f, 0f, 1f, 0f)

        vcp.draw(layer)

        GL11.glEnable(GL11.GL_DEPTH_TEST)
//        GL11.glDisable(GL11.GL_BLEND)
    }


    fun drawSingleLine(
        matrixStack: MatrixStack,
        start: Vec3d,
        end: Vec3d,
        color: Color,
        alpha: Float = 1f,
        withOffset: Boolean = true,
        depthTest: Boolean = false,
    ) {

        val vcp = getVertexConsumerProvider()

        val layer = if (depthTest) GemRenderLayers.LINES else GemRenderLayers.ESP_LINES
        val bufferBuilder = vcp.getBuffer(layer)
        val posMatrix = matrixStack.peek()
        var bb2 = end
        if (withOffset)
            bb2 = end.add((getCameraPos().negate()))
        val x1 = start.x.toFloat()
        val y1 = start.y.toFloat()
        val z1 = start.z.toFloat()
        val x2 = bb2.x.toFloat()
        val y2 = bb2.y.toFloat()
        val z2 = bb2.z.toFloat()
        val normal = Vector3f(x2, y2, z2).sub(Vector3f(x1, y1, z1)).normalize()

        bufferBuilder.vertex(posMatrix, x1, y1, z1)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(normal.x(), normal.y(), normal.z())
        bufferBuilder.vertex(posMatrix, x2, y2, z2)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(normal.x(), normal.y(), normal.z())
        vcp.draw(layer)

    }

    fun drawSingleLine(
        matrixStack: Matrix3x2fStack,
        start: Vec3d,
        end: Vec3d,
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
            bb2 = end.add((getCameraPos().negate()))
        val x1 = start.x.toFloat()
        val y1 = start.y.toFloat()
        val z1 = start.z.toFloat()
        val x2 = bb2.x.toFloat()
        val y2 = bb2.y.toFloat()
        val z2 = bb2.z.toFloat()
        val normal = Vector3f(x2, y2, z2).sub(Vector3f(x1, y1, z1)).normalize()

        bufferBuilder.vertex(posMatrix, x1, y1)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(normal.x(), normal.y(), normal.z())
        bufferBuilder.vertex(posMatrix, x2, y2)
            .color(color.getRed(), color.getGreen(), color.getBlue(), alpha)
            .normal(normal.x(), normal.y(), normal.z())
        vcp.draw(layer)

    }

    /**
     * Cleans up the render system by popping the matrix stack, resetting the shader color, and enabling depth testing.
     * @param matrixStack The matrix stack to clean up.
     */
    fun cleanupRender(matrixStack: MatrixStack) {
        matrixStack.pop()
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
    }

    /**
     * Gets the look vector of the player.
     * @param delta The delta time.
     * @return The look vector of the player.
     */
    fun getLookVec(delta: Float): Vec3d {
        val mc = MinecraftClient.getInstance()
        val pitch = mc.player!!.getPitch(delta)
        val yaw = mc.player!!.getYaw(delta)
        return Rotation(pitch, yaw).asLookVec()

    }

    /**
     * Sets up the render by enabling blending, disabling depth testing, pushing the matrix stack and
     * applying the regional render offset.
     */
    fun setupRender(matrixStack: MatrixStack) {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        matrixStack.push()
        val region = getCameraRegionPos()

        applyRegionalRenderOffset(matrixStack, region)
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    /**
     * Sets up the render with the position color shader.
     */
    @Deprecated("Use setupRender(matrixStack: MatrixStack) instead", ReplaceWith("setupRender(matrixStack)"))
    fun setupRenderWithShader(matrixStack: MatrixStack) {
        setupRender(matrixStack)
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
//        RenderSystem.applyModelViewMatrix()
    }

    /**
     * Gets a buffer builder for rendering.
     *
     * @return A buffer builder for rendering.
     */
    fun getBufferBuilder(): BufferBuilder {
        val tessellator = Tessellator.getInstance()
        return tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)

    }

    /**
     * Gets the center of the screen based on the players look vector.
     * @param partialTicks The delta time.
     */
    fun getCenterOfScreen(partialTicks: Float): Vec3d {
        val regionVec = getCameraRegionPos().toVec3d()
        return getLookVec(partialTicks).add(getCameraPos()).subtract(regionVec)
    }

    /**
     * Draws the given buffer builder with the global program.
     */
//    @Deprecated("Broken in 1.21.5", level = DeprecationLevel.ERROR)

    /**
     * Draws the given buffer builder with the global program, then cleans up the render.
     */
//    @Deprecated("Broken in 1.21.5", level = DeprecationLevel.ERROR)
//    fun drawBuffer(buffer: BufferBuilder, matrixStack: MatrixStack) {
//        drawBuffer(buffer)
//        cleanupRender(matrixStack)
//    }

    /**
     * Offsets the given position by the camera region position.
     */
    fun offsetPosWithCamera(pos: Vec3d): Vec3d {
        return pos.subtract(getCameraRegionPos().toVec3d())
    }


    fun getLerpedPos(e: Entity, partialTicks: Float): Vec3d {
        return MathUtils.lerp(
            partialTicks, e.entityPos,
            Vec3d(
                e.lastRenderX,
                e.lastRenderY,
                e.lastRenderZ
            )
        )
    }

    fun getLerpedBox(e: Entity, delta: Float): Box {
        val offset =
            MathUtils.lerp(
                delta, e.entityPos,
                Vec3d(
                    e.lastRenderX,
                    e.lastRenderY,
                    e.lastRenderZ
                )
            ).subtract(e.entityPos)
        return e.boundingBox.offset(offset)
    }

    fun renderEntityEsp(
        matrixStack: MatrixStack,
        box: Box,
        color: Color,
        alpha: Float,
    ) {
        matrixStack.push()
        drawOutlinedBox(box, matrixStack, color, alpha)
        matrixStack.pop()
    }


    fun getVertexConsumerProvider(): VertexConsumerProvider.Immediate {
        return GavinsModClient.minecraftClient.getBufferBuilderStorage().entityVertexConsumers
    }

//    fun drawLayer(layer: RenderLayer) {
//        layer.startDrawing()
//        val frameBuffer = layer.target
//        val pipeline = layer.pipeline
//        val layer = layer.
//    }
}


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
import com.peasenet.util.math.MathUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.chunk.Chunk
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11


/**
 * A utility class for rendering tracers and esp's.
 * @author gt3ch1
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

    /**
     * Gets the camera position.
     * @return The camera position.
     */
    fun getCameraPos(): Vec3d {
        val camera = MinecraftClient.getInstance().blockEntityRenderDispatcher.camera
        return camera.pos
    }

    /**
     * Gets the camera block position.
     * @return The camera block position.
     */
    private fun getCameraBlockPos(): BlockPos {
        val camera = GavinsModClient.minecraftClient.entityRenderDispatcher.camera
        return camera.blockPos
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
    private fun applyRegionalRenderOffset(stack: MatrixStack, region: RegionPos) {
        val offset = region.toVec3d().subtract(getCameraPos())
        stack.translate(offset.x, offset.y, offset.z)
    }

    /**
     * Draws an outlined box. This will lerp the box to the current position of the entity.
     * @param partialTicks The delta time.
     * @param bufferBuilder The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param entity The entity to draw the box around.
     * @param color The color of the box.
     * @param alpha The alpha of the box.
     */
    fun drawOutlinedBox(
        partialTicks: Float,
        bufferBuilder: BufferBuilder,
        matrix4f: Matrix4f,
        entity: Entity,
        color: Color,
        alpha: Float,
        lerp: Boolean = true
    ) {

        var box = entity.boundingBox
        if (lerp) {
            val lerped = MathUtils.lerp(
                partialTicks,
                entity.pos,
                entity,
                getCameraRegionPos()
            )
            box = Box(
                lerped.x + box.minX,
                lerped.y + box.minY,
                lerped.z + box.minZ,
                lerped.x + box.maxX,
                lerped.y + box.maxY,
                lerped.z + box.maxZ
            )
        }
        drawOutlinedBox(
            box, bufferBuilder, matrix4f, color, alpha
        )
    }

    fun drawOutlinedBox(
        partialTicks: Float,
        bufferBuilder: BufferBuilder,
        matrix4f: Matrix4f,
        entity: Entity,
        entityBox: Box,
        color: Color,
        alpha: Float
    ) {

        val lerped = MathUtils.lerp(
            partialTicks,
            entity.pos,
            entity,
            getCameraRegionPos()
        )
        val box = Box(
            lerped.x + entityBox.minX,
            lerped.y + entityBox.minY,
            lerped.z + entityBox.minZ,
            lerped.x + entityBox.maxX,
            lerped.y + entityBox.maxY,
            lerped.z + entityBox.maxZ
        )
        drawOutlinedBox(
            box, bufferBuilder, matrix4f, color, alpha
        )
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
        bb: Box, buffer: BufferBuilder, matrix4f: Matrix4f, color: Color = Colors.WHITE, alpha: Float = 1f
    ) {

        var minX = bb.minX.toFloat()
        var minY = bb.minY.toFloat()
        var minZ = bb.minZ.toFloat()
        var maxX = bb.maxX.toFloat()
        var maxY = bb.maxY.toFloat()
        var maxZ = bb.maxZ.toFloat()
        var max = Vec3d(maxX.toDouble(), maxY.toDouble(), maxZ.toDouble()).subtract(getCameraRegionPos().toVec3d())
        var min = Vec3d(minX.toDouble(), minY.toDouble(), minZ.toDouble()).subtract(getCameraRegionPos().toVec3d())
        val newBB = Box(min, max)
        minX = newBB.minX.toFloat()
        minY = newBB.minY.toFloat()
        minZ = newBB.minZ.toFloat()
        maxX = newBB.maxX.toFloat()
        maxY = newBB.maxY.toFloat()
        maxZ = newBB.maxZ.toFloat()

        // bottom face
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        // top face
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        // corners
        buffer.vertex(matrix4f, minX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, minZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, maxX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, minY, maxZ).color(color.red, color.green, color.blue, alpha)
        buffer.vertex(matrix4f, minX, maxY, maxZ).color(color.red, color.green, color.blue, alpha)
    }

    /**
     * Draws a line from the center of the clients screen to the given end point.
     * @param buffer The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param partialTicks The delta time.
     * @param end The end point of the line.
     * @param color The color of the line.
     * @param alpha The alpha of the line.
     * @param offsetEnd Whether to offset the end point by the camera region position.
     */
    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        partialTicks: Float,
        end: Vec3d,
        color: Color = Colors.WHITE,
        alpha: Float = 1f,
        offsetEnd: Boolean = false
    ) {
        if (offsetEnd) {
            drawSingleLine(
                buffer,
                matrix4f,
                getCenterOfScreen(partialTicks),
                end.subtract(getCameraRegionPos().toVec3d()),
                color,
                alpha
            )
        } else {
            drawSingleLine(buffer, matrix4f, getCenterOfScreen(partialTicks), end, color, alpha)
        }
    }

    /**
     * Draws a line from the center of the clients screen to the given end point. This will lerp the end point to
     * the current position of the entity.
     * @param buffer The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param partialTicks The delta time.
     * @param end The end point of the line.
     * @param color The color of the line.
     * @param alpha The alpha of the line.
     */
    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        partialTicks: Float,
        entity: Entity,
        color: Color = Colors.WHITE,
        alpha: Float = 1f,
    ) {
        val lerpedPos = MathUtils.lerp(
            partialTicks, entity.pos, Vec3d(
                entity.prevX,
                entity.prevY,
                entity.prevZ
            ), getCameraRegionPos()
        )
        val box = Box(
            lerpedPos.x + entity.boundingBox.minX,
            lerpedPos.y + entity.boundingBox.minY,
            lerpedPos.z + entity.boundingBox.minZ,
            lerpedPos.x + entity.boundingBox.maxX,
            lerpedPos.y + entity.boundingBox.maxY,
            lerpedPos.z + entity.boundingBox.maxZ
        )
        val center = box.center
        drawSingleLine(buffer, matrix4f, getCenterOfScreen(partialTicks), center, color, alpha)
    }

    /**
     * Draws a line from the center of the clients screen to the given end point.
     * @param buffer The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param partialTicks The delta time.
     * @param end The end point of the line.
     * @param color The color of the line.
     * @param alpha The alpha of the line.
     */
    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        partialTicks: Float,
        end: BlockPos,
        color: Color = Colors.WHITE,
        alpha: Float = 1f
    ) {
        drawSingleLine(
            buffer,
            matrix4f,
            getCenterOfScreen(partialTicks),
            end.subtract(getCameraRegionPos().toVec3i()).toCenterPos(),
            color,
            alpha
        )
    }


    fun drawOutlinedPlane(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        start: Vec3d,
        end: Vec3d,
        color: Color = Colors.WHITE,
        alpha: Float = 1f,
        withOffset: Boolean = false
    ) {
        if (!withOffset) {
            val box = Box(
                start.x, start.y, start.z, end.x, end.y, end.z
            )
            drawOutlinedBox(box, buffer, matrix4f, color, alpha)
        } else {
            val box = Box(
                start.subtract(getCameraRegionPos().toVec3d()),
                end.subtract(getCameraRegionPos().toVec3d())
            )
            drawOutlinedBox(box, buffer, matrix4f, color, alpha)
        }
    }

    /**
     * Draws a line from the center of the clients screen to the given end point.
     * @param buffer The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param end The end point of the line.
     * @param color The color of the line.
     * @param alpha The alpha of the line.
     */
    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        start: Vec3d,
        end: Vec3d,
        color: Color = Colors.WHITE,
        alpha: Float = 1f,
        withOffset: Boolean = false
    ) {
        if (withOffset) {
            drawSingleLine(
                buffer,
                matrix4f,
                start.subtract(getCameraRegionPos().toVec3d()).toVector3f(),
                end.subtract(getCameraRegionPos().toVec3d()).toVector3f(),
                color,
                alpha
            )
            return
        }
        drawSingleLine(buffer, matrix4f, start.toVector3f(), end.toVector3f(), color, alpha)
    }

    fun drawSingleLine(
        buffer: BufferBuilder,
        matrix4f: Matrix4f,
        start: Vec3d,
        direction: Direction,
        color: Color = Colors.WHITE,
        alpha: Float = 1f,
        withOffset: Boolean = false
    ) {
        val end = when (direction) {
            Direction.UP -> start.add(0.0, 1.0, 0.0)
            Direction.DOWN -> start.add(0.0, -1.0, 0.0)
            Direction.NORTH -> start.add(0.0, 0.0, -1.0)
            Direction.SOUTH -> start.add(0.0, 0.0, 1.0)
            Direction.EAST -> start.add(1.0, 0.0, 0.0)
            Direction.WEST -> start.add(-1.0, 0.0, 0.0)
        }
        drawSingleLine(buffer, matrix4f, start, end, color, alpha, withOffset)
    }

    /**
     * Draws a line from the center of the clients screen to the given end point.
     * @param bufferBuilder The buffer builder to draw with.
     * @param matrix4f The matrix to draw with.
     * @param start The start point of the line.
     * @param end The end point of the line.
     * @param color The color of the line.
     * @param alpha The alpha of the line.
     */
    private fun drawSingleLine(
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

    /**
     * Draws an outlined box.
     * @param bb The box to draw.
     * @param buffer The buffer builder to draw with.
     */
    private fun drawOutlinedBox(bb: Box, buffer: BufferBuilder) {
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

    /**
     * Cleans up the render system by popping the matrix stack, resetting the shader color, and enabling depth testing.
     * @param matrixStack The matrix stack to clean up.
     */
    fun cleanupRender(matrixStack: MatrixStack) {
        matrixStack.pop()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        matrixStack.push()
        val region = getCameraRegionPos()
        applyRegionalRenderOffset(matrixStack, region);
    }

    /**
     * Sets up the render with the position color shader.
     */
    fun setupRenderWithShader(matrixStack: MatrixStack) {
        setupRender(matrixStack)
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.applyModelViewMatrix()
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
    private fun getCenterOfScreen(partialTicks: Float): Vec3d {
        val regionVec = getCameraRegionPos().toVec3d();
        return getLookVec(partialTicks).add(getCameraPos()).subtract(regionVec);
    }

    /**
     * Draws the given buffer builder with the global program.
     */
    fun drawBuffer(bufferBuilder: BufferBuilder) {
        try {
            val end = bufferBuilder.end()
            BufferRenderer.drawWithGlobalProgram(end)
        } catch (_: Exception) {

        }
        resetRenderSystem()
    }

    /**
     * Draws the given buffer builder with the global program, then cleans up the render.
     */
    fun drawBuffer(buffer: BufferBuilder, matrixStack: MatrixStack) {
        drawBuffer(buffer)
        cleanupRender(matrixStack)
    }

    /**
     * Offsets the given position by the camera region position.
     */
    fun offsetPosWithCamera(pos: Vec3d): Vec3d {
        return pos.subtract(getCameraRegionPos().toVec3d())
    }

}


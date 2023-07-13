/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.util

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.mixinterface.ISimpleOption
import com.peasenet.util.PlayerUtils.getNewPlayerPosition
import com.peasenet.util.event.BlockEntityRenderEvent
import com.peasenet.util.event.EntityRenderEvent
import com.peasenet.util.event.EventManager
import com.peasenet.util.event.WorldRenderEvent
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import java.util.function.Consumer

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A utility class for rendering tracers and esp's.
 */
object RenderUtils {
    /**
     * How many chunks away to render things.
     */
    private var CHUNK_RADIUS = GavinsModClient.minecraftClient.options.viewDistance.value

    /**
     * The last player configured gamma.
     */
    private var LAST_GAMMA = 0.0

    /**
     * Draws a single line in the given color.
     *
     * @param stack     The matrix stack to use.
     * @param buffer    The buffer to write to.
     * @param playerPos The position of the player.
     * @param boxPos    The center of the location we want to draw a line to.
     * @param color     The color to draw the line in.
     */
    @JvmOverloads
    fun renderSingleLine(stack: MatrixStack, buffer: VertexConsumer, playerPos: Vec3d, boxPos: Vec3d, color: Color, alpha: Float = 1f) {
        val normal = Vec3d(boxPos.getX() - playerPos.getX(), boxPos.getY() - playerPos.getY(), boxPos.getZ() - playerPos.getZ())
        normal.normalize()
        val matrix4f = stack.peek().positionMatrix
        val matrix3f = stack.peek().normalMatrix
        buffer.vertex(matrix4f, playerPos.getX().toFloat(), playerPos.getY().toFloat(), playerPos.getZ().toFloat()).color(color.red, color.green, color.blue, alpha).normal(matrix3f, normal.getX().toFloat(), normal.getY().toFloat(), normal.getZ().toFloat()).next()
        buffer.vertex(matrix4f, boxPos.getX().toFloat(), boxPos.getY().toFloat(), boxPos.getZ().toFloat()).color(color.red, color.green, color.blue, alpha).normal(matrix3f, normal.getX().toFloat(), normal.getY().toFloat(), normal.getZ().toFloat()).next()
    }

    /**
     * Processes events for rendering player, chest, item, and mob tracers or esp's in the world.
     *
     * @param context The render context.
     */
    @JvmStatic
    fun afterEntities(context: WorldRenderContext) {
//        RenderUtils.context = context;
        CHUNK_RADIUS = GavinsModClient.minecraftClient.options.viewDistance.value / 2
        // this helps with lag
        val minecraft = MinecraftClient.getInstance()
        val level = minecraft.world
        val player = minecraft.player
        val stack = context.matrixStack()
        val delta = context.tickDelta()
        val mainCamera = minecraft.gameRenderer.camera
        val camera = mainCamera.pos
        setupRenderSystem()
        stack.push()
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)
        RenderSystem.applyModelViewMatrix()
        stack.translate(-camera.x, -camera.y, -camera.z)
        assert(player != null)
        assert(level != null)
        val event = WorldRenderEvent(level!!, stack, buffer, delta)
        EventManager.eventManager.call(event)
        tessellator.draw()
        stack.pop()
        resetRenderSystem()
    }

    @JvmStatic
    fun beforeBlockOutline(context: WorldRenderContext, h: HitResult?): Boolean {
        CHUNK_RADIUS = GavinsModClient.minecraftClient.options.viewDistance.value
        val minecraft = MinecraftClient.getInstance()
        val level = minecraft.world
        val player = minecraft.player
        val stack = context.matrixStack()
        val delta = context.tickDelta()
        val mainCamera = minecraft.gameRenderer.camera
        val camera = mainCamera.pos
        setupRenderSystem()
        stack.push()
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)
        RenderSystem.applyModelViewMatrix()
        stack.translate(-camera.x, -camera.y, -camera.z)
        assert(player != null)
        val playerPos = getNewPlayerPosition(delta, mainCamera)
        assert(level != null)
        val chunkX = player!!.chunkPos.x
        val chunkZ = player.chunkPos.z
        drawBlockMods(level, stack, buffer, playerPos, chunkX, chunkZ, delta)
        drawEntityMods(level, player, stack, delta, buffer, playerPos)
        val event = WorldRenderEvent(level!!, stack, buffer, delta)
        EventManager.eventManager.call(event)
        tessellator.draw()
        stack.pop()
        resetRenderSystem()
        return true
    }

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
     * Sets up the render system for the tracers and esps to work.
     */
    private fun setupRenderSystem() {
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    }

    /**
     * Draws Chest ESPs and tracers.
     *
     * @param level     The world.
     * @param stack     The matrix stack.
     * @param buffer    The buffer to write to.
     * @param playerPos The player's position.
     * @param chunkX   The player's chunk x.
     * @param chunkZ   The player's chunk z.
     */
    private fun drawBlockMods(level: ClientWorld?, stack: MatrixStack, buffer: BufferBuilder, playerPos: Vec3d, chunkX: Int, chunkZ: Int, delta: Float) {
        for (x in -CHUNK_RADIUS..CHUNK_RADIUS) {
            for (z in -CHUNK_RADIUS..CHUNK_RADIUS) {
                val chunkX1 = chunkX + x
                val chunkZ1 = chunkZ + z
                if (level!!.getChunk(chunkX1, chunkZ1) != null) {
                    val blockEntities = level.getChunk(chunkX1, chunkZ1).blockEntities
                    for ((blockPos, blockEntity) in blockEntities) {
                        val aabb = Box(blockPos)
                        val boxPos = aabb.center
                        val event = BlockEntityRenderEvent(blockEntity, stack, buffer, boxPos, playerPos, delta)
                        EventManager.eventManager.call(event)
                    }
                }
            }
        }
    }

    /**
     * Draws a box on the world.
     *
     * @param stack  The matrix stack.
     * @param buffer The buffer to write to.
     * @param aabb   The box to draw.
     * @param c      The color to draw the box in.
     */
    @JvmOverloads
    fun drawBox(stack: MatrixStack?, buffer: BufferBuilder?, aabb: Box, c: Color, alpha: Float = 1f) {
        // draw vertices of the box
        val x1 = aabb.minX.toFloat()
        val y1 = aabb.minY.toFloat()
        val z1 = aabb.minZ.toFloat()
        val x2 = aabb.maxX.toFloat()
        val y2 = aabb.maxY.toFloat()
        val z2 = aabb.maxZ.toFloat()
        val matrix4f = stack!!.peek().positionMatrix
        val color = c.getAsInt(alpha)
        stack.push()
        renderPlane(buffer!!, matrix4f, x1, x2, y1, y1, z1, z2, color)
        renderPlane(buffer, matrix4f, x2, x2, y1, y2, z1, z2, color)
        renderPlane(buffer, matrix4f, x1, x2, y2, y2, z1, z2, color)
        renderPlane(buffer, matrix4f, x1, x1, y1, y2, z1, z2, color)
        stack.pop()
    }

    /**
     * Renders a 2 dimensional plane.
     * @param buffer The buffer to write to.
     * @param matrix4f The matrix to use.
     * @param x1 The first x coordinate.
     * @param x2 The second x coordinate.
     * @param y1 The first y coordinate.
     * @param y2 The second y coordinate.
     * @param z1 The first z coordinate.
     * @param z2 The second z coordinate.
     * @param c The color to draw the plane in.
     */
    private fun renderPlane(buffer: BufferBuilder, matrix4f: Matrix4f, x1: Float, x2: Float, y1: Float, y2: Float, z1: Float, z2: Float, c: Int) {
        if (x1 == x2) {
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
            buffer.vertex(matrix4f, x1, y2, z1).color(c).next()

            buffer.vertex(matrix4f, x1, y2, z1).color(c).next()
            buffer.vertex(matrix4f, x1, y2, z2).color(c).next()

            buffer.vertex(matrix4f, x1, y2, z2).color(c).next()
            buffer.vertex(matrix4f, x1, y1, z2).color(c).next()

            buffer.vertex(matrix4f, x1, y1, z2).color(c).next()
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
        } else if (z1 == z2) {
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
            buffer.vertex(matrix4f, x2, y1, z1).color(c).next()

            buffer.vertex(matrix4f, x2, y1, z1).color(c).next()
            buffer.vertex(matrix4f, x2, y2, z1).color(c).next()

            buffer.vertex(matrix4f, x2, y2, z1).color(c).next()
            buffer.vertex(matrix4f, x1, y2, z1).color(c).next()

            buffer.vertex(matrix4f, x1, y2, z1).color(c).next()
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
        } else if (y1 == y2) {
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
            buffer.vertex(matrix4f, x2, y1, z1).color(c).next()

            buffer.vertex(matrix4f, x2, y1, z1).color(c).next()
            buffer.vertex(matrix4f, x2, y1, z2).color(c).next()

            buffer.vertex(matrix4f, x2, y1, z2).color(c).next()
            buffer.vertex(matrix4f, x1, y1, z2).color(c).next()

            buffer.vertex(matrix4f, x1, y1, z2).color(c).next()
            buffer.vertex(matrix4f, x1, y1, z1).color(c).next()
        }
    }

    /**
     * Draws the Entity based ESP's and tracers.
     *
     * @param level     The world.
     * @param player    The player.
     * @param stack     The matrix stack.
     * @param delta     The change in time.
     * @param buffer    The buffer to write to.
     * @param playerPos The player's position.
     */
    private fun drawEntityMods(level: ClientWorld?, player: ClientPlayerEntity?, stack: MatrixStack, delta: Float, buffer: BufferBuilder, playerPos: Vec3d) {
        level!!.entities.forEach(Consumer { e: Entity ->
            if (e.squaredDistanceTo(player) > 64 * CHUNK_RADIUS * 16 || player === e) return@Consumer
            val aabb = getEntityBox(delta, e)
            val boxPos = aabb.center
            val event = EntityRenderEvent(e, stack, buffer, boxPos, playerPos, delta)
            EventManager.eventManager.call(event)
        })
    }

    /**
     * Gets the bounding box of an entity.
     *
     * @param delta The delta time.
     * @param e     The entity.
     * @return The bounding box of the entity.
     */
    fun getEntityBox(delta: Float, e: Entity): Box {
        val x = e.prevX + (e.x - e.prevX) * delta
        val y = e.prevY + (e.y - e.prevY) * delta
        val z = e.prevZ + (e.z - e.prevZ) * delta
        return e.type.createSimpleBoundingBox(x, y, z)
    }

    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    fun setHighGamma() {
        if (GavinsMod.fullbrightConfig.gammaFade) {
            fadeGammaUp()
        } else {
            gamma = GavinsMod.fullbrightConfig.maxGamma.toDouble()
        }
    }

    /**
     * Resets the gamma to the players last configured value.
     */
    fun setLowGamma() {
        if (GavinsMod.fullbrightConfig.gammaFade) {
            fadeGammaDown()
        } else {
            gamma = LAST_GAMMA
        }
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
            val maxGamma = GavinsMod.fullbrightConfig.maxGamma
            if (newValue < 0.0) newValue = 0.0
            if (newValue > maxGamma) newValue = maxGamma.toDouble()
            val newGamma = GavinsModClient.minecraftClient.options.gamma
            if (newGamma.value != newValue) {
                val newGamma2 = (newGamma as ISimpleOption<Double>)
                newGamma2.forceSetValue(newValue)
            }
        }
    val isHighGamma: Boolean
        get() = gamma == 16.0
    val isLastGamma: Boolean
        get() = gamma <= LAST_GAMMA

    fun setLastGamma() {
        if (gamma > 1) return
        LAST_GAMMA = gamma
    }

    fun getLastGamma(): Double {
        return LAST_GAMMA
    }

    private fun fadeGammaUp() {
        gamma += 0.2f
    }

    private fun fadeGammaDown() {
        gamma -= 0.2f
        if (gamma < getLastGamma()) gamma = getLastGamma()
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
        val bufferBuilder = Tessellator.getInstance().buffer
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], newAlpha)
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
        drawBox(xt1, yt1, xt2, yt2, matrix, bufferBuilder)
        Tessellator.getInstance().draw()
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
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt1.toFloat(), 0f).next()
        bufferBuilder.vertex(matrix, xt1.toFloat(), yt2.toFloat(), 0f).next()
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt2.toFloat(), 0f).next()
        bufferBuilder.vertex(matrix, xt2.toFloat(), yt1.toFloat(), 0f).next()
    }
}
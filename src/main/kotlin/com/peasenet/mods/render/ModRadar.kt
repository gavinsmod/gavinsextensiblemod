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
package com.peasenet.mods.render

import com.peasenet.config.render.RadarConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.mod.render.GuiRadar
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d
import org.joml.Matrix3x2fStack
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A mod that allows for a radar-like view of the world.
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-11-2023
 */
class ModRadar : RenderMod(
    "gavinsmod.mod.render.radar",
    "radar"
), InGameHudRenderListener {
    /**
     * Creates a radar overlay in the top-right corner of the screen.
     */
    init {
        clickSetting {
            title = translationKey
            callback = { MinecraftClient.getInstance().setScreen(GuiRadar()) }
        }
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onRenderInGameHud(drawContext: DrawContext, delta: Float, forceRender: Boolean) {
        val canRender = !Mods.isActive("gui") && !Mods.isActive("settings") || forceRender
        if (!canRender) return
        val stack = drawContext.matrices
        RadarConfig.x = client.window.scaledWidth - config.size - 10
        val radarBox = BoxF(
            PointF(RadarConfig.x.toFloat(), RadarConfig.y.toFloat()),
            config.size.toFloat(),
            config.size.toFloat()
        )
        GuiUtil.fill(
            radarBox,
            stack,
            config.backgroundColor,
            config.backgroundAlpha
        )
        drawEntitiesOnRadar(stack)
        GuiUtil.drawOutline(radarBox, stack)
    }

    /**
     * Draws all applicable entities on the radar.
     *
     * @param stack The matrix stack.
     */
    private fun drawEntitiesOnRadar(stack: Matrix3x2fStack) {
        val player = client.getPlayer()

        val yaw = player.yaw
        val entities = world.entities
        for (entity in entities) {
            if (!canRenderEntity(entity)) continue
            // get entity x and z relative to player
            val color = getColorFromEntity(entity)
            val point = getScaledPos(getPointRelativeToYaw(entity.entityPos, yaw))
            val box = BoxF(point, config.pointSize.toFloat(), config.pointSize.toFloat())
            GuiUtil.drawBox(
                color,
                box,
                stack,
                config.pointAlpha
            )
        }
    }

    /**
     * Gets the color for the entity on the radar.
     *
     * @param entity The entity to get the color for.
     *
     * @return The color for the entity on the radar. Defaults to white.
     */
    private fun getColorFromEntity(entity: Entity): Color {
        if (entity is PlayerEntity) return config.playerColor
        if (entity is ItemEntity) return config.itemColor
        if (entity.type.spawnGroup.isPeaceful) return config.peacefulMobColor
        return if (!entity.type.spawnGroup.isPeaceful) config.hostileMobColor else Colors.WHITE
    }

    /**
     * Gets the scaled position relative to the radar.
     *
     * @param location The location to scale.
     * @return A scaled position relative to the radar, clamped to the radar.
     */
    private fun getScaledPos(location: PointF): PointF {
        val halfConfigSize = config.size / 2f
        // offset the point to the center of the radar.
        return clampPoint(location)
            .add(RadarConfig.x, RadarConfig.y)
            .add(halfConfigSize, halfConfigSize)
            .subtract(PointF(pointOffset(), pointOffset()))
    }

    /**
     * Clamps the given point to the edges of the radar.
     *
     * @param point The point to clamp.
     * @return The clamped point.
     */
    private fun clampPoint(point: PointF): PointF {
        // scale point so that (0,0) is the center of the radar
        var newLoc = point
        val distance = newLoc.distance()
        val halfConfig = config.size / 2f
        val halfConfigOffset = halfConfig - pointOffset()
        val scale = (halfConfig) / distance
        if (distance > halfConfig)
            newLoc = newLoc.multiply(scale)
        if (newLoc.x < -halfConfigOffset) newLoc = PointF(-halfConfigOffset, newLoc.y)
        if (newLoc.x > halfConfigOffset) newLoc = PointF(halfConfigOffset, newLoc.y)
        if (newLoc.y < -halfConfigOffset) newLoc = PointF(newLoc.x, -halfConfigOffset)
        if (newLoc.y > halfConfigOffset) newLoc = PointF(newLoc.x, halfConfigOffset)
        return newLoc
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private fun canRenderEntity(entity: Entity): Boolean {
        if (entity is PlayerEntity) return config.isShowPlayer
        if (entity is MobEntity) {
            return if (!entity.getType().spawnGroup.isPeaceful) config.isShowHostileMob else config.isShowPeacefulMob
        }
        return if (entity is ItemEntity) config.isShowItem else false
    }

    /**
     * Gets the point relative to the waypoint and the player's yaw.
     *
     * @param loc The waypoint to get the position of.
     * @param yaw The yaw of the player.
     * @return A new PointF with the x and z values.
     */
    private fun getPointRelativeToYaw(loc: Vec3d?, yaw: Float): PointF {
        val player = client.getPlayer()
        val x = loc!!.getX() - player.x
        val z = loc.getZ() - player.z
        return calculateDistance(yaw, x, z)
    }

    companion object {

        /**
         * The configuration for this mod.
         */
        val config: RadarConfig = Settings.getConfig("radar")

        /**
         * Calculates the offset to draw the points on the radar.
         *
         * @return The offset to draw the points on the radar.
         */
        fun pointOffset(): Float {
            return (config.pointSize.toFloat()).div(2f)
        }

        /**
         * Calculates the position and distance of the given coordinates from the player.
         *
         * @param yaw The player yaw.
         * @param x   The x coordinate.
         * @param z   The z coordinate.
         * @return The position and distance of the given coordinates from the player.
         */
        private fun calculateDistance(yaw: Float, x: Double, z: Double): PointF {
            var x1 = x
            var z1 = z
            val atan = atan2(z1, x1)
            val angle = Math.toDegrees(atan) - yaw
            val distance = sqrt(x1 * x1 + z1 * z1)
            x1 = cos(Math.toRadians(angle)) * distance * -1
            z1 = sin(Math.toRadians(angle)) * distance * -1
            return PointF(x1, z1)
        }
    }
}

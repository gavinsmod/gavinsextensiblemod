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
package com.peasenet.mods.render

import com.peasenet.config.RadarConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.mod.render.GuiRadar
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A mod that allows for a radar-like view of the world.
 *
 * @author gt3ch1
 * @version 04-11-2023
 */
class ModRadar : RenderMod(
    "Radar",
    "gavinsmod.mod.render.radar",
    "radar"
), InGameHudRenderListener {
    /**
     * Creates a radar overlay in the top-right corner of the screen.
     */
    init {
        val clickSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.drawn")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiRadar()) }
            .buildClickSetting()
        addSetting(clickSetting)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onRenderInGameHud(drawContext: DrawContext, delta: Float) {
        var canRender = isActive && !Mods.isActive("gui") && !Mods.isActive("settings")
        canRender = canRender || GuiRadar.visible
        if (!canRender) return
        val stack = drawContext.matrices
        RadarConfig.x = client.window.scaledWidth - config.size - 10
        val radarBox = BoxF(
            PointF(RadarConfig.x.toFloat(), RadarConfig.y.toFloat()),
            config.size.toFloat(),
            config.size.toFloat()
        )
        GuiUtil.drawBox(
            config.backgroundColor, radarBox, stack, config.backgroundAlpha
        )
        drawEntitiesOnRadar(stack)
        GuiUtil.drawOutline(radarBox, stack)
    }

    /**
     * Draws all applicable entities on the radar.
     *
     * @param stack - The matrix stack.
     */
    private fun drawEntitiesOnRadar(stack: MatrixStack) {
        val player = client.getPlayer()

        val yaw = player.yaw
        val entities = world.entities
        for (entity in entities) {
            if (!canRenderEntity(entity)) continue
            // get entity x and z relative to player
            val color = getColorFromEntity(entity)
            val point = getScaledPos(getPointRelativeToYaw(entity.pos, yaw))
            GuiUtil.drawBox(
                color,
                BoxF(point, config.pointSize.toFloat(), config.pointSize.toFloat()),
                stack,
                config.pointAlpha
            )
        }
    }

    private fun getColorFromEntity(entity: Entity): Color {
        if (entity is PlayerEntity) return config.playerColor
        if (entity is ItemEntity) return config.itemColor
        if (entity.type.spawnGroup.isPeaceful) return config.peacefulMobColor
        return if (!entity.type.spawnGroup.isPeaceful) config.hostileMobColor else Colors.WHITE
    }

    /**
     * Gets the scaled position relative to the radar.
     *
     * @param location - The location to scale.
     * @return A scaled position relative to the radar, clamped to the radar.
     */
    private fun getScaledPos(location: PointF): PointF {
        // check the distance of w to the player.
        var newLoc = clampPoint(location)

        // offset the point to the center of the radar.
        newLoc = PointF(
            newLoc.x + RadarConfig.x + config.size / 2f,
            newLoc.y + RadarConfig.y + config.size / 2f
        )
        return newLoc
    }

    /**
     * Clamps the given point to the edges of the radar.
     *
     * @param point - The point to clamp.
     * @return The clamped point.
     */
    private fun clampPoint(point: PointF): PointF {
        var newPoint = point
        val offset = config.pointSize - pointOffset
        // if the point is touching any edges of the radar, clamp it to the edge.
        // right side
        if (newPoint.x >= config.size / 2f - offset) newPoint =
            PointF(config.size / 2f - offset, newPoint.y)
        // left side
        if (newPoint.x <= -config.size / 2f + pointOffset) newPoint =
            PointF(-config.size / 2f + pointOffset, newPoint.y)
        // bottom side
        if (newPoint.y >= config.size / 2f - offset) newPoint =
            PointF(newPoint.x, config.size / 2f - offset)
        // top side
        if (newPoint.y <= -config.size / 2f + pointOffset) newPoint =
            PointF(newPoint.x, -config.size / 2f + pointOffset)
        // offset the point to be centered

        return newPoint.subtract(PointF(pointOffset, pointOffset))
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
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
     * @param loc - The waypoint to get the position of.
     * @param yaw - The yaw of the player.
     * @return A new PointF with the x and z values.
     */
    private fun getPointRelativeToYaw(loc: Vec3d?, yaw: Float): PointF {
        val player = client.getPlayer()

        val x = loc!!.getX() - player.x
        val z = loc.getZ() - player.z
        return calculateDistance(yaw, x, z)
    }

    companion object {


        val config: RadarConfig
            get() {
                return Settings.getConfig<RadarConfig>("radar")
            }

        val pointOffset: Float
            /**
             * Calculates the offset to draw the points on the radar.
             *
             * @return The offset to draw the points on the radar.
             */
            get() = if (config.pointSize == 1) 0F else (config.pointSize - 1) / 2F

        /**
         * Calculates the position and distance of the given coordinates from the player.
         *
         * @param yaw - The player yaw.
         * @param x   - The x coordinate.
         * @param z   - The z coordinate.
         * @return The position and distance of the given coordinates from the player.
         */
        private fun calculateDistance(yaw: Float, x: Double, z: Double): PointF {
            var x1 = x
            var z1 = z
            val arctan = atan2(z1, x1)
            val angle = Math.toDegrees(arctan) - yaw
            val distance = sqrt(x1 * x1 + z1 * z1)
            x1 = cos(Math.toRadians(angle)) * distance * -1
            z1 = sin(Math.toRadians(angle)) * distance * -1
            return PointF(x1, z1)
        }
    }
}

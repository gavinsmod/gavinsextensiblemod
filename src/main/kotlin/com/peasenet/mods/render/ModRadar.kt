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
import com.peasenet.main.GavinsMod
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.settings.*
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
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
class ModRadar : Mod(Type.RADAR), InGameHudRenderListener {
    /**
     * Creates a radar overlay in the top-right corner of the screen.
     */
    init {
        val playerEntityColor = ColorSetting(
            "gavinsmod.settings.radar.player.color",
            radarConfig.playerColor
        )
        playerEntityColor.setCallback { radarConfig.playerColor = playerEntityColor.color }
        val hostileMobEntityColor = ColorSetting(
            "gavinsmod.settings.radar.mob.hostile.color",
            radarConfig.hostileMobColor
        )
        hostileMobEntityColor.setCallback { radarConfig.hostileMobColor = hostileMobEntityColor.color }
        hostileMobEntityColor.color = radarConfig.hostileMobColor
        val peacefulMobEntityColor = ColorSetting(
            "gavinsmod.settings.radar.mob.peaceful.color",
            radarConfig.peacefulMobColor
        )
        peacefulMobEntityColor.setCallback { radarConfig.peacefulMobColor = peacefulMobEntityColor.color }
        peacefulMobEntityColor.color = radarConfig.peacefulMobColor
        val entityItemColor = ColorSetting(
            "gavinsmod.settings.radar.item.color",
            radarConfig.itemColor
        )
        entityItemColor.setCallback { radarConfig.itemColor = entityItemColor.color }
        entityItemColor.color = radarConfig.itemColor
        val waypointColor = ColorSetting(
            "gavinsmod.settings.radar.waypoint.color",
            radarConfig.waypointColor
        )
        waypointColor.color = radarConfig.waypointColor
        scaleSetting = ClickSetting("gavinsmod.settings.radar.scale")
        pointSizeSetting = ClickSetting("gavinsmod.settings.radar.pointsize")
        val peacefulMobsSetting = ToggleSetting("gavinsmod.settings.radar.mob.peaceful")
        peacefulMobsSetting.setCallback { radarConfig.isShowPeacefulMob = peacefulMobsSetting.value }
        peacefulMobsSetting.value = radarConfig.isShowPeacefulMob
        val hostileMobsSetting = ToggleSetting("gavinsmod.settings.radar.mob.hostile")
        hostileMobsSetting.setCallback { radarConfig.isShowHostileMob = hostileMobsSetting.value }
        hostileMobsSetting.value = radarConfig.isShowHostileMob
        val itemsSetting = ToggleSetting("gavinsmod.settings.radar.item")
        itemsSetting.setCallback { radarConfig.isShowItem = itemsSetting.value }
        itemsSetting.value = radarConfig.isShowItem
        val waypointsSetting = ToggleSetting("gavinsmod.settings.radar.waypoints")
        waypointsSetting.setCallback { radarConfig.isShowWaypoint = waypointsSetting.value }
        waypointsSetting.value = radarConfig.isShowWaypoint
        val playerSetting = ToggleSetting("gavinsmod.settings.radar.player")
        playerSetting.setCallback { radarConfig.isShowPlayer = playerSetting.value }
        playerSetting.value = radarConfig.isShowPlayer
        val useWaypointColorSetting = ToggleSetting("gavinsmod.settings.radar.waypoint.usecolor")
        useWaypointColorSetting.setCallback { radarConfig.isUseWaypointColor = useWaypointColorSetting.value }
        useWaypointColorSetting.value = radarConfig.isUseWaypointColor
        val backgroundAlphaSetting = SlideSetting("gavinsmod.settings.radar.background.alpha")
        backgroundAlphaSetting.setCallback { radarConfig.backgroundAlpha = backgroundAlphaSetting.value }
        backgroundAlphaSetting.value = radarConfig.backgroundAlpha
        val pointAlphaSetting = SlideSetting("gavinsmod.settings.radar.point.alpha")
        pointAlphaSetting.setCallback { radarConfig.pointAlpha = pointAlphaSetting.value }
        pointAlphaSetting.value = radarConfig.pointAlpha
        val color = SubSetting(110, 10, "gavinsmod.settings.radar.color")
        val drawSettings = SubSetting(110, 10, "gavinsmod.settings.radar.drawn")
        color.add(playerEntityColor)
        color.add(hostileMobEntityColor)
        color.add(peacefulMobEntityColor)
        color.add(entityItemColor)
        color.add(waypointColor)
        scaleSetting.setCallback { increaseScale() }
        pointSizeSetting.setCallback { togglePointSize() }
        drawSettings.add(pointSizeSetting)
        drawSettings.add(scaleSetting)
        drawSettings.add(peacefulMobsSetting)
        drawSettings.add(hostileMobsSetting)
        drawSettings.add(itemsSetting)
        drawSettings.add(waypointsSetting)
        drawSettings.add(playerSetting)
        drawSettings.add(useWaypointColorSetting)
        drawSettings.add(backgroundAlphaSetting)
        drawSettings.add(pointAlphaSetting)
        drawSettings.add(color)
        addSetting(drawSettings)
        updateScaleText(pointSizeSetting, radarConfig.pointSize)
        updateScaleText(scaleSetting, radarConfig.scale)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    /**
     * Callback method for the scale setting.
     */
    private fun increaseScale() {
        radarConfig.scale = radarConfig.scale + 1
        updateScaleText(scaleSetting, radarConfig.scale)
    }

    override fun onRenderInGameHud(stack: MatrixStack, delta: Float) {
        if (!isActive) return
        RadarConfig.x = client.window.scaledWidth - RadarConfig.size - 10
        GuiUtil.drawBox(
            Colors.DARK_GRAY, BoxF(
                PointF(RadarConfig.x.toFloat(), RadarConfig.y.toFloat()),
                radarConfig.size.toFloat(),
                radarConfig.size.toFloat()
            ), stack, radarConfig.backgroundAlpha
        )
        drawEntitiesOnRadar(stack)
        if (radarConfig.isShowWaypoint) drawWaypointsOnRadar(stack)
    }

    /**
     * Draws enabled waypoints on the radar.
     *
     * @param stack - The matrix stack.
     */
    private fun drawWaypointsOnRadar(stack: MatrixStack) {
        val player = client.getPlayer()

        val yaw = player.yaw
        val waypoints =
                GavinsMod.waypointConfig.getLocations().stream().filter { obj: Waypoint -> obj.isEnabled }.toList()
        for (w in waypoints) {
            var color = w.color
            if (!radarConfig.isUseWaypointColor) color = radarConfig.waypointColor
            val location = getScaledPos(getPointRelativeToYaw(w.pos, yaw))
            GuiUtil.drawBox(
                color,
                BoxF(location, radarConfig.pointSize.toFloat(), radarConfig.pointSize.toFloat()),
                stack,
                radarConfig.pointAlpha
            )
        }
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
                BoxF(point, radarConfig.pointSize.toFloat(), radarConfig.pointSize.toFloat()),
                stack,
                radarConfig.pointAlpha
            )
        }
    }

    private fun getColorFromEntity(entity: Entity): Color {
        if (entity is PlayerEntity) return radarConfig.playerColor
        if (entity is ItemEntity) return radarConfig.itemColor
        if (entity.type.spawnGroup.isPeaceful) return radarConfig.peacefulMobColor
        return if (!entity.type.spawnGroup.isPeaceful) radarConfig.hostileMobColor else Colors.WHITE
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
            newLoc.x + RadarConfig.x + radarConfig.size / 2f,
            newLoc.y + RadarConfig.y + radarConfig.size / 2f
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
        val offset = radarConfig.pointSize - pointOffset
        // if the point is touching any edges of the radar, clamp it to the edge.
        if (newPoint.x >= radarConfig.size / 2f - offset) newPoint =
            PointF(radarConfig.size / 2f - offset, newPoint.y)
        if (newPoint.x <= -radarConfig.size / 2f + offset) newPoint =
            PointF(-radarConfig.size / 2f + offset, newPoint.y)
        if (newPoint.y >= radarConfig.size / 2f - offset) newPoint =
            PointF(newPoint.x, radarConfig.size / 2f - offset)
        if (newPoint.y <= -radarConfig.size / 2f + offset) newPoint =
            PointF(newPoint.x, -radarConfig.size / 2f + offset)
        // offset the point to be centered

        return newPoint.subtract(PointF(pointOffset, pointOffset))
    }

    /**
     * Callback for the point size setting.
     */
    private fun togglePointSize() {
        radarConfig.updatePointSizeCallback()
        updateScaleText(pointSizeSetting, radarConfig.pointSize)
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private fun canRenderEntity(entity: Entity): Boolean {
        if (entity is PlayerEntity) return radarConfig.isShowPlayer
        if (entity is MobEntity) {
            return if (!entity.getType().spawnGroup.isPeaceful) radarConfig.isShowHostileMob else radarConfig.isShowPeacefulMob
        }
        return if (entity is ItemEntity) radarConfig.isShowItem else false
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
        /**
         * The setting relating to the radar scale.
         */
        private lateinit var scaleSetting: ClickSetting

        /**
         * The setting relating to the radar point size.
         */
        private lateinit var pointSizeSetting: ClickSetting

        /**
         * Updates the title for the given setting.
         *
         * @param setting - The setting to change the title for.
         * @param value   - The integer value to append to the title.
         */
        private fun updateScaleText(setting: ClickSetting, value: Int) {
            setting.setTitle(Text.translatable(setting.translationKey).append(Text.literal(" (%s)".format(value))))
        }

        val pointOffset: Float
            /**
             * Calculates the offset to draw the points on the radar.
             *
             * @return The offset to draw the points on the radar.
             */
            get() = if (radarConfig.pointSize == 1) 0F else radarConfig.pointSize / 2F

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
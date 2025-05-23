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

import com.peasenet.config.tracer.TracerConfig
import com.peasenet.config.waypoint.WaypointConfig
import com.peasenet.extensions.toVec3d
import com.peasenet.gui.mod.waypoint.GuiWaypoint
import com.peasenet.main.Settings
import com.peasenet.mods.ModCategory
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.util.Dimension
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.CameraBob
import com.peasenet.util.listeners.CameraBobListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box

/**
 * Creates a new mod to control waypoints.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 03-02-2023
 */
class ModWaypoint : RenderMod(
    "gavinsmod.mod.render.waypoints", "waypoints", ModCategory.WAYPOINTS
), RenderListener, CameraBobListener {

    init {
        reloadSettings()
    }


    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
        em.subscribe(CameraBobListener::class.java, this)
        for (w in Settings.getConfig<WaypointConfig>("waypoints").getLocations()) {
            if (!w.hasDimensions()) {
                PlayerUtils.sendMessage(
                    "§6[WARNING]§7 Waypoint \"§b${w.name}§7\" has no dimensions set and will not be rendered.", true
                )
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(RenderListener::class.java, this)
        em.unsubscribe(CameraBobListener::class.java, this)
    }

    override fun reloadSettings() {
        modSettings.clear()
        clickSetting {
            title = "gavinsmod.settings.render.waypoints.add"
            callback = { MinecraftClient.getInstance().setScreen(GuiWaypoint()) }
            symbol = '+'
        }
        val waypoints = Settings.getConfig<WaypointConfig>("waypoints").getLocations().stream()
            .sorted(Comparator.comparing { obj: Waypoint -> obj.name })
        for (w in waypoints.toArray()) createWaypoint(w as Waypoint)
    }

    /**
     * Creates a new setting to edit the given waypoint and adds it to the given setting.
     *
     * @param waypoint - The waypoint to edit.
     */
    private fun createWaypoint(waypoint: Waypoint) {
        clickSetting {
            title = waypoint.name
            callback = { MinecraftClient.getInstance().setScreen(GuiWaypoint(waypoint)) }
            hoverable = true
            color = waypoint.color
            transparency = 0.5f
        }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        val playerDimension = Dimension.fromValue(MinecraftClient.getInstance().player!!.world.dimension.effects.path!!)
        val waypointLocs =
            Settings.getConfig<WaypointConfig>("waypoints").getLocations().filter { w -> w.canRender(playerDimension) }
        if (waypointLocs.isEmpty()) return
        matrixStack.push()
        for (w in waypointLocs) {
            val pos = w.coordinates.toVec3d()
            val bb = Box(
                pos.x + 1, pos.y, pos.z + 1, pos.x, pos.y + 1.0, pos.z
            )
            if (w.renderEsp) RenderUtils.drawOutlinedBox(bb, matrixStack, w.color)
            if (w.renderTracer) {
                val origin = RenderUtils.getLookVec(partialTicks).multiply(10.0)
                RenderUtils.drawSingleLine(
                    matrixStack, origin, bb.center, w.color
                )
            }
        }
        matrixStack.pop()
//        RenderUtils.cleanupRender(matrixStack)
    }

    override fun onCameraViewBob(c: CameraBob) {
        if (Settings.getConfig<TracerConfig>("tracer").viewBobCancel) c.cancel()
    }
}

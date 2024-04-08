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

import com.peasenet.config.WaypointConfig
import com.peasenet.config.TracerConfig
import com.peasenet.gui.mod.waypoint.GuiWaypoint
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.mods.ModCategory
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.settings.ClickSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.SubSetting
import com.peasenet.util.Dimension
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.CameraBob
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.CameraBobListener
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.client.MinecraftClient

import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import java.util.function.Function

/**
 * @author gt3ch1
 * @version 03-02-2023
 * Creates a new mod to control waypoints.
 */
class ModWaypoint : RenderMod(
    "Waypoints",
    "gavinsmod.mod.render.waypoints",
    "waypoints",
    ModCategory.WAYPOINTS
), EntityRenderListener, CameraBobListener {
    init {
        reloadSettings()
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderListener::class.java, this)
        em.subscribe(CameraBobListener::class.java, this)
        for (w in Settings.getConfig<WaypointConfig>("waypoints").getLocations()) {
            if (!w.hasDimensions()) {
                PlayerUtils.sendMessage(
                    "§6[WARNING]§7 Waypoint \"§b${w.name}§7\" has no dimensions set and will not be rendered.",
                    true
                )
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderListener::class.java, this)
        em.unsubscribe(CameraBobListener::class.java, this)
    }

    override fun reloadSettings() {
        modSettings.clear()
//        setting = SubSetting(setting.gui.width.toInt(), 10, "gavinsmod.mod.render.waypoints")
        setting = SettingBuilder()
            .setWidth(100f)
            .setHeight(10f)
            .setTitle("gavinsmod.mod.render.waypoints")
            .buildSubSetting()
        openMenu = SettingBuilder()
            .setTitle("gavinsmod.settings.render.waypoints.add")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiWaypoint()) }
            .setSymbol('+')
            .buildClickSetting()
        addSetting(openMenu)
        // get all waypoints and add them to the menu
        val waypoints = Settings.getConfig<WaypointConfig>("waypoints").getLocations().stream()
            .sorted(Comparator.comparing(Function<Waypoint, String> { obj: Waypoint -> obj.name }))
        for (w in waypoints.toArray()) createWaypoint(w as Waypoint)
    }

    /**
     * Creates a new setting to edit the given waypoint and adds it to the given setting.
     *
     * @param waypoint - The waypoint to edit.
     */
    private fun createWaypoint(waypoint: Waypoint) {
        val clickSetting = SettingBuilder()
            .setTitle(Text.literal(waypoint.name))
            .setCallback {
                MinecraftClient.getInstance().setScreen(GuiWaypoint(waypoint))
            }
            .setHoverable(true)
            .setBackgroundColor(waypoint.color!!)
            .setTransparency(0.5f)
            .buildClickSetting()
        addSetting(clickSetting)
    }

    override fun onEntityRender(er: EntityRender) {
        val playerDimension = Dimension.fromValue(MinecraftClient.getInstance().player!!.world.dimension.effects.path!!)
        Settings.getConfig<WaypointConfig>("waypoints").getLocations().stream().filter { obj: Waypoint ->
            obj.canRender(playerDimension)
        }.forEach { w: Waypoint ->
            val aabb = Box(BlockPos(w.x, w.y, w.z))
            val boxPos = aabb.center
            if (w.isTracerEnabled) RenderUtils.renderSingleLine(
                er.stack,
                er.buffer!!,
                er.playerPos!!,
                boxPos,
                w.color!!
            )
            if (w.isEspEnabled) RenderUtils.drawBox(er.stack, er.buffer, aabb, w.color!!)
        }
    }

    override fun onCameraViewBob(c: CameraBob) {
        if (Settings.getConfig<TracerConfig>("tracer").viewBobCancel) c.cancel()
    }

    companion object {
        private lateinit var setting: SubSetting
        private lateinit var openMenu: ClickSetting
    }
}

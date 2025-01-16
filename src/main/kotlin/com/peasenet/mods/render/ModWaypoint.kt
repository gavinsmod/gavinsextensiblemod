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
package com.peasenet.mods.render

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.TracerConfig
import com.peasenet.config.WaypointConfig
import com.peasenet.extensions.toVec3d
import com.peasenet.gui.mod.waypoint.GuiWaypoint
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
import com.peasenet.util.listeners.CameraBobListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.ShaderProgramKeys
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
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
        setting =
            SettingBuilder().setWidth(100f).setHeight(10f).setTitle("gavinsmod.mod.render.waypoints").buildSubSetting()
        openMenu = SettingBuilder().setTitle("gavinsmod.settings.render.waypoints.add")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiWaypoint()) }.setSymbol('+').buildClickSetting()
        addSetting(openMenu)
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
        val clickSetting = SettingBuilder().setTitle(Text.literal(waypoint.name)).setCallback {
            MinecraftClient.getInstance().setScreen(GuiWaypoint(waypoint))
        }.setHoverable(true).setBackgroundColor(waypoint.color).setTransparency(0.5f).buildClickSetting()
        addSetting(clickSetting)
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        val playerDimension = Dimension.fromValue(MinecraftClient.getInstance().player!!.world.dimension.effects.path!!)
        val waypointLocs =
            Settings.getConfig<WaypointConfig>("waypoints").getLocations().filter { w -> w.canRender(playerDimension) }
        if (waypointLocs.isEmpty()) return;
        RenderUtils.setupRender(matrixStack)
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR)
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        for (w in waypointLocs) {
            val pos = RenderUtils.offsetPosWithCamera(w.coordinates.toVec3d())
            val bb = Box(
                pos.x + 1, pos.y, pos.z + 1, pos.x, pos.y + 1.0, pos.z
            )
            if (w.renderEsp) RenderUtils.drawOutlinedBox(bb, bufferBuilder, entry, w.color)
            if (w.renderTracer) {
                RenderUtils.drawSingleLine(
                    bufferBuilder, entry, partialTicks, bb.center, w.color
                )
            }
        }
        RenderUtils.drawBuffer(bufferBuilder)
    }

    override fun onCameraViewBob(c: CameraBob) {
        if (Settings.getConfig<TracerConfig>("tracer").viewBobCancel) c.cancel()
    }

    companion object {
        private lateinit var setting: SubSetting
        private lateinit var openMenu: ClickSetting
    }
}

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
package com.peasenet.gui

import com.peasenet.config.esp.EspConfig
import com.peasenet.config.misc.MiscConfig
import com.peasenet.config.tracer.TracerConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsMod.Companion.setEnabled
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.settings.Setting
import com.peasenet.settings.slideSetting
import com.peasenet.settings.toggleSetting
import net.minecraft.text.Text
import java.util.function.Consumer

/**
 * A settings gui to control certain features of the mod.
 * @author GT3CH1
 * @since 04-11-2023
 * @version 01-12-2025
 */
class GuiSettings : GuiElement(Text.translatable("gavinsmod.gui.settings")) {
    /**
     * Creates a new GUI settings screen.
     */

    init {
        renderDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(10f, 20f))
            .setWidth(100)
            .setHeight(10)
            .setTitle("gavinsmod.settings.render")
            .setDraggable(true)
            .buildScroll()
        miscDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(115f, 20f))
            .setWidth(100)
            .setHeight(10)
            .setTitle("gavinsmod.settings.misc")
            .setDraggable(true)
            .buildScroll()
        guiDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(220f, 20f))
            .setWidth(100)
            .setHeight(10)
            .setTitle("gavinsmod.settings.gui")
            .setDraggable(true)
            .setHidden(false)
            .buildScroll()
        waypointDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(325f, 20f))
            .setWidth(100)
            .setHeight(10)
            .setTitle("gavinsmod.mod.render.waypoints")
            .setDraggable(true)
            .buildScroll()
        espDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(10f, 90f))
            .setWidth(110)
            .setHeight(10)
            .setTitle("gavinsmod.settings.esp")
            .setDraggable(true)
            .buildScroll()
        tracerDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(125f, 90f))
            .setWidth(115)
            .setHeight(10)
            .setTitle("gavinsmod.settings.tracer")
            .setDraggable(true)
            .buildScroll()
        combatDropdown = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(245f, 90f))
            .setWidth(100)
            .setHeight(10)
            .setTitle("gavinsmod.settings.combat")
            .setDraggable(true)
            .buildScroll()
        resetButton = GuiBuilder<GuiScroll>()
            .setTopLeft(PointF(0f, 1f))
            .setWidth(4)
            .setHeight(10)
            .setTitle("gavinsmod.settings.reset")
            .setDraggable(true)
            .buildClick()
        reloadGui()
        initialized = true
    }

    override fun init() {
        super.init()
        val titleW = textRenderer.getWidth(Text.translatable("gavinsmod.gui.settings")) + 16
        val resetText = Text.translatable("gavinsmod.settings.reset")
        val width = textRenderer.getWidth(resetText)
        if (resetPos == null) resetPos = PointF(titleW.toFloat(), 1f)
        resetButton.title = resetText
        if (resetWidth.toDouble() == 0.0) resetWidth = (width + 4).toFloat()
        resetButton.width = resetWidth
        resetButton.position = resetPos!!
        resetButton.setDefaultPosition(resetButton.box)
        resetButton.setBackground(Colors.DARK_RED)
        resetButton.callback = {
            GavinsMod.gui.reset()
            GavinsMod.guiSettings.reset()
        }
        resetButton.isHoverable = true
    }

    override fun close() {
        setEnabled("settings", false)
        super.close()
    }

    /**
     * Reloads this gui by clearing all children, and recreating them.
     */
    fun reloadGui() {
        guis.forEach(Consumer { obj: Gui -> obj.clearChildren() })
        guis.clear()
        miscSettings()
        addSettings(tracerDropdown, ModCategory.TRACERS)
        addSettings(espDropdown, ModCategory.ESP)
        addSettings(renderDropdown, ModCategory.RENDER)
        addSettings(miscDropdown, ModCategory.MISC)
        addSettings(guiDropdown, ModCategory.GUI)
        addSettings(combatDropdown, ModCategory.COMBAT)
        addSettings(waypointDropdown, ModCategory.WAYPOINTS)
        if (tracerDropdown.hasChildren())
            guis.add(tracerDropdown)
        if (espDropdown.hasChildren())
            guis.add(espDropdown)
        if (renderDropdown.hasChildren())
            guis.add(renderDropdown)
        if (miscDropdown.hasChildren())
            guis.add(miscDropdown)
        if (guiDropdown.hasChildren())
            guis.add(guiDropdown)
        if (waypointDropdown.hasChildren())
            guis.add(waypointDropdown)
        if (combatDropdown.hasChildren())
            guis.add(combatDropdown)
        guis.forEach(Consumer { g: Gui -> g.isParent = true })
        guis.add(resetButton)
    }

    /**
     * Creates the esp settings scroll gui.
     *
     * @param parent   - The parent gui.
     * @param category - The category of mod types to add to the parent gui.
     */
    private fun addSettings(parent: Gui, category: ModCategory) {
        val modList = ArrayList<Mod>()
        // get all mods in esp category and have settings then add them to espDropdown
        Mods.mods.stream().filter { m: Mod -> m.modCategory === category && m.hasSettings() }
            .forEach { e: Mod -> modList.add(e) }
        for (m in modList) {
            val modSettings = m.settings
            for (s in modSettings) {
                s.setShrunkForScrollbar(false)
                parent.addElement(s)
            }
        }
    }

    companion object {

        var initialized: Boolean = false

        /**
         * The tracer dropdown
         */
        private lateinit var tracerDropdown: GuiScroll

        /**
         * The esp dropdown
         */
        private lateinit var espDropdown: GuiScroll

        /**
         * The render dropdown
         */
        private lateinit var renderDropdown: GuiScroll

        /**
         * The miscellaneous dropdown
         */
        private lateinit var miscDropdown: GuiScroll


        private lateinit var waypointDropdown: GuiScroll

        /**
         * The dropdown containing gui settings.
         */
        private lateinit var guiDropdown: GuiScroll
        private lateinit var combatDropdown: GuiScroll
        private lateinit var resetButton: GuiClick
        private var resetWidth = 0f
        private var resetPos: PointF? = null

        /**
         * Sets up miscellaneous settings that don't really have a proper
         * home.
         */
        private fun miscSettings() {
            espDropdown.addElement(slideSetting {
                title = "gavinsmod.settings.alpha"
                value = Settings.getConfig<EspConfig>("esp").alpha
                callback = { Settings.getConfig<EspConfig>("esp").alpha = it.value }
            })
            espDropdown.addElement(slideSetting {
                title = "gavinsmod.settings.size"
                value = Settings.getConfig<EspConfig>("esp").espSize
                callback = {
                    val size = 0.25f + (it.value * 0.75f)
                    Settings.getConfig<EspConfig>("esp").espSize = size
                }
            })
            miscDropdown.addElement(toggleSetting {
                title = "gavinsmod.generic.background"
                state = Settings.getConfig<MiscConfig>("misc").background
                callback = { Settings.getConfig<MiscConfig>("misc").background = it.state }
            })
            tracerDropdown.addElement(slideSetting {
                title = "gavinsmod.generic.alpha"
                value = Settings.getConfig<TracerConfig>("tracer").alpha
                callback = { Settings.getConfig<TracerConfig>("tracer").alpha = it.value }
            })
            tracerDropdown.addElement(toggleSetting {
                title = "gavinsmod.settings.tracer.viewbobcancel"
                state = Settings.getConfig<TracerConfig>("tracer").viewBobCancel
                callback = { Settings.getConfig<TracerConfig>("tracer").viewBobCancel = it.state }
            })
        }
    }
}

fun GuiScroll.addElement(setting: Setting) {
    addElement(setting.gui!!)
}
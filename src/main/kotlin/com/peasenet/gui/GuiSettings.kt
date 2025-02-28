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
import com.peasenet.settings.SettingBuilder
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
        renderDropdown = GuiScroll(PointF(10f, 20f), 100, 10, Text.translatable("gavinsmod.settings.render"))
        miscDropdown = GuiScroll(PointF(115f, 20f), 100, 10, Text.translatable("gavinsmod.settings.misc"))
        guiDropdown = GuiScroll(PointF(220f, 20f), 100, 10, Text.translatable("gavinsmod.settings.gui"))
        waypointDropdown = GuiScroll(PointF(325f, 20f), 100, 10, Text.translatable("gavinsmod.mod.render.waypoints"))
        espDropdown = GuiScroll(PointF(10f, 90f), 110, 10, Text.translatable("gavinsmod.settings.esp"))
        tracerDropdown = GuiScroll(PointF(125f, 90f), 115, 10, Text.translatable("gavinsmod.settings.tracer"))
        combatDropdown = GuiScroll(PointF(245f, 90f), 100, 10, Text.translatable("gavinsmod.settings.combat"))
        resetButton = GuiClick(PointF(0f, 1f), 4, 10, Text.translatable("gavinsmod.settings.reset"))
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
        resetButton.position = resetPos
        resetButton.setDefaultPosition(resetButton.box)
        resetButton.setBackground(Colors.DARK_RED)
        resetButton.setCallback {
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
            val espAlpha = SettingBuilder()
                .setTitle("gavinsmod.settings.alpha")
                .setValue(Settings.getConfig<EspConfig>("esp").alpha)
                .buildSlider()
            espAlpha.setCallback { Settings.getConfig<EspConfig>("esp").alpha = espAlpha.value }
            espDropdown.addElement(espAlpha.gui)

            val espSizeSlider = SettingBuilder()
                .setTitle("gavinsmod.settings.size")
                .setValue(Settings.getConfig<EspConfig>("esp").espSize)
                .buildSlider()
            espSizeSlider.setCallback {
                val size = 0.25f + (espSizeSlider.value * 0.75f)
                Settings.getConfig<EspConfig>("esp").espSize = size
            }
            espDropdown.addElement(espSizeSlider.gui)

            val backgroundOverlay = SettingBuilder()
                .setTitle("gavinsmod.generic.background")
                .setState(Settings.getConfig<MiscConfig>("misc").background)
                .buildToggleSetting()
            backgroundOverlay.setCallback {
                Settings.getConfig<MiscConfig>("misc").background = backgroundOverlay.value
            }
            miscDropdown.addElement(backgroundOverlay.gui)

            val tracerAlpha = SettingBuilder()
                .setTitle("gavinsmod.generic.alpha")
                .setValue(Settings.getConfig<TracerConfig>("tracer").alpha)
                .buildSlider()
            tracerAlpha.setCallback { Settings.getConfig<TracerConfig>("tracer").alpha = tracerAlpha.value }

            val tracerViewBob = SettingBuilder()
                .setTitle("gavinsmod.settings.tracer.viewbobcancel")
                .setState(Settings.getConfig<TracerConfig>("tracer").viewBobCancel)
                .buildToggleSetting()
            tracerViewBob.setCallback {
                Settings.getConfig<TracerConfig>("tracer").viewBobCancel = tracerViewBob.value
            }
            tracerDropdown.addElement(tracerViewBob.gui)
            tracerDropdown.addElement(tracerAlpha.gui)
        }
    }
}

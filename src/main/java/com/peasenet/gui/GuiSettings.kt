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
package com.peasenet.gui

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsMod.Companion.setEnabled
import com.peasenet.main.Mods.Companion.mods
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.settings.SlideSetting
import net.minecraft.text.Text
import java.util.function.Consumer

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A settings gui to control certain features of the mod.
 */
class GuiSettings : GuiElement(Text.translatable("gavinsmod.gui.settings")) {
    /**
     * Creates a new GUI settings screen.
     */
    init {
        renderDropdown = GuiScroll(PointF(10f, 20f), 100, 10, Text.translatable("gavinsmod.settings.render"))
        miscDropdown = GuiScroll(PointF(115f, 20f), 100, 10, Text.translatable("gavinsmod.settings.misc"))
        guiDropdown = GuiScroll(PointF(220f, 20f), 100, 10, Text.translatable("gavinsmod.settings.gui"))
        espDropdown = GuiScroll(PointF(10f, 130f), 110, 10, Text.translatable("gavinsmod.settings.esp"))
        tracerDropdown = GuiScroll(PointF(125f, 130f), 115, 10, Text.translatable("gavinsmod.settings.tracer"))
        resetButton = GuiClick(PointF(0f, 1f), 4, 10, Text.translatable("gavinsmod.settings.reset"))
        reloadGui()
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
            GavinsMod.gui!!.reset()
            GavinsMod.guiSettings!!.reset()
        }
        resetButton.isHoverable = true
    }

    override fun close() {
        setEnabled(Type.SETTINGS, false)
        super.close()
    }

    /**
     * Reloads this gui by clearing all children, and recreating them.
     */
    fun reloadGui() {
        guis.forEach(Consumer { obj: Gui -> obj.clearChildren() })
        guis.clear()
        // Create a plain gui in the top right corner
        miscSettings()
        addSettings(tracerDropdown, Type.Category.TRACERS)
        addSettings(espDropdown, Type.Category.ESPS)
        addSettings(renderDropdown, Type.Category.RENDER)
        addSettings(miscDropdown, Type.Category.MISC)
        addSettings(guiDropdown, Type.Category.GUI)
        guis.add(tracerDropdown)
        guis.add(espDropdown)
        guis.add(renderDropdown)
        guis.add(miscDropdown)
        guis.add(guiDropdown)
        guis.forEach(Consumer { g: Gui -> g.isParent = true })
        guis.add(resetButton)
    }

    /**
     * Creates the esp settings scroll gui.
     *
     * @param parent   - The parent gui.
     * @param category - The category of mod types to add to the parent gui.
     */
    private fun addSettings(parent: Gui, category: Type.Category) {
        val modList = ArrayList<Mod>()
        // get all mods in esp category and have settings then add them to espDropdown
        mods.stream().filter { m: Mod -> m.category === category && m.hasSettings() }
            .forEach { e: Mod -> modList.add(e) }
        for (m in modList) {
            val modSettings = m.settings
            for (s in modSettings) {
                s.gui?.setShrunkForScrollbar(false)
                parent.addElement(s.gui)
            }
        }
    }

    companion object {
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

        /**
         * The dropdown containing gui settings.
         */
        private lateinit var guiDropdown: GuiScroll
        private lateinit var resetButton: GuiClick
        private var resetWidth = 0f
        private var resetPos: PointF? = null

        /**
         * Sets up miscellaneous settings that don't really have a proper
         * home.
         */
        private fun miscSettings() {
            val espAlpha = SlideSetting("gavinsmod.settings.alpha")
            espAlpha.setCallback { GavinsMod.espConfig!!.alpha = espAlpha.value }
            espAlpha.value = GavinsMod.espConfig!!.alpha
            val tracerAlpha = SlideSetting("gavinsmod.settings.alpha")
            tracerAlpha.setCallback { GavinsMod.tracerConfig!!.alpha = tracerAlpha.value }
            tracerAlpha.value = GavinsMod.tracerConfig!!.alpha
            espDropdown.addElement(espAlpha.gui)
            tracerDropdown.addElement(tracerAlpha.gui)
        }
    }
}
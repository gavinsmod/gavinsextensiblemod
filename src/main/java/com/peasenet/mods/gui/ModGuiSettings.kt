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
package com.peasenet.mods.gui

import com.peasenet.gavui.util.GavUISettings
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SlideSetting
import com.peasenet.settings.ToggleSetting

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the player to configure certain settings of gavinsmod.
 */
class ModGuiSettings : Mod(Type.SETTINGS) {
    init {
        val guiSounds = ToggleSetting("gavinsmod.settings.gui.sound")
        guiSounds.setCallback { GavUISettings.add("gui.sound", guiSounds.value) }
        guiSounds.value = GavUISettings.getBool("gui.sound")

        val fgColor = GavUISettings.getColor("gui.color.foreground")
        val bgColor = GavUISettings.getColor("gui.color.background")
        val catColor = GavUISettings.getColor("gui.color.category")
        val enColor = GavUISettings.getColor("gui.color.enabled")
        val disColor = GavUISettings.getColor("gui.color.disabled")
        val borderColor = GavUISettings.getColor("gui.color.border")
        val frozenColor = GavUISettings.getColor("gui.color.frozen")


        val foregroundColorSetting =
            ColorSetting("gavinsmod.settings.gui.color.foreground", GavUISettings.getColor("gui.color.foreground"))
        foregroundColorSetting.setCallback { GavUISettings.add("gui.color.foreground", foregroundColorSetting.color) }

        val backgroundColorSetting = ColorSetting("gavinsmod.settings.gui.color.background", bgColor)
        backgroundColorSetting.setCallback { GavUISettings.add("gui.color.background", backgroundColorSetting.color) }

        val categoryColorSetting = ColorSetting("gavinsmod.settings.gui.color.category", catColor)
        categoryColorSetting.setCallback { GavUISettings.add("gui.color.category", categoryColorSetting.color) }

        val enabledColorSetting = ColorSetting("gavinsmod.settings.gui.color.enabled", enColor)
        enabledColorSetting.setCallback { GavUISettings.add("gui.color.enabled", enabledColorSetting.color) }

        val borderColorSetting = ColorSetting("gavinsmod.settings.gui.color.border", borderColor)
        borderColorSetting.setCallback { GavUISettings.add("gui.color.border", borderColorSetting.color) }

        val frozenColorSetting = ColorSetting("gavinsmod.settings.gui.color.frozen", frozenColor)
        frozenColorSetting.setCallback { GavUISettings.add("gui.color.frozen", frozenColorSetting.color) }
        val guiAlpha = SlideSetting("gavinsmod.settings.alpha")
        guiAlpha.setCallback { GavUISettings.add("gui.alpha", guiAlpha.value) }
        guiAlpha.value = GavUISettings.getFloat("gui.alpha")
        addSetting(guiAlpha)
        addSetting(backgroundColorSetting)
        addSetting(foregroundColorSetting)
        addSetting(categoryColorSetting)
        addSetting(enabledColorSetting)
        addSetting(frozenColorSetting)
        addSetting(borderColorSetting)
        addSetting(guiSounds)
    }

    override fun activate() {
        GavinsModClient.minecraftClient.setScreen(GavinsMod.guiSettings)
        setEnabled(true)
    }

    override fun deactivate() {
        setEnabled(false)
    }
}
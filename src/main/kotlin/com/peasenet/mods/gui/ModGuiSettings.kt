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
package com.peasenet.mods.gui

import com.peasenet.gavui.util.GavUISettings
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.SlideSetting
import com.peasenet.settings.ToggleSetting
import org.lwjgl.glfw.GLFW

/**
 * A mod that allows the player to configure certain settings of gavinsmod.
 * @author GT3CH1
 * @since 03-13-2023
 * @version 01-15-2025
 */
class ModGuiSettings : GuiMod(
    "gavinsmod.settings.gui",
    "settings",
    GLFW.GLFW_KEY_O
) {
    init {
        val guiSounds = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.gui.sound")
            .setState(GavUISettings.getBool("gui.sound"))
            .setCallback { GavUISettings.add("gui.sound", it.value) }
            .buildToggleSetting()

        val fgColor = GavUISettings.getColor("gui.color.foreground")
        val bgColor = GavUISettings.getColor("gui.color.background")
        val catColor = GavUISettings.getColor("gui.color.category")
        val enColor = GavUISettings.getColor("gui.color.enabled")
        val borderColor = GavUISettings.getColor("gui.color.border")
        val frozenColor = GavUISettings.getColor("gui.color.frozen")

        val foregroundColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.foreground")
            .setColor(fgColor)
            .setCallback { GavUISettings.add("gui.color.foreground", it.color) }
            .buildColorSetting()

        val backgroundColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.background")
            .setColor(bgColor)
            .setCallback { GavUISettings.add("gui.color.background", it.color) }
            .buildColorSetting()

        val categoryColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.category")
            .setColor(catColor)
            .setCallback { GavUISettings.add("gui.color.category", it.color) }
            .buildColorSetting()

        val enabledColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.enabled")
            .setColor(enColor)
            .setCallback { GavUISettings.add("gui.color.enabled", it.color) }
            .buildColorSetting()

        val borderColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.border")
            .setColor(borderColor)
            .setCallback { GavUISettings.add("gui.color.border", it.color) }
            .buildColorSetting()

        val frozenColorSetting = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.gui.color.frozen")
            .setColor(frozenColor)
            .setCallback { GavUISettings.add("gui.color.frozen", it.color) }
            .buildColorSetting()

        val guiAlpha = SettingBuilder<SlideSetting>().setTitle("gavinsmod.settings.alpha")
            .setValue(GavUISettings.getFloat("gui.alpha"))
            .setCallback { GavUISettings.add("gui.alpha", it.value) }
            .buildSlideSetting()
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
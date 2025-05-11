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
        val fgColor = GavUISettings.getColor("gui.color.foreground")
        val bgColor = GavUISettings.getColor("gui.color.background")
        val catColor = GavUISettings.getColor("gui.color.category")
        val enColor = GavUISettings.getColor("gui.color.enabled")
        val borderColor = GavUISettings.getColor("gui.color.border")
        val frozenColor = GavUISettings.getColor("gui.color.frozen")
        colorSetting {
            title = "gavinsmod.settings.gui.color.foreground"
            color = fgColor
            callback = {
                GavUISettings.add("gui.color.foreground", it.color)
            }
        }
        colorSetting {
            title = "gavinsmod.settings.gui.color.background"
            color = bgColor
            callback = { GavUISettings.add("gui.color.background", it.color) }
        }
        colorSetting {
            title = "gavinsmod.settings.gui.color.category"
            color = catColor
            callback = { GavUISettings.add("gui.color.category", it.color) }
        }
        colorSetting {
            title = "gavinsmod.settings.gui.color.enabled"
            color = enColor
            callback = { GavUISettings.add("gui.color.enabled", it.color) }
        }
        colorSetting {
            title = "gavinsmod.settings.gui.color.border"
            color = borderColor
            callback = { GavUISettings.add("gui.color.border", it.color) }
        }
        colorSetting {
            title = "gavinsmod.settings.gui.color.frozen"
            color = frozenColor
            callback = { GavUISettings.add("gui.color.frozen", it.color) }
        }
        slideSetting {
            title = "gavinsmod.settings.alpha"
            value = GavUISettings.getFloat("gui.alpha")
            callback = { GavUISettings.add("gui.alpha", it.value) }
        }
        toggleSetting {
            title = "gavinsmod.settings.gui.sound"
            state = GavUISettings.getBool("gui.sound")
            callback = { GavUISettings.add("gui.sound", it.state) }
        }
    }

    override fun activate() {
        GavinsModClient.minecraftClient.setScreen(GavinsMod.guiSettings)
        setEnabled(true)
    }

    override fun deactivate() {
        setEnabled(false)
    }
}
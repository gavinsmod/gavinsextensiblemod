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
package com.peasenet.settings

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiCycle
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors

/**
 * A setting that allows the user to change a color value.
 * @param builder - The setting builder.
 * @author GT3CH1
 * @version 07-18-2023
 */
class ColorSetting() : CallbackSetting<ColorSetting>() {
    override lateinit var gui: GuiCycle

    var color = Colors.WHITE
        get() = getColor(gui.currentIndex)
        set(value) {
            field = value
            gui.currentIndex = Colors.COLORS.indexOf(value)
        }


    private fun getColor(index: Int): Color {
        return Colors.COLORS[index]
    }

    fun build(): ColorSetting {
        gui = GuiBuilder<GuiCycle>()
            .setWidth(settingOptions.width)
            .setHeight(settingOptions.height)
            .setTitle(settingOptions.title)
            .setCallback {
                var index = it.currentIndex
                index = (index + 1) % Colors.COLORS.size
                it.currentIndex = index
                it.setBackground(Colors.COLORS[index])
                callback?.invoke(this)
            }
            .setHoverable(settingOptions.hoverable)
            .setCycleSize(settingOptions.cycleSize)
            .setCurrentCycleIndex(settingOptions.cycleIndex)
            .setBackgroundColor(Colors.COLORS[settingOptions.cycleIndex])
            .buildCycle()
        return this
    }
}

fun colorSetting(init: ColorSetting.() -> Unit): ColorSetting {
    val setting = ColorSetting()
    setting.init()
    return setting.build()
}
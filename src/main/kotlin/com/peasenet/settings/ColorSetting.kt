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

import com.peasenet.gavui.GavUI
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiCycle
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF

/**
 * A setting that allows the user to change a color value.
 * @author GT3CH1
 * @version 07-18-2023
 */
class ColorSetting(
    topLeft: PointF = PointF(0F, 0F),
    width: Float = 0F,
    height: Float = 10F,
    title: String = "",
    hoverable: Boolean = true,
    transparency: Float = -1f,
    symbol: Char = '\u0000',
    cycleIndex: Int = 0,
    cycleSize: Int = 0,
    color: Color = GavUI.backgroundColor(),
    override var callback: ((ColorSetting) -> Unit)? = null,
) : CallbackSetting<ColorSetting>(
    topLeft,
    width,
    height,
    title,
    hoverable = hoverable,
    transparency = transparency,
    symbol = symbol,
    cycleIndex = cycleIndex,
    cycleSize = cycleSize,
    color = color,
    callback = callback
) {
    override var gui: GuiCycle = GuiCycle(GuiBuilder())

    fun build(): ColorSetting {
        gui = GuiBuilder<GuiCycle>()
            .setWidth(width)
            .setHeight(height)
            .setTitle(title)
            .setCallback {
//                var index = it.currentIndex
//                index = (index + 1) % Colors.COLORS.size
//                it.currentIndex = index
                it.setBackground(Colors.COLORS[it.currentIndex])
                callback?.invoke(this)
            }
            .setHoverable(hoverable)
            .setCycleSize(Colors.COLORS.size)
            .setBackgroundColor(color)
            .setCurrentCycleIndex(Colors.COLORS.indexOf(color))
            .setTopLeft(topLeft)
            .buildCycle()
        return this
    }

    override var color: Color = color
        get() {
            return gui.backgroundColor
        }
        set(value) {
            gui.setBackground(value)
            field = value
        }
}

fun colorSetting(init: ColorSetting.() -> Unit): ColorSetting {
    val setting = ColorSetting()
    setting.init()
    return setting.build()
}
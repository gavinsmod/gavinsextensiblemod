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
class ColorSetting(builder: SettingBuilder<*>) : Setting() {
    /**
     * The cycle element that allows the user to change the color value.
     */
    override val gui: GuiCycle = GuiBuilder<GuiCycle>()
        .setWidth(builder.getWidth())
        .setHeight(builder.getHeight())
        .setTitle(builder.getTitle())
        .setCycleSize(Colors.COLORS.size)
        .setCurrentCycleIndex(Colors.getColorIndex(builder.getColor()!!))
        .setBackgroundColor(builder.getColor() ?: Colors.BLACK)
        .setTopLeft(builder.getTopLeft())
        .setTransparency(builder.getTransparency())
        .setHoverable(builder.isHoverable())
        .setCallback {
            var index = it.currentIndex
            if (index < 0)
                index = Colors.COLORS.size - 1
            it.currentIndex = index
            color = Colors.COLORS[index]
            it.setBackground(Colors.COLORS[index])
            (builder.settingCallback as? (ColorSetting) -> Unit)?.invoke(this)
        }
        .buildCycle()

    var color: Color = builder.getColor() ?: Colors.INDIGO
}
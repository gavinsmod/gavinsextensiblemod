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
package com.peasenet.settings

import com.peasenet.gavui.GuiCycle
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A setting that allows the user to change a color value.
 */
class ColorSetting(translationKey: String, var color: Color) : Setting(translationKey) {
    /**
     * The cycle element that allows the user to change the color value.
     */
    override val gui: GuiCycle

    /**
     * Creates a new cycle setting used for selecting a color.
     *
     * @param translationKey - The translation key of the setting.
     */
    init {
        gui = GuiCycle(90, 10, Text.translatable(translationKey), Colors.COLORS.size)
        gui.setBackground(color)
        gui.setCallback {
            var index = gui.currentIndex
            if (index < 0)
                index = Colors.COLORS.size - 1
            gui.currentIndex = index
            color = Colors.COLORS[index]
            gui.setBackground(color)
            onClick()
        }
        gui.currentIndex = Colors.getColorIndex(color)
    }

    /**
     * Sets the color and color index to the given value.
     *
     * @param index - The color index.
     */
    fun setColorIndex(index: Int) {
        gui.currentIndex = index
        gui.setBackground(Colors.COLORS[index])
    }
}
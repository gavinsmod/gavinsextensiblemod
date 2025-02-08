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
import com.peasenet.gavui.GuiDropdown
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF

/**
 * A class that represents a mod setting. This class should not be instantiated directly.
 * For examples of how to use this class, see the [com.peasenet.settings] package. @author GT3CH1
 * @version 07-18-2023
 */
abstract class Setting(var settingOptions: SettingOptions = SettingOptions()) {
    open val gui: Gui? = null


    fun settings(init: SettingOptions.() -> Unit): SettingOptions {
        val settingOptions = SettingOptions()
        settingOptions.init()
        this.settingOptions = settingOptions
        return settingOptions
    }
}


data class SettingOptions(
    var topLeft: PointF = PointF(0F, 0F),
    var width: Float = 0F,
    var height: Float = 10F,
    @JvmField
    var title: String = "",
    var state: Boolean = false,
    var hoverable: Boolean = false,
    var transparency: Float = -1f,
    var symbol: Char = '\u0000',
    var cycleIndex: Int = 0,
    var cycleSize: Int = 0,
    var maxChildren: Int = 4,
    var defaultMaxChildren: Int = 4,
    var direction: GuiDropdown.Direction = GuiDropdown.Direction.DOWN,
    var children: MutableList<Setting> = mutableListOf(),
    var color: Color = Colors.INDIGO,
)
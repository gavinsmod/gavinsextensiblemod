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
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.util.GavUISettings

/**
 * A setting that contains one of two finite states - on or off.
 * @param builder - The builder used to create this toggle setting.
 * @author GT3CH1
 * @version 03-02-2023
 */
class ToggleSetting(builder: SettingBuilder) : Setting() {
    /**
     * The gui element that is used to display this toggle setting.
     */
    override var gui: GuiToggle = GuiBuilder()
        .setWidth(builder.getWidth())
        .setHeight(10F)
        .setTitle(builder.getTitle())
        .setIsOn(builder.getState())
        .setCallback(builder.getCallback())
        .setHoverable(builder.isHoverable())
        .setTopLeft(builder.getTopLeft())
        .buildToggle()

    /**
     * The current value of this toggle setting.
     */
    var value = false
        get() = gui.isOn
        set(value) {
            field = value
            gui.setBackground(if (value) GavUISettings.getColor("gui.color.enabled") else GavUISettings.getColor("gui.color.background"))
            gui.setState(value)
        }
}
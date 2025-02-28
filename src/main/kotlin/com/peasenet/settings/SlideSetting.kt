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
import com.peasenet.gavui.GuiSlider

/**
 * @author GT3CH1
 * @version 03-02-2023
 * A setting that can be clicked. This is purely dependant on the given callback.
 */
class SlideSetting(builder: SettingBuilder) : Setting() {
    /**
     * The gui used to display the setting.
     */
    override var gui: GuiSlider = GuiBuilder()
        .setWidth(builder.getWidth())
        .setHeight(builder.getHeight())
        .setSlideValue(builder.getValue())
        .setTitle(builder.getTitle())
        .setCallback(builder.getCallback())
        .setTransparency(builder.getTransparency())
        .setDefaultMaxChildren(builder.getMaxChildren())
        .setMaxChildren(builder.getMaxChildren())
        .setTopLeft(builder.getTopLeft())
       .buildSlider()

    companion object;

    /**
     * The current float value of the setting.
     */
    var value: Float
        get() = gui.value
        set(alpha) {
            gui.value = alpha
        }
}
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
class SlideSetting(
    value: Float = 0.5f,
    override var callback: ((SlideSetting) -> Unit)? = null,
) : CallbackSetting<SlideSetting>(value = value, callback = callback) {
    /**
     * The gui used to display the setting.
     */
    override var gui: GuiSlider? = null

    fun build(): SlideSetting {
        gui = GuiBuilder<GuiSlider>()
            .setWidth(width)
            .setHeight(height)
            .setSlideValue(value)
            .setTitle(title)
            .setCallback { callback?.invoke(this) }
            .setTransparency(transparency)
            .setDefaultMaxChildren(defaultMaxChildren)
            .setMaxChildren(maxChildren)
            .setTopLeft(topLeft)
            .buildSlider()
        return this
    }

    override var value: Float = value
        get() {
            return gui?.value ?: field
        }
        set(value) {
            field = value
            gui?.value = value
        }
}

fun slideSetting(init: SlideSetting.() -> Unit): SlideSetting {
    val setting = SlideSetting()
    setting.init()
    return setting.build()
}
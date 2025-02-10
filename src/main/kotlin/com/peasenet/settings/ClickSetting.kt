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
import com.peasenet.gavui.GuiClick

/**
 * A setting that can be clicked. This is purely dependent on the given callback.
 *
 * @author GT3CH1
 * @version 07-18-2023
 */
class ClickSetting() : CallbackSetting<ClickSetting>(hoverable = true) {
    override lateinit var gui: GuiClick
    fun build(): ClickSetting {
        gui = GuiBuilder<GuiClick>()
            .setWidth(width)
            .setHeight(height)
            .setTitle(title)
            .setCallback {
                callback?.invoke(this)
            }
            .setHoverable(hoverable)
            .setBackgroundColor(color)
            .setTransparency(transparency)
            .setTopLeft(topLeft)
            .buildClick()
        return this
    }
}

fun clickSetting(block: ClickSetting.() -> Unit): ClickSetting {
    val setting = ClickSetting()
    setting.block()
    return setting.build()
}
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
import com.peasenet.gavui.GuiScroll

/**
 * A setting that contains multiple sub settings within a dropdown element.
 * @author GT3CH1
 * @version 03-02-2023
 */
class SubSetting(
) : Setting() {
    override lateinit var gui: GuiScroll

    fun build(): SubSetting {
        gui = GuiBuilder<GuiScroll>()
            .setWidth(width)
            .setHeight(height)
            .setTitle(title)
            .setMaxChildren(maxChildren)
            .setDefaultMaxChildren(defaultMaxChildren)
            .setDirection(direction)
            .setChildren(
                ArrayList(children.map { it.gui!! })
            )
            .buildScroll()
        return this
    }

    fun toggleSetting(init: ToggleSetting.() -> Unit): ToggleSetting {
        var setting = ToggleSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

    fun clickSetting(init: ClickSetting.() -> Unit): ClickSetting {
        var setting = ClickSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

    fun subSetting(init: SubSetting.() -> Unit): SubSetting {
        var setting = SubSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

    fun slideSetting(init: SlideSetting.() -> Unit): SlideSetting {
        var setting = SlideSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

    fun colorSetting(init: ColorSetting.() -> Unit): ColorSetting {
        var setting = ColorSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

    fun cycleSetting(init: CycleSetting.() -> Unit): CycleSetting {
        var setting = CycleSetting()
        setting.init()
        setting = setting.build()
        children.add(setting)
        return setting
    }

}

fun subSetting(init: SubSetting.() -> Unit): SubSetting {
    val setting = SubSetting()
    setting.init()
    return setting.build()
}
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
import com.peasenet.gavui.GuiScroll

/**
 * A setting that contains multiple sub settings within a dropdown element.
 * @param builder The SettingBuilder to use.
 * @author GT3CH1
 * @version 03-02-2023
 */
class SubSetting(
    settingOptions: SettingOptions = SettingOptions(),
) : Setting(settingOptions) {
    override lateinit var gui: GuiScroll

    fun build(): SubSetting {
        gui = GuiBuilder<GuiScroll>()
            .setWidth(settingOptions.width)
            .setHeight(settingOptions.height)
            .setTitle(settingOptions.title)
            .setMaxChildren(settingOptions.maxChildren)
            .setDefaultMaxChildren(settingOptions.defaultMaxChildren)
            .setDirection(settingOptions.direction)
            .setChildren(
                ArrayList(settingOptions.children.map { it.gui!! })
            )
            .buildScroll()
        return this
    }

    //
//    /**
//     * Adds a new subsetting to this element.
//     *
//     * @param setting - The setting to add.
//     */
//    fun add(init: Setting.() -> Unit) {
//        settingOptions.children.add(Setting().apply(init))
//    }
}

fun subSetting(init: SubSetting.() -> Unit): SubSetting {
    val setting = SubSetting()
    setting.init()
    return setting.build()
}
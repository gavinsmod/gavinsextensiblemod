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

/**
 *
 * @author GT3CH1
 * @version 01-25-2025
 * @since 01-25-2025
 */
class CycleSetting : CallbackSetting<CycleSetting>() {
    override lateinit var gui: GuiCycle

    init {
//        gui = GuiBuilder<GuiCycle>()
//            .setWidth(builder.getWidth())
//            .setHeight(builder.getHeight())
//            .setTitle(builder.getTitle())
//            .setHoverable(builder.isHoverable())
//            .setBackgroundColor(builder.getColor() ?: GavUI.backgroundColor())
//            .setTransparency(builder.getTransparency())
//            .setTranslationKey(builder.getTranslationKey())
//            .setSymbol(builder.getSymbol())
//            .setTopLeft(builder.getTopLeft())
//            .setCycleSize(builder.getOptions().size)
//            .setCurrentCycleIndex(builder.getOptions().indexOf(builder.getOptionsValue()))
//            .setCallback { builder.settingCallback?.invoke(this) }
//            .buildCycle()
    }

    fun build(): CycleSetting {
        gui = GuiBuilder<GuiCycle>()
//            .setHeight(settingOptions.getHeight())
//            .setTitle(settingOptions.getTitle())
//            .setHoverable(settingOptions.isHoverable())
//            .setBackgroundColor(settingOptions.getColor() ?: GavUI.backgroundColor())
//            .setTransparency(settingOptions.getTransparency())
//            .setTranslationKey(settingOptions.getTranslationKey())
//            .setSymbol(settingOptions.getSymbol())
//            .setTopLeft(settingOptions.getTopLeft())
//            .setCycleSize(settingOptions.getOptions().size)
//            .setCurrentCycleIndex(settingOptions.getOptions().indexOf(settingOptions.getOptionsValue()))
//            .setCallback { settingOptions.settingCallback?.invoke(this) }
//            .buildCycle()

            .setWidth(settingOptions.width)
            .setHeight(settingOptions.height)
            .setTitle(settingOptions.title)
            .setHoverable(settingOptions.hoverable)
            .setBackgroundColor(GavUI.backgroundColor())
            .setTransparency(settingOptions.transparency)
            .setSymbol(settingOptions.symbol)
            .setTopLeft(settingOptions.topLeft)
            .setCycleSize(settingOptions.cycleSize)
            .setCurrentCycleIndex(settingOptions.cycleIndex)
            .setCallback { callback?.invoke(this) }
            .buildCycle()
        return this
    }
}


fun cycleSetting(init: CycleSetting.() -> Unit): CycleSetting {
    val setting = CycleSetting().apply { init() }
    return setting.build()
}

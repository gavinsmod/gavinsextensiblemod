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

import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.callbacks.GuiCallback
import net.minecraft.text.Text

/**
 *
 *
 *
 * @author gt3ch1
 * @version 05/18/2023
 */
class SettingBuilder {
    private lateinit var translationKey: String
    private lateinit var color: Color
    private var state: Boolean = false
    private var value: Float = 0f
    private lateinit var title: Text
    private var callback: GuiCallback? = null
    private var width: Int = 0
    private var height: Int = 0
    private var guiPosition: PointF = PointF(0f, 0f)
    private var transparency: Float = -1f
    var maxChildren: Int = 4

    fun setTranslationKey(translationKey: String): SettingBuilder {
        this.translationKey = translationKey
        setTitle(translationKey)
        return this
    }

    fun setColor(color: Color): SettingBuilder {
        this.color = color
        return this
    }

    fun setTitle(text: Text): SettingBuilder {
        this.title = text
        return this
    }

    fun setGuiSize(width: Int, height: Int): SettingBuilder {
        this.width = width
        this.height = height
        return this
    }

    fun setState(state: Boolean): SettingBuilder {
        this.state = state
        return this
    }

    fun setTitle(translationKey: String): SettingBuilder {
        this.title = Text.translatable(translationKey)
        this.translationKey = translationKey
        return this
    }

    fun setCallback(callback: GuiCallback): SettingBuilder {
        this.callback = callback
        return this
    }

    fun setValue(value: Float): SettingBuilder {
        this.value = value
        return this
    }

    fun setWidth(width: Int): SettingBuilder {
        this.width = width
        return this
    }

    fun setHeight(height: Int): SettingBuilder {
        this.height = height
        return this
    }

    fun getState(): Boolean {
        return this.state
    }

    fun getTranslationKey(): String {
        return this.translationKey
    }

    fun setDefaultMaxChildren(child: Int): SettingBuilder {
        this.maxChildren = child
        return this
    }

    fun getDefaultMaxChildren(): Int {
        return this.maxChildren
    }

    fun getTitle(): Text {
        return this.title
    }

    fun getColor(): Color {
        return this.color
    }


    fun buildToggle(): ToggleSetting {
        val toggle = GuiBuilder()
            .setWidth(90f)
            .setHeight(10f)
            .setIsOn(state)
            .setTitle(title)
            .setTopLeft(guiPosition)
            .setTransparency(transparency)
            .buildToggle()
        return ToggleSetting(toggle)
    }

    fun buildSlider(): SlideSetting {
        val slider = GuiBuilder()
            .setWidth(90f)
            .setHeight(10f)
            .setSlideValue(value)
            .setTitle(title)
            .setCallback(callback)
            .setTransparency(transparency)
            .setDefaultMaxChildren(maxChildren)
            .setMaxChildren(maxChildren)
            .buildSlider()
        return SlideSetting.fromSlider(slider)
    }

    fun getTopLeft(): PointF {
        return this.guiPosition
    }

    fun setTopLeft(pos: PointF): SettingBuilder {
        this.guiPosition = pos
        return this
    }

    fun buildColorSetting(): ColorSetting {
        return ColorSetting(this)
    }

    fun buildSubSetting(): SubSetting {
        val subSetting = SubSetting(width, height, translationKey)
        return subSetting
    }

    fun getTransparency(): Float {
        return this.transparency
    }

    fun setTransparency(transparency: Float): SettingBuilder {
        this.transparency = transparency
        return this
    }


}
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
import com.peasenet.gavui.GuiDropdown.Direction
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
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
    private var symbol: Char = 0.toChar()
    private var translationKey: String = ""
    private var color: Color = Colors.INDIGO
    private var state: Boolean = false
    private var value: Float = 0f
    private lateinit var title: Text
    private var callback: GuiCallback? = null
    private var width: Float = 0f
    private var height: Float = 0f
    private var guiPosition: PointF = PointF(0f, 0f)
    private var transparency: Float = -1f
    private var direction: Direction = Direction.RIGHT
    private var hoverable: Boolean = true
    private var maxChildren: Int = 4
    private var defaultMaxChildren: Int = 4

    fun setHoverable(hoverable: Boolean): SettingBuilder {
        this.hoverable = hoverable
        return this
    }

    fun isHoverable(): Boolean {
        return this.hoverable
    }

    fun setDirection(direction: Direction): SettingBuilder {
        this.direction = direction
        return this
    }

    fun getDirection(): Direction {
        return this.direction
    }

    fun setTopLeft(x: Float, y: Float): SettingBuilder {
        this.guiPosition = PointF(x, y)
        return this
    }

    fun setTopLeft(point: PointF): SettingBuilder {
        this.guiPosition = point
        return this
    }

    fun getTopLeft(): PointF {
        return this.guiPosition
    }

    fun getMaxChildren(): Int {
        return this.maxChildren
    }

    fun setMaxChildren(maxChildren: Int): SettingBuilder {
        this.maxChildren = maxChildren
        return this
    }

    fun getDefaultMaxChildren(): Int {
        return this.defaultMaxChildren
    }

    fun setDefaultMaxChildren(defaultMaxChildren: Int): SettingBuilder {
        this.defaultMaxChildren = defaultMaxChildren
        return this
    }

    fun setWidth(width: Float): SettingBuilder {
        this.width = width
        return this
    }

    fun setWidth(width: Int): SettingBuilder {
        this.width = width.toFloat()
        return this
    }

    fun setHeight(height: Int): SettingBuilder {
        this.height = height.toFloat()
        return this
    }

    fun getTitle(): Text {
        return this.title
    }

    fun setHeight(height: Float): SettingBuilder {
        this.height = height
        return this
    }

    fun getHeight(): Float {
        return this.height
    }

    fun getWidth(): Float {
        return this.width
    }

    fun setTitle(translationKey: String): SettingBuilder {
        this.translationKey = translationKey
        setTitle(Text.translatable(translationKey))
        return this
    }

    fun setTitle(title: Text): SettingBuilder {
        this.title = title
        return this
    }

    fun getTranslationKey(): String {
        return this.translationKey
    }

    fun setColor(color: Color): SettingBuilder {
        this.color = color
        return this
    }

    fun setState(state: Boolean): SettingBuilder {
        this.state = state
        return this
    }

    fun setValue(value: Float): SettingBuilder {
        this.value = value
        return this
    }


    fun getState(): Boolean {
        return this.state
    }

    fun getSymbol(): Char {
        return this.symbol
    }

    fun getColor(): Color {
        return this.color
    }

    fun buildColorSetting(): ColorSetting {
        return ColorSetting(this)
    }

    fun buildToggleSetting(): ToggleSetting {
        return ToggleSetting(this)
    }

    fun buildSubSetting(): SubSetting {
        return SubSetting(this)
    }

    fun buildClickSetting(): ClickSetting {
        return ClickSetting(this)
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
            .setTopLeft(guiPosition)
            .buildSlider()
        return SlideSetting.fromSlider(slider)
    }

    fun getTransparency(): Float {
        return this.transparency
    }

    fun setTransparency(transparency: Float): SettingBuilder {
        this.transparency = transparency
        return this
    }

    fun setBackgroundColor(color: Color): SettingBuilder {
        this.color = color
        return this
    }

    fun setCallback(function: GuiCallback): SettingBuilder {
        this.callback = function
        return this
    }

    fun setSymbol(symbol: Char): SettingBuilder {
        this.symbol = symbol
        return this
    }

    fun getCallback(): GuiCallback? {
        return callback;
    }

}
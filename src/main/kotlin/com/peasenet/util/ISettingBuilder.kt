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

package com.peasenet.util

import com.peasenet.gavui.GavUI
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.math.PointF
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.Setting
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 02-02-2025
 * @since 02-02-2025
 */
abstract class NewSettingBuilder<T : Setting> {
    protected var width = 0f
    protected var height = 0f
    protected var title: Text? = null
    protected var callback: ((T) -> Unit)? = null
    protected var hoverable = false
    protected var color = GavUI.backgroundColor()
    protected var transparency = 0f
    protected var translationKey: String = ""
    protected var symbol: Char = '\u0000'
    protected var topLeft = PointF(0f, 0f)
    var cycleSize = 0
    var currentIndex = 0

    fun width(width: Float) = apply { this.width = width }
    fun height(height: Float) = apply { this.height = height }
    fun title(title: Text) = apply { this.title = title }
    fun callback(callback: ((T) -> Unit)?) = apply { this.callback = callback }
    fun hoverable(hoverable: Boolean) = apply { this.hoverable = hoverable }
    fun color(color: Color) = apply { this.color = color }
    fun transparency(transparency: Float) = apply { this.transparency = transparency }
    fun translationKey(translationKey: String) = apply { this.translationKey = translationKey }
    fun symbol(symbol: Char) = apply { this.symbol = symbol }
    fun topLeft(topLeft: PointF) = apply { this.topLeft = topLeft }
    abstract fun build(): T
}

fun NewSettingBuilder<ColorSetting>.cycleSize(cycleSize: Int) = apply { this.cycleSize = cycleSize }
fun NewSettingBuilder<ColorSetting>.currentIndex(currentIndex: Int) = apply { this.currentIndex = currentIndex }

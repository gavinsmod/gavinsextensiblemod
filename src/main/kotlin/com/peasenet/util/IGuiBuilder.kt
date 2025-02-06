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

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiCycle
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 02-02-2025
 * @since 02-02-2025
 */
abstract class NewGuiBuilder<T : Gui> {
    var callback: ((T) -> Unit)? = null
    var width: Float = 0f
    var height: Float = 0f
    var title: Text = Text.of("")
    var hoverable: Boolean = false
    var backgroundColor: Color = Colors.BLACK
    var isHidden: Boolean = false
    var transparency: Float = -1f
    var drawBorder: Boolean = false
    var isParent: Boolean = false
    var symbol: Char = '\u0000'
    var topLeft: PointF = PointF(0f, 0f)
    var translationKey: String = ""
    var cycleSize = 0
    var currentIndex = 0

    fun callback(callback: (T) -> Unit) = apply { this.callback = callback }
    fun width(width: Float) = apply { this.width = width }
    fun height(height: Float) = apply { this.height = height }
    fun title(title: Text) = apply { this.title = title }
    fun hoverable(hoverable: Boolean) = apply { this.hoverable = hoverable }
    fun backgroundColor(backgroundColor: Color) = apply { this.backgroundColor = backgroundColor }
    fun isHidden(isHidden: Boolean) = apply { this.isHidden = isHidden }
    fun transparency(transparency: Float) = apply { this.transparency = transparency }
    fun drawBorder(drawBorder: Boolean) = apply { this.drawBorder = drawBorder }
    fun isParent(isParent: Boolean) = apply { this.isParent = isParent }
    fun symbol(symbol: Char) = apply { this.symbol = symbol }
    fun topLeft(topLeft: PointF) = apply { this.topLeft = topLeft }
    fun translationKey(translationKey: String) = apply { this.translationKey = translationKey }

    abstract fun build(): T
}

fun NewGuiBuilder<GuiCycle>.cycleSize(cycleSize: Int) = apply { this.cycleSize = cycleSize }
fun NewGuiBuilder<GuiCycle>.currentIndex(currentIndex: Int) = apply { this.currentIndex = currentIndex }
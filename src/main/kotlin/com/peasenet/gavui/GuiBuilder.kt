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
package com.peasenet.gavui

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.Direction
import net.minecraft.text.Text

/**
 * @author GT3CH1
 * @version 05/18/2023
 * A builder for all GavUI GUIs.
 */
class GuiBuilder<T : Gui> {
    var width: Float = 0f
        private set
    var height: Float = 0f
        private set
    var topLeft: PointF = PointF(0, 0)
        private set
    var canHover: Boolean = true
        private set
    var isHidden: Boolean = false
        private set
    var isFrozen: Boolean = false
        private set
    var isOpen: Boolean = false
        private set
    var isOn: Boolean = false
        private set
    var isParent = false
        private set
    var isDraggable: Boolean = false
        private set
    var drawBorder: Boolean = true
        private set
    var title: Text? = null
        private set
    var symbol: Char = '\u0000'
        private set
    var backgroundColor: Color = Colors.BLACK
        private set
    var box: BoxF? = null
        private set
    var callback: ((T) -> Unit)? = null
        private set
    var cycleSize: Int = 0
        private set
    var currentCycleIndex: Int = 0
        private set
    var maxChildren: Int = 4
        private set
    var defaultMaxChildren: Int = 4
        private set
    var slideValue: Float = 0f
        private set
    var children: ArrayList<Gui> = ArrayList()
        private set
    var translationKey: String? = null
        private set
    var transparency: Float = -1f
        private set
    var direction: Direction = Direction.DOWN
        private set


    fun setDirection(direction: Direction): GuiBuilder<T> {
        this.direction = direction
        return this
    }

    fun setTransparency(transparency: Float): GuiBuilder<T> {
        this.transparency = transparency
        return this
    }

    fun setChildren(children: ArrayList<Gui>): GuiBuilder<T> {
        this.children = children
        return this
    }

    fun setWidth(width: Float): GuiBuilder<T> {
        this.width = width
        this.box = BoxF(topLeft, width, height)
        return this
    }

    fun setDrawBorder(drawBorder: Boolean): GuiBuilder<T> {
        this.drawBorder = drawBorder
        return this
    }

    fun setWidth(width: Int): GuiBuilder<T> {
        this.width = width.toFloat()
        this.box = BoxF(topLeft, width.toFloat(), height)
        return this
    }

    fun setHeight(height: Float): GuiBuilder<T> {
        this.height = height
        this.box = BoxF(topLeft, width, height)
        return this
    }

    fun setHeight(height: Int): GuiBuilder<T> {
        this.height = height.toFloat()
        this.box = BoxF(topLeft, width, height.toFloat())
        return this
    }

    fun setTopLeft(point: PointF): GuiBuilder<T> {
        topLeft = point
        this.box = BoxF(topLeft, width, height)
        return this
    }

    fun setHoverable(canHover: Boolean): GuiBuilder<T> {
        this.canHover = canHover
        return this
    }

    fun setHidden(hidden: Boolean): GuiBuilder<T> {
        this.isHidden = hidden
        return this
    }

    fun setTitle(translationKey: String?): GuiBuilder<T> {
        if (translationKey.isNullOrEmpty()) return this
        this.title = Text.translatable(translationKey)
        this.translationKey = translationKey
        return this
    }

    fun setTitle(title: Text?): GuiBuilder<T> {
        this.title = title
        return this
    }

    fun setSymbol(symbol: Char): GuiBuilder<T> {
        this.symbol = symbol
        return this
    }

    fun setBackgroundColor(backgroundColor: Color): GuiBuilder<T> {
        this.backgroundColor = backgroundColor
        return this
    }

    fun setCallback(callback: ((T) -> Unit)?): GuiBuilder<T> {
        this.callback = callback
        return this
    }

    fun setCycleSize(cycleSize: Int): GuiBuilder<T> {
        this.cycleSize = cycleSize
        return this
    }

    fun setCurrentCycleIndex(currentCycleIndex: Int): GuiBuilder<T> {
        this.currentCycleIndex = currentCycleIndex
        return this
    }

    fun setMaxChildren(maxChildren: Int): GuiBuilder<T> {
        this.maxChildren = maxChildren
        return this
    }

    fun setDefaultMaxChildren(defaultMaxChildren: Int): GuiBuilder<T> {
        this.defaultMaxChildren = defaultMaxChildren
        return this
    }

    fun setSlideValue(slideValue: Float): GuiBuilder<T> {
        this.slideValue = slideValue
        return this
    }

    fun setTopLeft(x: Int, y: Int): GuiBuilder<T> {
        topLeft = PointF(x, y)
        this.box = BoxF(topLeft, width, height)
        return this
    }

    fun setTopLeft(x: Float, y: Float): GuiBuilder<T> {
        topLeft = PointF(x, y)
        this.box = BoxF(topLeft, width, height)
        return this
    }


    fun setIsOn(isOn: Boolean): GuiBuilder<T> {
        this.isOn = isOn
        return this
    }

    fun setIsParent(isParent: Boolean): GuiBuilder<T> {
        this.isParent = isParent
        return this
    }


    fun setDraggable(isDraggable: Boolean): GuiBuilder<T> {
        this.isDraggable = isDraggable
        return this
    }

    fun buildSlider(): GuiSlider {
        validate()
        return GuiSlider(this as GuiBuilder<GuiSlider>)
    }

    fun buildCycle(): GuiCycle {
        validate()
        return GuiCycle(this as GuiBuilder<GuiCycle>)
    }

    fun buildToggle(): GuiToggle {
        validate()
        return GuiToggle(this as GuiBuilder<GuiToggle>)
    }

    fun buildScroll(): GuiScroll {
        validate()
        return GuiScroll(this as GuiBuilder<GuiScroll>)
    }

    fun build(): Gui {
        validate()
        return Gui(this as GuiBuilder<Gui>)
    }

    fun buildClick(): GuiClick {
        validate()
        return GuiClick(this as GuiBuilder<GuiClick>)
    }

    private fun validate() {
        if (width == 0f) width = 100f
        if (height == 0f) height = 10f
    }
}

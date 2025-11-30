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

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics

/**
 * @author GT3CH1
 * @version 7/1/2022
 * A simple toggleable gui element.
 */
class GuiToggle(builder: GuiBuilder<out GuiToggle>) : GuiClick(builder) {

    /**
     * Gets whether the toggle is on.
     *
     * @return Whether the toggle is on.
     */
    /**
     * Gets whether the toggle is on.
     */
    var isOn: Boolean = false

    /**
     * The callback method to be called when the element is rendered.
     */
    var renderCallback: ((Gui) -> Unit)? = null

    init {
        this.setState(builder.isOn)
    }

    /**
     * Sets the current state of this toggle element.
     *
     * @param on - the new state of this toggle element.
     */
    fun setState(on: Boolean) {
        isOn = on
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clickedGui = this
        val tmpIsOn = isOn
        isOn = !isOn
        if (!super.mouseClicked(mouseX, mouseY, button) || isHidden) {
            isOn = tmpIsOn
            return false
        }
        return true
    }

    override fun render(drawContext: GuiGraphics, tr: Font, mouseX: Int, mouseY: Int, delta: Float) {
        if (isHidden) return
        symbolOffsetY= 0
        symbol = if (isOn) "☑" else "☐"
        if (renderCallback != null) renderCallback!!(this)
        backgroundColor = if (isOn) GavUI.enabledColor() else GavUI.backgroundColor()
        super.render(drawContext, tr, mouseX, mouseY, delta)
    }
}

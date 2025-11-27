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

import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GuiUtil
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import kotlin.math.max
import kotlin.math.min

/**
 * A gui that allows for controlling a value with a slider.
 *
 * @author GT3CH1
 * @version 09-02-2024
 * @since 01/07/2022
 */
class GuiSlider(builder: GuiBuilder<out GuiSlider>) : Gui(builder) {
    /**
     * Callback to be called when the slider is moved.
     */
    var callback: ((Gui) -> Unit)? = null

    /**
     * The current value of the slider.
     */
    var value: Float = 0f

    /**
     * Creates a new slider gui element.
     *
     */
    init {
        callback = {
            (builder.callback as? ((GuiSlider) -> Unit)?).let {
                it?.invoke(this)
            }
        }
        value = builder.slideValue
    }


    /**
     * Sets the current value of the slider based on the mouse position.
     *
     * @param mouseX - The x position of the mouse.
     */
    private fun setValue(mouseX: Double) {
        value = ((mouseX - x) / (width - 2)).toFloat()
        value = max(0.0, min(1.0, value.toDouble())).toFloat()
        // round to 2 decimal places
        value = Math.round(value * 100) / 100f
        if (callback != null) callback!!(this)
    }

    override fun render(drawContext: DrawContext, tr: TextRenderer, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(drawContext, tr, mouseX, mouseY, delta)
        if (!isHidden) drawTickMark(drawContext)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (((button == 0 && (mouseWithinGui(
                mouseX,
                mouseY
            )) && clickedGui == null) || clickedGui?.uUID == this.uUID)
        ) {
            setValue(mouseX)
            clickedGui = this
            return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0 && mouseWithinGui(mouseX, mouseY) && !isHidden) {
            setValue(mouseX)
            clickedGui = this
            dragging = true
            return true
        }
        clickedGui = null
        return false
    }

    /**
     * Draws the tick mark on the slider.
     *
     * @param drawContext - The draw matrix to draw on.
     */
    private fun drawTickMark(drawContext: DrawContext) {
        // wrap x value between (x+1) and (x+width-1)
        val xVal = x + 1 + ((width - 2) * value)
        val xCoord = xVal.coerceIn(x+1, x + width - 2).toInt()
        drawContext.drawVerticalLine(xCoord, (y).toInt(), (y+height-1).toInt(), Colors.WHITE.withAlpha(0.75f).asInt )
//        GuiUtil.fill(box, drawContext, Colors.WHITE.withAlpha(0.75f))

    }
}

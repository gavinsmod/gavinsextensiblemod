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

import com.peasenet.gavui.util.GavUISettings
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvents

/**
 * @author GT3CH1
 * @version 01/07/2023
 * A gui element that cycles through a list of settings when clicked on.
 * A callback is called when clicked on.
 */
class GuiCycle(builder: GuiBuilder<out GuiClick>) : GuiClick(builder) {
    /**
     * The size of the cycle.
     */
    private var cycleSize = 0
    var currentIndex: Int = 0


    init {
        cycleSize = (builder.cycleSize)
        currentIndex = builder.currentCycleIndex
    }


    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val increment = if (button == 0) 1 else -1
        if (mouseWithinGui(mouseX, mouseY)) {
            // move the cycle index by the increment, wrapping around if necessary
            currentIndex = (currentIndex + increment)
            if (currentIndex < 0) {
                currentIndex = cycleSize - 1
            }
            // y is modulo not working
            currentIndex %= cycleSize
            if (GavUISettings.getBool("gui.sound")) {
                MinecraftClient.getInstance().player!!.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f)
            }
            if (callback != null) callback!!(this)
            clickedGui = this
            return true
        }
        return false
    }

}

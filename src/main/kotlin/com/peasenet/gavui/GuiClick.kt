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
 * @version 7/1/2022
 * Creates a GUI that allows the user to toggle mods on and off by clicking.
 */
open class GuiClick(builder: GuiBuilder<out GuiClick>) : Gui(builder) {
    /**
     * The callback used when the user clicks on the GUI.
     */
    var callback: ((Gui) -> Unit)? = null

    init {
        callback = {
            (builder.callback as? ((Gui) -> Unit)?).let {
                it?.invoke(this)
            }
        }
    }

    /**
     * Handles clicks on the gui.
     *
     * @param mouseX The x coordinate of the mouse.
     * @param mouseY The y coordinate of the mouse.
     * @param button The mouse button that was clicked.
     */
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false
        // check if mouseX and mouseY are within the bounds of the gui.
        val inGui = mouseWithinGui(mouseX, mouseY) && !isHidden
        if (inGui && GavUISettings.getBool("gui.sound")) MinecraftClient.getInstance().player!!.playSound(
            SoundEvents.UI_BUTTON_CLICK.value(),
            0.5f,
            1f
        )
        if (inGui && !isHidden) {
            if (callback != null) callback!!(this)
        }

        return inGui
    }
}

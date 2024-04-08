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

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiSlider
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.callbacks.GuiCallback
import net.minecraft.text.Text

/**
 * A class that represents a mod setting. This class should not be instantiated directly.
 * For examples of how to use this class, see the [com.peasenet.setitngs] package.
 * @author GT3CH1
 * @version 07-18-2023
 */
abstract class Setting() {

    /**
     * The GUI used for this setting.
     */
    open val gui: Gui? = null

    /**
     * Sets the callback for this setting.
     * Only used for GuiClick and GuiSlider.
     */
    fun setCallback(callback: GuiCallback) {
        if (gui != null) {
            (gui as? GuiClick)?.setCallback(callback)
            (gui as? GuiSlider)?.setCallback(callback)
        }
    }

    /**
     * Gets the GUI for this setting.
     */
    open fun getTitle(): Text {
        return gui!!.title
    }

    /**
     * Gets the width of this setting.
     */
    @Deprecated("Unused", ReplaceWith("gui!!.width"))
    open fun getWidth(): Float {
        return gui!!.width
    }

    /**
     * Sets the position of this setting.
     */
    @Deprecated("Unused", ReplaceWith("gui!!.position = pos"))  fun setPos(pos: PointF) {
        gui!!.position = pos
    }
}

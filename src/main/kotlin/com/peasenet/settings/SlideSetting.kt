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

import com.peasenet.gavui.GuiSlider

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A setting that can be clicked. This is purely dependant on the given callback.
 */
class SlideSetting(slider: GuiSlider) : Setting() {
    /**
     * The gui used to display the setting.
     */
    override var gui: GuiSlider = slider

    companion object {
        fun fromSlider(slider: GuiSlider): SlideSetting {
            return SlideSetting(slider)
        }
    }

    var value: Float
        /**
         * Gets the current float value of the setting.
         *
         * @return The current float value of the setting.
         */
        get() = gui.value
        /**
         * Sets the current value of the setting.
         *
         * @param alpha The new value of the setting.
         */
        set(alpha) {
            gui.value = alpha
        }
}
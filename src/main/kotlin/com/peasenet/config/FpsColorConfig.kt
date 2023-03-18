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
package com.peasenet.config

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors

/**
 * The configuration for the FPS colors.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class FpsColorConfig : Config<FpsColorConfig>() {
    var slowFps: Color = Colors.RED
        /**
         * Sets the color for slow FPS.
         * @param slowFps - The color for slow FPS.
         */
        set(slowFps) {
            field = slowFps
            saveConfig()
        }

    /**
     * Gets the color for ok FPS.
     * The color for 50% to 85% of the FPS - default is yellow.
     *
     * @return The color for ok FPS.
     */
    var okFps: Color = Colors.YELLOW
        /**
         * Sets the color for ok FPS.
         * @param okFps - The color for ok FPS.
         */
        set(okFps) {
            field = okFps
            saveConfig()
        }

    /**
     * Gets the color for fast FPS.
     * @return The color for fast FPS.
     */
    var fastFps: Color = Colors.GREEN
        /**
         * Sets the color for fast FPS.
         * @param fastFps - The color for fast FPS.
         */
        set(fastFps) {
            field = fastFps
            saveConfig()
        }

    /**
     * Gets whether the FPS colors are enabled.
     * @return Whether the FPS colors are enabled.
     */
    var isColorsEnabled = true
        /**
         * Sets whether the FPS colors are enabled.
         * @param colorsEnabled - Whether the FPS colors are enabled.
         */
        set(colorsEnabled) {
            field = colorsEnabled
            saveConfig()
        }

    init {
        key = "fpsColors"
    }

}
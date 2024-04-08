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

import com.peasenet.main.Mods

/**
 * Configuration file for fullbright.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
open class FullbrightConfig : Config<FullbrightConfig>() {
    var gamma: Float = 1.0F
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Whether to automatically enable fullbright when the player is in a dark area.
     */
    var autoFullBright = true
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Whether to fade the brightness.
     */
    var gammaFade = true
        set(value) {
            field = value
            saveConfig()
        }

    init {
        key = "fullbright"
    }

    val maxGamma: Float
        /**
         * Gets the maximum gamma value allowed.
         * If XRAY is enabled, this will be 16.
         * If it is not, then it will be between 1 and 4 inclusive.
         * @return max gamma value.
         */
        get() = if (Mods.isActive("xray")) 16F else 1 + 15 * gamma
}
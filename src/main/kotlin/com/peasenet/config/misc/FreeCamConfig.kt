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

package com.peasenet.config.misc

import com.peasenet.config.Config
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors

/**
 *
 * @author GT3CH1
 * @version 09-16-2024
 * @since 09-16-2024
 */
class FreeCamConfig : Config<FreeCamConfig>() {
    init {
        key = "freecam"
    }

    var tracerEnabled: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    var espEnabled: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    var freeCamSpeed: Float = 1f
        set(value) {
            field = value
            saveConfig()
        }

    var alpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }

    var color: Color = Colors.GOLD
        set(value) {
            field = value
            saveConfig()
        }

}
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
package com.peasenet.gavui.color

import com.peasenet.gavui.color.Color.Companion.fromInt

/**
 * A class containing pre-defined colors.
 * Note: Use this site to generate color names: [color-name.com](https://www.color-name.com/hex/)
 * @author GT3CH1
 * @version 02-02-2025
 * @since 7/1/2022
 */
object Colors {
    val RED: Color = Color(255, 0, 0)
    val DARK_RED: Color = fromInt(0x610b0b)
    val GREEN: Color = Color(0, 255, 0)
    val DARK_GREEN: Color = Color(0, 127, 0)
    val BLUE: Color = Color(0, 0, 255)
    val YELLOW: Color = Color(255, 255, 0)
    val PURPLE: Color = Color(255, 0, 255)
    val CYAN: Color = Color(0, 255, 255)
    val WHITE: Color = Color(255, 255, 255)
    val BLACK: Color = Color(0, 0, 0)
    val GRAY: Color = Color(128, 128, 128)
    val DARK_GRAY: Color = Color(16, 16, 16)
    val DARK_CYAN: Color = Color(0, 128, 255)
    val GOLD: Color = Color(255, 215, 0)
    val INDIGO: Color = fromInt(0x273859)
    val SHADOW_BLUE: Color = fromInt(0x7686A6)
    val DARK_SPRING_GREEN: Color = fromInt(0x1D734B)
    val MEDIUM_SEA_GREEN: Color = fromInt(0x32a670)
    val DESERT_SAND: Color = fromInt(0xD9D2B0)
    val RED_ORANGE: Color = fromInt(0xFF4500)

    /**
     * The list of all colors.
     */
    val COLORS: Array<Color> = arrayOf(
        RED, DARK_RED, GREEN, DARK_GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE, BLACK, GRAY, DARK_GRAY, DARK_CYAN, GOLD,
        INDIGO, SHADOW_BLUE, DARK_SPRING_GREEN, MEDIUM_SEA_GREEN, DESERT_SAND, RED_ORANGE
    )

    /**
     * Returns the color with the given index.
     *
     * @param color - the index of the color to return
     * @return the color with the given index, -1 if the search failed.
     */
    fun getColorIndex(color: Color): Int {
        for (i in COLORS.indices) {
            if (COLORS[i].equals(color)) {
                return i
            }
        }
        return -1
    }

}

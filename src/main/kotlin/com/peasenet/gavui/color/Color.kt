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

import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * A representation of a color. The maximum value for each channel is 255, and the minimum is 0.
 *
 * @author GT3CH1
 * @version 02-02-2025
 * @since 01/07/2022
 */
class Color(red: Int, green: Int, blue: Int) : Serializable {
    /**
     * The red value of this color.
     */
    // Make sure the values are within the range via clamping
    val red = max(0.0, min(255.0, red.toDouble())).toInt()

    /**
     * The green value of this color.
     */
    val green = max(0.0, min(255.0, green.toDouble())).toInt()

    /**
     * The blue value of this color.
     */
    val blue = max(0.0, min(255.0, blue.toDouble())).toInt()

    /**
     * Gets the red value of this color. Will be within 0 and 1.
     *
     * @return red value
     */
    fun getRed(): Float {
        return red / 255f
    }

    /**
     * Gets the green value of this color. Will be within 0 and 1.
     *
     * @return green value
     */
    fun getGreen(): Float {
        return green / 255f
    }

    /**
     * Gets the blue value of this color. Will be within 0 and 1.
     *
     * @return blue value
     */
    fun getBlue(): Float {
        return blue / 255f
    }

    val asFloatArray: FloatArray
        /**
         * Gets the float array value of this color. values range from 0 to 1.
         *
         * @return float array of color values
         */
        get() = floatArrayOf(getRed(), getGreen(), getBlue(), 1f)

    val asInt: Int
        /**
         * Gets the int value of this color, will be converted to its hex equivalent.
         *
         * @return int value of color
         */
        get() = (red shl 16) or (green shl 8) or blue


    /**
     * Gets the int value of this color, will be converted to its hex equivalent.
     *
     * @param alpha - the alpha value
     * @return int value of color
     */
    fun getAsInt(alpha: Float): Int {
        var alpha = alpha
        if (alpha > 1) alpha = alpha / 255f
        if (alpha < 0) alpha = 1f
        return (alpha * 255).toInt() shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * Checks if two colors are equal. This is determined if the red, blue, and green channels are the same values.
     *
     * @param other - The other color.
     * @return True if the RGB channels match.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val color = other as Color
        return red == color.red && green == color.green && blue == color.blue
    }

    override fun hashCode(): Int {
        var result = red
        result = 31 * result + green
        result = 31 * result + blue
        return result
    }

    fun brighten(amount: Float): Color {
        var amount = amount
        if (amount < 0) amount = 0f
        if (amount > 1) amount = 1f
        var newR = red + (amount * 255).toInt()
        var newG = green + (amount * 255).toInt()
        var newB = blue + (amount * 255).toInt()
        if (newR > 255) newR = 255
        if (newG > 255) newG = 255
        if (newB > 255) newB = 255
        // check if the color is too bright, if so, darken it
        if (newR + newG + newB > 255 * 3) {
            newR = red - (amount * 255).toInt()
            newG = green - (amount * 255).toInt()
            newB = blue - (amount * 255).toInt()
        }
        // check if the color is the same. if so, return a new color that is closer to white
        if (newR == red) {
            newG += (amount * 255).toInt()
            newB += blue + (amount * 255).toInt()
            return Color(newR, newG, newB)
        }
        if (newG == green) {
            newR += red + (amount * 255).toInt()
            newB += blue + (amount * 255).toInt()
            return Color(newR, newG, newB)
        }
        if (newB == blue) {
            newR += red + (amount * 255).toInt()
            newG += green + (amount * 255).toInt()
            return Color(newR, newG, newB)
        }
        return Color(newR, newG, newB)
    }

    fun invert(): Color {
        return Color(255 - red, 255 - green, 255 - blue)
    }

    /**
     * Calculates the similarity between two colors. The value will be between 0 and 1, with 0 being the same color and 1 being the opposite color.
     *
     * @param other - the other color
     * @return the similarity
     */
    fun similarity(other: Color): Float {
        val maxRed = max(red.toDouble(), other.red.toDouble()).toInt()
        val maxGreen = max(green.toDouble(), other.green.toDouble()).toInt()
        val maxBlue = max(blue.toDouble(), other.blue.toDouble()).toInt()
        val minRed = min(red.toDouble(), other.red.toDouble()).toInt()
        val minGreen = min(green.toDouble(), other.green.toDouble()).toInt()
        val minBlue = min(blue.toDouble(), other.blue.toDouble()).toInt()
        val r = (maxRed - minRed)
        val g = (maxGreen - minGreen)
        val b = (maxBlue - minBlue)
        val similarity = (r + g + b) / (255f * 3f)
        return similarity
    }

    companion object {
        /**
         * Gets a color from an int value.
         *
         * @param i - the int value
         * @return the color
         */
        @JvmStatic
        fun fromInt(i: Int): Color {
            return Color(i shr 16 and 0xFF, i shr 8 and 0xFF, i and 0xFF)
        }
    }
}

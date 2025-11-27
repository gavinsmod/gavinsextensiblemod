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
package com.peasenet.gavui.math

import kotlin.math.sqrt

/**
 * A point in 2D space.
 *
 * @param x The x coordinate of the point.
 * @param y The y coordinate of the point.
 * @author GT3CH1
 * @version 02-02-2025
 * @since 12/30/2022
 */
@JvmRecord
data class PointF
/**
 * Creates a new point in 2D space.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 */(val x: Float, val y: Float) {
    constructor(x: Double, y: Double) : this(x.toFloat(), y.toFloat())

    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    /**
     * Adds two points together
     *
     * @param other - The other point to add.
     * @return The sum of the two points.
     */
    fun add(other: PointF): PointF {
        return PointF(x + other.x, y + other.y)
    }

    fun add(x: Double, y: Double): PointF {
        return PointF(this.x + x, this.y + y)
    }

    fun add(x: Int, y: Int): PointF {
        return PointF(this.x + x, this.y + y)
    }

    fun add(x: Float, y: Float): PointF {
        return PointF(this.x + x, this.y + y)
    }

    /**
     * Subtracts two points together
     *
     * @param other - The other point to subtract.
     * @return A point with the difference of the two points.
     */
    fun subtract(other: PointF): PointF {
        return PointF(x - other.x, y - other.y)
    }

    fun multiply(other: Float): PointF {
        return PointF(x * other, y * other)
    }

    /**
     * Calculates the distance between the origin and the point.
     *
     * @return The distance between the origin and the point.
     */
    fun distance(): Float {
        return sqrt((x * x + y * y))
    }
    fun manhattanDistance(): Float {
        return kotlin.math.abs(x) + kotlin.math.abs(y)
    }
}

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

/**
 * A BoxF is a box with floating point values.
 *
 * @author GT3CH1
 * @version 02-02-2025
 * @since 12-30-2022
 */

class BoxF {
    var width: Float = 0f
        private set
    var height: Float = 0f
        private set

    var topLeft: PointF = PointF(0, 0)
        set(value) {
            field = value
            this.bottomRight = topLeft.add(PointF(width, height))
        }
    var bottomRight: PointF
        private set
    val x1: Float
        get() = topLeft.x
    val y1: Float
        get() = topLeft.y
    val x2: Float
        get() = bottomRight.x
    val y2: Float
        get() = bottomRight.y

    constructor(topLeft: PointF, width: Float, height: Float) {
        this.topLeft = topLeft
        this.width = width
        this.height = height
        this.bottomRight = topLeft.add(PointF(this.width, this.height))
    }

    constructor(x: Float, y: Float, width: Float, height: Float) : this(PointF(x, y), width, height)

    fun from(other: BoxF) {
        this.topLeft = other.topLeft
        this.bottomRight = other.bottomRight
        this.width = other.width
        this.height = other.height
    }

    fun setMiddle(middle: PointF) {
        setTopLeftPoint(middle.subtract(PointF(width / 2, height / 2)))
    }

    private fun setTopLeftPoint(topLeft: PointF) {
        this.topLeft = topLeft
        this.bottomRight = topLeft.add(PointF(width, height))
    }

    // expands the box by size in all directions
    fun expand(size: Int): BoxF {
        return BoxF(
            PointF(topLeft.x - size, topLeft.y - size),
            width + size * 2,
            height + size * 2
        )
    }


    companion object {
        fun copy(box: BoxF): BoxF {
            return BoxF(box.topLeft, box.width, box.height)
        }
    }
}
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

package com.peasenet.util.block

/**
 * An enum class that contains the neighbors of a block.
 * @param mask The mask of the neighbor.
 * @author GT3CH1
 * @version 09-12-2024
 * @since 09-12-2024
 */
enum class Neighbors(val mask: Int) {
    /**
     * The top middle (x+) neighbor.
     */
    East(1 shl 2),

    /**
     * The middle left (z-) neighbor.
     */
    North(1 shl 4),

    /**
     * The middle right (z+) neighbor.
     */
    South(1 shl 6),

    /**
     * The bottom middle (x-) neighbor.
     */
    West(1 shl 8),

    /**
     * The above (y+) neighbor.
     */
    Above(1 shl 10),

    /**
     * The below (y-) neighbor.
     */
    Below(1 shl 11),

    /**
     * No neighbors.
     */
    None(0);
}

infix fun Neighbors.and(other: Boolean): Int {
    return if (other) this.mask else 0
}

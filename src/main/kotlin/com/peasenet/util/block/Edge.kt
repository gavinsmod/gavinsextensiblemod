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
 * An edge of a block.
 * @author GT3CH1
 * @version 09-12-2024
 * @since 09-12-2024
 */
enum class Edge(val mask: Int) {
    /**
     * The lower x-axis edge of a block.
     */
    Edge1(1 shl 1),

    /**
     * The lower z+ axis edge of a block.
     */
    Edge2(1 shl 2),

    /**
     * The lower x+ axis edge of a block.
     */
    Edge3(1 shl 3),

    /**
     * The lower z- axis edge of a block.
     */
    Edge4(1 shl 4),

    /**
     * The x- and z- corner of a block.
     */
    Edge5(1 shl 5),

    /**
     * The x- and z+ corner of a block.
     */
    Edge6(1 shl 6),

    /**
     * The x+ and z+ corner of a block.
     */
    Edge7(1 shl 7),

    /**
     * The x+ and z- corner of a block.
     */
    Edge8(1 shl 8),

    /**
     * The upper x- axis edge of a block.
     */
    Edge9(1 shl 9),

    /**
     * The upper z+ axis edge of a block.
     */
    Edge10(1 shl 10),

    /**
     * The upper x+ axis edge of a block.
     */
    Edge11(1 shl 11),

    /**
     * The upper z- axis edge of a block.
     */
    Edge12(1 shl 12),

    /**
     * All edges of a block.
     */
    All(0xFFFF),

    /**
     * No edges of a block.
     */
    None(0)
}

infix fun Edge.or(other: Edge): Int {
    return this.mask or other.mask
}

infix fun Edge.or(other: Int): Int {
    return this.mask or other
}
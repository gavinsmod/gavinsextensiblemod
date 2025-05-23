﻿/*
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

package com.peasenet.extensions

import com.peasenet.util.block.Edge
import com.peasenet.util.block.Neighbors

/**
 * Extension functions for the Int class.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 01-15-2025
 */

// ok look at this
fun Int.wrapAround(minVal: Int, maxVal: Int): Int {
    if (this < minVal) return maxVal
    if (this > maxVal) return minVal
    return this
}

fun Int.or(other: Neighbors) {
    this or other.mask
}

fun Int.and(other: Neighbors) {
    this and other.mask
}

fun Int.not(other: Neighbors) {
    this and other.mask.inv()
}

infix fun Int.nand(other: Neighbors) {
    this and other.mask.inv()
}

infix fun Int.or(other: Edge): Int {
    return this or other.mask
}

infix fun Int.and(other: Edge): Int {
    return this and other.mask
}

infix fun Int.not(other: Edge) {
    this and other.mask.inv()
}

infix fun Int.nand(other: Edge): Int {
    return this and other.mask.inv()
}

infix operator fun Int.times(other: Neighbors): Int {
    return this * other.mask
}
/*
 * MIT License
 *
 * Copyright (c) 2022-2024.
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

package com.peasenet.util

/**
 * The dimension of a Minecraft world.
 * @param dimension The dimension of a Minecraft world. Either "overworld", "the_nether", or "the_end".
 * @author gt3ch1
 * @version 03/22/2023
 */
enum class Dimension(val dimension: String) {
    OVERWORLD("overworld"),
    NETHER("the_nether"),
    END("the_end");

    companion object {
        /**
         * Gets the dimension from a string.
         */
        fun fromValue(dim: String): Dimension {
            for (d in Dimension.values()) {
                if (d.dimension == dim)
                    return d
            }
            throw NotImplementedError("Dimension $dim is not implemented.")
        }
    }
}
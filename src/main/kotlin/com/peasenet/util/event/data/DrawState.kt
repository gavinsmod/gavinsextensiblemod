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
package com.peasenet.util.event.data

import net.minecraft.world.level.block.state.BlockState

/**
 * Draw side data class for the draw side event. This event is cancelable.
 * @param state The current [BlockState]
 * @see Cancellable
 * @author GT3CH1
 * @version 01-26-2025
 * @since 03-02-2023
 */
data class DrawState(val state: BlockState) : Cancellable() {
    private var shouldDraw: Boolean? = null

    /**
     * Sets whether this state should be drawn.
     * @param shouldDraw Whether this state should be drawn.
     */
    fun setShouldDraw(shouldDraw: Boolean) {
        this.shouldDraw = shouldDraw
    }

    /**
     * Gets whether this state should be drawn. If null, [setShouldDraw] was not called.
     */
    fun shouldDraw(): Boolean? {
        return shouldDraw
    }
}
/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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

package com.peasenet.util.event

import com.peasenet.util.event.data.BlockRender
import com.peasenet.util.listeners.BlockRenderListener
import net.minecraft.block.BlockState
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class BlockRenderEvent : Event<BlockRenderListener> {


    private var blockRender: BlockRender

    /**
     * Creates a new world render event.
     *
     * @param stack     - The matrix stack.
     * @param buffer    - The buffer builder.
     * @param center    - The box.
     * @param playerPos - The delta.
     */
    constructor(
        blockState: BlockState,
        stack: MatrixStack,
        buffer: BufferBuilder,
        blockPos: BlockPos,
        playerPos: Vec3d,
        delta: Float
    ) {
        blockRender = BlockRender(blockState, stack, buffer, blockPos, playerPos, delta)
    }
    
    constructor(blockRender: BlockRender) {
        this.blockRender = blockRender;
    }

    override fun fire(listeners: ArrayList<BlockRenderListener>) {
        for (listener in listeners)
        {
            listener.onBlockRender(blockRender)
        }
    }

    override val event: Class<BlockRenderListener>
        get() = BlockRenderListener::class.java
}
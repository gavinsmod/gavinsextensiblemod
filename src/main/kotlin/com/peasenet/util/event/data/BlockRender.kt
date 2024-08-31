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
package com.peasenet.util.event.data

import net.minecraft.block.BlockState
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

/**
 * Data class for the block BlockState render event.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class BlockRender
/**
 * Creates a new block BlockState render event.
 *
 * @param blockState    - The block BlockState.
 * @param stack     - The matrix stack.
 * @param buffer    - The buffer builder.
 * @param center    - The center of the block BlockState.
 * @param playerPos - The current player's position.
 * @param delta     - Change in ticks.
 */(
    /**
     * The block BlockState.
     */
    var blockState : BlockState,
    /**
     * The matrix stack.
     */
    var stack: MatrixStack?,
    /**
     * The buffer builder.
     */
    var buffer: BufferBuilder?,
    /**
     * The center of the block BlockState.
     */
    var blockPos: BlockPos?,
    /**
     * The current player's position.
     */
    var playerPos: Vec3d?,
    /**
     * Change in ticks.
     */
    var delta: Float
) : Cancellable()

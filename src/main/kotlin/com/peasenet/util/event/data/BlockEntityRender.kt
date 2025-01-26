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

import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

/**
 * Data class for the block entity render event. This can be cancelled.
 * @see Cancellable
 * @author GT3CH1
 * @version 01-26-2025
 * @since 03-02-2023
 */
data class BlockEntityRender
/**
 * Creates a new block entity render event.
 *
 * @param entity    - The block entity.
 * @param stack     - The matrix stack.
 * @param buffer    - The buffer builder.
 * @param center    - The center of the block entity.
 * @param playerPos - The current player's position.
 * @param delta     - Change in ticks.
 */(
    var entity: BlockEntity,
    var stack: MatrixStack?,
    private var buffer: BufferBuilder?,
    private var center: Vec3d?,
    private var playerPos: Vec3d?,
    var delta: Float,
) : Cancellable()

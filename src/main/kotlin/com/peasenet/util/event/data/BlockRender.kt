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
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

/**
 * Data class for the block BlockState render event. This can be cancelled.
 * @see Cancellable
 *
 * @param blockState The block BlockState.
 * @param stack The matrix stack.
 * @param buffer The buffer builder.
 * @param blockPos The center of the block BlockState.
 * @param playerPos The current player's position.
 * @param delta Change in ticks.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 03-02-2023
 */
data class BlockRender
    (
    private var blockState: BlockState,
    var stack: PoseStack?,
    private var buffer: BufferBuilder?,
    var blockPos: BlockPos?,
    private var playerPos: Vec3?,
    var delta: Float,
) : Cancellable()

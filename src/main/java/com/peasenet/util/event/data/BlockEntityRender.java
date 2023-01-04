/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.util.event.data;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

/**
 * Data class for the block entity render event.
 *
 * @author gt3ch1
 * @version 01/03/2022
 */
public class BlockEntityRender extends Cancellable {

    /**
     * The block entity.
     */
    public BlockEntity entity;

    /**
     * The matrix stack.
     */
    public MatrixStack stack;

    /**
     * The buffer builder.
     */
    public BufferBuilder buffer;

    /**
     * The center of the block entity.
     */
    public Vec3d center;

    /**
     * The current player's position.
     */
    public Vec3d playerPos;

    /**
     * Change in ticks.
     */
    public float delta;

    /**
     * Creates a new block entity render event.
     *
     * @param entity    - The block entity.
     * @param stack     - The matrix stack.
     * @param buffer    - The buffer builder.
     * @param center    - The center of the block entity.
     * @param playerPos - The current player's position.
     * @param delta     - Change in ticks.
     */
    public BlockEntityRender(BlockEntity entity, MatrixStack stack, BufferBuilder buffer, Vec3d center, Vec3d playerPos, float delta) {
        this.entity = entity;
        this.stack = stack;
        this.buffer = buffer;
        this.center = center;
        this.playerPos = playerPos;
        this.delta = delta;
    }

}

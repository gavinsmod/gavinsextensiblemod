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

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

/**
 * Data class for the tessellate block event.
 *
 * @author gt3ch1
 * @version 01/03/2023
 */
public class TessellateBlock extends Cancellable {

    /**
     * The block state.
     */
    BlockState blockState;

    /**
     * The block position.
     */
    BlockPos blockPos;

    /**
     * The baked model.
     */
    BakedModel model;

    /**
     * The matrix stack.
     */
    MatrixStack matrixStack;

    /**
     * Creates a new TessellateBlock data class.
     *
     * @param blockState  - The block state.
     * @param blockPos    - The block position.
     * @param model       - The baked model.
     * @param matrixStack - The matrix stack.
     */
    public TessellateBlock(BlockState blockState, BlockPos blockPos, BakedModel model, MatrixStack matrixStack) {
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.model = model;
        this.matrixStack = matrixStack;
    }

    /**
     * Gets the block state.
     *
     * @return The block state.
     */
    public BlockState getBlockState() {
        return blockState;
    }

    /**
     * Gets the block position.
     *
     * @return The block position.
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * Gets the baked model.
     *
     * @return The baked model.
     */
    public BakedModel getModel() {
        return model;
    }

    /**
     * Gets the matrix stack.
     *
     * @return The matrix stack.
     */
    public MatrixStack getMatrixStack() {
        return matrixStack;
    }
}

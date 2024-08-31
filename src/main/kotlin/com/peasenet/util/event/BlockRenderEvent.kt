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
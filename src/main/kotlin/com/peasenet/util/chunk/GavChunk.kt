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

package com.peasenet.util.chunk

import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsModClient
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import net.minecraft.block.BlockState
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.*
import net.minecraft.world.chunk.Chunk
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * A GavChunk is a chunk that contains a list of GavBlocks used for
 * block ESP or tracers.
 *
 * @param x The x position of the chunk.
 * @param z The z position of the chunk.
 * @since 09-12-2024
 * @version 09-12-2024
 * @author GT3CH1
 */
class GavChunk(val x: Int, val z: Int) {
    /**
     * The position of the chunk.
     */
    val pos: BlockPos
        get() = BlockPos(x, 0, z)

    fun getCoordinate(xOffset: Int = 0, zOffset: Int = 0) = Pair(x + xOffset, z + zOffset)

    constructor(blockPos: BlockPos) : this(blockPos.x, blockPos.z) {
    }

    /**
     * A map of GavBlocks in the chunk.
     */
    private val blocks: HashMap<Long, GavBlock> = HashMap()

    private val visibleBlocks = HashSet<GavBlock>()

    /**
     * Adds a block to the chunk.
     *
     * @param pos The position of the block.
     */
    fun addBlock(pos: BlockPos, visibleFilter: (BlockState, BlockPos) -> Boolean) {
        val block = GavBlock(pos.x, pos.y, pos.z, visibleFilter)
        addBlock(block)
    }

    fun addBlock(block: GavBlock) {
        synchronized(this) {
            blocks[block.key] = block
            block.update()
            if (block.isVisible()) {
                visibleBlocks.add(block)
            }
        }
    }

    private fun updateBlockNeighbors(block: GavBlock) {
        setOf(
            block.pos.up(),
            block.pos.down(),
            block.pos.north(),
            block.pos.south(),
            block.pos.east(),
            block.pos.west(),
            block.pos.up().north(),
            block.pos.up().south(),
            block.pos.up().east(),
            block.pos.up().west(),
            block.pos.up().north().east(),
            block.pos.up().north().west(),
            block.pos.up().south().east(),
            block.pos.up().south().west(),
            block.pos.down().north(),
            block.pos.down().south(),
            block.pos.down().east(),
            block.pos.down().west(),
            block.pos.down().north().east(),
            block.pos.down().north().west(),
            block.pos.down().south().east(),
            block.pos.down().south().west(),
        ).forEach {
            updateBlock(it)
        }
    }

    fun removeBlock(block: GavBlock) {
        synchronized(this) {
            blocks.remove(block.key)
            visibleBlocks.remove(block)
            updateBlockNeighbors(block)
//            updateBlocks()
        }
    }

    fun clear() {
        synchronized(this) {
            blocks.clear()
            visibleBlocks.clear()
        }
    }

    private fun updateBlock(blockPos: BlockPos) {
        val gavBlock = GavBlock(blockPos.x, blockPos.y, blockPos.z)
        synchronized(this) { updateBlock(gavBlock) }
    }

    private fun updateBlock(block: GavBlock) {

        val key = block.key
        val gavBlock = blocks[key] ?: return
        gavBlock.update()
        if (gavBlock.isVisible()) {
            visibleBlocks.add(gavBlock)
        } else {
            visibleBlocks.remove(gavBlock)
        }
    }

    val hasBlocks: Boolean
        get() = blocks.isNotEmpty()

    /**
     * Updates the blocks in the chunk.
     */
    fun updateBlocks() {
        synchronized(this) {
            visibleBlocks.clear()
            blocks.values.forEach { block ->
                updateBlock(block).also {
                    if (block.isVisible()) {
                        visibleBlocks.add(block)
                    }
                }
            }
        }
    }

    /**
     * Checks if the chunk is in the render distance.
     *
     * @return True if the chunk is in the render distance, false otherwise.
     */
    fun inRenderDistance(renderDistance: Int = RenderUtils.getRenderDistance()): Boolean {
        val currentDistance = this.getRenderDistance()
        val res = currentDistance <= renderDistance
        return res
    }

    /**
     * Gets the render distance of the chunk.
     *
     * @return The render distance of the chunk.
     */
    private fun getRenderDistance(): Double {
        // get the distance from the player to the chunk
        val playerPos = GavinsModClient.minecraftClient.getPlayer().blockPos
        val chunkPos = this.pos
        val x = abs(playerPos.x - chunkPos.x.times(16))
        val z = abs(playerPos.z - chunkPos.z.times(16))
        return sqrt((x * x + z * z).toDouble()).div(16f)
    }

    /**
     * Renders the blocks in the chunk.
     *
     * @param matrixStack The matrix stack.
     * @param bufferBuilder The buffer builder.
     * @param blockColor The color of the block.q
     * @param partialTicks The partial ticks.
     * @param alpha The alpha of the block.
     * @param structureEsp True if structure ESP is enabled, false otherwise.
     * @param blockTracer True if block tracers are enabled, false otherwise.
     */
    fun render(
        matrixStack: MatrixStack, bufferBuilder: BufferBuilder, blockColor: Color,
        partialTicks: Float,
        alpha: Float,
        structureEsp: Boolean = false, blockTracer: Boolean = false,
    ) {
        synchronized(this) {
            visibleBlocks.forEach { block ->
                block.render(
                    matrixStack, bufferBuilder, blockColor, partialTicks, alpha, structureEsp, blockTracer
                )
            }
        }
    }


    companion object {
        /**
         * Searches the given chunk for blocks that are in the block list.
         * @param chunk - The Chunk to search.
         * @param blocks - The list of blocks to search for.
         * @return The GavChunk containing the blocks.
         */
        fun search(
            chunk: Chunk,
            filter: (BlockState, BlockPos) -> Boolean,
        ): GavChunk {
            val searchChunk = GavChunk(chunk.pos.x, chunk.pos.z)
            val tempBlockPos = BlockPos.Mutable()
            for (x in chunk.pos.startX until chunk.pos.endX) {
                for (z in chunk.pos.startZ..chunk.pos.endZ) {
                    val chunkHeight = 256
                    for (y in GavinsModClient.minecraftClient.getWorld().bottomY until chunkHeight) {
                        tempBlockPos.set(x, y, z)
                        val blockState = chunk.getBlockState(tempBlockPos)
                        if (filter(blockState, tempBlockPos)) {
                            searchChunk.addBlock(tempBlockPos, filter)
                        }
                    }
                }
            }
            return searchChunk
        }

    }
}


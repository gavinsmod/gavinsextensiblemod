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

import com.peasenet.config.BlockListConfig
import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsModClient
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.*
import net.minecraft.world.Heightmap
import net.minecraft.world.chunk.Chunk
import kotlin.collections.HashMap
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
    val pos: BlockPos = BlockPos(x, 0, z)

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
    fun addBlock(pos: BlockPos) {
        val block = GavBlock(pos.x, pos.y, pos.z)
        addBlock(block)
    }

    fun addBlock(block: GavBlock) {
        blocks[block.key] = block
        block.update()
        if (block.isVisible()) {
            visibleBlocks.add(block)
        }
    }

    fun removeBlock(block: GavBlock) {
        blocks.remove(block.key)
        visibleBlocks.remove(block)
    }

    fun clear() {
        blocks.clear()
        visibleBlocks.clear()
    }

    val hasBlocks: Boolean
        get() = blocks.isNotEmpty()

    /**
     * Updates the blocks in the chunk.
     */
    fun updateBlocks() {
        visibleBlocks.clear()
        for (block in blocks.values) {
            block.update()
            if (block.isVisible()) {
                visibleBlocks.add(block)
            }
        }
    }

    /**
     * Checks if the chunk is in the render distance.
     *
     * @return True if the chunk is in the render distance, false otherwise.
     */
    fun inRenderDistance(): Boolean {
        val distance = RenderUtils.getRenderDistance() - 1
        val currentDistance = this.getRenderDistance()
        val res = currentDistance <= distance
        return res
    }

    /**
     * Gets the render distance of the chunk.
     *
     * @return The render distance of the chunk.
     */
    fun getRenderDistance(): Double {
        // get the distance from the player to the chunk
        val playerPos = GavinsModClient.minecraftClient.getPlayer().blockPos
        val chunkPos = this.pos
        val x = abs(playerPos.x - chunkPos.x.times(16))
        val z = abs(playerPos.z - chunkPos.z.times(16))
        return sqrt((x * x + z * z).toDouble()).div(16)
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
        structureEsp: Boolean = false, blockTracer: Boolean
    ) {
        for (block in visibleBlocks) {
            block.render(matrixStack, bufferBuilder, blockColor, partialTicks, alpha, structureEsp, blockTracer)
        }
    }

    companion object {
        /**
         * Searches the given chunk for blocks that are in the block list.
         * @param chunk - The Chunk to search.
         * @param blocks - The list of blocks to search for.
         * @return The GavChunk containing the blocks.
         */
        fun search(chunk: Chunk, blocks: List<String>): GavChunk {
            val searchChunk = GavChunk(chunk.pos.x, chunk.pos.z)
            val tempBlockPos = BlockPos.Mutable()
            for (x in chunk.pos.startX..chunk.pos.endX) {
                for (z in chunk.pos.startZ..chunk.pos.endZ) {
                    val chunkHeight =
                        chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).get(x - chunk.pos.startX, z - chunk.pos.startZ)
                    for (y in GavinsModClient.minecraftClient.getWorld().bottomY..chunkHeight + 1) {
                        tempBlockPos.set(x, y, z)
                        val block = chunk.getBlockState(tempBlockPos).block
                        if (blocks.contains(BlockListConfig.getId(block))) {
                            searchChunk.addBlock(tempBlockPos)
                        }
                    }
                }
            }
            return searchChunk
        }

    }
}


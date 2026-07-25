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
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.chunk.ChunkAccess

/**
 * A GavChunk is a chunk that contains a list of GavBlocks used for
 * block ESP or tracers.
 *
 * @param chunkPos The position of the chunk.
 * @since 09-12-2024
 * @version 01-25-2025
 * @author GT3CH1
 */
class GavChunk(val chunkPos: ChunkPos) {

    /**
     * A map of GavBlocks in the chunk.
     */
    private val blocks: HashMap<Long, GavBlock> = HashMap()

    private val visibleBlocks = HashMap<Long, GavBlock>()

    val key: Int
        get() = chunkPos.hashCode()

    /**
     * Adds a block to the chunk.
     *
     * @param pos The position of the block.
     */
    fun addBlock(pos: BlockPos, visibleFilter: (BlockPos) -> Boolean) {
        val block = GavBlock(pos.x, pos.y, pos.z, visibleFilter)
        addBlock(block)
    }

    fun addBlock(block: GavBlock) {
        synchronized(this) {
            blocks[block.pos.asLong()] = block
            block.update()
            if (block.isVisible()) {
                visibleBlocks[block.pos.asLong()] = (block)
            }
        }
    }

    fun updateBlockNeighbors(block: GavBlock) {
        setOf(
            block.pos,
            block.pos.above(),
            block.pos.below(),
            block.pos.north(),
            block.pos.south(),
            block.pos.east(),
            block.pos.west(),
        ).forEach {
            updateBlock(it)
        }
    }

    fun removeBlock(block: GavBlock) {
        synchronized(this) {
            blocks.remove(block.pos.asLong())
            visibleBlocks.remove(block.pos.asLong())
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
        val key = block.pos.asLong()
        val gavBlock = blocks[key] ?: return
        gavBlock.update()
        if (gavBlock.isVisible()) {
            visibleBlocks[gavBlock.pos.asLong()] = gavBlock
        } else {
            visibleBlocks.remove(gavBlock.pos.asLong())
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
                updateBlock(block)
            }
        }
    }

    /**
     * Checks if the chunk is in the render distance.
     *
     * @return True if the chunk is in the render distance, false otherwise.
     */
    fun inRenderDistance(renderDistance: Int = RenderUtils.getRenderDistance()): Boolean {
        val player = GavinsModClient.minecraftClient.getPlayer()
        val playerChunk = player.chunkPosition()
        val dx = playerChunk.x - chunkPos.x
        val dz = playerChunk.z - chunkPos.z
        val renderDistanceSquared = renderDistance * renderDistance
        return (dx * dx + dz * dz) <= renderDistanceSquared
    }

    /**
     * Renders the blocks in the chunk.
     *
     * @param matrixStack The matrix stack.
     * @param buffer The line buffer.
     * @param blockColor The color of the block.q
     * @param partialTicks The partial ticks.
     * @param alpha The alpha of the block.
     * @param structureEsp True if structure ESP is enabled, false otherwise.
     * @param blockTracer True if block tracers are enabled, false otherwise.
     */
    fun render(
        matrixStack: PoseStack, blockColor: Color,
        partialTicks: Float,
        alpha: Float,
        structureEsp: Boolean = false, blockTracer: Boolean = false,
        buffer: VertexConsumer,
        maxBlockRenderDistance: Double? = null,
        blockRenderFilter: ((GavBlock) -> Boolean)? = null,
    ) {
        synchronized(this) {
            if (visibleBlocks.isEmpty()) return
            renderBlockSnapshot.clear()
            renderBlockSnapshot.addAll(visibleBlocks.values)
        }

        val cameraPos = RenderUtils.getCameraPos(partialTicks)
        val tracerStart = if (blockTracer) cameraPos.add(RenderUtils.getLookVec()) else null
        val playerPos = GavinsModClient.minecraftClient.getPlayer().position()
        val maxBlockDistanceSq = maxBlockRenderDistance?.let { it * it }

        for (block in renderBlockSnapshot) {
            if (maxBlockDistanceSq != null) {
                val dx = (block.x + 0.5) - playerPos.x
                val dy = (block.y + 0.5) - playerPos.y
                val dz = (block.z + 0.5) - playerPos.z
                if ((dx * dx + dy * dy + dz * dz) > maxBlockDistanceSq) {
                    continue
                }
            }
            if (blockRenderFilter != null && !blockRenderFilter.invoke(block)) {
                continue
            }
            block.render(
                matrixStack,
                blockColor,
                partialTicks,
                alpha,
                structureEsp,
                blockTracer,
                buffer,
                cameraPos,
                tracerStart,
            )
        }
    }


    companion object {
        /**
         * Searches the given chunk for blocks that are in the block list.
         * @param chunk The Chunk to search.
         * @param filter The filter to use.
         * @return The GavChunk containing the blocks.
         */
        fun search(
            chunk: ChunkAccess,
            filter: (BlockPos) -> Boolean,
        ): GavChunk {
            val searchChunk = GavChunk(chunk.pos)
            val tempBlockPos = BlockPos.MutableBlockPos()
            for (x in chunk.pos.minBlockX..chunk.pos.maxBlockX) {
                for (z in chunk.pos.minBlockZ..chunk.pos.maxBlockZ) {
                    val chunkHeight = GavinsModClient.minecraftClient.getWorld()
                        .getHeightmapPos(Heightmap.Types.WORLD_SURFACE, BlockPos(x, 0, z)).y + 1
                    for (y in GavinsModClient.minecraftClient.getWorld().minSectionY until chunkHeight) {
                        tempBlockPos.set(x, y, z)
                        if (filter(tempBlockPos)) {
                            searchChunk.addBlock(tempBlockPos, filter)
                        }
                    }
                }
            }
            return searchChunk
        }
    }

    private val renderBlockSnapshot = ArrayList<GavBlock>()
}


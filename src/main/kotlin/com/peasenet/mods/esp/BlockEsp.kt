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

package com.peasenet.mods.esp

import com.peasenet.annotations.Exclude
import com.peasenet.config.commons.IBlockEspTracerConfig
import com.peasenet.main.GavinsMod
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.event.data.WorldRender
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.block.BlockState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk

/**
 *
 * @author GT3CH1
 * @version 01-18-2025
 * @since 01-18-2025
 */
abstract class BlockEsp<T : IBlockEspTracerConfig>(
    translationKey: String,
    chatCommand: String,
) : EspMod<T>(
    translationKey, chatCommand
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener, RenderListener {

    @Exclude
    protected val chunks = HashMap<Long, GavChunk>()

    abstract fun getSettings(): T

    open fun chunkInRenderDistance(chunk: GavChunk): Boolean = true

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        synchronized(chunks) {
            if (chunks.isEmpty()) return
            RenderUtils.setupRenderWithShader(matrixStack)
            val bufferBuilder = RenderUtils.getBufferBuilder()
            chunks.values.filter { chunkInRenderDistance(it) }.toMutableList().forEach {
//                synchronized(it) {
                    it.render(
                        matrixStack,
                        bufferBuilder,
                        getSettings().blockColor,
                        partialTicks,
                        getSettings().alpha,
                        getSettings().structureEsp,
                        getSettings().blockTracer
                    )
//                }
            }
            RenderUtils.drawBuffer(bufferBuilder, matrixStack)
        }
    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        searchChunk(chunkUpdate.chunk)
    }


    open fun searchBlock(chunk: Chunk, blockPos: BlockPos, blockState: BlockState): Boolean {
        return true
    }

    override fun onWorldRender(worldRender: WorldRender) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }

    /**
     * Updates the neighbor chunks of the given chunk.
     * @param chunk - The Chunk to update the neighbors of.
     */
    protected fun updateNeighborChunks(chunk: Chunk) {
        val main = chunks[ChunkPos.toLong(chunk.pos.x, chunk.pos.z)]
        val east = chunks[ChunkPos.toLong(chunk.pos.x + 1, chunk.pos.z)]
        val west = chunks[ChunkPos.toLong(chunk.pos.x - 1, chunk.pos.z)]
        val south = chunks[ChunkPos.toLong(chunk.pos.x, chunk.pos.z + 1)]
        val north = chunks[ChunkPos.toLong(chunk.pos.x, chunk.pos.z - 1)]
        main?.updateBlocks()
        north?.updateBlocks()
        south?.updateBlocks()
        east?.updateBlocks()
        west?.updateBlocks()
    }

    protected fun addBlocksFromChunk(searchedChunk: GavChunk, chunk: Chunk) {
        synchronized(chunks) {
            if (searchedChunk.hasBlocks) {
                chunks[chunk.pos.toLong()] = searchedChunk
            } else {
                chunks[chunk.pos.toLong()]?.clear()
                chunks.remove(chunk.pos.toLong())
            }
        }
    }

    fun updateBlock(x: Int, y: Int, z: Int) {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = client.getWorld().getChunk(chunkX, chunkZ) ?: return
        val key = ChunkPos.toLong(chunkX, chunkZ)
        val espChunk = chunks[key] ?: return
        espChunk.updateBlocks();
        searchChunk(chunk)
//        updateNeighborChunks(chunk)
    }

    protected fun checkChunk(
        key: Long,
        bue: BlockUpdate,
        added: Boolean,
        gavBlock: GavBlock,
        chunk: Chunk,
    ) {
//        searchChunk(chunk)
        var espChunk = chunks[key]
        if (espChunk == null) {
            val gavChunk = GavChunk(bue.blockPos)
            chunks[key] = gavChunk
            GavinsMod.LOGGER.debug("(onBlockUpdate) Added chunk: $key")
            espChunk = gavChunk

        }
        if (added)
            espChunk.addBlock(gavBlock)
        else
            espChunk.removeBlock(gavBlock)
        updateNeighborChunks(chunk)
    }

    abstract fun searchChunk(chunk: Chunk)
}
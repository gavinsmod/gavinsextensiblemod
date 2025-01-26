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
import com.peasenet.extensions.east
import com.peasenet.extensions.north
import com.peasenet.extensions.south
import com.peasenet.extensions.west
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.event.data.WorldRender
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.util.math.MatrixStack
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


    abstract fun getSettings(): T

    open fun chunkInRenderDistance(chunk: GavChunk): Boolean = false

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        synchronized(chunks) {
            if (chunks.isEmpty()) return
            RenderUtils.setupRenderWithShader(matrixStack)
            val bufferBuilder = RenderUtils.getBufferBuilder()
            chunks.values.filter { chunkInRenderDistance(it) }.toMutableList().forEach {
                it.render(
                    matrixStack,
                    bufferBuilder,
                    getSettings().blockColor,
                    partialTicks,
                    getSettings().alpha,
                    getSettings().structureEsp,
                    getSettings().blockTracer
                )
            }
            RenderUtils.drawBuffer(bufferBuilder, matrixStack)
        }
    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        searchChunk(chunkUpdate.chunk)
    }

    override fun onWorldRender(worldRender: WorldRender) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }

    /**
     * Updates the neighbor chunks of the given chunk in a 3x3 grid with the given chunk in the center.
     * @param chunk - The Chunk to update the neighbors of.
     */
    private fun updateNeighborChunks(chunk: Chunk) {
        val main = chunks[chunk.pos.toLong()]
        val north = chunks[chunk.pos.north().toLong()]
        val south = chunks[chunk.pos.south().toLong()]
        val east = chunks[chunk.pos.east().toLong()]
        val west = chunks[chunk.pos.west().toLong()]
        val northEast = chunks[chunk.pos.east().north().toLong()]
        val northWest = chunks[chunk.pos.west().north().toLong()]
        val southEast = chunks[chunk.pos.east().south().toLong()]
        val southWest = chunks[chunk.pos.west().south().toLong()]
        main?.updateBlocks()
        north?.updateBlocks()
        south?.updateBlocks()
        east?.updateBlocks()
        west?.updateBlocks()
        northEast?.updateBlocks()
        northWest?.updateBlocks()
        southEast?.updateBlocks()
        southWest?.updateBlocks()
    }

    /**
     * Adds
     */
    protected fun addBlocksFromChunk(searchedChunk: GavChunk, chunk: Chunk) {
        synchronized(chunks) {
            chunks[searchedChunk.chunkPos.toLong()]?.clear()
            if (searchedChunk.hasBlocks) {
                chunks[chunk.pos.toLong()] = searchedChunk
            } else {
                chunks.remove(chunk.pos.toLong())
            }
            updateNeighborChunks(chunk)
        }
    }

    protected fun checkChunk(
        added: Boolean,
        remove: Boolean,
        gavBlock: GavBlock,
        chunk: Chunk,
    ) {
        val key = chunk.pos.toLong()
        var espChunk = chunks[key]
        if (espChunk == null) {
            val gavChunk = GavChunk(chunk.pos)
            chunks[key] = gavChunk
            espChunk = gavChunk
        }
        if (added)
            espChunk.addBlock(gavBlock)
        else if (remove)
            espChunk.removeBlock(gavBlock)
        espChunk.updateBlockNeighbors(gavBlock)
        updateNeighborChunks(chunk)
    }

    abstract fun searchChunk(chunk: Chunk)

    companion object {
        @Exclude
        @JvmStatic
        val chunks = HashMap<Long, GavChunk>()
    }
}
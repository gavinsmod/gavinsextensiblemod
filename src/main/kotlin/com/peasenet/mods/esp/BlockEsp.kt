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

import com.peasenet.config.commons.IBlockEspTracerConfig
import com.peasenet.extensions.east
import com.peasenet.extensions.north
import com.peasenet.extensions.south
import com.peasenet.extensions.west
import com.peasenet.util.GemRenderLayers
import com.peasenet.util.RenderUtils.getVertexConsumerProvider
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.event.data.WorldRender
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import kotlinx.atomicfu.locks.synchronized
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk
import org.joml.Matrix3x2fStack
import org.lwjgl.opengl.GL11

/**
 *
 * An abstract class for block ESP mods.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 * @param T The type of the config that must extend [IBlockEspTracerConfig].
 *
 * @see EspMod
 * @see BlockUpdateListener
 * @see WorldRenderListener
 * @see ChunkUpdateListener
 * @see RenderListener
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 01-18-2025
 */
abstract class BlockEsp<T : IBlockEspTracerConfig>(
    translationKey: String,
    chatCommand: String,
) : EspMod<T>(
    translationKey, chatCommand
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener, RenderListener {


    override fun onEnable() {
        super.onEnable()
        synchronized(chunks) {
            chunks.clear()
        }
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        synchronized(chunks) {
            chunks.clear()
        }
    }

    /**
     * Gets the settings of type [T].
     * @see IBlockEspTracerConfig
     */
    abstract fun getSettings(): T


    /**
     * Performs a search on the given chunk to find blocks to add to a list of [GavChunk]s.
     * @param chunk The chunk to search.
     */
    abstract fun searchChunk(chunk: Chunk)

    /**
     * Gets if the chunk is in render distance.
     * ~~~kotlin
     * val distance = MinecraftClient.getInstance().gameRenderer.viewDistance / 2
     * return chunk.inRenderDistance(distance)
     * ~~~
     * @param chunk The chunk to check.
     * @return If the chunk is in render distance.
     */
    open fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance()
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        // TODO: MC 1.21.10 update
        synchronized(chunks) {
            if (chunks.isEmpty()) return
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            val vcp = getVertexConsumerProvider()
            val layer = GemRenderLayers.LINES
            val buffer = vcp.getBuffer(layer)
            chunks.values.filter { chunkInRenderDistance(it) }.toMutableList().forEach {
                it.render(
                    matrixStack,
                    getSettings().blockColor,
                    partialTicks,
                    getSettings().alpha,
                    getSettings().structureEsp,
                    getSettings().blockTracer,
                    buffer
                )
            }

            vcp.draw(layer)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
//            RenderUtils.drawBuffer(bufferBuilder, matrixStack)
        }
    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        synchronized(chunks) {
            searchChunk(chunkUpdate.chunk)
        }
    }

    override fun onWorldRender(worldRender: WorldRender) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }


    /**
     * Adds the given
     */
    protected fun addBlocksFromChunk(searchedChunk: GavChunk) {
        synchronized(chunks) {
            chunks[searchedChunk.key]?.clear()
            if (searchedChunk.hasBlocks) {
                chunks[searchedChunk.key] = searchedChunk
            } else {
                chunks.remove(searchedChunk.key)
            }
            updateNeighborChunks(searchedChunk.chunkPos)
        }
    }

    /**
     * Updates the chunk with the given [chunkPos] and [gavBlock]. If [addBlock] is true, the block will be added to the chunk.
     * Otherwise, the block will be removed from the chunk.
     *
     * @param addBlock If the block should be added to the chunk.
     * @param gavBlock The [GavBlock] to add or remove.
     * @param chunkPos The [ChunkPos] of the chunk to update.
     */
    protected fun updateChunk(
        addBlock: Boolean,
        gavBlock: GavBlock,
        chunkPos: ChunkPos,
    ) {
        val key = chunkPos.toLong()
        var espChunk = chunks[key]
        if (espChunk == null) {
            val gavChunk = GavChunk(chunkPos)
            chunks[key] = gavChunk
            espChunk = gavChunk
        }
        if (addBlock)
            espChunk.addBlock(gavBlock)
        else
            espChunk.removeBlock(gavBlock)
        espChunk.updateBlockNeighbors(gavBlock)
        updateNeighborChunks(chunkPos)
    }

    /**
     * Updates the neighbor chunks of the given chunk in a 3x3 grid with the given [chunkPos] in the center.
     * @param chunkPos The center chunk position.
     */
    private fun updateNeighborChunks(chunkPos: ChunkPos) {
        val main = chunks[chunkPos.toLong()]
        val north = chunks[chunkPos.north().toLong()]
        val south = chunks[chunkPos.south().toLong()]
        val east = chunks[chunkPos.east().toLong()]
        val west = chunks[chunkPos.west().toLong()]
        val northEast = chunks[chunkPos.east().north().toLong()]
        val northWest = chunks[chunkPos.west().north().toLong()]
        val southEast = chunks[chunkPos.east().south().toLong()]
        val southWest = chunks[chunkPos.west().south().toLong()]
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

    val chunks = HashMap<Long, GavChunk>()
}
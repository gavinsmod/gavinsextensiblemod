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

import com.peasenet.config.commons.BlockListConfig
import com.peasenet.config.esp.BlockEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.util.Direction
import com.peasenet.main.Settings
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 09-01-2024
 * @see EspMod
 */
class ModBlockEsp : BlockEsp<BlockEspConfig>(
    "gavinsmod.mod.esp.blockesp", "blockesp"
) {

    init {
        subSettings {
            title = translationKey
            direction = Direction.RIGHT
            colorSetting {
                title = "gavinsmod.generic.color"
                color = getSettings().blockColor
                callback = {
                    getSettings().blockColor = it.color
                }
            }
            slideSetting {
                title = "gavinsmod.settings.alpha"
                value = getSettings().alpha
                callback = {
                    getSettings().alpha = it.value
                }
            }
            toggleSetting {
                title = "gavinsmod.mod.esp.blockesp.structure"
                callback = {
                    getSettings().structureEsp = it.state
                }
            }
            toggleSetting {
                title = "gavinsmod.generic.tracers"
                callback = {
                    getSettings().blockTracer = it.state
                }
            }
        }
    }


    override fun onEnable() {
        em.subscribe(RenderListener::class.java, this)
        chunks.clear()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
        // search for chunks within render distance
        GemExecutor.execute {
            RenderUtils.getVisibleChunks().forEach(this::searchChunk)
        }
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        chunks.clear()
        super.onDisable()
    }

    override fun getColor(): Color {
        return getSettings().blockColor
    }


    private val blocks
        get() = getSettings().blocks

    override fun getSettings(): BlockEspConfig = Settings.getConfig("blockesp")

    private fun blockFilter(blockPos: BlockPos): Boolean {
        return blocks.contains(BlockListConfig.getId(world.getBlockState(blockPos).block))
    }

    override fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance()
    }

    override fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            synchronized(chunk) {
                GavChunk.search(
                    chunk
                ) { pos ->
                    blockFilter(pos)
                }.also {
                    addBlocksFromChunk(it)
                }
            }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val enabledBlocks = config.blocks
        val chunk = client.getWorld().getChunk(bue.blockPos) ?: return
        val blockOldId = BlockListConfig.getId(bue.oldState.block)
        val blockNewId = BlockListConfig.getId(bue.newState.block)
        val added = enabledBlocks.contains(blockNewId) && !enabledBlocks.contains(blockOldId)
        val removed = !added && !enabledBlocks.contains(blockNewId) && enabledBlocks.contains(blockOldId)
        if (!added && !removed) {
            return
        }
        val gavBlock = GavBlock(bue.blockPos) { blockPos ->
            blockFilter(blockPos)
        }
        updateChunk(added, gavBlock, chunk.pos)
    }


    private companion object {
        val config: BlockEspConfig = Settings.getConfig("blockesp")
    }
}
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

import com.peasenet.config.esp.CaveEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsMod
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.WorldRender
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.block.MultifaceGrowthBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 01-18-2025
 * @since 09-01-2024
 * @see EspMod
 */
class ModCaveEsp : BlockEsp<CaveEspConfig>(
    "gavinsmod.mod.esp.cave", ChatCommand.CaveEsp.chatCommand
) {

    private val chunksToRender: Int
        get() {
            return (MinecraftClient.getInstance().options.viewDistance.value)
        }

    init {
        val subSetting = SettingBuilder().setTitle(translationKey).buildSubSetting()
        val colorSetting =
            SettingBuilder().setTitle("gavinsmod.generic.color").setColor(getSettings().blockColor).buildColorSetting()
        colorSetting.setCallback {
            getSettings().blockColor = colorSetting.color
        }
        val alphaSetting =
            SettingBuilder().setTitle("gavinsmod.generic.alpha").setValue(getSettings().alpha).buildSlider()
        alphaSetting.setCallback {
            getSettings().alpha = alphaSetting.value
        }
        subSetting.add(alphaSetting)
        subSetting.add(colorSetting)
        addSetting(subSetting)
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
            val visibleChunks: List<Chunk> = RenderUtils.getVisibleChunks(chunksToRender)
            visibleChunks.forEach(this::searchChunk)
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


    override fun getSettings(): CaveEspConfig = Settings.getConfig("caveesp")


    override fun searchBlock(chunk: Chunk, blockPos: BlockPos, blockState: BlockState): Boolean {
        if (!blockState.isAir && blockState.block !is MultifaceGrowthBlock && !blockState.isLiquid)
            return false
        return ((canWalkThrough(blockPos, blockState, chunk) || canWalkOn(blockPos, blockState, chunk)) && hasRoof(
            blockPos,
            blockState,
            chunk
        ))
    }

    private fun canWalkThrough(blockPos: BlockPos, blockState: BlockState, chunk: Chunk): Boolean {
        val above = chunk.getBlockState(blockPos.up())
        val below = chunk.getBlockState(blockPos.down(1))
        return blockState.isAir && (above.isAir || below.isAir)
    }

    private fun canWalkOn(blockPos: BlockPos, blockState: BlockState, chunk: Chunk): Boolean {
        val below = chunk.getBlockState(blockPos.down())
        return blockState.isAir && !below.isAir && canWalkThrough(blockPos, below, chunk)
    }

    private fun hasRoof(blockPos: BlockPos, blockState: BlockState, chunk: Chunk): Boolean {
        var tmpBlockPos = BlockPos.Mutable(blockPos.x, blockPos.y, blockPos.z)
        try {
            while (tmpBlockPos.y < 256) {
                val blockState1 = chunk.getBlockState(tmpBlockPos)
                if (!blockState1.isAir && blockState1.block !is LeavesBlock) {
                    return true
                }
                tmpBlockPos = tmpBlockPos.up().mutableCopy()
            }
        } catch (exception: IllegalArgumentException) {
            GavinsMod.LOGGER.error("Error for checking roof, blockPos: $blockPos, chunk x: ${chunk.pos.startX}, chunk z: ${chunk.pos.startZ}")
        }
        return false
    }


    /**
     * Searches the given chunk for blocks that are in the block list.
     * @param chunk - The Chunk to search.
     */
    override fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            synchronized(chunk) {
                GavChunk.search(
                    chunk
                ) { blockState, pos ->
                    searchBlock(chunk, pos, blockState)
                }.also {
                    addBlocksFromChunk(it, chunk)
                }
            }
        }
    }

    override fun onWorldRender(worldRender: WorldRender) {
        synchronized(chunks) {
            chunks.values.removeIf { !it.inRenderDistance(chunksToRender) }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val added = bue.newState.isAir && !bue.oldState.isAir
        val removed = !added && !bue.newState.isAir && bue.oldState.isAir
        val key = ChunkPos.toLong(bue.blockPos)
        val chunk = client.getWorld().getChunk(bue.blockPos) ?: return
        if (!added && !removed && !searchBlock(chunk, bue.blockPos, bue.newState)) {
            return
        }
        val gavBlock = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z) { blockState, blockPos ->
            searchBlock(chunk, blockPos, bue.newState)
        }
//        checkChunk(key, bue, added, gavBlock, chunk)
        searchChunk(chunk)
    }

    override fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance(chunksToRender)
    }
}
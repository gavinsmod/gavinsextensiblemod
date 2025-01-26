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
import com.peasenet.settings.CycleSetting
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
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 01-18-2025
 * @see EspMod
 * @see BlockEsp
 * @see CaveEspConfig
 */
class ModCaveEsp : BlockEsp<CaveEspConfig>(
    "gavinsmod.mod.esp.cave", ChatCommand.CaveEsp.chatCommand
) {
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

        val searchMode = getSettings().searchMode.name.lowercase()
        val searchModeSetting = SettingBuilder()
            .setTitle(searchTranslationKey)
            .setOptions(SearchType.entries.map { it.name.lowercase() })
            .setValue(searchMode)
            .buildCycleSetting()
        searchModeSetting.setCallback {
            updateSearchMode(searchModeSetting)
        }
        searchModeSetting.gui.title = Text.translatable("$searchTranslationKey.$searchMode")

        subSetting.add(alphaSetting)
        subSetting.add(colorSetting)
        subSetting.add(searchModeSetting)
        addSetting(subSetting)
    }

    companion object {
        private const val searchTranslationKey = "gavinsmod.mod.esp.cave"
    }

    private val chunksToRender: Int
        get() {
            return (MinecraftClient.getInstance().options.viewDistance.value)
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
//            chunks.values.forEach { searchChunk(it) }
            RenderUtils.getVisibleChunks(chunksToRender).forEach { searchChunk(it) }
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


    override fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            synchronized(chunk) {
                GavChunk.search(
                    chunk
                ) { pos ->
                    searchBlock(pos)
                }.also {
                    addBlocksFromChunk(it)
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
        val chunk = world.getChunk(bue.blockPos) ?: return
        val gavBlock = GavBlock(bue.blockPos) { pos -> searchBlock(pos) }
        if (!added && !removed) {
            return
        }
        updateChunk(added, gavBlock, chunk.pos)
    }

    override fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance(chunksToRender)
    }


    /**
     * Callback for when the search mode is changed. This will update what search parameters the
     * feature is using.
     * @param searchMode The new search mode.
     * @see SearchType
     */
    private fun updateSearchMode(searchMode: CycleSetting) {
        getSettings().searchMode = when (searchMode.gui.currentIndex) {
            0 -> SearchType.Caves
            1 -> SearchType.Tunnel
            else -> SearchType.Caves
        }
        val searchModeName = getSettings().searchMode.name.lowercase()
        searchMode.gui.title = Text.translatable("$searchTranslationKey.$searchModeName")
        GemExecutor.execute {
            val visibleChunks: List<Chunk> = RenderUtils.getVisibleChunks(chunksToRender)
            visibleChunks.forEach(this::searchChunk)
        }
    }

    /**
     * Helper method to search a [GavChunk] for blocks.
     * @param gavChunk The [GavChunk] to search.
     *
     * @see searchChunk
     */
    private fun searchChunk(gavChunk: GavChunk) {
        val chunk = world.getChunk(gavChunk.chunkPos.x, gavChunk.chunkPos.z) ?: return
        searchChunk(chunk)
    }

    /**
     * Checks if the given [blockPos] is a valid block to render, depending on the [SearchType] setting.
     * @param blockPos The [BlockPos] to check.
     * @return True if the block at [blockPos] is air, and that the player can walk through or on it, false otherwise.
     *
     * @see SearchType
     */
    private fun searchBlock(blockPos: BlockPos): Boolean {
        val newBlockState = world.getBlockState(blockPos)
        if (!newBlockState.isAir && newBlockState.block !is MultifaceGrowthBlock && !newBlockState.isLiquid) return false
        val searchMode = getSettings().searchMode
        return when (searchMode) {
            SearchType.Caves -> {
                ((canWalkThrough(blockPos, newBlockState) || canWalkOn(blockPos, newBlockState)) && hasRoof(
                    blockPos
                ))
            }

            SearchType.Tunnel -> {
                return isTunnel(blockPos) && hasRoof(blockPos)
            }
        }
    }

    private fun isTunnel(blockPos: BlockPos): Boolean {
        return world.getBlockState(blockPos.up()).isAir && !world.getBlockState(blockPos.up(2)).isAir
    }


    /**
     * Gets whether the player can walk through this [blockState] at [blockPos], by checking if there is air above or below.
     * @param blockPos The [BlockPos] to check.
     * @param blockState The [BlockState] to check.
     * @return True if the player can walk through this block, false otherwise.
     */
    private fun canWalkThrough(blockPos: BlockPos, blockState: BlockState): Boolean {
        val above = world.getBlockState(blockPos.up())
        val below = world.getBlockState(blockPos.down())
        return blockState.isAir && (above.isAir || below.isAir)
    }

    /**
     * Gets whether the player can walk on this [blockState] at [blockPos].
     * @param blockPos The [BlockPos] to check.
     * @param blockState The [BlockState] to check.
     */
    private fun canWalkOn(blockPos: BlockPos, blockState: BlockState): Boolean {
        val below = world.getBlockState(blockPos.down())
        return blockState.isAir && !below.isAir && canWalkThrough(blockPos, blockState)
    }

    /**
     * Checks whether this [blockPos] has a roof, i.e. there is a block above it.
     * @param blockPos - The [BlockPos] to check.
     * @return True if there is a block above it, false otherwise.
     */
    private fun hasRoof(blockPos: BlockPos): Boolean {
        var tmpBlockPos = BlockPos.Mutable(blockPos.x, blockPos.y, blockPos.z)
        try {
            val maxY = world.getChunk(blockPos).topYInclusive
            while (tmpBlockPos.y < maxY) {
                val blockState1 = world.getBlockState(tmpBlockPos)
                if (!blockState1.isAir && blockState1.block !is LeavesBlock) {
                    return true
                }
                tmpBlockPos = tmpBlockPos.up().mutableCopy()
            }
        } catch (exception: IllegalArgumentException) {
            GavinsMod.LOGGER.error("Error for checking roof, blockPos: $blockPos")
        }
        return false
    }
}

/**
 * Different types of CaveESP searches.
 */
enum class SearchType {
    /**
     * Detects full caves.
     */
    Caves,

    /**
     * Detects tunnels only (at least 2 blocks high).
     */
    Tunnel
}
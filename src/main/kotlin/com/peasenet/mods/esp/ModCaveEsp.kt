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
import com.peasenet.gavui.util.Direction
import com.peasenet.main.GavinsMod
import com.peasenet.main.Settings
import com.peasenet.settings.CycleSetting
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.WorldRender
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.MultifaceSpreadeableBlock
import net.minecraft.world.level.block.SnowLayerBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

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
    "gavinsmod.mod.esp.cave", ChatCommand.CaveEsp.command
) {
    init {
        subSettings {
            title = translationKey
            direction = Direction.RIGHT
            slideSetting {
                title = "gavinsmod.settings.alpha"
                value = getSettings().alpha
                callback = { getSettings().alpha = it.value }
            }
            colorSetting {
                title = "gavinsmod.generic.color"
                color = getSettings().blockColor
                callback = { getSettings().blockColor = it.color }
            }
            cycleSetting {
                title = searchTranslationKey + "." + getSettings().searchMode.name.lowercase()
                cycleSize = SearchType.entries.size
                cycleIndex = getSettings().searchMode.ordinal
                callback = { updateSearchMode(it) }
            }
            cycleSetting {
                title = renderDistanceTranslationKey + "." + getSettings().blockRenderDistance.name.lowercase()
                cycleSize = CaveBlockRenderDistance.entries.size
                cycleIndex = getSettings().blockRenderDistance.ordinal
                callback = { updateRenderDistanceMode(it) }
            }
//            toggleSetting {
//                title = hideVisibleTranslationKey
//                state = getSettings().hideVisibleBlocks
//                callback = { getSettings().hideVisibleBlocks = it.state }
//            }
            toggleSetting {
                title = "gavinsmod.mod.esp.blockesp.structure"
                state = getSettings().structureEsp
                callback = { getSettings().structureEsp = it.state }
            }
        }
    }

    companion object {
        private const val searchTranslationKey = "gavinsmod.mod.esp.cave"
        private const val renderDistanceTranslationKey = "gavinsmod.mod.esp.cave.renderdistance"
        private const val hideVisibleTranslationKey = "gavinsmod.mod.esp.cave.hidevisible"
    }

    private val chunksToRender: Int
        get() {
            return (Minecraft.getInstance().options.renderDistance().get()) / 2
        }

    override fun onEnable() {
        roofTopByColumn.clear()
        terrainSurfaceByColumn.clear()
        chunks.clear()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
//        GemExecutor.execute {
        RenderUtils.getVisibleChunks(chunksToRender).forEach(this::searchChunk)
//        }
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        roofTopByColumn.clear()
        terrainSurfaceByColumn.clear()
        chunks.clear()
        super.onDisable()
    }

    override fun getColor(): Color {
        return getSettings().blockColor
    }

    override fun getSettings(): CaveEspConfig = Settings.getConfig(ChatCommand.CaveEsp)


    override fun searchChunk(chunk: ChunkAccess) {
//        GemExecutor.execute {
        synchronized(chunk) {
            GavChunk.search(
                chunk
            ) { pos ->
                searchBlock(pos)
            }.also {
                addBlocksFromChunk(it)
            }
        }
//        }
    }


    override fun onWorldRender(worldRender: WorldRender) {
        synchronized(chunks) {
            chunks.values.removeIf { !it.inRenderDistance(chunksToRender) }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val added = bue.newState.isAir && !bue.oldState.isAir
        val removed = !added && !bue.newState.isAir && bue.oldState.isAir
        val chunk = world.getChunk(bue.blockPos)
        val key = columnKey(bue.blockPos.x, bue.blockPos.z)
        roofTopByColumn.remove(key)
        terrainSurfaceByColumn.remove(key)
        val gavBlock = GavBlock(bue.blockPos, { pos -> searchBlock(pos) })
        if (!added && !removed) {
            return
        }
        updateChunk(added, gavBlock, chunk.pos)
    }

    override fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance(chunksToRender)
    }

    override fun maxBlockRenderDistance(): Double? {
        return getSettings().blockRenderDistance.maxDistance
    }

    override fun shouldRenderBlock(block: GavBlock, partialTicks: Float): Boolean {
        if (!getSettings().hideVisibleBlocks) {
            return true
        }
        val player = client.getPlayer()
        val eyePos = player.getEyePosition(partialTicks)
        val blockCenter = Vec3(block.x + 0.5, block.y + 0.5, block.z + 0.5)
        val hitResult = world.clip(
            ClipContext(
                eyePos,
                blockCenter,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player,
            )
        )

        // MISS means there is no occluding block between the player and the cave block.
        return hitResult.type != HitResult.Type.MISS
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
        roofTopByColumn.clear()
        terrainSurfaceByColumn.clear()
        val searchModeName = getSettings().searchMode.name.lowercase()
        searchMode.gui.title = Component.translatable("$searchTranslationKey.$searchModeName")
//        GemExecutor.execute {
        val visibleChunks: List<ChunkAccess> = RenderUtils.getVisibleChunks(chunksToRender)
        visibleChunks.forEach(this::searchChunk)
//        }
    }

    private fun updateRenderDistanceMode(renderDistanceMode: CycleSetting) {
        getSettings().blockRenderDistance =
            CaveBlockRenderDistance.entries.getOrElse(renderDistanceMode.gui.currentIndex) {
                CaveBlockRenderDistance.BLOCKS_100
            }
        val modeName = getSettings().blockRenderDistance.name.lowercase()
        renderDistanceMode.gui.title = Component.translatable("$renderDistanceTranslationKey.$modeName")
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
        if (!newBlockState.isAir && newBlockState.block !is MultifaceSpreadeableBlock && newBlockState.fluidState.isEmpty) return false
        val searchMode = getSettings().searchMode
        return when (searchMode) {
            SearchType.Caves -> {
                val above = world.getBlockState(blockPos.above())
                val below = world.getBlockState(blockPos.below())
                val canWalkThrough = canWalkThrough(newBlockState, above, below)
                val canWalkOn = canWalkOn(newBlockState, above, below)
                (canWalkThrough || canWalkOn) && hasRoof(blockPos)
            }

            SearchType.Tunnel -> {
                isTunnel(blockPos) && hasRoof(blockPos)
            }
        }
    }

    private fun isTunnel(blockPos: BlockPos): Boolean {
        return world.getBlockState(blockPos.above()).isAir && !world.getBlockState(blockPos.above(2)).isAir
    }


    /**
     * Gets whether the player can walk through this [blockState], by checking if there is air above or below.
     * @return True if the player can walk through this block, false otherwise.
     */
    private fun canWalkThrough(blockState: BlockState, above: BlockState, below: BlockState): Boolean {
        return blockState.isAir && (above.isAir || below.isAir)
    }

    /**
     * Gets whether the player can walk on this [blockState].
     */
    private fun canWalkOn(blockState: BlockState, above: BlockState, below: BlockState): Boolean {
        return blockState.isAir && above.isAir && !below.isAir
    }

    private val roofTopByColumn = hashMapOf<Long, Int>()
    private val terrainSurfaceByColumn = hashMapOf<Long, Int>()

    /**
     * Checks whether this [blockPos] has a roof, i.e. there is a block above it.
     * @param blockPos - The [BlockPos] to check.
     * @return True if there is a block above it, false otherwise.
     */
    private fun hasRoof(blockPos: BlockPos): Boolean {
        val columnKey = columnKey(blockPos.x, blockPos.z)
        val terrainY = getTerrainSurfaceY(columnKey, blockPos)
        if (terrainY == Int.MIN_VALUE || blockPos.y >= terrainY) {
            return false
        }

        val cachedTop = roofTopByColumn[columnKey]
        if (cachedTop != null) {
            return cachedTop >= blockPos.y
        }

        val tmpBlockPos = BlockPos.MutableBlockPos(blockPos.x, 0, blockPos.z)
        try {
            for (y in terrainY downTo blockPos.y) {
                tmpBlockPos.setY(y)
                val blockState = world.getBlockState(tmpBlockPos)
                if (isRoofCandidate(blockState)) {
                    roofTopByColumn[columnKey] = y
                    return true
                }
            }
        } catch (_: IllegalArgumentException) {
            GavinsMod.LOGGER.error("Error for checking roof, blockPos: $blockPos")
        }
        roofTopByColumn[columnKey] = Int.MIN_VALUE
        return false
    }

    private fun getTerrainSurfaceY(columnKey: Long, blockPos: BlockPos): Int {
        val cachedSurface = terrainSurfaceByColumn[columnKey]
        if (cachedSurface != null) {
            return cachedSurface
        }

        val chunk = world.getChunk(blockPos)
        val localX = blockPos.x and 15
        val localZ = blockPos.z and 15

        // WORLD_SURFACE_WG tracks terrain-generation height and excludes later placed structures/features.
        val terrainY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, localX, localZ)
        terrainSurfaceByColumn[columnKey] = terrainY
        return terrainY
    }

    private fun isRoofCandidate(blockState: BlockState): Boolean {
        return !blockState.isAir &&
                blockState.fluidState.isEmpty &&
                blockState.block !is LeavesBlock &&
                blockState.block !is SnowLayerBlock &&
                !blockState.`is`(BlockTags.LOGS) &&
                !blockState.`is`(Blocks.SNOW_BLOCK) &&
                !blockState.`is`(Blocks.POWDER_SNOW)
    }

    private fun columnKey(x: Int, z: Int): Long {
        return (x.toLong() shl 32) xor (z.toLong() and 0xffffffffL)
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

enum class CaveBlockRenderDistance(val maxDistance: Double?) {
    BLOCKS_50(50.0),
    BLOCKS_100(100.0),
    BLOCKS_150(150.0),
    UNLIMITED(null),
}


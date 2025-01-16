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
import com.peasenet.config.BlockEspConfig
import com.peasenet.config.BlockListConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.GavinsMod
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 09-01-2024
 * @see EspMod
 */
class ModBlockEsp : EspMod<BlockEspConfig>(
    "gavinsmod.mod.esp.blockesp",
    "blockesp"
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener, RenderListener {

    @Exclude
    val chunks = HashMap<Long, GavChunk>()

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
        val toggleSetting = SettingBuilder()
            .setTitle("gavinsmod.mod.esp.blockesp.structure")
            .setState(getSettings().structureEsp)
            .buildToggleSetting()
        val blockTracer = SettingBuilder()
            .setTitle("gavinsmod.generic.tracers")
            .setState(getSettings().blockTracer)
            .buildToggleSetting()
        toggleSetting.setCallback { getSettings().structureEsp = toggleSetting.value }
        blockTracer.setCallback { getSettings().blockTracer = blockTracer.value }
        subSetting.add(toggleSetting)
        subSetting.add(blockTracer)
        subSetting.add(alphaSetting)
        subSetting.add(colorSetting)
        val menu = SettingBuilder().setWidth(100f).setHeight(10f).setTitle("gavinsmod.generic.settings")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiBlockEsp()) }.buildClickSetting()
        subSetting.add(menu)
        addSetting(subSetting)
    }


    override fun getColor(): Color {
        return getSettings().blockColor
    }

    override fun onEnable() {
        super.onEnable()
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
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        chunks.clear()
    }

    companion object {
        fun getSettings(): BlockEspConfig = Settings.getConfig("blockesp") as BlockEspConfig
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (chunks.isEmpty()) return
        RenderUtils.setupRenderWithShader(matrixStack)
        val bufferBuilder = RenderUtils.getBufferBuilder()
        synchronized(chunks) {
            val renderableChunks = chunks.values.filter { it.inRenderDistance() }.sortedBy { it.getRenderDistance() }
            renderableChunks.forEach {
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
        }
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }


    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        searchChunk(chunkUpdate.chunk)
    }

    /**
     * Updates the neighbor chunks of the given chunk.
     * @param chunk - The Chunk to update the neighbors of.
     */
    private fun updateNeighborChunks(chunk: GavChunk) {
        chunk.updateBlocks()
        chunks[ChunkPos.toLong(chunk.x + 1, chunk.z)]?.updateBlocks()
        chunks[ChunkPos.toLong(chunk.x + -1, chunk.z)]?.updateBlocks()
        chunks[ChunkPos.toLong(chunk.x, chunk.z + 1)]?.updateBlocks()
        chunks[ChunkPos.toLong(chunk.x, chunk.z + -1)]?.updateBlocks()
    }

    /**
     * Searches the given chunk for blocks that are in the block list.
     * @param chunk - The Chunk to search.
     */
    private fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            val searchedChunk = GavChunk.search(chunk, Settings.getConfig<BlockEspConfig>("blockesp").blocks.toList())
            if (searchedChunk.hasBlocks) {
                synchronized(chunks) {
                    chunks[chunk.pos.toLong()] = searchedChunk
                    updateNeighborChunks(searchedChunk)
                    GavinsMod.LOGGER.debug("(onChunkUpdate) Added chunk: ${chunk.pos.toLong()}")
                }
            } else {
                synchronized(chunks) {
                    chunks[chunk.pos.toLong()]?.clear()
                    chunks.remove(chunk.pos.toLong())
                    GavinsMod.LOGGER.debug("(onChunkUpdate) Removed chunk: {}", chunk.pos.toLong())
                }
            }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val enabledBlocks = Settings.getConfig<BlockEspConfig>("blockesp").blocks
        val chunkX = bue.blockPos.x shr 4
        val chunkZ = bue.blockPos.z shr 4
        val key = ChunkPos.toLong(chunkX, chunkZ)
        val blockOldId = BlockListConfig.getId(bue.oldState.block)
        val blockNewId = BlockListConfig.getId(bue.newState.block)
        val added = enabledBlocks.contains(blockNewId) && !enabledBlocks.contains(blockOldId)
        val removed = !added && !enabledBlocks.contains(blockNewId) && enabledBlocks.contains(blockOldId)
        if (!added && !removed) {
            return
        }

        val gavBlock = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z)
        GemExecutor.execute {
            synchronized(chunks) {
                var espChunk = chunks[key]
                if (espChunk == null) {
                    val chunk = GavChunk(chunkX, chunkZ)
                    chunks[key] = chunk
                    GavinsMod.LOGGER.debug("(onBlockUpdate) Added chunk: $key")
                    espChunk = chunk
                }
                if (added) espChunk.addBlock(gavBlock)
                else espChunk.removeBlock(gavBlock)
                updateNeighborChunks(espChunk)
            }
        }
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }
}
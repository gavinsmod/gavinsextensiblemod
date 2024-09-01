/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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

package com.peasenet.mods.tracer

import com.peasenet.annotations.Exclude
import com.peasenet.config.BlockTracerConfig
import com.peasenet.config.BlockListConfig
import com.peasenet.gui.mod.tracer.GuiBlockTracer
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.GavBlock
import com.peasenet.util.GavChunk
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk

class ModBlockTracer : TracerMod(
    "Block ESP",
    "gavinsmod.mod.tracer.blocktracer",
    "blocktracer"
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener {

    companion object {
        val config: BlockTracerConfig
            get() {
                return Settings.getConfig("blocktracer")
            }
    }

    @Exclude
    private val tracerChunks = HashMap<Long, GavChunk>()

    init {
        val subSetting = SettingBuilder()
            .setTitle("gavinsmod.mod.tracer.blocktracer")
            .buildSubSetting()
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.block.color")
            .setColor(config.blockColor)
            .buildColorSetting()
        colorSetting.setCallback {
            config.blockColor = colorSetting.color
        }

        val culling = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.block.culling")
            .setCallback {
                config.culling = !config.culling
                client.setChunkCulling(config.culling)
            }
            .buildToggleSetting()
        subSetting.add(colorSetting)
        // add gui setting
        val menu = SettingBuilder()
            .setWidth(100f)
            .setHeight(10f)
            .setTitle("gavinsmod.settings.tracer.block.menu")
            .setCallback { minecraftClient.setScreen(GuiBlockTracer()) }
            .buildClickSetting()
        subSetting.add(menu)
        addSetting(subSetting)
    }

    override fun onEnable() {
        super.onEnable()
        tracerChunks.clear()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
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
        tracerChunks.clear()
    }

    override fun onEntityRender(er: EntityRender) {
    }

    override fun onRenderBlockEntity(er: BlockEntityRender) {

    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        searchChunk(chunkUpdate.chunk)
    }


    /**
     * Searches the given chunk for blocks that are in the block list.
     * @param chunk - The Chunk to search.
     */
    private fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            val searchedChunk =
                GavChunk.search(chunk, Settings.getConfig<BlockTracerConfig>("blocktracer").blocks.toList())
            if (searchedChunk.blocks.isNotEmpty()) {
                synchronized(tracerChunks) {
                    tracerChunks[chunk.pos.toLong()] = searchedChunk
                    GavinsMod.LOGGER.debug("(onChunkUpdate) Added chunk: ${chunk.pos.toLong()}")
                }
            } else {
                synchronized(tracerChunks) {
                    tracerChunks[chunk.pos.toLong()]?.blocks?.clear()
                    tracerChunks.remove(chunk.pos.toLong())
                    GavinsMod.LOGGER.debug("(onChunkUpdate) Removed chunk: {}", chunk.pos.toLong())
                }
            }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val enabledBlocks = Settings.getConfig<BlockTracerConfig>("blocktracer").blocks
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

        val _block = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z)
        GemExecutor.execute {
            synchronized(tracerChunks) {
                var tracerChunk = tracerChunks[key]
                if (tracerChunk == null) {
                    val chunk = GavChunk(chunkX, chunkZ)
                    tracerChunks[key] = chunk
                    GavinsMod.LOGGER.debug("(onBlockUpdate) Added chunk: $key")
                    tracerChunk = chunk
                }
                if (added)
                    tracerChunk.blocks[_block.key] = _block
                else
                    tracerChunk.blocks.remove(_block.key)
            }
        }
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(tracerChunks) {
            for (chunk in tracerChunks.values.filter { it.inRenderDistance() }) {
                for (block in chunk.blocks.values) {
                    val box = Box(
                        block.x.toDouble(),
                        block.y.toDouble(),
                        block.z.toDouble(),
                        block.x + 1.0,
                        block.y + 1.0,
                        block.z + 1.0
                    )
                    val mainCamera = MinecraftClient.getInstance().gameRenderer.camera
                    val playerPos = PlayerUtils.getNewPlayerPosition(delta, mainCamera)
                    RenderUtils.renderSingleLine(
                        stack,
                        bufferBuilder,
                        playerPos,
                        box.center,
                        config.blockColor,
                        config.alpha
                    )
                }
            }
            // clear out all chunks that are not in render distance
            tracerChunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }
}
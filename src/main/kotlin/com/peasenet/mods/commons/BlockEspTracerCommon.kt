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

package com.peasenet.mods.commons

import com.peasenet.annotations.Exclude
import com.peasenet.config.BlockEspConfig
import com.peasenet.config.BlockListConfig
import com.peasenet.config.commons.IBlockEspTracerConfig
import com.peasenet.main.GavinsMod
import com.peasenet.main.Settings
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.GavBlock
import com.peasenet.util.GavChunk
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk
import org.lwjgl.glfw.GLFW

/**
 * A class that contains settings and methods used for Block ESP or tracers.
 *
 * @param T A Configuration that extends IBlockEspTracerConfig.
 * @param name The name of the mod.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 * @param category The category of the mod.
 * @param onMenuOpen A lambda that is called when the menu is opened.
 * @param keyBinding The key binding of the mod.
 *
 * @since 09-01-2024
 * @version 09-01-2024
 * @author GT3CH1
 * @see IBlockEspTracerConfig
 */
abstract class BlockEspTracerCommon<T : IBlockEspTracerConfig>(
    name: String,
    translationKey: String,
    chatCommand: String,
    category: ModCategory,
    onMenuOpen: () -> Unit = {},
    keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN,
) : Mod(
    name, translationKey, chatCommand, category, keyBinding
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener, RenderListener {
    fun getSettings(): T {
        return Settings.getConfig(chatCommand)
    }

    // A small vertex buffer for rendering the block outlines.
    protected var vertexBuffer: VertexBuffer? = null

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
        // add gui setting
        val menu = SettingBuilder().setWidth(100f).setHeight(10f).setTitle("gavinsmod.generic.settings")
            // open class "M" gui


            .setCallback { onMenuOpen() }.buildClickSetting()
        subSetting.add(menu)
        addSetting(subSetting)
    }

    @Exclude
    protected val chunks = HashMap<Long, GavChunk>()
    override fun onEnable() {
        super.onEnable()
        chunks.clear()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
        vertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
        val box = Box(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
        RenderUtils.drawOutlinedBox(box, vertexBuffer!!)
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

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        searchChunk(chunkUpdate.chunk)
    }


    /**
     * Searches the given chunk for blocks that are in the block list.
     * @param chunk - The Chunk to search.
     */
    private fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            val searchedChunk = GavChunk.search(chunk, Settings.getConfig<BlockEspConfig>("blockesp").blocks.toList())
            if (searchedChunk.blocks.isNotEmpty()) {
                synchronized(chunks) {
                    chunks[chunk.pos.toLong()] = searchedChunk
                    GavinsMod.LOGGER.debug("(onChunkUpdate) Added chunk: ${chunk.pos.toLong()}")
                }
            } else {
                synchronized(chunks) {
                    chunks[chunk.pos.toLong()]?.blocks?.clear()
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

        val _block = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z)
        GemExecutor.execute {
            synchronized(chunks) {
                var espChunk = chunks[key]
                if (espChunk == null) {
                    val chunk = GavChunk(chunkX, chunkZ)
                    chunks[key] = chunk
                    GavinsMod.LOGGER.debug("(onBlockUpdate) Added chunk: $key")
                    espChunk = chunk
                }
                if (added) espChunk.blocks[_block.key] = _block
                else espChunk.blocks.remove(_block.key)
            }
        }
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }
}
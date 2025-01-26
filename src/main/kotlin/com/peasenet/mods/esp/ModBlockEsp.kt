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
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import com.peasenet.util.block.GavBlock
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.ChunkPos
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
        val toggleSetting =
            SettingBuilder().setTitle("gavinsmod.mod.esp.blockesp.structure").setState(getSettings().structureEsp)
                .buildToggleSetting()
        val blockTracer = SettingBuilder().setTitle("gavinsmod.generic.tracers").setState(getSettings().blockTracer)
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

    override fun getSettings(): BlockEspConfig = Settings.getConfig("blockesp") as BlockEspConfig

    override fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            synchronized(chunks) {
                val blocks = Settings.getConfig<BlockEspConfig>(ChatCommand.BlockEsp.chatCommand)
                val searchedChunk = GavChunk.search(
                    chunk,
                ) { pos -> blocks.blocks.contains(BlockListConfig.getId(chunk.getBlockState(pos).block)) }
                addBlocksFromChunk(searchedChunk)
            }
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        val enabledBlocks = config.blocks
        val chunk = client.getWorld().getChunk(bue.blockPos) ?: return
        val key = ChunkPos.toLong(bue.blockPos)
        val blockOldId = BlockListConfig.getId(bue.oldState.block)
        val blockNewId = BlockListConfig.getId(bue.newState.block)
        val added = enabledBlocks.contains(blockNewId) && !enabledBlocks.contains(blockOldId)
        val removed = !added && !enabledBlocks.contains(blockNewId) && enabledBlocks.contains(blockOldId)
        if (!added && !removed) {
            return
        }
        val gavBlock = GavBlock(bue.blockPos) { blockPos ->
            val blockState = chunk.getBlockState(blockPos)
            config.blocks.contains(BlockListConfig.getId(blockState.block))
        }
        GemExecutor.execute {
            synchronized(chunks) {
                updateChunk(added, gavBlock, chunk.pos)
            }
        }
    }


    private companion object {
        val config: BlockEspConfig = Settings.getConfig("blockesp")
    }
}
package com.peasenet.mods.esp

import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsMod
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.GavBlock
import com.peasenet.util.GavChunk
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.block.Blocks
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos

class ModBlockEsp : EspMod(
    "Block ESP",
    "gavinsmod.mod.esp.blockesp",
    "blockesp"
), BlockUpdateListener, WorldRenderListener {

    private val espChunks = HashMap<Long, GavChunk>()

    init {
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.block.color")
            .setColor(config.blockColor)
            .buildColorSetting()
        colorSetting.setCallback {
            config.blockColor = colorSetting.color
        }
        addSetting(colorSetting)
    }
    
    override fun onEnable() {
        super.onEnable()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        espChunks.clear()
    }

    override fun onBlockUpdate(bue: BlockUpdate) {

        val chunkX = bue.blockPos.x shr 4
        val chunkZ = bue.blockPos.z shr 4
        val key = ChunkPos.toLong(chunkX, chunkZ)
        val added =
            (bue.oldState.block.defaultState == Blocks.COBBLESTONE.defaultState) && (bue.newState.block.defaultState != Blocks.COBBLESTONE.defaultState)

        val block = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z)
        if (added) {
            var espChunk = espChunks[key]
            if (espChunk == null) {
                val chunk = GavChunk(chunkX, chunkZ)
                espChunks[key] = chunk
                GavinsMod.LOGGER.info("Added chunk: $key")
                espChunk = chunk
            }
            espChunk.blocks[block.key] = block

        } else {
            val espChunk = espChunks[key] ?: return
            espChunk.blocks[block.key] ?: return
            espChunk.blocks.remove(block.key)
            GavinsMod.LOGGER.info("Removed block: ${block.key}")
            if (espChunk.blocks.isEmpty()) {
                espChunks.remove(key)
                GavinsMod.LOGGER.info("Removed chunk: $key")
            }
        }
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        for (chunk in espChunks.values) {
            for (block in chunk.blocks.values) {
                val box = Box(
                    block.x.toDouble(),
                    block.y.toDouble(),
                    block.z.toDouble(),
                    block.x + 1.0,
                    block.y + 1.0,
                    block.z + 1.0
                )
                RenderUtils.drawBox(stack, bufferBuilder, box, config.blockColor, config.alpha)
            }
//            RenderUtils.drawBox(stack, bufferBuilder, box, Color(150, 150, 150), delta)
        }
    }
}
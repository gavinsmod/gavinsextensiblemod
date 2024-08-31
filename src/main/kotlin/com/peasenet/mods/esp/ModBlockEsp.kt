package com.peasenet.mods.esp

import com.peasenet.config.BlockEspConfig
import com.peasenet.config.BlockListConfig
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.GavBlock
import com.peasenet.util.GavChunk
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos

class ModBlockEsp : EspMod(
    "Block ESP",
    "gavinsmod.mod.esp.blockesp",
    "blockesp"
), BlockUpdateListener, WorldRenderListener, ChunkUpdateListener {

    private val espChunks = HashMap<Long, GavChunk>()

    init {
        val subSetting = SettingBuilder()
            .setTitle("gavinsmod.mod.esp.blockesp")
            .buildSubSetting()
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.block.color")
            .setColor(config.blockColor)
            .buildColorSetting()
        colorSetting.setCallback {
            config.blockColor = colorSetting.color
        }
        subSetting.add(colorSetting)
        // add gui setting
        val menu = SettingBuilder()
            .setWidth(100f)
            .setHeight(10f)
            .setTitle("gavinsmod.settings.esp.block.menu")
            .setCallback { minecraftClient.setScreen(GuiBlockEsp()) }
            .buildClickSetting()
        subSetting.add(menu)
        addSetting(subSetting)
    }

    override fun onEnable() {
        super.onEnable()
        espChunks.clear()
        em.subscribe(BlockUpdateListener::class.java, this)
        em.subscribe(WorldRenderListener::class.java, this)
        em.subscribe(ChunkUpdateListener::class.java, this)
        // search for chunks within render distance
        GemExecutor.execute {
            synchronized(espChunks) {
                val renderDistance = minecraftClient.options.viewDistance.value
                val playerChunk =
                    minecraftClient.getWorld()
                        .getChunk(minecraftClient.getPlayer().chunkPos.x, minecraftClient.getPlayer().chunkPos.z)
                for (x in playerChunk.pos.x - renderDistance / 2..playerChunk.pos.x + renderDistance / 2) {
                    for (z in playerChunk.pos.z - renderDistance / 2..playerChunk.pos.z + renderDistance / 2) {
                        val chunk = minecraftClient.getWorld().getChunk(x, z)
                        val gavChunk =
                            GavChunk.search(chunk, Settings.getConfig<BlockEspConfig>("blockesp").blocks.toList())
                        if (gavChunk.blocks.isNotEmpty()) {
                            espChunks[gavChunk.key] = gavChunk
                        }
                    }
                }
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockUpdateListener::class.java, this)
        em.unsubscribe(WorldRenderListener::class.java, this)
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        espChunks.clear()
    }

    override fun activate() {
        super.activate()
    }

    override fun deactivate() {
        super.deactivate()
    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        GavChunk.search(chunkUpdate.chunk, Settings.getConfig<BlockEspConfig>("blockesp").blocks.toList())
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
        GemExecutor.execute {
            synchronized(espChunks) {
//                for(block in enabledBlocks.stream().toList()) {
                val _block = GavBlock(bue.blockPos.x, bue.blockPos.y, bue.blockPos.z)
                if (added) {
                    var espChunk = espChunks[key]
                    if (espChunk == null) {
                        val chunk = GavChunk(chunkX, chunkZ)
                        espChunks[key] = chunk
                        GavinsMod.LOGGER.info("Added chunk: $key")
                        espChunk = chunk
                    }
                    espChunk.blocks[_block.key] = _block
                    GavinsMod.LOGGER.info("Added block: ${_block.key}")

                } else {
                    val espChunk = espChunks[key]
                    if (espChunk != null) {
                        espChunk.blocks.remove(_block.key)
                        GavinsMod.LOGGER.info("Removed block: ${_block.key}")
                        if (espChunk.blocks.isEmpty()) {
                            espChunks.remove(key)
                            GavinsMod.LOGGER.info("Removed chunk: $key")
                        }
                    }
                }
            }
//            }
        }
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(espChunks) {
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
            }
        }
    }
}
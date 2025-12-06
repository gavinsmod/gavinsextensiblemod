package com.peasenet.mods.render

import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.ChunkPos

/**
 *
 * @author GT3CH1
 * @version 12-06-2025
 * @since 12-06-2025
 */
class ModOreSim : RenderMod("gavinsmod.mod.render.oresim", "oresim"), ChunkUpdateListener, RenderListener,
    BlockUpdateListener {
    private val testSeed = -5456165414318645875L
    override fun onEnable() {
        em.subscribe(ChunkUpdateListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
        em.subscribe(BlockUpdateListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(ChunkUpdateListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        em.unsubscribe(BlockUpdateListener::class.java, this)
        super.onDisable()
    }

    override fun onChunkUpdate(chunkUpdate: ChunkUpdate) {
        TODO("Not yet implemented")
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        val chunkX = client.getPlayer().chunkPos.x
        val chunkZ = client.getPlayer().chunkPos.z
        for (x in chunkX - 5..chunkX + 5) {
            renderChunkAt(x, chunkZ, matrixStack)
        }
        for (z in chunkZ - 5..chunkZ + 5) {
            renderChunkAt(chunkX, z, matrixStack)
        }
    }

    private fun renderChunkAt(chunkX: Int, chunkZ: Int, matrixStack: MatrixStack) {
        val key = ChunkPos.toLong(chunkX, chunkZ)

    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        TODO("Not yet implemented")
    }
}
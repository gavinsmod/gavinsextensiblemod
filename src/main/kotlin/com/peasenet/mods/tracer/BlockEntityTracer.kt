package com.peasenet.mods.tracer

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.gl.ShaderProgramKeys
import net.minecraft.client.util.math.MatrixStack

/**
 * A helper class for creating mods that trace block entities.
 * @param T The type of block entity to trace.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 * @param blockFilter A lambda that filters block entities.
 * @see TracerMod
 *
 * @version 01-15-2025
 * @since 09-06-2024
 * @author GT3CH1
 */
@Suppress("UNCHECKED_CAST")
abstract class BlockEntityTracer<T : BlockEntity>(
    translationKey: String,
    chatCommand: String,
    val blockFilter: (BlockEntity) -> Boolean
) : TracerMod<T>(translationKey, chatCommand), RenderListener {
    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(RenderListener::class.java, this)
    }

    override fun onTick() {
        super.onTick()
        entityList.clear()
        val level = client.getWorld()
        for (x in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
            for (z in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
                val chunk = level.getChunk(x + client.getPlayer().chunkPos.x, z + client.getPlayer().chunkPos.z)
                for ((_, blockEntity) in chunk.blockEntities) {
                    if (blockFilter(blockEntity)) {
                        entityList.add(blockEntity as T)
                    }
                }
            }
        }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (entityList.isEmpty()) return
        RenderUtils.setupRender(matrixStack)
//        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
//        RenderSystem.applyModelViewMatrix()
//        RenderSystem.matrix
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        for (e in entityList) {
            RenderUtils.drawSingleLine(
                bufferBuilder, entry, partialTicks, e.pos, getColor(), config.alpha
            )
        }
        RenderUtils.drawBuffer(bufferBuilder)
        RenderUtils.cleanupRender(matrixStack)
    }

    abstract fun getColor(): Color
}
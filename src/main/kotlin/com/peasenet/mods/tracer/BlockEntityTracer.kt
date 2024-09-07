package com.peasenet.mods.tracer

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack

/**
 * A helper class for creating mods that trace block entities.
 * @param T The type of block entity to trace.
 * @param name The name of the mod.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 * @param blockFilter A lambda that filters block entities.
 * @see TracerMod
 *
 * @version 09-06-2024
 * @since 09-06-2024
 * @author GT3CH1
 */
abstract class BlockEntityTracer<T : BlockEntity>(
    name: String,
    translationKey: String,
    chatCommand: String,
    val blockFilter: (BlockEntity) -> Boolean
) : TracerMod<T>(name, translationKey, chatCommand), RenderListener {
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
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.applyModelViewMatrix()
        val region = RenderUtils.getCameraRegionPos()
        val entry = matrixStack.peek().positionMatrix
        val tessellator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        val regionVec = region.toVec3d();
        val start = RenderUtils.getLookVec(partialTicks).add(RenderUtils.getCameraPos()).subtract(regionVec);
        for (e in entityList) {
            val entityPos = e.pos.subtract(region.toVec3i()).toCenterPos()
            RenderUtils.drawSingleLine(
                bufferBuilder, entry, start, entityPos, getColor(), config.alpha
            )
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }

    abstract fun getColor(): Color
}
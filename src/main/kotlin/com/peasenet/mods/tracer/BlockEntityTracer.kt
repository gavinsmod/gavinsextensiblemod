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
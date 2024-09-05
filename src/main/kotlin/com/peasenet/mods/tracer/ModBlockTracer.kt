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

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.BlockEspConfig
import com.peasenet.config.BlockTracerConfig
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.gui.mod.tracer.GuiBlockTracer
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.mods.ModCategory
import com.peasenet.mods.commons.BlockEspTracerCommon
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box

/**
 * A tracer mod that draws lines to blocks in the world.
 *
 * @author GT3CH1
 * @version 09-01-2024
 * @since 09-01-2024
 * @see TracerMod
 * @see BlockUpdateListener
 * @see WorldRenderListener
 * @see ChunkUpdateListener
 *
 */
class ModBlockTracer : BlockEspTracerCommon<BlockTracerConfig>("Block Tracer",
    "gavinsmod.mod.tracer.blocktracer",
    "blocktracer",
    ModCategory.TRACERS,
    { minecraftClient.setScreen(GuiBlockTracer()) }) {
    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        synchronized(chunks) {
            if(chunks.isEmpty()) return

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
            for (chunk in chunks.values) {
                for (block in chunk.blocks.values) {
                    val pos = BlockPos(block.x, block.y, block.z)
                    val center = pos.toCenterPos().subtract(region.toVec3d())
                    RenderUtils.drawSingleLine(
                        bufferBuilder, entry, start, center, getSettings().blockColor, getSettings().alpha
                    )
                }
            }
            try {
                val end = bufferBuilder.end()
                BufferRenderer.drawWithGlobalProgram(end)
                RenderUtils.cleanupRender(matrixStack)
            } catch (_: IllegalStateException) {

            }
        }
    }
}
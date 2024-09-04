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
import com.peasenet.config.TracerConfig
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.RenderListener
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.EnderChestBlockEntity
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Colors
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A mod that allows the player to see tracers towards beehives.
 */
@Deprecated("This class is deprecated and will be removed in a future release.")
class ModBeehiveTracer : TracerMod<BeehiveBlockEntity>(
    "Beehive Tracer",
    "gavinsmod.mod.tracer.beehive",
    "beehivetracer",
) {
    init {
        val colorSetting =
            SettingBuilder().setTitle("gavinsmod.settings.tracer.beehive.color").setColor(config.beehiveColor)
                .buildColorSetting()
        colorSetting.setCallback { config.beehiveColor = colorSetting.color }
        addSetting(colorSetting)
    }

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
                    if (blockEntity is BeehiveBlockEntity) {
                        entityList.add(blockEntity)
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
        var bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        val regionVec = region.toVec3d();
        val start = RenderUtils.getLookVec(partialTicks).add(RenderUtils.getCameraPos()).subtract(regionVec);
        for (e in entityList) {
            val entityPos = e.pos.subtract(region.toVec3i()).toCenterPos()
            RenderUtils.drawSingleLine(bufferBuilder, entry, start, entityPos, config.beehiveColor, config.alpha)
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }

}

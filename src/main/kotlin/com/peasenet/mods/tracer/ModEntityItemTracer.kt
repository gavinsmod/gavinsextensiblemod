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
import com.peasenet.mods.tracer.TracerMod.Companion
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.util.math.Box

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A mod that allows the player to see tracers towards items.
 */
class ModEntityItemTracer : TracerMod<ItemEntity>(
    "Item Tracer", "gavinsmod.mod.tracer.item", "itemtracer"
), RenderListener {
    init {
        val colorSetting = SettingBuilder().setTitle("gavinsmod.settings.tracer.item.color").setColor(config.itemColor)
            .buildColorSetting()
        colorSetting.setCallback { config.itemColor = colorSetting.color }
        colorSetting.color = config.itemColor
        addSetting(colorSetting)
    }

    override fun onTick() {
        super.onTick()
        entityList.clear()
        for (entity in client.getWorld().entities) {
            if (entity.type == EntityType.ITEM) {
                entityList.add(entity as ItemEntity)
            }
        }
    }

    companion object {
        private val config: TracerConfig
            get() {
                return Settings.getConfig<TracerConfig>("tracer")
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
            val entityPos = e.pos.subtract(region.toVec3d())
            val center = e.boundingBox.center.subtract(region.toVec3d())
            RenderUtils.drawSingleLine(
                bufferBuilder, entry, start, center, config.itemColor, config.alpha
            )
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }
}

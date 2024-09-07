﻿/*
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

package com.peasenet.mods.esp

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.mods.tracer.TracerMod
import com.peasenet.mods.tracer.TracerMod.Companion
import com.peasenet.util.RenderUtils
import com.peasenet.util.math.MathUtils
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

/**
 * A class that represents an ESP mod for entities.
 * @param T The type of entity to render.
 * @param name The name of the mod.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 *
 * @author GT3CH1
 * @version 09-01-2024
 * @since 09-01-2024
 *
 */
abstract class EntityEsp<T : Entity>(
    name: String, translationKey: String, chatCommand: String, val entityFilter: (Entity) -> Boolean
) : EspMod<T>(name, translationKey, chatCommand) {

    override fun onTick() {
        super.onTick()
        espList.clear()
        client.getWorld().entities.filter { entityFilter(it) }.forEach { espList.add(it as T) }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (espList.isEmpty()) return
        RenderUtils.setupRender(matrixStack)
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.applyModelViewMatrix()
        val entry = matrixStack.peek().positionMatrix
        val tessellator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (e in espList) {
            var box = e.boundingBox
            val lerped = MathUtils.lerp(
                partialTicks,
                e.pos,
                Vec3d(e.lastRenderX, e.lastRenderY, e.lastRenderZ),
                RenderUtils.getCameraRegionPos()
            )
            box = Box(
                lerped.x + box.minX,
                lerped.y + box.minY,
                lerped.z + box.minZ,
                lerped.x + box.maxX,
                lerped.y + box.maxY,
                lerped.z + box.maxZ
            )
            RenderUtils.drawOutlinedBox(
                box, bufferBuilder, entry, getColor(e), config.alpha
            )
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }


    /**
     * Gets the color of the entity for rendering.
     * @param entity The entity to get the color of.
     * @return The color of the entity.
     */
    abstract fun getColor(entity: T): Color
}
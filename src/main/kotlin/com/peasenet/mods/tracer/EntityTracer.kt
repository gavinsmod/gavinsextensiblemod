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

import com.peasenet.config.tracer.TracerConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.world.entity.Entity
import org.joml.Matrix3x2fStack

/**
 * A tracer mod that traces entities of a specific type.
 * @param T The type of entity to trace.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command to toggle the mod.
 * @param entityFilter A filter to determine which entities to trace.
 * @see TracerMod
 *
 * @version 01-15-2025
 * @since 09-06-2024
 * @author GT3CH1
 */
@Suppress("UNCHECKED_CAST")
abstract class EntityTracer<T : Entity>(
    translationKey: String, chatCommand: String, val entityFilter: (Entity) -> Boolean,
) : TracerMod<T>(translationKey, chatCommand), RenderListener {
    override fun onTick() {
        super.onTick()
        entityList.clear()
        client.getWorld().entitiesForRendering().filter { entityFilter(it) }.forEach { entityList.add(it as T) }
    }

    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        // TODO: MC 1.21.10 update
        if (entityList.isEmpty()) return
        for (e in entityList) {
            val tracerOrigin = RenderUtils.getLookVec(partialTicks).scale(1.0)
            val end = (RenderUtils.getLerpedBox(e, partialTicks).center)
            matrixStack.pushPose()
            RenderUtils.drawSingleLine(
                matrixStack,
                tracerOrigin,
                end,
                getColor(e),
                config.alpha,
                withOffset = true,
                depthTest = false
            )
            matrixStack.popPose()
        }
    }

    open fun getAlpha(): Float {
        return Settings.getConfig<TracerConfig>("tracer").alpha
    }

    open fun getColor(entity: T): Color {
        return Colors.WHITE
    }
}
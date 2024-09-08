package com.peasenet.mods.tracer

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.TracerConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.math.MathUtils
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import kotlin.math.floor

/**
 * A tracer mod that traces entities of a specific type.
 * @param T The type of entity to trace.
 * @param name The name of the mod.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command to toggle the mod.
 * @param entityFilter A filter to determine which entities to trace.
 * @see TracerMod
 *
 * @version 09-06-2024
 * @since 09-06-2024
 * @author GT3CH1
 */
abstract class EntityTracer<T : Entity>(
    name: String, translationKey: String, chatCommand: String, val entityFilter: (Entity) -> Boolean
) : TracerMod<T>(name, translationKey, chatCommand), RenderListener {
    override fun onTick() {
        super.onTick()
        entityList.clear()
        client.getWorld().entities.filter { entityFilter(it) }.forEach { entityList.add(it as T) }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (entityList.isEmpty()) return
        RenderUtils.setupRenderWithShader(matrixStack)
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        for (e in entityList) {
            RenderUtils.drawSingleLine(bufferBuilder, entry, partialTicks, e, getColor(e), config.alpha)
        }
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }

    open fun getAlpha(): Float {
        return Settings.getConfig<TracerConfig>("tracer").alpha
    }

    open fun getColor(entity: T): Color {
        return Colors.WHITE
    }
}
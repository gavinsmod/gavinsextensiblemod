package com.peasenet.mods.tracer

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.TracerConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType

abstract class EntityTracer<T : Entity>(
    name: String,
    translationKey: String,
    chatCommand: String,
    val entityFilter: (Entity) -> Boolean
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
        client.getWorld().entities.filter { entityFilter(it) }.forEach { entityList.add(it as T) }
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
            val center = e.boundingBox.center.subtract(region.toVec3d())
            RenderUtils.drawSingleLine(
                bufferBuilder, entry, start, center, getColor(e), config.alpha
            )
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }

    open fun getAlpha(): Float {
        return Settings.getConfig<TracerConfig>("tracer").alpha
    }

    open fun getColor(entity: T): Color {
        return Colors.WHITE
    }
}
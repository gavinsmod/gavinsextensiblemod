package com.peasenet.util.event.data

import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 09-16-2024
 *
 * A data class that contains the data needed for [ModHealthTag][com.peasenet.mods.render.ModHealthTag].
 * @param entity The entity to render the health tag for.
 * @param matrixStack The matrix stack to render the health tag with.
 * @param vertexConsumerProvider The vertex consumer provider to render the health tag with.
 * @param light The light level to render the health tag with.
 * @see com.peasenet.mods.render.ModHealthTag
 * @see com.peasenet.util.event.EntityRenderNameEvent
 * @see com.peasenet.util.listeners.EntityRenderNameListener
 *
 * @sample com.peasenet.mods.render.ModHealthTag
 */
data class EntityNameRender(
    val entity: Entity,
    val matrixStack: MatrixStack,
    var vertexConsumerProvider: VertexConsumerProvider?,
    var light: Int,
)
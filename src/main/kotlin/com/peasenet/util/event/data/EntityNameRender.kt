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
 * @version 01-06-2025
 * @since 09-16-2024
 */
data class EntityNameRender(
    val entity: Entity,
    var text: MutableText?,
)
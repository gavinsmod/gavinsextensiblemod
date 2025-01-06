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
package com.peasenet.mods.render

import com.peasenet.util.event.data.EntityNameRender
import com.peasenet.util.listeners.EntityRenderNameListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityAttachmentType
import net.minecraft.entity.LivingEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Formatting

/**
 * @author gt3ch1
 * @version 04-10-2023
 * A mod that shows entity's health as a tag above their head.
 */
class ModHealthTag : RenderMod(
    "Health Tags",
    "gavinsmod.mod.render.hptags",
    "hptags"
) {

    //SEE: EntityRenderer#renderLabelIfPresent
    fun renderLabel(
        entity: LivingEntity,
        text: MutableText,

        ): MutableText {
        val currentHp = entity.health.toInt()
        val formattedMutableText = Text.literal(" ").append(currentHp.toString()).append(" HP")
        return text.append(formattedMutableText.formatted(getColor(entity)))
//        val dispatcher = MinecraftClient.getInstance().entityRenderDispatcher
//        val d: Double = dispatcher.getSquaredDistanceToCamera(entity)
//        if (d > 4096) return
//        val attachmentVec =
//            entity.attachments.getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta))
//                ?: return
//        val i = if ("deadmau5" == text.string) -10 else 0
//        matrices.push()
//        matrices.translate(attachmentVec.x, attachmentVec.y + 0.5, attachmentVec.z)
//        matrices.multiply(dispatcher.rotation)
//        matrices.scale(0.025f, -0.025f, 0.025f)
//        val matrix4f = matrices.peek().positionMatrix
//        val f = MinecraftClient.getInstance().options.getTextBackgroundColor(0.25f)
//
//        val renderer: TextRenderer = MinecraftClient.getInstance().textRenderer
//        val g = -renderer.getWidth(text) / 2.0f
//        renderer.draw(
//            text,
//            g,
//            i.toFloat(),
//            0x20FFFFFF,
//            false,
//            matrix4f,
//            vertexConsumers,
//            TextLayerType.NORMAL,
//            f,
//            light
//        )
//        renderer.draw(
//            text,
//            g,
//            i.toFloat(),
//            0xFFFFFFFF.toInt(),
//            false,
//            matrix4f,
//            vertexConsumers,
//            TextLayerType.SEE_THROUGH,
//            0,
//            light
//        )
//        matrices.pop()
    }

//    private fun addHealth(entity: LivingEntity, tag: Text): MutableText {
//    }

    private fun getColor(entity: LivingEntity): Formatting {
        return if (entity.health > entity.maxHealth * 0.8) {
            Formatting.GREEN
        } else if (entity.health > entity.maxHealth * 0.6) {
            Formatting.YELLOW
        } else if (entity.health > entity.maxHealth * 0.4) {
            Formatting.GOLD
        } else {
            Formatting.RED
        }
    }

//    override fun onEntityNameRender(er: EntityNameRender) {
//        if (er.entity !is LivingEntity) return
//        renderLabel(
//            er.entity,
//            addHealth(er.entity, er.entity.name),
//            er.matrixStack,
//            er.vertexConsumerProvider,
//            er.light,
//            er.tickDelta
//        )
//    }
}
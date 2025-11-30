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
package com.peasenet.mods.render

import com.peasenet.util.event.data.EntityNameRender
import com.peasenet.util.listeners.EntityRenderNameListener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.world.entity.EntityAttachment
import net.minecraft.world.entity.LivingEntity
import net.minecraft.network.chat.Component
import net.minecraft.ChatFormatting

/**
 *
 * A mod that shows entity's health as a tag above their head. Listening for [EntityNameRender][com.peasenet.util.event.EntityRenderNameEvent] events.
 *
 * @see EntityRenderNameListener
 * @see com.peasenet.util.event.EntityRenderNameEvent
 * @see com.peasenet.util.event.data.EntityNameRender
 * @see com.peasenet.mods.render.RenderMod
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 09-16-2024
 *
 */
class ModHealthTag : RenderMod(
    "gavinsmod.mod.render.hptags", "hptags"
), EntityRenderNameListener {

    override fun onEnable() {
        em.subscribe(EntityRenderNameListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(EntityRenderNameListener::class.java, this)
        super.onDisable()
    }

    /**
     * Renders a health tag above an entities head.
     */
    private fun renderHealthTag(
        er: EntityNameRender,
    ) {
        val entity = er.entity as LivingEntity
        val matrixStack = er.matrixStack
        val vertexConsumers = er.vertexConsumerProvider
        val light = er.light
        val textRenderer = Minecraft.getInstance().font
        val dispatcher = Minecraft.getInstance().entityRenderDispatcher
        val attachmentVec = entity.attachments.getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(0f))
        matrixStack.pushPose()
        matrixStack.translate(attachmentVec!!.x, attachmentVec.y + 0.75, attachmentVec.z)
//        matrixStack.multiply(dispatcher.rotation)
        matrixStack.scale(0.025f, -0.025f, 0.025f)
        val currentHp = entity.health.toInt()
        val text = Component.literal("").append(currentHp.toString()).append(" HP").withStyle(getColor(entity))
        val g = -textRenderer.width(text) / 2.0f
        val i = if (er.entity.name.string == "deadmau5") -10 else 0
        val matrix4f = matrixStack.last().pose()
        val f = textRenderer.width(text) / 2.0f
        val j = (Minecraft.getInstance().options.getBackgroundOpacity(0.25f) * 255.0f).toInt() shl 24
        textRenderer.drawInBatch(
            text,
            g,
            i.toFloat(),
            0xFFFFFFFF.toInt(),
            false,
            matrix4f,
            vertexConsumers,
            Font.DisplayMode.NORMAL,
            j,
            light
        )
        textRenderer.drawInBatch(
            text,
            g,
            i.toFloat(),
            0xFFFFFFFF.toInt(),
            false,
            matrix4f,
            vertexConsumers,
            Font.DisplayMode.SEE_THROUGH,
            0,
            light
        )
        matrixStack.popPose()
    }

    /**
     * Gets the color formatting for the health tag.
     */
    private fun getColor(entity: LivingEntity): ChatFormatting {
        return if (entity.health > entity.maxHealth * 0.8) {
            ChatFormatting.GREEN
        } else if (entity.health > entity.maxHealth * 0.6) {
            ChatFormatting.YELLOW
        } else if (entity.health > entity.maxHealth * 0.4) {
            ChatFormatting.GOLD
        } else {
            ChatFormatting.RED
        }
    }

    override fun onEntityNameRender(er: EntityNameRender) {
        if (er.entity !is LivingEntity) return
        renderHealthTag(er)
    }
}
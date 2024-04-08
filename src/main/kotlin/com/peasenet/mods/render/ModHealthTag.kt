/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mods.render

import com.peasenet.gavui.color.Colors
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderNameListener
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.entity.LivingEntity

/**
 * @author gt3ch1
 * @version 04-10-2023
 * A mod that shows entity's health as a tag above their head.
 */
class ModHealthTag : RenderMod(
    "Health Tags",
    "gavinsmod.mod.render.hptags",
    "hptags"
), EntityRenderNameListener {
    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderNameListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderNameListener::class.java, this)
    }

    override fun onEntityRender(er: EntityRender) {
        val entity = er.entity
        val matrices = er.stack
        val textRenderer = client.textRenderer
        val d = client.entityRenderDispatcher.getSquaredDistanceToCamera(entity)
        if (entity !is LivingEntity) return
        if (d > 1024) return
        val currentHealth: Float = entity.health
        // round to 1 decimal place
        val roundedHealth = (currentHealth * 10).toInt() / 10.0
        val text = "$roundedHealth HP"
        val bl = !entity.isSneaky()
        val f = entity.getHeight() + 0.5f
        matrices.push()
        matrices.translate(0.0, f.toDouble(), 0.0)
        matrices.multiply(client.entityRenderDispatcher.rotation)
        matrices.scale(-0.025f, -0.025f, 0.025f)
        val matrix4f = matrices.peek().positionMatrix
        val g = client.options.getTextBackgroundOpacity(0.5f)
        val j = (g * 255.0f).toInt() shl 24
        val h = (-textRenderer.getWidth(text) / 2).toFloat()
        var color = Colors.GREEN.asInt
        val percentHealth: Double = (entity.health / entity.maxHealth).toDouble()
        if (percentHealth <= 0.75) color = Colors.YELLOW.asInt
        if (percentHealth <= 0.5) color = Colors.GOLD.asInt
        if (percentHealth <= 0.25) color = Colors.RED.asInt
        textRenderer.draw(text, h, 0f, color, false, matrix4f, er.vertexConsumers, TextLayerType.NORMAL, j, er.light)
        if (bl) {
            textRenderer.draw(
                text,
                h,
                0f,
                color,
                false,
                matrix4f,
                er.vertexConsumers,
                TextLayerType.NORMAL,
                0,
                er.light
            )
        }
        matrices.pop()
    }
}
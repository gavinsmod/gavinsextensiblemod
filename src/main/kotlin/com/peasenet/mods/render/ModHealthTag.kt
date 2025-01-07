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
import net.minecraft.entity.LivingEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

/**
 * @author gt3ch1
 * @version 01-06-2025
 * A mod that shows entity's health as a tag above their head.
 */
class ModHealthTag : RenderMod(
    "Health Tags", "gavinsmod.mod.render.hptags", "hptags"
), EntityRenderNameListener {

    override fun onEnable() {
        em.subscribe(EntityRenderNameListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(EntityRenderNameListener::class.java, this)
        super.onDisable()
    }

    private fun renderLabel(
        entity: LivingEntity,
        text: MutableText?,
    ): MutableText {
        val currentHp = entity.health.toInt()
        val formattedMutableText = Text.literal("").append(currentHp.toString()).append(" HP")
        if (text != null) return text.append(formattedMutableText.formatted(getColor(entity)))
        return formattedMutableText.formatted(getColor(entity))
    }

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

    override fun onEntityNameRender(er: EntityNameRender) {
        if (er.entity !is LivingEntity) return
        val tmpName = er.text ?: Text.of("")
        var entity = er.entity
        er.text = renderLabel(entity, tmpName.copy())
    }
}
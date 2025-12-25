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

import com.peasenet.config.render.HealthTagConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gui.mod.render.GuiHealthTag
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.util.ChatCommand
import com.peasenet.util.event.data.EntityNameRender
import com.peasenet.util.listeners.EntityRenderNameListener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.world.entity.EntityAttachment
import net.minecraft.world.entity.LivingEntity
import net.minecraft.network.chat.Component
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Style

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

    init {
        clickSetting {
            title = "gavinsmod.mod.render.hptags"
            callback = {
                client.setScreen(GuiHealthTag())
            }
        }
    }

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
        val currentHp = er.entity.health.toInt()
        val entity = er.entity;
        // check if mob is hostile
        if (entity.type.category.isFriendly && !config.friendlyMobs) {
            er.cancel()
        }
        if (!entity.type.category.isFriendly && !config.hostileMobs) {
            er.cancel()
        }
        if(er.isCancelled)
            return
        val style = Style.EMPTY.withColor(getColor(er.entity).asInt)
        val text = Component.empty().append(currentHp.toString()).append(" HP").withStyle(style)
        er.nameTag = text
    }

    /**
     * Gets the color formatting for the health tag.
     */
    private fun getColor(entity: LivingEntity): Color {
        return if (entity.health > entity.maxHealth * 0.8) {
            config.highHealthColor
        } else if (entity.health > entity.maxHealth * 0.6) {
            config.midHealthColor
        } else if (entity.health > entity.maxHealth * 0.4) {
            config.midLowHealthColor
        } else {
            config.lowHealthColor
        }
    }

    companion object {
        val config: HealthTagConfig
            get() {
                return Settings.getConfig(ChatCommand.HealthTag)
            }
    }


    override fun onEntityNameRender(er: EntityNameRender) {
        if (er.entity !is LivingEntity) return
        renderHealthTag(er)
    }
}
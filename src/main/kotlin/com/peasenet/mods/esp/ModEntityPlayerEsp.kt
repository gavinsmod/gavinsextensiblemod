/*
 * MIT License
 *
 * Copyright (c) 2022-2024.
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
package com.peasenet.mods.esp

import com.peasenet.config.EspConfig
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.entity.player.PlayerEntity

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the player to see an ESP to other players.
 */
class ModEntityPlayerEsp : EspMod(
    "Player ESP",
    "gavinsmod.mod.esp.player",
    "playeresp"
), EntityRenderListener {
    init {
//        val colorSetting = ColorSetting("gavinsmod.settings.esp.player.color", config.playerColor)
//        colorSetting.setCallback {
//            config.playerColor = colorSetting.color
//        }
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.player.color")
            .setColor(config.playerColor)
            .buildColorSetting()
        addSetting(colorSetting)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderListener::class.java, this)
    }

    override fun onEntityRender(er: EntityRender) {
        if (er.entity !is PlayerEntity || er.buffer == null) return
        val box = er.entity.boundingBox
        RenderUtils.drawBox(er.stack, er.buffer, box, config.playerColor, config.alpha)
    }
}

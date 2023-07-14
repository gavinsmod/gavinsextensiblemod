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
package com.peasenet.mods.esp

import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.entity.EntityType

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the player to see an esp (a box) around items.
 */
class ModEntityItemEsp : EspMod(
    "Item ESP",
    "gavinsmod.mod.esp.item",
    "itemesp"
), EntityRenderListener {
    init {
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.item.color")
            .setColor(espConfig.itemColor)
            .buildColorSetting()

        colorSetting.setCallback { espConfig.itemColor = colorSetting.color }
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
        if (er.entityType !== EntityType.ITEM) return
        if (er.buffer == null) return
        val box = RenderUtils.getEntityBox(er.delta, er.entity)
        RenderUtils.drawBox(er.stack, er.buffer, box, espConfig.itemColor, espConfig.alpha)
    }
}
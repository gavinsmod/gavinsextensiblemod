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
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.listeners.BlockEntityRenderListener
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.EnderChestBlockEntity
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.util.math.Box

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the client to see an esp (a box) around chests.
 */
class ModChestEsp : EspMod(
    "Chest ESP",
    "gavinsmod.mod.esp.chest",
    "chestesp"
), BlockEntityRenderListener {
    init {
//        val colorSetting = ColorSetting(
//            "gavinsmod.settings.esp.chest.color",
//            espConfig.chestColor
//        )
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.chest.color")
            .setColor(espConfig.chestColor)
            .buildColorSetting()
        colorSetting.setCallback {
            espConfig.chestColor = colorSetting.color
        }
        addSetting(colorSetting)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(BlockEntityRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockEntityRenderListener::class.java, this)
    }

    override fun onRenderBlockEntity(er: BlockEntityRender) {
        val box = Box(er.entity.pos)
        if (er.buffer == null) return
        if (er.entity is ChestBlockEntity || er.entity is ShulkerBoxBlockEntity
            || er.entity is EnderChestBlockEntity
        ) RenderUtils.drawBox(er.stack, er.buffer, box, espConfig.chestColor, espConfig.alpha)
    }
}
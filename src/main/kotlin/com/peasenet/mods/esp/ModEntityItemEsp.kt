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
package com.peasenet.mods.esp

import com.peasenet.gavui.color.Color
import com.peasenet.gui.mod.GuiItemEspTracerConfig
import com.peasenet.main.GavinsModClient
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils.CHUNK_RADIUS
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.ItemEntity

/**
 * A mod that allows the player to see an esp (a box) around items.
 *
 * @see RenderListener
 * @see GuiItemEspTracerConfig
 * @see com.peasenet.util.data.ItemEntityFilter
 *
 * @author gt3ch1
 * @version 01-12-2025
 * @since 03-02-2023
 */
class ModEntityItemEsp : EntityEsp<ItemEntity>(
    "Item ESP",
    "gavinsmod.mod.esp.item",
    "itemesp",
    { it is ItemEntity && it.squaredDistanceTo(GavinsModClient.player!!.getPos()) < 64 * 16 * CHUNK_RADIUS }),
    RenderListener {
    init {

        val menu = SettingBuilder().setTitle("gavinsmod.mod.esp.item").buildClickSetting()
        menu.setCallback {
            client.setScreen(GuiItemEspTracerConfig(config))
        }
        addSetting(menu)
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {/* TODO: Work on setting filters for items */
        if (config.useItemEspFilter) {
            espList = espList.filter {
                config.itemFilterList.any { filter -> filter.customNameMatches(it) }
            }.toMutableList()
        }
        super.onRender(matrixStack, partialTicks)
    }

    override fun getColor(entity: ItemEntity): Color = getColor()

    override fun getColor(): Color = config.itemColor
}

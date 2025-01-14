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
package com.peasenet.mods.tracer

import com.peasenet.gavui.color.Color
import com.peasenet.gui.mod.GuiItemEspTracerConfig
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.ItemEntity

/**
 * A mod that allows the player to see tracers towards items.
 *
 * @see EntityTracer
 * @see RenderListener
 * @see GuiItemEspTracerConfig
 *
 * @author gt3ch1
 * @version 01-12-2025
 * @since 04-11-2023
 */
class ModEntityItemTracer :
    EntityTracer<ItemEntity>("Item Tracer", "gavinsmod.mod.tracer.item", "itemtracer", { it is ItemEntity }),
    RenderListener {
    init {
        val menu = SettingBuilder().setTitle("gavinsmod.mod.tracer.item").buildClickSetting()
        menu.setCallback {
            client.setScreen(GuiItemEspTracerConfig(config))
        }
        addSetting(menu)
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {/* TODO: Work on setting filters for items */
        if (config.useItemEspFilter) {
            entityList = entityList.filter {
                config.itemFilterList.any { filter -> filter.customNameMatches(it) }
            }.toMutableList()
        }
        super.onRender(matrixStack, partialTicks)
    }

    override fun getColor(entity: ItemEntity): Color {
        return config.itemColor
    }


}

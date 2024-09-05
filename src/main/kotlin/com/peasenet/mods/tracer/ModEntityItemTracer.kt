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

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.TracerConfig
import com.peasenet.gavui.color.Color
import com.peasenet.main.Settings
import com.peasenet.mods.tracer.TracerMod.Companion
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.util.math.Box

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A mod that allows the player to see tracers towards items.
 */
class ModEntityItemTracer :
    EntityTracer<ItemEntity>("Item Tracer", "gavinsmod.mod.tracer.item", "itemtracer", { it is ItemEntity }),
    RenderListener {
    init {
        val colorSetting = SettingBuilder().setTitle("gavinsmod.settings.tracer.item.color").setColor(config.itemColor)
            .buildColorSetting()
        colorSetting.setCallback { config.itemColor = colorSetting.color }
        colorSetting.color = config.itemColor
        addSetting(colorSetting)
    }

    override fun getColor(entity: ItemEntity): Color {
        return config.itemColor
    }


}

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
package com.peasenet.mods.tracer

import com.peasenet.gavui.color.Color
import com.peasenet.util.listeners.RenderListener
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.EnderChestBlockEntity
import net.minecraft.block.entity.ShulkerBoxBlockEntity

/**
 * A mod that allows the player to see tracers towards chests.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-11-2023
 */
class ModChestTracer : BlockEntityTracer<BlockEntity>(
    "gavinsmod.mod.tracer.chest",
    "chesttracer",
    { it is ChestBlockEntity || it is EnderChestBlockEntity || it is ShulkerBoxBlockEntity }
), RenderListener {
    init {
//        val colorSetting =
//            SettingBuilder<ColorSetting>().setTitle("gavinsmod.settings.tracer.chest.color").setColor(config.chestColor)
//                .setCallback { config.chestColor = it.color }
//                .buildColorSetting()
//        addSetting(colorSetting)
        colorSetting {
            title = "gavinsmod.settings.tracer.chest.color"
            color = config.chestColor
            callback = { config.chestColor = it.color }
        }
    }

    override fun getColor(): Color {
        return config.chestColor
    }
}

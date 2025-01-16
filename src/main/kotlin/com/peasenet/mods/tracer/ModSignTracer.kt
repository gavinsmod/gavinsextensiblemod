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
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.SignBlockEntity

/**
 * A mod that allows the player to see tracers towards beehives.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-11-2023
 */
class ModSignTracer : BlockEntityTracer<BeehiveBlockEntity>("gavinsmod.mod.tracer.sign",
    ChatCommand.SignTracer.chatCommand,
    { it is SignBlockEntity }) {
    init {
        val colorSetting =
            SettingBuilder().setTitle("gavinsmod.settings.color.sign").setColor(config.signColor)
                .buildColorSetting()
        colorSetting.setCallback { config.signColor = colorSetting.color }
        addSetting(colorSetting)
    }

    override fun getColor(): Color {
        return config.signColor
    }

}

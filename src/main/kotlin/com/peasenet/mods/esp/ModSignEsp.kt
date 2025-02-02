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

package com.peasenet.mods.esp

import com.peasenet.gavui.color.Color
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import net.minecraft.block.entity.SignBlockEntity

/**
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 01-14-2025
 */
class ModSignEsp : BlockEntityEsp<SignBlockEntity>(
    "Sign ESP",
    "gavinsmod.mod.esp.sign",
    ChatCommand.SignEsp.chatCommand,
    { it is SignBlockEntity }) {
    init {
        val colorSetting =
            SettingBuilder<ColorSetting>().setTitle("gavinsmod.settings.color.sign").setColor(config.signColor)
                .setCallback { config.signColor = it.color }.buildColorSetting()
        addSetting(colorSetting)
    }

    override fun getColor(): Color = config.signColor
}
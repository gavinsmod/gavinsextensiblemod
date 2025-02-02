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
package com.peasenet.gui.mod.tracer

import com.peasenet.config.tracer.TracerConfig
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.Settings
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.ToggleSetting
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.text.Text

/**
 * A GUI that allows the player to filter what mobs they want to have tracers rendered for.

 *
 * @see TracerConfig
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-11-2023
 */
class GuiMobTracer : GuiMobSelection(Text.translatable("gavinsmod.settings.mobtracer")) {
    private val settings = Settings.getConfig<TracerConfig>("tracer")

    override fun init() {
        val height = 24f
        var pos = PointF(10f, height)
        hostileColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.color.hostileMob")
            .setColor(settings.hostileMobColor)
            .setTopLeft(pos)
            .setCallback { settings.hostileMobColor = it.color }
            .buildColorSetting()
        pos = pos.add(0f, 12f)

        peacefulColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.color.peacefulMob")
            .setColor(settings.peacefulMobColor)
            .setTopLeft(pos)
            .setCallback { settings.peacefulMobColor = it.color }
            .buildColorSetting()
        pos = pos.add(0f, 12f)
        enabledOnly = SettingBuilder<ToggleSetting>()
            .setTopLeft(pos)
            .setWidth(0f)
            .setHeight(10f)
            .setTitle(Text.literal("Enabled Only"))
            .setCallback { _ -> updateItemList() }
            .buildToggleSetting()
        pos = pos.add(0f, 12f)

        peacefulToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.mob.hostile")
            .setState(settings.showHostileMobs)
            .setTopLeft(pos)
            .setCallback { settings.showHostileMobs = it.value }
            .buildToggleSetting()
        pos = pos.add(0f, 12f)

        hostileToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.mob.peaceful")
            .setState(settings.showPeacefulMobs)
            .setTopLeft(pos)
            .setCallback { settings.showPeacefulMobs = it.value }
            .buildToggleSetting()
        super.init()
    }

    override fun isItemEnabled(item: ItemStack): Boolean {
        return settings.inList(item.item as SpawnEggItem)
    }

    override fun handleItemToggle(item: ItemStack) {
        val spawnEgg = (item.item as SpawnEggItem)
        if (isItemEnabled(item)) settings.removeMob(spawnEgg)
        else settings.addMob(spawnEgg)

    }
}

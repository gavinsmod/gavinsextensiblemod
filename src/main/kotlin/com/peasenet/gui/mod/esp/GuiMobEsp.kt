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
package com.peasenet.gui.mod.esp

import com.peasenet.config.EspConfig
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.text.Text

/**
 * A gui that allows the player to filter what mobs are shown in MobESP.
 *
 * @see EspConfig
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-11-2023
 */
class GuiMobEsp : GuiMobSelection(Text.translatable("gavinsmod.settings.mobesp")) {
    private companion object {
        val config: EspConfig = Settings.getConfig("esp")
    }

    override fun init() {
        val height = 24f
        var pos = PointF(10f, height)
        hostileColor = SettingBuilder()
            .setTitle("gavinsmod.settings.color.hostileMob")
            .setColor(config.hostileMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        hostileColor!!.setCallback { config.hostileMobColor = hostileColor!!.color }
        pos = pos.add(0f, 12f)

        peacefulColor = SettingBuilder()
            .setTitle("gavinsmod.settings.color.peacefulMob")
            .setColor(config.peacefulMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        peacefulColor!!.setCallback { config.peacefulMobColor = peacefulColor!!.color }
        pos = pos.add(0f, 12f)

        enabledOnly = GuiBuilder()
            .setTopLeft(pos)
            .setTitle("gavinsmod.generic.enabledOnly")
            .buildToggle()
        enabledOnly.setCallback {
            updateItemList()
        }
        pos = pos.add(0f, 12f)

        hostileToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.mob.hostile")
            .setTopLeft(pos)
            .buildToggleSetting()
        hostileToggle!!.setCallback { config.showHostileMobs = hostileToggle!!.value }
        pos = pos.add(0f, 12f)

        peacefulToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.mob.peaceful")
            .setState(config.showPeacefulMobs)
            .setTopLeft(pos)
            .buildToggleSetting()
        peacefulToggle!!.setCallback { config.showPeacefulMobs = peacefulToggle!!.value }
        super.init()
    }

    override fun isItemEnabled(item: ItemStack): Boolean {
        return config.inList(item.item as SpawnEggItem)
    }

    override fun handleItemToggle(item: ItemStack) {
        val spawnEgg = (item.item as SpawnEggItem)
        if (isItemEnabled(item))
            config.removeMob(spawnEgg)
        else
            config.addMob(spawnEgg)
    }
}

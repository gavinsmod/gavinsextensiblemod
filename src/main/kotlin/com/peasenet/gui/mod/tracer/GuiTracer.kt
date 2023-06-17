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
package com.peasenet.gui.mod.tracer

import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.GavinsMod
import com.peasenet.settings.SettingBuilder
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A gui that allows the player to search for blocks and add them to the xray list.
 */
class GuiTracer : GuiMobSelection(Text.translatable("gavinsmod.settings.mobtracer")) {


    override fun init() {
        val height = 24f
        var pos = PointF(10f, height)
        hostileColor = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.mob.hostile.color")
            .setColor(GavinsMod.tracerConfig.hostileMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        hostileColor.setCallback { GavinsMod.tracerConfig.hostileMobColor = hostileColor.color }
        pos = pos.add(0f, 12f)

        peacefulColor = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.mob.peaceful.color")
            .setColor(GavinsMod.tracerConfig.peacefulMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        peacefulColor.setCallback { GavinsMod.tracerConfig.peacefulMobColor = peacefulColor.color }
        pos = pos.add(0f, 12f)
        enabledOnly = GuiBuilder()
            .setTopLeft(pos)
            .setWidth(0f)
            .setHeight(10f)
            .setTitle(Text.literal("Enabled Only"))
            .setCallback(this::updateItemList)
            .buildToggle()
        pos = pos.add(0f, 12f)

        peacefulToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.mob.hostile")
            .setState(GavinsMod.tracerConfig.showHostileMobs)
            .setTopLeft(pos)
            .buildToggleSetting()
        peacefulToggle.setCallback { GavinsMod.tracerConfig.showHostileMobs = peacefulToggle.value }
        pos = pos.add(0f, 12f)

        hostileToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.mob.peaceful")
            .setState(GavinsMod.tracerConfig.showPeacefulMobs)
            .setTopLeft(pos)
            .buildToggleSetting()
        hostileToggle.setCallback { GavinsMod.tracerConfig.showPeacefulMobs = hostileToggle.value }
        super.init()
    }

    override fun isItemEnabled(item: ItemStack): Boolean {
        return GavinsMod.tracerConfig.mobIsShown(item.item as SpawnEggItem)
    }

    override fun handleItemToggle(item: ItemStack) {
        val spawnEgg = (item.item as SpawnEggItem)
        if (isItemEnabled(item)) GavinsMod.tracerConfig.removeMob(spawnEgg)
        else GavinsMod.tracerConfig.addMob(spawnEgg)

    }
}

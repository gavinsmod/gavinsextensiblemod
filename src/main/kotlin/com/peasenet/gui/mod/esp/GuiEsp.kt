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
package com.peasenet.gui.mod.esp

import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.GavinsMod
import com.peasenet.mods.Mod
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A gui that allows the player to search for blocks and add them to the xray list.
 */
class GuiEsp : GuiMobSelection(Text.translatable("gavinsmod.settings.mobesp")) {

    override fun init() {
        val height = 24f
        var pos = PointF(10f, height)
        hostileColor = ColorSetting("gavinsmod.settings.esp.mob.hostile.color", Mod.espConfig.hostileMobColor)
        hostileColor.setCallback { Mod.espConfig.hostileMobColor = hostileColor.color }
        hostileColor.color = Mod.espConfig.hostileMobColor
        hostileColor.gui.position = pos
        pos = pos.add(0f, 12f)

        peacefulColor = ColorSetting("gavinsmod.settings.esp.mob.peaceful.color", Mod.espConfig.peacefulMobColor)
        peacefulColor.setCallback { Mod.espConfig.peacefulMobColor = peacefulColor.color }
        peacefulColor.color = Mod.espConfig.peacefulMobColor
        peacefulColor.gui.position = pos
        pos = pos.add(0f, 12f)

        enabledOnly = GuiToggle(pos, 0, 10, Text.literal("Enabled Only"))
        enabledOnly.setCallback {
            updateItemList()
        }
        pos = pos.add(0f, 12f)

        hostileToggle = ToggleSetting("gavinsmod.settings.esp.mob.hostile")
        hostileToggle.setCallback { Mod.espConfig.showHostileMobs = hostileToggle.value }
        hostileToggle.value = Mod.espConfig.showHostileMobs
        hostileToggle.gui.position = pos
        pos = pos.add(0f, 12f)

        peacefulToggle = ToggleSetting("gavinsmod.settings.esp.mob.peaceful")
        peacefulToggle.setCallback { Mod.espConfig.showPeacefulMobs = peacefulToggle.value }
        peacefulToggle.value = Mod.espConfig.showPeacefulMobs
        peacefulToggle.gui.position = pos
        super.init()
    }

    override fun isItemEnabled(item: ItemStack): Boolean {
        return GavinsMod.espConfig.mobIsShown(item.item as SpawnEggItem)
    }

    override fun handleItemToggle(item: ItemStack) {
        val spawnEgg = (item.item as SpawnEggItem)
        if (isItemEnabled(item)) GavinsMod.espConfig.removeMob(spawnEgg)
        else GavinsMod.espConfig.addMob(spawnEgg)
    }
}

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
package com.peasenet.gui.mod.esp

import com.peasenet.config.esp.EspConfig
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.Settings
import com.peasenet.settings.colorSetting
import com.peasenet.settings.toggleSetting
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
        val height = 14f
        var pos = PointF(10f, height)
        hostileColor = colorSetting {
            title = "gavinsmod.settings.color.hostileMob"
            color = config.hostileMobColor
            topLeft = pos
            callback = { config.hostileMobColor = it.color }
        }
        pos = pos.add(0f, 13f)
        peacefulColor = colorSetting {
            title = "gavinsmod.settings.color.peacefulMob"
            color = config.peacefulMobColor
            topLeft = pos
            callback = { config.peacefulMobColor = it.color }
        }
        pos = pos.add(0f, 13f)
        enabledOnly = toggleSetting {
            topLeft = pos
            title = "gavinsmod.generic.enabledOnly"
            callback = {
                updateItemList()
            }
        }
        pos = pos.add(0f, 13f)
        hostileToggle = toggleSetting {
            title = "gavinsmod.settings.mob.hostile"
            topLeft = pos
            callback = { config.showHostileMobs = it.state }
            state = config.showHostileMobs
        }
        pos = pos.add(0f, 13f)

        peacefulToggle = toggleSetting {
            title = "gavinsmod.settings.mob.peaceful"
            topLeft = pos
            callback = { config.showPeacefulMobs = it.state }
            state = config.showPeacefulMobs
        }
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

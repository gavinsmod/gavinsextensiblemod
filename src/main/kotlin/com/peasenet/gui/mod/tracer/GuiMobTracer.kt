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
import com.peasenet.settings.colorSetting
import com.peasenet.settings.toggleSetting
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
        hostileColor = colorSetting {
            title = "gavinsmod.settings.color.hostileMob"
            color = settings.hostileMobColor
            topLeft = pos
            callback = { settings.hostileMobColor = it.color }
        }
        pos = pos.add(0f, 12f)
        peacefulColor = colorSetting {
            title = "gavinsmod.settings.color.peacefulMob"
            color = settings.peacefulMobColor
            topLeft = pos
            callback = { settings.peacefulMobColor = it.color }
        }
        pos = pos.add(0f, 12f)
        pos = pos.add(0f, 13f)
        enabledOnly = toggleSetting {
            topLeft = pos
            title = "gavinsmod.generic.enabledOnly"
            callback = {
                updateItemList()
            }
        }
        pos = pos.add(0f, 12f)
        peacefulToggle = toggleSetting {
            topLeft = pos
            title = "gavinsmod.settings.mob.hostile"
            callback = { settings.showHostileMobs = it.state }
        }
        pos = pos.add(0f, 12f)
        hostileToggle = toggleSetting {
            topLeft = pos
            this.height = 10f
            title = "gavinsmod.settings.mob.peaceful"
            callback = { settings.showPeacefulMobs = it.state }
        }
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

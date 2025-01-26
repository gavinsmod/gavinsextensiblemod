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

package com.peasenet.gui.mod.combat

import com.peasenet.config.commons.MobAttackFilterConfig
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.GuiMobSelection
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.ToggleSetting
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.text.Text
import kotlin.math.max

/**
 *
 * A GUI that allows the player to filter what mobs are attacked, and whether players should be targeted as well.
 * @param settingKey The key for the setting in the config.
 * @param translationKey The translation key for the GUI.
 *
 * @sample GuiAutoAttack
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 01-12-2025
 */
open class GuiAttackFilter(settingKey: String, translationKey: String) :
    GuiMobSelection(Text.translatable(translationKey)) {
    init {
        config = Settings.getConfig(settingKey)
    }

    private companion object {
        var config: MobAttackFilterConfig<*>? = null
    }

    private lateinit var excludePlayers: ToggleSetting

    override fun init() {
        showPeacefulColor = false
        showHostileColor = false
        showHostileToggle = false
        showPeacefulToggle = false
        val height = 24f
        var pos = PointF(10f, height)
        excludePlayers = SettingBuilder().setTitle("gavinsmod.generic.excludePlayers").setState(config!!.excludePlayers)
            .setTopLeft(pos)
            .setWidth(client!!.textRenderer.getWidth(Text.translatable("gavinsmod.generic.excludePlayers")) + 30)
            .buildToggleSetting()
        excludePlayers.setCallback {
            excludePlayers.value = !excludePlayers.value
            config!!.excludePlayers = excludePlayers.value
        }
        pos = pos.add(0f, 12f)
        enabledOnly =
            GuiBuilder().setTopLeft(pos).setTitle("gavinsmod.generic.enabledOnly").setWidth(excludePlayers.gui.width)
                .buildToggle()
        enabledOnly.setCallback {
            updateItemList()
        }
        val maxWidth = max(
            client!!.textRenderer.getWidth(Text.translatable("gavinsmod.generic.excludePlayers")) + 14f,
            enabledOnly.width
        )
        excludePlayers.gui.width = maxWidth
        enabledOnly.width = maxWidth
        additionalGuis.add(excludePlayers.gui)
        super.init()
    }

    override fun isItemEnabled(item: ItemStack): Boolean {
        return config!!.mobIsShown(item.item as SpawnEggItem)
    }

    override fun handleItemToggle(item: ItemStack) {
        val spawnEgg = item.item as SpawnEggItem
        if (config!!.mobIsShown(spawnEgg)) {
            config!!.removeMob(spawnEgg)
        } else {
            config!!.addMob(spawnEgg)
        }
    }
}
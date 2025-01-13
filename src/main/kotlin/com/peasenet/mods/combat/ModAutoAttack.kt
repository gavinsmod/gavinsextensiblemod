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
package com.peasenet.mods.combat

import com.peasenet.config.AutoAttackConfig
import com.peasenet.config.EspConfig
import com.peasenet.gui.mod.combat.GuiAutoAttack
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.PlayerUtils
import net.minecraft.util.hit.EntityHitResult

/**
 * A mod that makes the player attack the entity that it is currently looking at.
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 03-02-2023
 */
class ModAutoAttack : CombatMod(
    "Auto Attack",
    "gavinsmod.mod.combat.autoattack",
    "autoattack",
) {
    init {
        val click = SettingBuilder().setTitle(translationKey).buildClickSetting()
        click.setCallback {
            client.setScreen(GuiAutoAttack())
        }
        addSetting(click)
    }

    override fun onTick() {
        val target = client.crosshairTarget() as? EntityHitResult ?: return
        val entity = target.entity
        if (config.mobIsShown(entity.type))
            PlayerUtils.attackEntity(entity)
        if (!config.excludePlayers)
            PlayerUtils.attackEntity(entity)
    }

    companion object {
        val config: AutoAttackConfig
            get() {
                return Settings.getConfig("autoattack")
            }
    }
}
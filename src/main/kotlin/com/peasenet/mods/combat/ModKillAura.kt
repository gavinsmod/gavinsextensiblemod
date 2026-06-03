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
package com.peasenet.mods.combat

import com.peasenet.config.combat.KillAuraConfig
import com.peasenet.gui.mod.combat.GuiKillAura
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.util.ChatCommand
import com.peasenet.util.PlayerUtils
import com.peasenet.util.math.MathUtils
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.stream.StreamSupport

/**
 * A mod that makes the player face and attack the nearest mob.
 * @see CombatMod
 * @author GT3CH1
 * @version 11-27-2025
 * @since 03-02-2023
 */
class ModKillAura : CombatMod(
    "gavinsmod.mod.combat.killaura",
    ChatCommand.KillAura,
) {

    private companion object {
        val config = Settings.getConfig<KillAuraConfig>(ChatCommand.KillAura)
    }

    init {
        clickSetting {
            callback = {
                client.setScreen(GuiKillAura())
            }
            title = translationKey
        }
    }

    override fun onTick() {
        var stream = StreamSupport.stream(
            GavinsModClient.minecraftClient.getWorld().entitiesForRendering().spliterator(),
            false
        )
            .filter { e: Entity? -> e is LivingEntity }
            .filter { obj: Entity -> obj.isAlive }
            .filter { e: Entity -> PlayerUtils.distanceToEntity(e) <= 32 }
            .filter { e: Entity -> e.id != GavinsModClient.minecraftClient.getPlayer().id }
            .sorted { e1: Entity, e2: Entity? ->
                (PlayerUtils.distanceToEntity(e1).toInt() - PlayerUtils.distanceToEntity(e2)).toInt()
            }
            .map { e: Entity -> e as LivingEntity }
        if (config.shownMobs.isNotEmpty()) {
            stream = stream.filter { entity: LivingEntity? -> config.mobIsShown(entity!!.type!!) }
        }
        if (config.excludePlayers) {
            stream = stream.filter { it !is Player }
        }
        for (entity in stream) {
            entity.let { MathUtils.getRotationToEntity(it!!) }.let {
                PlayerUtils.setRotation(it)
            }
            PlayerUtils.attackEntity(entity)
        }
    }
}
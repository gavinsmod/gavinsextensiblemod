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

package com.peasenet.config.commons

import com.peasenet.config.Config
import com.peasenet.main.GavinsModClient
import net.minecraft.entity.EntityType
import net.minecraft.item.SpawnEggItem

/**
 * A configuration that allows the player to filter what mobs are attacked, and whether players should be targeted as well.
 *
 * @example [com.peasenet.mods.combat.ModAutoAttack]
 *
 * @see Config
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 01-12-2025
 */
open class MobAttackFilterConfig<E : Config<E>> : Config<E>() {

    /** * The key for this configuration is "esp".
     * Removes a mob from the shown mobs list.
     * @param mob The mob to remove (EntityType)
     */
    fun removeMob(mob: EntityType<*>) {
        shownMobs.remove(mob.toString())
        saveConfig()
    }

    /**
     * A list containing the mobs that are shown. Default value is an empty list.
     * The list is populated with the translation keys of the mobs.
     */
    var shownMobs: ArrayList<String> = ArrayList()
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Whether to exclude players from being targeted.
     */
    var excludePlayers: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Removes a mob from the shown mobs list.
     * @param spawnEggItem The mob to remove (SpawnEggItem)
     */
    fun removeMob(spawnEggItem: SpawnEggItem) {
        var registryManager = GavinsModClient.minecraftClient.getWorld().registryManager;
        removeMob(spawnEggItem.getEntityType(registryManager, spawnEggItem.defaultStack))
    }

    /**
     * Adds a mob to the shown mobs list.
     * @param spawnEggItem The mob to add (SpawnEggItem)
     */
    fun addMob(spawnEggItem: SpawnEggItem) {
        val registryManager = GavinsModClient.minecraftClient.getWorld().registryManager;
        addMob(spawnEggItem.getEntityType(registryManager, spawnEggItem.defaultStack))
    }

    /**
     * Adds a mob to the shown mobs list.
     * @param mob The mob to add (EntityType)
     */
    fun addMob(mob: EntityType<*>) {
        shownMobs.add(mob.toString())
        saveConfig()
    }

    /**
     * Checks if a mob is shown.
     * @param egg The mob to check (SpawnEggItem)
     * @return Whether the mob is shown.
     */
    fun mobIsShown(egg: SpawnEggItem): Boolean {
        val registryManager = GavinsModClient.minecraftClient.getWorld().registryManager;
        return mobIsShown(egg.getEntityType(registryManager, egg.defaultStack))
    }


    /**
     * Checks if a mob is shown.
     * @param mob The mob to check (EntityType)
     * @return Whether the mob is shown.
     */
    fun mobIsShown(mob: EntityType<*>): Boolean {
        val inList = shownMobs.contains(mob.translationKey);
        return inList
    }
}
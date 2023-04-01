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
package com.peasenet.config

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import net.minecraft.entity.EntityType
import net.minecraft.item.SpawnEggItem

/**
 * @author gt3ch1
 * @version 04-01-2023
 */
open class TracerEspConfig<E> : Config<TracerEspConfig<E>>() {
    var chestColor: Color = Colors.PURPLE
        set(value) {
            field = value
            saveConfig()
        }
    var hostileMobColor: Color = Colors.RED
        set(value) {
            field = value
            saveConfig()
        }
    var peacefulMobColor: Color = Colors.GREEN
        set(value) {
            field = value
            saveConfig()
        }
    var playerColor: Color = Colors.YELLOW
        set(value) {
            field = value
            saveConfig()
        }
    var itemColor: Color = Colors.CYAN
        set(value) {
            field = value
            saveConfig()
        }
    var beehiveColor: Color = Colors.GOLD
        set(value) {
            field = value
            saveConfig()
        }
    var furnaceColor: Color = Colors.RED_ORANGE
        set(value) {
            field = value
            saveConfig()
        }
    var alpha: Float = 0.5f
        set(value) {
            field = value
            saveConfig()
        }
    var showHostileMobs: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var showPeacefulMobs: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var shownMobs: ArrayList<String> = ArrayList()
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Removes a mob from the shown mobs list.
     * @param mob The mob to remove (EntityType)
     */
    fun removeMob(mob: EntityType<*>) {
        shownMobs.remove(mob.translationKey)
        saveConfig()
    }

    /**
     * Removes a mob from the shown mobs list.
     * @param spawnEggItem The mob to remove (SpawnEggItem)
     */
    fun removeMob(spawnEggItem: SpawnEggItem) {
        removeMob(spawnEggItem.getEntityType(null))
    }

    /**
     * Adds a mob to the shown mobs list.
     * @param spawnEggItem The mob to add (SpawnEggItem)
     */
    fun addMob(spawnEggItem: SpawnEggItem) {
        addMob(spawnEggItem.getEntityType(null))
    }

    /**
     * Adds a mob to the shown mobs list.
     * @param mob The mob to add (EntityType)
     */
    fun addMob(mob: EntityType<*>) {
        shownMobs.add(mob.translationKey)
        saveConfig()
    }

    /**
     * Checks if a mob is shown.
     * @param egg The mob to check (SpawnEggItem)
     * @return Whether the mob is shown.
     */
    fun mobIsShown(egg: SpawnEggItem): Boolean {
        return mobIsShown(egg.getEntityType(null))
    }


    /**
     * Checks if a mob is shown.
     * @param mob The mob to check (EntityType)
     * @return Whether the mob is shown.
     */
    fun mobIsShown(mob: EntityType<*>): Boolean {
        val inList = shownMobs.contains(mob.translationKey);
        if (mob.spawnGroup.isPeaceful && showPeacefulMobs)
            return inList
        if (!mob.spawnGroup.isPeaceful && showHostileMobs)
            return inList
        return false
    }
}


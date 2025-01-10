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
package com.peasenet.config

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.GavinsModClient
import com.peasenet.mods.esp.ItemEspFilter
import net.minecraft.entity.EntityType
import net.minecraft.item.SpawnEggItem

/**
 * This is the shared configuration for tracers and esps, as they tend to have very similar settings.
 * @author gt3ch1
 * @version 04-01-2023
 */
open class TracerEspConfig<E> : Config<TracerEspConfig<E>>() {


    /**
     * The color for chests. Default value is [Colors.PURPLE]
     */
    var chestColor: Color = Colors.PURPLE
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for hostile mobs. Default value is [Colors.RED]
     */
    var hostileMobColor: Color = Colors.RED
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for peaceful mobs. Default value is [Colors.GREEN]
     */
    var peacefulMobColor: Color = Colors.GREEN
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for players. Default value is [Colors.YELLOW]
     */
    var playerColor: Color = Colors.YELLOW
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for items. Default value is [Colors.CYAN]
     */
    var itemColor: Color = Colors.CYAN
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for beehives. Default value is [Colors.GOLD]
     */
    var beehiveColor: Color = Colors.GOLD
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for furnaces. Default value is [Colors.RED_ORANGE]
     */
    var furnaceColor: Color = Colors.RED_ORANGE
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The color for block esp. Default value is [Colors.SHADOW_BLUE]
     */
    var blockColor: Color = Colors.SHADOW_BLUE
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The alpha value for rendering. Default value is 0.5f. Valid values are 0.0f to 1.0f.
     */
    var alpha: Float = 0.5f
        set(value) {
            field = value.coerceAtLeast(0.0f).coerceAtMost(1.0f)
            saveConfig()
        }

    /**
     * Whether hostile mobs are shown. Default value is true.
     */
    var showHostileMobs: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Whether peaceful mobs are shown. Default value is true.
     */
    var showPeacefulMobs: Boolean = true
        set(value) {
            field = value
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

    var itemEspFilterList: ArrayList<ItemEspFilter> = ArrayList()
        set(value) {
            field = value
            saveConfig()
        }

    var useItemEspFilter: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    /** * The key for this configuration is "esp".

     * Removes a mob from the shown mobs list.
     * @param mob The mob to remove (EntityType)
     */
    fun removeMob(mob: EntityType<*>) {
        shownMobs.remove(mob.toString())
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
        if (mob.spawnGroup.isPeaceful && showPeacefulMobs)
            return inList
        if (!mob.spawnGroup.isPeaceful && showHostileMobs)
            return inList
        return false
    }
}


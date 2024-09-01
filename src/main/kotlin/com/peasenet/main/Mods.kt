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
package com.peasenet.main

import com.peasenet.config.*
import com.peasenet.mods.Mod
import com.peasenet.mods.combat.ModAutoAttack
import com.peasenet.mods.combat.ModAutoCrit
import com.peasenet.mods.combat.ModKillAura
import com.peasenet.mods.esp.*
import com.peasenet.mods.gui.ModGui
import com.peasenet.mods.gui.ModGuiSettings
import com.peasenet.mods.misc.ModFpsCounter
import com.peasenet.mods.misc.ModFreeCam
import com.peasenet.mods.misc.ModGuiTextOverlay
import com.peasenet.mods.movement.*
import com.peasenet.mods.render.*
import com.peasenet.mods.tracer.*


/**
 *
 * The class that handles keeping track of GEMs. Initializing this class will also initialize
 * the main GUI and the settings GUI.
 * @author gt3ch1
 * @version 07-18-2023
 */
class Mods {

    init {
        ModGui()
        ModGuiSettings()

        Settings.addConfig(FpsColorConfig())
        Settings.addConfig(FullbrightConfig())
        Settings.addConfig(XrayConfig())
        Settings.addConfig(BlockEspConfig())
        Settings.addConfig(RadarConfig())
        Settings.addConfig(WaypointConfig())
        /*@MODS@*/
        GavinsMod.addMod(ModAutoAttack())
        GavinsMod.addMod(ModAutoCrit())
        GavinsMod.addMod(ModKillAura())
        GavinsMod.addMod(ModBeehiveEsp())
        GavinsMod.addMod(ModChestEsp())
        GavinsMod.addMod(ModEntityItemEsp())
        GavinsMod.addMod(ModEntityPlayerEsp())
        GavinsMod.addMod(ModFurnaceEsp())
        GavinsMod.addMod(ModMobEsp())
        GavinsMod.addMod(ModFpsCounter())
        GavinsMod.addMod(ModFreeCam())
        GavinsMod.addMod(ModGuiTextOverlay())
        GavinsMod.addMod(ModAntiTrample())
        GavinsMod.addMod(ModAutoJump())
        GavinsMod.addMod(ModClimb())
        GavinsMod.addMod(ModDolphin())
        GavinsMod.addMod(ModFastMine())
        GavinsMod.addMod(ModFastPlace())
        GavinsMod.addMod(ModFly())
        GavinsMod.addMod(ModNoClip())
        GavinsMod.addMod(ModNoFall())
        GavinsMod.addMod(ModSpeed())
        GavinsMod.addMod(ModAntiFire())
        GavinsMod.addMod(ModAntiHurt())
        GavinsMod.addMod(ModAntiPumpkin())
        GavinsMod.addMod(ModBarrierDetect())
        GavinsMod.addMod(ModFullBright())
        GavinsMod.addMod(ModHealthTag())
        GavinsMod.addMod(ModNoNausea())
        GavinsMod.addMod(ModNoOverlay())
        GavinsMod.addMod(ModNoRain())
        GavinsMod.addMod(ModNoVignette())
        GavinsMod.addMod(ModRadar())
        GavinsMod.addMod(ModWaypoint())
        GavinsMod.addMod(ModXray())
        GavinsMod.addMod(ModBeehiveTracer())
        GavinsMod.addMod(ModChestTracer())
        GavinsMod.addMod(ModEntityItemTracer())
        GavinsMod.addMod(ModEntityPlayerTracer())
        GavinsMod.addMod(ModFurnaceTracer())
        GavinsMod.addMod(ModMobTracer())
        GavinsMod.addMod(ModBlockEsp())
    }

    companion object {

        /**
         * The map of mods, with their chat command as the key, and the mod as the value.
         */
        private val modMap = HashMap<String, Mod>()

        @JvmStatic
        val mods: ArrayList<Mod>
            /**
             * Gets the list of mods in sorted order as defined by the mod's name.
             *
             * @return The list of mods.
             */
            get() {
                // get all values from the map and add them to the list
                val values: Collection<Mod> = modMap.values
                // sort the list by name
                val list = ArrayList(values)
                list.sortWith(Comparator.comparing(Mod::name))
                return list
            }

        /**
         * Gets a mod from the given name.
         *
         * @param chatCommand - The chat command of the mod
         * @return The mod with the given chat command.
         */
        @JvmStatic
        fun getMod(chatCommand: String): Mod? {
            if (modMap[chatCommand] == null)
                return null;
            return modMap[chatCommand]!!
        }

        /**
         * Gets whether the given mod with the chat command is active.
         *
         * @param chatCommand - The chat command of the mod.
         */
        @JvmStatic
        fun isActive(chatCommand: String): Boolean {
            val mod: Mod = getMod(chatCommand) ?: return false
            return mod.isActive
        }

        /*
        * Adds a mod to the mod hashmap using the mod chat command as the key, and the mod as the value.
        *
        * @param m - The mod to add.
        */
        fun addMod(m: Mod) {
            modMap[m.chatCommand] = m
        }
    }
}
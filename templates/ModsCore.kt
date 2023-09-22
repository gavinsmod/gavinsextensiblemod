/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
package com.peasenet.main

import com.peasenet.mods.Mod
import com.peasenet.mods.gui.ModGui
import com.peasenet.mods.gui.ModGuiSettings


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
        /*@MODS@*/
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

        /**
         * Adds a mod to the mod hashmap using the mod chat command as the key, and the mod as the value.
         *
         * @param m - The mod to add.
         */
        fun addMod(m: Mod) {
            modMap[m.chatCommand] = m
        }
    }
}
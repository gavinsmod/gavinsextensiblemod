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
package com.peasenet.main

import com.peasenet.gavui.GavUI
import com.peasenet.gui.mod.*
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.ChatCommand
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author GT3CH1
 * @version 01-15-2025
 * The main initializer of the mod.
 */
class GavinsMod : ModInitializer {

    companion object {
        /**
         * The current version of the mod.
         */
        const val VERSION = "1.5.3"

        /**
         * A hashmap containing the category of each mod category and the corresponding gui.
         */
        val guiList = HashMap<ModCategory, GuiMod>()


        /**
         * The logger of the mod.
         */
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger("gavinsmod")

        /**
         * Adds a GEM to the main client to load when the client initializes.
         * @param mod - The GEM to add.
         */
        @JvmStatic
        fun addMod(mod: Mod) {
            GavinsModClient.modsToLoad.add(mod)
        }

        /**
         * Sets whether the given mod is enabled.
         *
         * @param chatCommand - The chat command of the mod to enable.
         * @param enabled - Whether the mod should be enabled.
         */
        @JvmStatic
        fun setEnabled(chatCommand: String, enabled: Boolean) {
            val theMod = Mods.mods.first { m: Mod -> m.chatCommand == chatCommand }
            if (enabled) theMod.activate() else theMod.deactivate()
        }

        /**
         * Gets all the mods in the given category.
         *
         * @param category The category to get the mods from.
         * @return The mods in the given category.
         */
        @JvmStatic
        fun getModsInCategory(category: ModCategory): java.util.ArrayList<Mod> {
            // use stream to filter by category and sort by mod name
            return Mods.mods.filter {
                it.modCategory == category
            }.toCollection(ArrayList())
        }

        /**
         * Gets whether the given mod is enabled.
         *
         * @param chatCommand - The chat command of the mod to check.
         * @return Whether the mod is enabled.
         */
        @JvmStatic
        @Deprecated("Use Mods.isActive instead", ReplaceWith("Mods.isActive(chatCommand)"))
        fun isEnabled(chatCommand: String): Boolean {
            val mod = Mods.getMod(chatCommand) ?: return false
            return mod.isActive
        }

        @JvmStatic
        @Deprecated("Use Mods.isActive instead", ReplaceWith("Mods.isActive(chatCommand)"))
        fun isEnabled(chatCommand: ChatCommand): Boolean {
            return isEnabled(chatCommand.command)
        }

    }

    override fun onInitialize() {
        GavUI.initialize()
        Settings.init()
        LOGGER.info("Settings loaded")
        Mods()

        LOGGER.info("GavinsMod initialized")
    }
}

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

import com.peasenet.gavui.GavUI
import com.peasenet.gavui.Gui
import com.peasenet.gavui.math.BoxF
import com.peasenet.gui.GuiMainMenu
import com.peasenet.gui.GuiSettings
import com.peasenet.gui.mod.*
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.ModCommands
import com.peasenet.util.executor.GemExecutor
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Consumer

/**
 * @author gt3ch1
 * @version 04-08-2024
 * The main initializer of the mod.
 */
class GavinsMod : ModInitializer {

    companion object {
        /**
         * The current version of the mod.
         */
        const val VERSION = "v1.4.10"

        /**
         * A hashmap containing the category of each mod category and the corresponding gui.
         */
        val guiList = HashMap<ModCategory, GuiMod>()

        /**
         * The list of mods to load upon initialization.
         */
        private val modsToLoad = ArrayList<Mod>()

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
            modsToLoad.add(mod)
        }

        /**
         * The gui used to display the main mod menu.
         */
        lateinit var gui: GuiMainMenu

        /**
         * The gui used to display the settings menu.
         */
        lateinit var guiSettings: GuiSettings


        /**
         * Hook for chat commands.
         */
        private var modCommands: ModCommands? = null

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
            return Mods.mods.stream().filter { mod: Mod -> mod.modCategory === category }
                .sorted(Comparator.comparing(Mod::name)).collect({ ArrayList() },
                    { obj: java.util.ArrayList<Mod>, e: Mod -> obj.add(e) }) { obj: java.util.ArrayList<Mod>, c: java.util.ArrayList<Mod>? ->
                    obj.addAll(c!!)
                }
        }

        /**
         * Gets whether the given mod is enabled.
         *
         * @param chatCommand - The chat command of the mod to check.
         * @return Whether the mod is enabled.
         */
        @JvmStatic
        fun isEnabled(chatCommand: String): Boolean {
            val mod = Mods.getMod(chatCommand) ?: return false
            return mod.isActive
        }
    }

    override fun onInitialize() {
        GavUI.initialize()
        GemExecutor.init()
        Settings.init()
        LOGGER.info("Settings loaded")
        Mods()
        modsToLoad.forEach(Consumer { m: Mod -> Mods.addMod(m) })
        guiList[ModCategory.MOVEMENT] = GuiMovement()
        guiList[ModCategory.COMBAT] = GuiCombat()
        guiList[ModCategory.ESP] = GuiESP()
        guiList[ModCategory.MISC] = GuiMisc()
        val guiRender = GuiRender()
        // fix for issue #55
        val guis = ModGuiUtil.getGuiToggleFromCategory(
            ModCategory.WAYPOINTS, BoxF(guiRender.position, guiRender.width, guiRender.height)
        )
        guis.forEach { guiRender.addElement(it) }
        guiList[ModCategory.RENDER] = guiRender
        guiList[ModCategory.TRACERS] = GuiTracers()
        guiList.values.forEach(Consumer { g: Gui -> g.isParent = true })
        // remove all the guis that have no children
        guiList.values.removeIf { g: Gui -> g.children.isEmpty() }
        // collect all the guis that have children into an array list
        val guisWithChildren = ArrayList<Gui>()
        guiList.values.forEach { guisWithChildren.add(it) }
        gui = GuiMainMenu(guisWithChildren)
        guiSettings = GuiSettings()
        modCommands = ModCommands()

        LOGGER.info("GavinsMod initialized")
    }
}

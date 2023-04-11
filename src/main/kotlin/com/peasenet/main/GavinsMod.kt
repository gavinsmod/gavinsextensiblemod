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
package com.peasenet.main

import com.peasenet.config.*
import com.peasenet.gavui.GavUI
import com.peasenet.gavui.Gui
import com.peasenet.gavui.math.BoxF
import com.peasenet.gui.GuiMainMenu
import com.peasenet.gui.GuiSettings
import com.peasenet.gui.mod.*
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.util.ModCommands
import com.peasenet.util.event.EventManager
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Consumer
import java.util.stream.Stream

/**
 * @author gt3ch1
 * @version 04-11-2023
 * The main initializer of the mod.
 */
class GavinsMod : ModInitializer {
    override fun onInitialize() {
        GavUI.initialize()
        LOGGER.info("Loading settings")
        Settings.initialize()
        espConfig = Settings.settings["esp"] as EspConfig
        tracerConfig = Settings.settings["tracer"] as TracerConfig
        xrayConfig = Settings.settings["xray"] as XrayConfig
        fullbrightConfig = Settings.settings["fullbright"] as FullbrightConfig
        fpsColorConfig = Settings.settings["fpsColors"] as FpsColorConfig
        radarConfig = Settings.settings["radar"] as RadarConfig
        waypointConfig = Settings.settings["waypoints"] as WaypointConfig
        miscConfig = Settings.settings["misc"] as MiscConfig
        LOGGER.info("Settings loaded")
        eventManager = EventManager()
        Mods()
        LOGGER.info("GavinsMod initialized")
        val guiList = java.util.ArrayList<Gui>()
        guiList.add(GuiMovement())
        guiList.add(GuiCombat())
        guiList.add(GuiESP())
        guiList.add(GuiMisc())

        val guiRender = GuiRender()
        // fix for issue #55
        val guis = ModGuiUtil.getGuiToggleFromCategory(Type.Category.WAYPOINTS, BoxF(guiRender.position, guiRender.width, guiRender.height))
        guis.forEach { guiRender.addElement(it) }


        guiList.add(guiRender)
        guiList.add(GuiTracers())
        guiList.forEach(Consumer { g: Gui -> g.isParent = true })
        gui = GuiMainMenu(guiList)
        guiSettings = GuiSettings()
        modCommands = ModCommands()
    }

    companion object {
        /**
         * The logger of the mod.
         */
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger("gavinsmod")

        /**
         * The current version of the mod.
         */
        const val VERSION = "v1.4.2"

        /**
         * The gui used to display the main mod menu.
         */
        lateinit var gui: GuiMainMenu

        /**
         * The gui used to display the settings menu.
         */
        lateinit var guiSettings: GuiSettings

        /**
         * The event manager of the mod.
         */
        lateinit var eventManager: EventManager

        /**
         * The ESP config.
         */
        lateinit var espConfig: EspConfig

        /**
         * The tracer config.
         */
        lateinit var tracerConfig: TracerConfig

        /**
         * The xray config.
         */
        lateinit var xrayConfig: XrayConfig

        /**
         * The fullbright config.
         */
        lateinit var fullbrightConfig: FullbrightConfig

        /**
         * The FPS Color config.
         */
        lateinit var fpsColorConfig: FpsColorConfig

        /**
         * The radar config.
         */
        lateinit var radarConfig: RadarConfig

        /**
         * The waypoint config.
         */
        lateinit var waypointConfig: WaypointConfig

        /**
         * The misc config.
         */
        lateinit var miscConfig: MiscConfig

        /**
         * Hook for chat commands.
         */
        private var modCommands: ModCommands? = null

        /**
         * Gets whether the given mod is enabled.
         *
         * @param mod - The mod type to check.
         * @return Whether the mod is enabled.
         */
        @JvmStatic
        fun isEnabled(mod: Type): Boolean {
            return Mods.getMod(mod.chatCommand).isActive
        }

        /**
         * Sets whether the given mod is enabled.
         *
         * @param mod     - The mod type to set.
         * @param enabled - Whether the mod should be enabled.
         */
        @JvmStatic
        fun setEnabled(mod: Type, enabled: Boolean) {
            // find mod via stream and set it to enabled.
            val theMod = Mods.mods.stream().filter { m: Mod -> m.type === mod }.findFirst().get()
            if (enabled) theMod.activate() else theMod.deactivate()
        }

        /**
         * Gets all the mods in the given category.
         *
         * @param category The category to get the mods from.
         * @return The mods in the given category.
         */
        @JvmStatic
        fun getModsInCategory(category: Type.Category): java.util.ArrayList<Mod> {
            // use stream to filter by category and sort by mod name
            return Mods.mods.stream().filter { mod: Mod -> mod.category === category }.sorted(Comparator.comparing(Mod::name)).collect({ ArrayList() }, { obj: java.util.ArrayList<Mod>, e: Mod -> obj.add(e) }) { obj: java.util.ArrayList<Mod>, c: java.util.ArrayList<Mod>? ->
                obj.addAll(c!!)
            }
        }

        val modsForTextOverlay: Stream<Mod>
            /**
             * Gets a stream of all the mods that are active and are not in the "GUI" category.
             *
             * @return A list of mods used for the text overlay.
             */
            get() = Mods.mods.stream().filter { mod: Mod -> mod.isActive && mod.category !== Type.Category.GUI && mod.type !== Type.MOD_GUI_TEXT_OVERLAY }.sorted(Comparator.comparing(Mod::name))
    }
}
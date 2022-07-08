/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.main;

import com.peasenet.gui.GuiMainMenu;
import com.peasenet.gui.GuiSettings;
import com.peasenet.gui.mod.*;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;


/**
 * @author gt3ch1
 * @version 7/1/2022
 * The main initializer of the mod.
 */
public class GavinsMod implements ModInitializer {

    /**
     * The logger of the mod.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("gavinsmod");

    /**
     * The current version of the mod.
     */
    public static final String VERSION = "v1.3";

    /**
     * The gui used to display the main mod menu.
     */
    public static GuiMainMenu gui;

    /**
     * The gui used to display the settings menu.
     */
    public static GuiSettings guiSettings;

    /**
     * Gets whether the given mod is enabled.
     *
     * @param mod - The mod type to check.
     * @return Whether the mod is enabled.
     */
    //TODO: Make this into a String based check and not Mod.
    public static boolean isEnabled(Type mod) {
        return Mods.getMod(mod.getChatCommand()).isActive();
    }

    /**
     * Sets whether the given mod is enabled.
     *
     * @param mod     - The mod type to set.
     * @param enabled - Whether the mod should be enabled.
     */
    public static void setEnabled(Type mod, boolean enabled) {
        // find mod via stream and set it to enabled.
        var theMod = Mods.getMods().stream().filter(m -> m.getType() == mod).findFirst().get();
        if (enabled) theMod.activate();
        else theMod.deactivate();
    }

    /**
     * Gets all the mods in the given category.
     *
     * @param category The category to get the mods from.
     * @return The mods in the given category.
     */
    public static ArrayList<Mod> getModsInCategory(Type.Category category) {
        // use stream to filter by category and sort by mod name
        return Mods.getMods().stream().filter(mod -> mod.getCategory() == category).sorted(Comparator.comparing(Mod::getName)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets a stream of all the mods that are active and are not in the "GUI" category.
     *
     * @return A list of mods used for the text overlay.
     */
    public static Stream<Mod> getModsForTextOverlay() {
        return Mods.getMods().stream().filter(mod -> mod.isActive() && mod.getCategory() != Type.Category.GUI && mod.getType() != Type.MOD_GUI_TEXT_OVERLAY).sorted(Comparator.comparing(Mod::getName));
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Loading settings");
        Settings.initialize();
        LOGGER.info("Settings loaded");
        new Mods();
        LOGGER.info("GavinsMod initialized");
        ArrayList<com.peasenet.gui.elements.Gui> guiList = new ArrayList<>();
        guiList.add(new GuiMovement());
        guiList.add(new GuiCombat());
        guiList.add(new GuiESP());
        guiList.add(new GuiMisc());
        guiList.add(new GuiRender());
        guiList.add(new GuiTracers());

        gui = new GuiMainMenu(guiList);
        guiSettings = new GuiSettings();
    }

}


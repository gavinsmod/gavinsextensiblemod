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

package com.peasenet.gui;

import com.peasenet.gavui.Gui;
import com.peasenet.gavui.GuiScroll;
import com.peasenet.gavui.math.PointD;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.Setting;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A settings gui to control certain features of the mod.
 */
public class GuiSettings extends GuiElement {

    /**
     * The tracer dropdown
     */
    GuiScroll tracerDropdown;

    /**
     * The esp dropdown
     */
    GuiScroll espDropdown;

    /**
     * The render dropdown
     */
    GuiScroll renderDropdown;

    /**
     * The miscellaneous dropdown
     */
    GuiScroll miscDropdown;

    /**
     * The dropdown containing gui settings.
     */
    GuiScroll guiDropdown;

    /**
     * Creates a new GUI settings screen.
     */
    public GuiSettings() {
        super(Text.translatable("gavinsmod.gui.settings"));
        guis = new ArrayList<>();
        renderDropdown = new GuiScroll(new PointD(10, 20), 100, 10, Text.translatable("gavinsmod.settings.render"));
        miscDropdown = new GuiScroll(new PointD(115, 20), 100, 10, Text.translatable("gavinsmod.settings.misc"));
        guiDropdown = new GuiScroll(new PointD(220, 20), 100, 10, Text.translatable("gavinsmod.settings.gui"));
        espDropdown = new GuiScroll(new PointD(10, 130), 110, 10, Text.translatable("gavinsmod.settings.esp"));
        tracerDropdown = new GuiScroll(new PointD(125, 130), 115, 10, Text.translatable("gavinsmod.settings.tracer"));
        reloadGui();

    }

    @Override
    public void close() {
        GavinsMod.setEnabled(Type.SETTINGS, false);
        super.close();
    }

    /**
     * Reloads this gui by clearing all children, and recreating them.
     */
    public void reloadGui() {
        guis.forEach(Gui::clearChildren);
        guis.clear();
        // Create a plain gui at the top right corner
        addSettings(tracerDropdown, Type.Category.TRACERS);
        addSettings(espDropdown, Type.Category.ESPS);
        addSettings(renderDropdown, Type.Category.RENDER);
        addSettings(miscDropdown, Type.Category.MISC);
        addSettings(guiDropdown, Type.Category.GUI);
        guis.add(tracerDropdown);
        guis.add(espDropdown);
        guis.add(renderDropdown);
        guis.add(miscDropdown);
        guis.add(guiDropdown);
        guis.forEach(g -> g.setParent(true));
    }

    /**
     * Creates the esp settings scroll gui.
     *
     * @param parent   - The parent gui.
     * @param category - The category of mod types to add to the parent gui.
     */
    private void addSettings(Gui parent, Type.Category category) {
        var modList = new ArrayList<Mod>();
        // get all mods in esp category and have settings then add them to espDropdown
        Mods.getMods().stream().filter(m -> m.getCategory() == category && m.hasSettings()).forEach(modList::add);
        for (Mod m : modList) {
            var modSettings = m.getSettings();
            for (Setting s : modSettings) {
                s.getGui().setWidth(parent.getWidth());
                parent.addElement(s.getGui());
            }
        }
    }
}

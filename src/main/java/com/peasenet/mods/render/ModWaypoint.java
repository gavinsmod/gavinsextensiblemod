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

package com.peasenet.mods.render;

import com.peasenet.gui.mod.waypoint.GuiWaypoint;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import net.minecraft.text.Text;

import java.util.Comparator;

/**
 * @author gt3ch1
 * @version 6/30/2022
 * Creates a new mod to control waypoints.
 */
public class ModWaypoint extends Mod {

    private static SubSetting setting;
    private static ClickSetting openMenu;

    public ModWaypoint() {
        super(Type.WAYPOINT);
        setting = new SubSetting(100, 10, "gavinsmod.mod.render.waypoints");
        openMenu = new ClickSetting("Open Menu", "gavinsmod.settings.render.waypoints.add");
        reloadSettings();
    }

    @Override
    public void reloadSettings() {
        modSettings.clear();
        setting = new SubSetting((int) setting.getGui().getWidth(), 10, "gavinsmod.mod.render.waypoints");
        openMenu.setCallback(() -> getClient().setScreen(new GuiWaypoint()));
        openMenu.getGui().setSymbol('\u002b');
        setting.add(openMenu);
        // get all waypoints and add them to the menu
        Settings.getWaypoints().stream().sorted(Comparator.comparing(Waypoint::getName)).forEach(waypoint -> createWaypoint(setting, waypoint));
        addSetting(setting);
    }

    /**
     * Creates a new setting to edit the given waypoint and adds it to the given setting.
     *
     * @param subSetting - The setting to add the waypoint to.
     * @param waypoint   - The waypoint to edit.
     */
    private void createWaypoint(SubSetting subSetting, Waypoint waypoint) {
        ToggleSetting clickSetting = new ToggleSetting("none", Text.literal(waypoint.getName()), true);
//        clickSetting.setWidth(105);
        clickSetting.setCallback(() -> {
            getClient().setScreen(new GuiWaypoint(waypoint));
            clickSetting.setValue(waypoint.isEnabled());
        });
        clickSetting.setValue(waypoint.isEnabled());
        setting.add(clickSetting);
    }
}

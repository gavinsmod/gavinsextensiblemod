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

import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/30/2022
 */
public class ModWaypoint extends Mod {
    public ModWaypoint() {
        super(Type.WAYPOINT);
        SubSetting subSetting = new SubSetting(50, 10, getTranslationKey());
        ClickSetting addWaypoint = new ClickSetting("none");
        addWaypoint.setCallback(() -> {
            var pos = getPlayer().getPos().add(0, 1, 0);
            var waypoint = new Waypoint(pos);
            waypoint.setEnabled(true);
            Settings.addWaypoint(waypoint);
            createWaypoint(subSetting, waypoint);
        });
        subSetting.add(addWaypoint);
        Settings.getWaypoints().forEach(waypoint -> createWaypoint(subSetting, waypoint));
        addSetting(subSetting);
    }

    private void createWaypoint(SubSetting subSetting, Waypoint waypoint) {
        ToggleSetting toggleSetting = new ToggleSetting("none", "gavinsmod.settings.waypoint.toggle");
        toggleSetting.setWidth(50);
        subSetting.add(toggleSetting);
        toggleSetting.setCallback(() ->
        {
            Settings.setWaypointState(waypoint, !waypoint.isEnabled());
            toggleSetting.setValue(Settings.getWaypoint(waypoint).isEnabled());
        });
        toggleSetting.setValue(Settings.getWaypoint(waypoint).isEnabled());
        toggleSetting.setTitle(Text.literal(waypoint.toString()));
    }
}

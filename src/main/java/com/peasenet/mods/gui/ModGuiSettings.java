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

package com.peasenet.mods.gui;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.ToggleSetting;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A mod that allows the player to configure certain settings of gavinsmod.
 */
public class ModGuiSettings extends Mod {
    public ModGuiSettings() {
        super(Type.SETTINGS);
        ColorSetting foregroundColorSetting = new ColorSetting("foregroundColor", "gavinsmod.settings.gui.color.foreground");
        ColorSetting backgroundColorSetting = new ColorSetting("backgroundColor", "gavinsmod.settings.gui.color.background");
        ColorSetting categoryColorSetting = new ColorSetting("categoryColor", "gavinsmod.settings.gui.color.category");
        ColorSetting enabledColorSetting = new ColorSetting("enabledColor", "gavinsmod.settings.gui.color.enabled");
        ToggleSetting guiSounds = new ToggleSetting("guiSounds", "gavinsmod.settings.gui.sound");

        addSetting(backgroundColorSetting);
        addSetting(foregroundColorSetting);
        addSetting(categoryColorSetting);
        addSetting(enabledColorSetting);
        addSetting(guiSounds);
    }

    @Override
    public void activate() {
        GavinsModClient.getMinecraftClient().setScreen(GavinsMod.guiSettings);
        setEnabled(true);
    }

    @Override
    public void deactivate() {
        setEnabled(false);
    }


}

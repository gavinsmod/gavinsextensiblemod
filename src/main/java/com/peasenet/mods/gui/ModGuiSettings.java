/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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

package com.peasenet.mods.gui;

import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.SlideSetting;
import com.peasenet.settings.ToggleSetting;

/**
 * @author gt3ch1
 * @version 12/31/2022
 * A mod that allows the player to configure certain settings of gavinsmod.
 */
public class ModGuiSettings extends Mod {
    public ModGuiSettings() {
        super(Type.SETTINGS);

        var guiSounds = new ToggleSetting("gavinsmod.settings.gui.sound");
        guiSounds.setCallback(() -> {
            GavUISettings.add("gui.sound", guiSounds.getValue());
        });
        guiSounds.setValue(GavUISettings.getBool("gui.sound"));

        var foregroundColorSetting = new ColorSetting("gavinsmod.settings.gui.color.foreground");
        foregroundColorSetting.setCallback(() -> {
            GavUISettings.add("gui.color.foreground", foregroundColorSetting.getColor());
        });
        foregroundColorSetting.setColor(GavUISettings.getColor("gui.color.foreground"));

        var backgroundColorSetting = new ColorSetting("gavinsmod.settings.gui.color.background");
        backgroundColorSetting.setCallback(() -> {
            GavUISettings.add("gui.color.background", backgroundColorSetting.getColor());
        });
        backgroundColorSetting.setColor(GavUISettings.getColor("gui.color.background"));

        var categoryColorSetting = new ColorSetting("gavinsmod.settings.gui.color.category");
        categoryColorSetting.setCallback(() -> {
            GavUISettings.add("gui.color.category", categoryColorSetting.getColor());
        });
        categoryColorSetting.setColor(GavUISettings.getColor("gui.color.category"));

        var enabledColorSetting = new ColorSetting("gavinsmod.settings.gui.color.enabled");
        enabledColorSetting.setCallback(() -> {
            GavUISettings.add("gui.color.enabled", enabledColorSetting.getColor());
        });
        enabledColorSetting.setColor(GavUISettings.getColor("gui.color.enabled"));

        var frozenColorSetting = new ColorSetting("gavinsmod.settings.gui.color.frozen");
        frozenColorSetting.setCallback(() -> {
            GavUISettings.add("gui.color.frozen", frozenColorSetting.getColor());
        });
        frozenColorSetting.setColor(GavUISettings.getColor("gui.color.frozen"));

        SlideSetting guiAlpha = new SlideSetting("gavinsmod.settings.alpha");
        guiAlpha.setCallback(() -> GavUISettings.add("gui.alpha", guiAlpha.getValue()));
        guiAlpha.setValue(GavUISettings.getFloat("gui.alpha"));
        addSetting(guiAlpha);

        addSetting(backgroundColorSetting);
        addSetting(foregroundColorSetting);
        addSetting(categoryColorSetting);
        addSetting(enabledColorSetting);
        addSetting(frozenColorSetting);
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

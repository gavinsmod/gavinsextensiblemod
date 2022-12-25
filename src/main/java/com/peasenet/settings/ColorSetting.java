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

package com.peasenet.settings;

import com.peasenet.gavui.GuiCycle;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.main.Settings;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * A setting that allows the user to change a color value.
 */
public class ColorSetting extends Setting {

    /**
     * The cycle element that allows the user to change the color value.
     */
    private final GuiCycle guiCycle;

    /**
     * The color value of the setting.
     */
    private Color color;

    private boolean isGavUi;

    /**
     * Creates a new color setting.
     *
     * @param name           - The name of the setting (ie, "foregroundColor").
     * @param translationKey - The translation key of the setting.
     */
    public ColorSetting(String name, String translationKey) {
        super(name, translationKey);
        if (name.contains("gavui")) {
            name = name.replace("gavui.", "");
            isGavUi = true;
            color = GavUISettings.getColor(name);
        } else color = Colors.WHITE;
        guiCycle = new GuiCycle(90, 10, Text.translatable(translationKey), Colors.COLORS.length);
        guiCycle.setBackground(color);
        String finalName = name;
        guiCycle.setCallback(() -> {
            color = Colors.COLORS[guiCycle.getCurrentIndex()];
            guiCycle.setBackground(color);
            if (isGavUi) {
                GavUISettings.add(finalName, color);
                GavUISettings.save();
            } else {
                Settings.add(finalName, color);
                Settings.save();
            }
            onClick();
        });
        guiCycle.setCurrentIndex(Colors.getColorIndex(color));
    }

    public ColorSetting(String translationKey) {
        this("none", translationKey);
    }

    /**
     * Sets the color and color index to the given value.
     *
     * @param index - The color index.
     */
    public void setColorIndex(int index) {
        guiCycle.setCurrentIndex(index);
        guiCycle.setBackground(Colors.COLORS[index]);
    }

    @Override
    public GuiCycle getGui() {
        return guiCycle;
    }

    /**
     * Gets the current color value.
     *
     * @return The current color value.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the background color of this setting.
     *
     * @param color - The color of the background.
     */
    public void setColor(Color color) {
        this.color = color;
        guiCycle.setBackground(color);
        guiCycle.setCurrentIndex(Colors.getColorIndex(color));
    }
}

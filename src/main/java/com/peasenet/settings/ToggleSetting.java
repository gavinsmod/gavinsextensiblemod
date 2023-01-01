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

package com.peasenet.settings;

import com.peasenet.gavui.GuiToggle;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GavUISettings;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 12/31/2022
 * A setting that contains one of two finite states - on or off.
 */
public class ToggleSetting extends Setting {

    /**
     * The gui element that is used to display this toggle setting.
     */
    GuiToggle gui;

    /**
     * The current value of this toggle setting.
     */
    private boolean value;

    /**
     * Creates a new toggle setting.
     *
     * @param key - The translation key of this toggle setting.
     */
    public ToggleSetting(String key) {
        super(key);
        value = false;
        gui = new GuiToggle(new PointF(0, 0), 90, 10, Text.translatable(key));
        gui.setState(value);
        gui.setCallback(this::onClick);
        gui.hide();
    }

    public ToggleSetting(Text literal) {
        super("none");
        gui = new GuiToggle(new PointF(0, 0), 90, 10, literal);
        gui.setState(value);
        gui.setCallback(this::onClick);
        gui.hide();
    }

    /**
     * Get the current state of this setting.
     *
     * @return The current state of this setting.
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Sets the value of this toggle setting.
     *
     * @param value - The value to set this toggle setting to.
     */
    public void setValue(boolean value) {
        this.value = value;
        gui.setBackground(value ? GavUISettings.getColor("gui.color.enabled") : GavUISettings.getColor("gui.color.background"));
        gui.setState(value);
    }

    @Override
    public GuiToggle getGui() {
        return gui;
    }
}

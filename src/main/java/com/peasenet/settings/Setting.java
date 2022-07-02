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

import com.peasenet.gui.elements.Gui;
import com.peasenet.util.callbacks.SettingsCallback;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A class that represents a mod setting. This class should not be instantiated directly.
 */
public abstract class Setting {

    /**
     * The name of the setting.
     */
    private final String name;

    /**
     * The callback of the setting.
     */
    private SettingsCallback callback;

    /**
     * Creates a new setting.
     *
     * @param name - The name of the setting (ie, "foregroundColor").
     */
    public Setting(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the setting.
     *
     * @return The name of the setting.
     */
    public String getName() {
        return name;
    }

    /**
     * What to run when the setting is clicked. By specifying a callback, you can run code when the setting is clicked.
     */
    public void onClick() {
        if (callback != null) callback.callback();
    }

    /**
     * Gets the gui element for this setting.
     *
     * @return The gui element for this setting.
     */
    public abstract Gui getGui();

    /**
     * Sets the callback of the setting. This will be run when #onClick is called.
     *
     * @param callback - The callback method to run.
     */
    public void setCallback(SettingsCallback callback) {
        this.callback = callback;
    }

    /**
     * Sets the width of the GUI element.
     *
     * @param width - The wanted width.
     */
    public void setWidth(int width) {
        getGui().setWidth(width);
    }

    public void setTitle(Text text) {
        getGui().setTitle(text);
    }

}

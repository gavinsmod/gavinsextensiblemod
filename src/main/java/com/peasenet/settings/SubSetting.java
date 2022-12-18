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

import com.peasenet.gavui.GuiDropdown;
import com.peasenet.gavui.GuiScroll;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A setting that contains multiple sub settings within a dropdown element.
 */
public class SubSetting extends Setting {

    /**
     * The dropdown menu that contains the sub settings.
     */
    private final GuiScroll dropdown;

    /**
     * Creates a new subsetting element. You can call #add(Setting) to add subsettings to this element.
     *
     * @param width          - The width of the dropdown element.
     * @param height         - The height of the dropdown element.
     * @param translationKey - The translation key.
     */
    public SubSetting(int width, int height, String translationKey) {
        super("none",translationKey);
        dropdown = new GuiScroll(width, height, Text.translatable(translationKey));
        dropdown.setDirection(GuiDropdown.Direction.RIGHT);
        dropdown.hide();
    }

    /**
     * Adds a new subsetting to this element.
     *
     * @param setting - The setting to add.
     */
    public void add(Setting setting) {
        dropdown.addElement(setting.getGui());
        setting.getGui().hide();
    }

    @Override
    public GuiScroll getGui() {
        return dropdown;
    }
}

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
import com.peasenet.gui.elements.GuiClick;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/30/2022
 */
public class ClickSetting extends Setting {

    private final GuiClick gui;

    public ClickSetting(String name) {
        super(name);
        gui = new GuiClick(new PointD(0, 0), 100, 10, Text.literal(""));
        gui.setCallback(this::onClick);
    }

    public ClickSetting(String name, String translationKey) {
        super(name);
        gui = new GuiClick(new PointD(0, 0), 100, 10, Text.translatable(translationKey));
        gui.setCallback(this::onClick);
    }

    public ClickSetting(String name, Text text) {
        super(name);
        gui = new GuiClick(new PointD(0, 0), 100, 10, text);
        gui.setCallback(this::onClick);
    }

    @Override
    public Gui getGui() {
        return gui;
    }


}

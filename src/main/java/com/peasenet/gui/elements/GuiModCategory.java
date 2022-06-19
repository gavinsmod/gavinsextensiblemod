/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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

package com.peasenet.gui.elements;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.Settings;
import com.peasenet.mods.Type;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/18/2022
 * A gui element that contains children of GuiToggles for different mod types.
 */
public class GuiModCategory extends GuiDropdown {
    public GuiModCategory(PointD position, int width, int height, Text title, Type.Category category) {
        super(position, width, height, title);
        setCategory(category);
    }

    /**
     * Sets the category of the dropdown
     */
    public void setCategory(Type.Category category) {
        this.category = category;
        var mods = GavinsMod.getModsInCategory(category);
        if (mods == null)
            return;
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var x = getX();
            var y = getY2() + i * 10;
            var gui = new GuiToggle(new PointD(x, y + 2), (int) getWidth(), (int) getHeight(), Text.translatable(mod.getTranslationKey()));
            gui.setCallback(mod::toggle);
            addElement(gui);
        }
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        super.render(matrices, tr);
        var mods = GavinsMod.getModsInCategory(getCategory());
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var gui = buttons.get(i);
            if(gui instanceof GuiToggle)
                ((GuiToggle) gui).setState(mod.isActive());
            gui.setBackground(mod.isActive() ? Settings.EnabledColor : Settings.BackgroundColor);
        }
    }
}

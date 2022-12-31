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

package com.peasenet.gui.mod;

import com.peasenet.gavui.Gui;
import com.peasenet.gavui.GuiToggle;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A utility class used to get the gui elements that are needed for each gui category.
 */
public class ModGuiUtil {
    /**
     * Gets a list of toggleable gui elements from the given mod category.
     *
     * @param category - The mod category we want to get the toggleable elements for.
     * @param box      - The box that contains the gui.
     * @return A list of toggleable gui elements.
     */
    public static ArrayList<Gui> getGuiToggleFromCategory(Type.Category category, BoxF box) {
        var guis = new ArrayList<Gui>();
        var mods = GavinsMod.getModsInCategory(category);
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var x = box.getTopLeft().x();
            var y = box.getBottomRight().y() + i * 10;
            var gui = new GuiToggle(new PointF(x, y + 2), (int) box.getWidth(), (int) box.getHeight(), Text.translatable(mod.getTranslationKey()));
            gui.setCallback(mod::toggle);
            gui.setRenderCallback(() -> gui.setState(mod.isActive()));
            gui.hide();
            guis.add(gui);
        }
        return guis;
    }
}

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

package com.peasenet.gui.mod;

import com.peasenet.gavui.GuiScroll;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.mods.ModCategory;
import net.minecraft.text.Text;

/**
 * A class that allows for the creation of a GuiScroll element given a mod category.
 * @author GT3CH1
 * @version 07-17-2023
 */
public class GuiMod extends GuiScroll {

    /**
     * The position for the GuiScroll element.
     */
    private final PointF position;
    
    /**
     * The width of the GuiScroll element.
     */
    private final int width;
    
    /**
     * The height of the GuiScroll element.
     */
    private final int height;
    
    /**
     * The title of the GuiScroll element.
     */
    private final Text title;
    
    /**
     * The category of the mod(s).
     */
    private final ModCategory category;
    
    /**
     * The maximum number of children for the GuiScroll element to be shown on one page.
     */
    private final int maxChildren;

    /**
     * Creates a new GuiScroll element given a position, width, height, title, and category. The maximum number of children is set to 6.
     * @param position - The position for the GuiScroll element.
     * @param width - The width of the GuiScroll element.
     * @param height - The height of the GuiScroll element.
     * @param title - The title of the GuiScroll element.
     * @param category - The category of the mod(s).
     */
    GuiMod(PointF position, int width, int height, Text title, ModCategory category) {
        this(position, width, height, title, category, 6);
    }


    /**
     * Creates a new GuiScroll element given a position, width, height, title, category, and maximum number of children.
     * @param position - The position for the GuiScroll element.
     * @param width - The width of the GuiScroll element.
     * @param height - The height of the GuiScroll element.
     * @param title - The title of the GuiScroll element.
     * @param category - The category of the mod(s).
     * @param maxChildren - The maximum number of children for the GuiScroll element to be shown on one page.
     */
    GuiMod(PointF position, int width, int height, Text title, ModCategory category, int maxChildren) {
        super(position, width, height, title, maxChildren, ModGuiUtil.getGuiToggleFromCategory(
                category,
                new BoxF(position, width, height)
        ));
        this.position = position;
        this.width = width;
        this.height = height;
        this.title = title;
        this.category = category;
        this.maxChildren = maxChildren;

    }
}

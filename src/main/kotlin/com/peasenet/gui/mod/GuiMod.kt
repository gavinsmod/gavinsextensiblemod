/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.peasenet.gui.mod

import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.mod.ModGuiUtil.getGuiToggleFromCategory
import com.peasenet.mods.ModCategory
import net.minecraft.text.Text

/**
 * Creates a new GuiScroll element given a position, width, height, title, category, and maximum number of children.
 *
 * @param position    - The position for the GuiScroll element.
 * @param width       - The width of the GuiScroll element.
 * @param height      - The height of the GuiScroll element.
 * @param title       - The title of the GuiScroll element.
 * @param category    - The category of the mod(s).
 * @param maxChildren - The maximum number of children for the GuiScroll element to be shown on one page.
 *
 *
 * @author GT3CH1
 * @version 02-01-2025
 * @since 07-17-2023
 */
open class GuiMod(
    position: PointF,
    width: Int,
    height: Int,
    title: Text?,
    category: ModCategory?,
    maxChildren: Int = 6,
) : GuiScroll(
    GuiBuilder<GuiScroll>().setTopLeft(position).setWidth(width).setHeight(height).setTitle(title)
        .setMaxChildren(maxChildren)
        .setDraggable(false)
        .setChildren(
            getGuiToggleFromCategory(
                category, BoxF(position, width.toFloat(), 11f)
            )
        )
) {}

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
package com.peasenet.gui.mod

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.main.GavinsMod.Companion.getModsInCategory
import com.peasenet.mods.ModCategory

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A utility class used to get the gui elements that are needed for each gui category.
 */
object ModGuiUtil {
    /**
     * Gets a list of toggleable gui elements from the given mod category.
     *
     * @param category - The mod category we want to get the toggleable elements for.
     * @param box      - The box that contains the gui.
     * @return A list of toggleable gui elements.
     */
    @JvmStatic
    fun getGuiToggleFromCategory(category: ModCategory?, box: BoxF): ArrayList<Gui> {
        val guis = ArrayList<Gui>()
        val mods = getModsInCategory(category!!)
        for (i in mods.indices) {
            val mod = mods[i]
            val x = box.topLeft.x
            val y = box.bottomRight.y + i * 10
            val gui = GuiBuilder()
                .setTopLeft(PointF(x, y + 2))
                .setWidth(box.width)
                .setHeight(box.height)
                .setTitle(mod.translationKey)
                .setCallback(mod::toggle)
                .setHidden(true)
                .buildToggle()
            gui.setRenderCallback { gui.setState(mod.isActive) }

            guis.add(gui)
        }
        return guis
    }
}
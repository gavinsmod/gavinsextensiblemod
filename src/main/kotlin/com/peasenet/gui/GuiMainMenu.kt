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
package com.peasenet.gui

import com.peasenet.gavui.Gui
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsMod.Companion.setEnabled
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 03-02-2023
 * The main menu for the mod.
 */
class GuiMainMenu(guis: ArrayList<Gui>) : GuiElement(Text.literal("Gavin's Mod " + GavinsMod.VERSION)) {
    /**
     * Creates a new main menu with a list of guis to display.
     *
     * @param guis - The list of guis to display.
     */
    init {
        this.guis = guis
    }

    override fun close() {
        setEnabled("gui", false)
        super.close()
    }
}
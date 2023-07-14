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

import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.mods.ModCategory
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 03-02-2023
 * Creates a new gui for combat mods as a dropdown.
 */
class GuiCombat
/**
 * Creates a new combat dropdown.
 *
 * @param position - The position of the dropdown.
 * @param width    - The width of the dropdown.
 * @param height   - The height of the dropdown.
 * @param title    - The title of the dropdown.
 */
/**
 * Creates a new combat dropdown.
 */
@JvmOverloads constructor(
    position: PointF? = PointF(100f, 20f),
    width: Int = 75,
    height: Int = 10,
    title: Text? = Text.translatable("gavinsmod.gui.combat")
) : GuiScroll(
    position, width, height, title, 4, ModGuiUtil.getGuiToggleFromCategory(
        ModCategory.COMBAT,
        BoxF(position, width.toFloat(), height.toFloat())
    )
)
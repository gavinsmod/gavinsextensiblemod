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
package com.peasenet.settings

import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick

/**
 * A setting that can be clicked. This is purely dependent on the given callback.
 * 
 * @param builder - The [SettingBuilder] used to create this setting.
 * @author gt3ch1
 * @version 07-18-2023
 */
class ClickSetting(builder: SettingBuilder) : Setting() {
    /**
     * The gui used to display the setting.
     */
    override val gui: GuiClick

    /**
     * Creates a new click setting with the given name (?) and translation key.
     *
     */
    init {
        gui = GuiBuilder()
            .setWidth(builder.getWidth())
            .setHeight(builder.getHeight())
            .setTitle(builder.getTitle())
            .setCallback(builder.getCallback())
            .setHoverable(builder.isHoverable())
            .setBackgroundColor(builder.getColor())
            .setTransparency(builder.getTransparency())
            .setTranslationKey(builder.getTranslationKey())
            .setSymbol(builder.getSymbol())
            .setTopLeft(builder.getTopLeft())
            .buildClick()
    }
}
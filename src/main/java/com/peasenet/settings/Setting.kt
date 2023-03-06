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

import com.peasenet.gavui.Gui
import com.peasenet.util.callbacks.SettingsCallback
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A class that represents a mod setting. This class should not be instantiated directly.
 */
abstract class Setting
/**
 * Creates a new setting.
 *
 * @param translationKey - The translation key of this setting (ie, "gavinsmod.settings.xray")
 */(
    /**
     * The translation key for the name of this setting.
     */
    val translationKey: String
) {
    /**
     * The callback of the setting.
     */
    private var callback: SettingsCallback? = null
    /**
     * Gets the translation key for this setting.
     *
     * @return The translation key.
     */

    /**
     * What to run when the setting is clicked. By specifying a callback, you can run code when the setting is clicked.
     */
    fun onClick() {
        if (callback != null) callback!!.callback()
    }

    /**
     * Gets the gui element for this setting.
     *
     * @return The gui element for this setting.
     */
    open val gui: Gui? = null

    /**
     * Sets the callback of the setting. This will be run when #onClick is called.
     *
     * @param callback - The callback method to run.
     */
    fun setCallback(callback: SettingsCallback) {
        this.callback = callback
    }

    /**
     * Sets the width of the GUI element.
     *
     * @param width - The wanted width.
     */
    fun setWidth(width: Int) {
        gui?.width = width.toFloat()
    }

    /**
     * Sets the title of the gui for this setting.
     *
     * @param text - The title of the gui.
     */
    fun setTitle(text: Text?) {
        gui?.title = text
    }

    abstract fun setCallback(callback: () -> Unit)
}
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
package com.peasenet.util

import com.peasenet.mods.ModCategory
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

/**
 * Used to get and create keybindings for different mods.
 * @author GT3CH1
 * @version 03-02-2023
 */
object KeyBindUtils {

    /**
     * Attemps to get a keybinding for a mod.
     * @param translationKey The translation key for the keybinding.
     * @param modCategory The mod category for the keybinding.
     * @param keyBind The keybind for the keybinding. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
     * 
     * @returns a keybind
     */
    private fun getKeyBinding(
        translationKey: String,
        modCategory: ModCategory,
        keyBind: Int = GLFW.GLFW_KEY_UNKNOWN
    ): KeyBinding {
        return KeyBinding(
            translationKey,
            InputUtil.Type.KEYSYM,
            keyBind,
            modCategory.keybindCategory
        )
    }

    /**
     * Registers a keybind for a mod.
     * @param translationKey The translation key for the keybinding.
     * @param modCategory The mod category for the keybinding.
     * @param keyBind The keybind for the keybinding. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
     */
    fun registerModKeybind(
        translationKey: String,
        modCategory: ModCategory,
        keyBind: Int = GLFW.GLFW_KEY_UNKNOWN
    ): KeyBinding {
        return KeyBindingHelper.registerKeyBinding(getKeyBinding(translationKey, modCategory,keyBind))
    }
}
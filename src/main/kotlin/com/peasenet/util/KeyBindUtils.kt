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
package com.peasenet.util

import com.peasenet.mods.ModCategory
import com.peasenet.mods.Type
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

/**
 * @author gt3ch1
 * @version 03-02-2023
 * Used to get and create keybindings for different mods.
 */
object KeyBindUtils {
    /**
     * Gets the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    private fun getKeyBinding(type: Type): KeyBinding {
        return KeyBinding(
            type.translationKey,
            InputUtil.Type.KEYSYM,
            type.keyBinding,
            type.category
        )
    }

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
     * Registers the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    fun registerKeyBindForType(type: Type): KeyBinding {
        return try {
            KeyBindingHelper.registerKeyBinding(getKeyBinding(type))
        } catch (e: Exception) {
            if (type.keyBinding == GLFW.GLFW_KEY_UNKNOWN) {
                registerEmptyKeyBind(type)
            } else getKeyBinding(type)
        }
    }


    fun registerKeyBindForType(
        translationKey: String,
        modCategory: ModCategory,
        keyBind: Int = GLFW.GLFW_KEY_UNKNOWN
    ): KeyBinding {
        if (keyBind == GLFW.GLFW_KEY_UNKNOWN)
            return registerEmptyKeyBind(translationKey, modCategory.keybindCategory)
        return KeyBindingHelper.registerKeyBinding(getKeyBinding(translationKey, modCategory,keyBind))
    }

    /**
     * Registers and returns the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    fun registerEmptyKeyBind(type: Type): KeyBinding {
        return KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                type.translationKey,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                type.category
            )
        )
    }

    fun registerEmptyKeyBind(translationKey: String, keyBindCategory: String): KeyBinding {
        return KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                keyBindCategory
            )
        )
    }

}
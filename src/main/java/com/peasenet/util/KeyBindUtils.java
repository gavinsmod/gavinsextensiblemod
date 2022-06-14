/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.util;

import com.peasenet.mods.Type;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * @author gt3ch1
 * @version 6/14/2022
 * Used to get and create keybindings for different mods.
 */
public class KeyBindUtils {
    /**
     * Gets the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding getKeyBinding(Type type) {
        return new KeyBinding(
                type.getTranslationKey(),
                InputUtil.Type.KEYSYM,
                type.getKeyBinding(),
                type.getCategory()
        );
    }

    /**
     * Registers the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding registerKeyBindForType(Type type) {
        return KeyBindingHelper.registerKeyBinding(getKeyBinding(type));
    }

    /**
     * Registers and returns the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding registerEmptyKeyBind(Type type) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                type.getTranslationKey(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                type.getCategory()
        ));
    }
}

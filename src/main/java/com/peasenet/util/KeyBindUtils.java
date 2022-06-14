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

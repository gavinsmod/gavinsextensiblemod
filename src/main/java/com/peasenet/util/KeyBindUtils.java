package com.peasenet.util;

import com.peasenet.mods.Mods;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * @author gt3ch1
 * Used to get and create keybindings for different mods.
 */
public class KeyBindUtils {
    /**
     * Gets the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding getKeyBinding(Mods type) {
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
    public static KeyBinding registerKeyBindForType(Mods type) {
        return KeyBindingHelper.registerKeyBinding(getKeyBinding(type));
    }

    /**
     * Registers and returns the keybinding for the given mod type.
     *
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding reigsterEmptyKeyBind(Mods type) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                type.getTranslationKey(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                type.getCategory()
        ));
    }
}

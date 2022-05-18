package com.peasenet.util;

import com.peasenet.mods.ModType;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class UtilKeyBind {
    /**
     * Gets the keybinding for the given mod type.
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding getKeyBinding(ModType type) {
        return new KeyBinding(
                type.getTranslationKey(),
                InputUtil.Type.KEYSYM,
                type.getKeyBinding(),
                type.getCategory()
        );
    }

    /**
     * Registers the keybinding for the given mod type.
     * @param type The mod type.
     * @return The keybinding.
     */
    public static KeyBinding registerKeyBindForType(ModType type) {
        return KeyBindingHelper.registerKeyBinding(getKeyBinding(type));
    }
}

package com.peasenet.mods;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModFastMine extends Mod {
    public ModFastMine() {

        super(Mods.FAST_MINE, ModCategory.MOVEMENT, KeyBindingHelper.registerKeyBinding(
                new KeyBinding(Mods.FAST_MINE.getTranslationKey(),
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        Mods.FAST_MINE.getCategory())));
    }
}

package com.peasenet.mods;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModFullBright extends Mod {


    public ModFullBright() {
        super(ModType.FULL_BRIGHT, ModCategory.WORLD, KeyBindingHelper.registerKeyBinding(
                new KeyBinding(ModType.FULL_BRIGHT.getTranslationKey(),
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_B,
                        ModType.FULL_BRIGHT.getCategory())));
    }

    @Override
    public void activate() {
        isEnabled = true;
        getClient().worldRenderer.reload();
        onEnable();
    }

    @Override
    public void deactivate() {
        getClient().worldRenderer.reload();
        isEnabled = false;
        onDisable();
    }
}

package com.peasenet.mods;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public enum ModType {

    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", GLFW.GLFW_KEY_F),
    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", GLFW.GLFW_KEY_G),
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", GLFW.GLFW_KEY_X),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", GLFW.GLFW_KEY_B);
    private final String translationKey;
    private final String name;
    private final String category;
    private final int keyBinding;


    ModType(String name, String translationKey, String category, int glfwKeyF) {
        this.name = name;
        this.category = category;
        this.translationKey = translationKey;
        this.keyBinding = glfwKeyF;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getCategory() {
        return category;
    }

    public int getKeyBinding() {
        return keyBinding;
    }
}

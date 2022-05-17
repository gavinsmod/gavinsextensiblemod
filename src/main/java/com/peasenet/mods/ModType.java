package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

public enum ModType {

    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", GLFW.GLFW_KEY_F),
    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", GLFW.GLFW_KEY_G),
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", GLFW.GLFW_KEY_X),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", GLFW.GLFW_KEY_B);
    private final String translationKey;
    private String name;
    private String category;
    private int keyBinding;


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

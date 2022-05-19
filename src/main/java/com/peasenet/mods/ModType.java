package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

import static com.peasenet.mods.ModCategory.TICKABLE;
import static com.peasenet.mods.ModCategory.WORLD;

public enum ModType {

    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", GLFW.GLFW_KEY_F, TICKABLE),
    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", GLFW.GLFW_KEY_G, WORLD),
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", GLFW.GLFW_KEY_X, WORLD),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", GLFW.GLFW_KEY_B, WORLD),
    MOB_TRACER("Mob Tracer", "key.gavinsmod.mobtracer", "category.gavinsmod.test", GLFW.GLFW_KEY_M, WORLD),
    CHEST_FINDER("Chest Finder", "key.gavinsmod.chestfinder", "category.gavinsmod.test", GLFW.GLFW_KEY_K, WORLD);
    private final String translationKey;
    private final String name;
    private final String category;
    private final int keyBinding;
    private final ModCategory modCategory;


    ModType(String name, String translationKey, String category, int glfwKeyF, ModCategory modCategory) {
        this.name = name;
        this.category = category;
        this.translationKey = translationKey;
        this.keyBinding = glfwKeyF;
        this.modCategory = modCategory;
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

    public ModCategory getModCategory() {
        return modCategory;
    }
}

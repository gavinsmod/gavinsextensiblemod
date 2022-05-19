package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

import static com.peasenet.mods.ModCategory.TICKABLE;
import static com.peasenet.mods.ModCategory.RENDER;

public enum ModType {

    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", GLFW.GLFW_KEY_F, TICKABLE),
    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", GLFW.GLFW_KEY_G, RENDER),
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", GLFW.GLFW_KEY_X, RENDER),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", GLFW.GLFW_KEY_B, RENDER),
    CHEST_ESP("Chest Finder", "key.gavinsmod.chestesp", "category.gavinsmod.test", GLFW.GLFW_KEY_K, RENDER),
    CHEST_TRACER("Chest Tracer", "key.gavinsmod.chesttracer", "category.gavinsmod.test", GLFW.GLFW_KEY_UNKNOWN, RENDER),
    ENTITY_ITEM_TRACER("Item Tracer", "key.gavinsmod.entityitemtracer", "category.gavinsmod.test", GLFW.GLFW_KEY_UNKNOWN, RENDER),
    ENTITY_ITEM_ESP("Item ESP", "key.gavinsmod.entityitemesp", "category.gavinsmod.test", GLFW.GLFW_KEY_UNKNOWN, RENDER),
    MOB_ESP("Mob ESP", "key.gavinsmod.mobesp", "category.gavinsmod.test", GLFW.GLFW_KEY_UNKNOWN, RENDER),
    MOB_TRACER("Mob Tracer", "key.gavinsmod.mobtracer", "category.gavinsmod.test", GLFW.GLFW_KEY_UNKNOWN, RENDER);
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

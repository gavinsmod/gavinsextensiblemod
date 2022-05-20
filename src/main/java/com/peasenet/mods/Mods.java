package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

import static com.peasenet.mods.ModCategory.MOVEMENT;
import static com.peasenet.mods.ModCategory.RENDER;

/**
 * @author gt3ch1
 * An enum containing all the mods.
 */
public enum Mods {

    // MOVEMENT
    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", GLFW.GLFW_KEY_F, MOVEMENT),

    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", GLFW.GLFW_KEY_G, MOVEMENT),

    AUTO_JUMP("Auto Jump", "key.gavinsmod.autojump", "category.gavinsmod.test", MOVEMENT),

    CLIMB("Climb", "key.gavinsmod.climb", "category.gavinsmod.test", MOVEMENT),

    //RENDER
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", GLFW.GLFW_KEY_X, RENDER),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", GLFW.GLFW_KEY_B, RENDER),
    CHEST_ESP("Chest Finder", "key.gavinsmod.chestesp", "category.gavinsmod.test", GLFW.GLFW_KEY_K, RENDER),
    CHEST_TRACER("Chest Tracer", "key.gavinsmod.chesttracer", "category.gavinsmod.test", RENDER),
    ENTITY_ITEM_TRACER("Item Tracer", "key.gavinsmod.entityitemtracer", "category.gavinsmod.test", RENDER),
    ENTITY_ITEM_ESP("Item ESP", "key.gavinsmod.entityitemesp", "category.gavinsmod.test", RENDER),
    MOB_ESP("Mob ESP", "key.gavinsmod.mobesp", "category.gavinsmod.test", RENDER),
    MOB_TRACER("Mob Tracer", "key.gavinsmod.mobtracer", "category.gavinsmod.test", RENDER);

    private final String translationKey;
    private final String name;
    private final String category;
    private final int keyBinding;
    private final ModCategory modCategory;


    /**
     * A mod enum containing the name, translation key, category, keybinding, and the mod category.
     *
     * @param name           - The name of the mod.
     * @param translationKey - The translation key of the mod.
     * @param category       - The settings category of the mod.
     * @param key            - The keybinding of the mod.
     * @param modCategory    - The mod category of the mod.
     */
    Mods(String name, String translationKey, String category, int key, ModCategory modCategory) {
        this.name = name;
        this.category = category;
        this.translationKey = translationKey;
        this.keyBinding = key;
        this.modCategory = modCategory;
    }

    /**
     * Creates a new mod type with an empty keybinding.
     *
     * @param name           - The name of the mod.
     * @param translationKey - The translation key of the mod.
     * @param category       - The settings category of the mod.
     * @param modCategory    - The mod category of the mod.
     */
    Mods(String name, String translationKey, String category, ModCategory modCategory) {
        this(name, translationKey, category, GLFW.GLFW_KEY_UNKNOWN, modCategory);
    }

    /**
     * Gets the name of the mod.
     *
     * @return The name of the mod.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the translation key of the mod.
     *
     * @return The translation key of the mod.
     */
    public String getTranslationKey() {
        return translationKey;
    }

    /**
     * Gets the settings category of the mod.
     *
     * @return The settings category of the mod.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the keybinding of the mod.
     *
     * @return The keybinding of the mod.
     */
    public int getKeyBinding() {
        return keyBinding;
    }

    /**
     * Gets the mod category of the mod.
     *
     * @return The mod category of the mod.
     */
    public ModCategory getModCategory() {
        return modCategory;
    }
}

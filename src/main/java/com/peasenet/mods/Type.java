package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

import static com.peasenet.mods.Type.Category.*;

/**
 * @author gt3ch1
 * An enum containing all the mods.
 */
public enum Type {

    // MOVEMENT
    FLY("Fly", "key.gavinsmod.fly", "category.gavinsmod.test", MOVEMENT, "fly"),
    FAST_MINE("Fast Mine", "key.gavinsmod.fastmine", "category.gavinsmod.test", MOVEMENT, "fastmine"),
    FAST_PLACE("Fast Place", "key.gavinsmod.fastplace", "category.gavinsmod.test", MOVEMENT, "fastplace"),
    AUTO_JUMP("Auto Jump", "key.gavinsmod.autojump", "category.gavinsmod.test", MOVEMENT, "autojump"),
    CLIMB("Climb", "key.gavinsmod.climb", "category.gavinsmod.test", MOVEMENT, "climb"),
    NO_CLIP("No Clip", "key.gavinsmod.noclip", "category.gavinsmod.test", MOVEMENT, "noclip"),
    NO_FALL("No Fall", "key.gavinsmod.nofall", "category.gavinsmod.test", MOVEMENT, "nofall"),
    //COMBAT
    KILL_AURA("Kill Aura", "key.gavinsmod.killaura", "category.gavinsmod.test", COMBAT, "killaura"),
    AUTO_CRIT("Auto Crit", "key.gavinsmod.autocrit", "category.gavinsmod.test", COMBAT, "autocrit"),
    //RENDER
    XRAY("Xray", "key.gavinsmod.xray", "category.gavinsmod.test", RENDER, "xray"),
    FULL_BRIGHT("Full Bright", "key.gavinsmod.fullbright", "category.gavinsmod.test", RENDER, "fullbright"),
    CHEST_ESP("Chest ESP", "key.gavinsmod.chestesp", "category.gavinsmod.test", RENDER, "chestesp"),
    CHEST_TRACER("Chest Tracer", "key.gavinsmod.chesttracer", "category.gavinsmod.test", RENDER, "chesttracer"),
    ENTITY_ITEM_TRACER("Item Tracer", "key.gavinsmod.entityitemtracer", "category.gavinsmod.test", RENDER, "itemtracer"),
    ENTITY_ITEM_ESP("Item ESP", "key.gavinsmod.entityitemesp", "category.gavinsmod.test", RENDER, "itemesp"),
    ENTITY_PLAYER_ESP("Player ESP", "key.gavinsmod.entityplayeresp", "category.gavinsmod.test", RENDER, "playeresp"),
    ENTITY_PLAYER_TRACER("Player Tracer", "key.gavinsmod.entityplayertracer", "category.gavinsmod.test", RENDER, "playertracer"),
    MOB_ESP("Mob ESP", "key.gavinsmod.mobesp", "category.gavinsmod.test", RENDER, "mobesp"),
    MOB_TRACER("Mob Tracer", "key.gavinsmod.mobtracer", "category.gavinsmod.test", RENDER, "mobtracer"),
    ANTI_HURT("Anti Hurt", "key.gavinsmod.antihurt", "category.gavinsmod.test", RENDER, "antihurt"),
    MOD_GUI("GUI", "key.gavinsmod.gui", "category.gavinsmod.test", GLFW.GLFW_KEY_I, GUI, "gui"),
    ANTI_PUMPKIN("Anti Pumpkin", "key.gavinsmod.antipumpkin", "category.gavinsmod.test", RENDER, "antipumpkin"),
    MOD_GUI_TEXT_OVERLAY("GUI Text Overlay", "key.gavinsmod.guitextoverlay", "category.gavinsmod.test", GUI, "guitextoverlay");


    private final String translationKey;
    private final String name;
    private final String category;
    private final String chatCommand;
    private final int keyBinding;
    private final Category modCategory;


    /**
     * A mod enum containing the name, translation key, category, keybinding, and the mod category.
     *
     * @param name           - The name of the mod.
     * @param translationKey - The translation key of the mod.
     * @param category       - The settings category of the mod.
     * @param key            - The keybinding of the mod.
     * @param modCategory    - The mod category of the mod.
     */
    Type(String name, String translationKey, String category, int key, Category modCategory, String chatCommand) {
        this.name = name;
        this.category = category;
        this.translationKey = translationKey;
        this.keyBinding = key;
        this.modCategory = modCategory;
        this.chatCommand = chatCommand;
    }

    /**
     * Creates a new mod type with an empty keybinding.
     *
     * @param name           - The name of the mod.
     * @param translationKey - The translation key of the mod.
     * @param category       - The settings category of the mod.
     * @param modCategory    - The mod category of the mod.
     */
    Type(String name, String translationKey, String category, Category modCategory, String chatCommand) {
        this(name, translationKey, category, GLFW.GLFW_KEY_UNKNOWN, modCategory, chatCommand);
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
    public Category getModCategory() {
        return modCategory;
    }

    /**
     * Gets the chat command of this enum.
     *
     * @return The chat command of this enum.
     */
    public String getChatCommand() {
        return chatCommand;
    }

    /**
     * @author gt3ch1
     * @version 5/18/2022
     * A list of different mod categories.
     */
    public enum Category {
        MOVEMENT("key.gavinsmod.gui.movement"),
        RENDER("key.gavinsmod.gui.render"),
        COMBAT("key.gavinsmod.gui.combat"),
        GUI("key.gavinsmod.gui.gui");

        public final String translationKey;

        Category(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }
}

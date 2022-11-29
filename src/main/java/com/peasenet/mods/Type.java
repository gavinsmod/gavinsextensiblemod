/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.mods;

import org.lwjgl.glfw.GLFW;

import static com.peasenet.mods.Type.Category.*;

/**
 * @author gt3ch1
 * @version 6/18/2022
 * An enum containing all the mods.
 */
public enum Type {

    // MOVEMENT
    FLY("Fly", "gavinsmod.mod.movement.fly", "gavinsmod.keybinds.movement", MOVEMENT, "fly"),
    FAST_MINE("Fast Mine", "gavinsmod.mod.movement.fastmine", "gavinsmod.keybinds.movement", MOVEMENT, "fastmine"),
    FAST_PLACE("Fast Place", "gavinsmod.mod.movement.fastplace", "gavinsmod.keybinds.movement", MOVEMENT, "fastplace"),
    AUTO_JUMP("Auto Jump", "gavinsmod.mod.movement.autojump", "gavinsmod.keybinds.movement", MOVEMENT, "autojump"),
    ANTI_TRAMPLE("Anti Trample", "gavinsmod.mod.movement.antitrample", "gavinsmod.keybinds.movement", MOVEMENT, "antitrample"),
    CLIMB("Climb", "gavinsmod.mod.movement.climb", "gavinsmod.keybinds.movement", MOVEMENT, "climb"),
    NO_CLIP("No Clip", "gavinsmod.mod.movement.noclip", "gavinsmod.keybinds.movement", MOVEMENT, "noclip"),
    NO_FALL("No Fall", "gavinsmod.mod.movement.nofall", "gavinsmod.keybinds.movement", MOVEMENT, "nofall"),
    DOLPHIN("Dolphin", "gavinsmod.mod.movement.dolphin", "gavinsmod.keybinds.movement", MOVEMENT, "dolphin"),

    //COMBAT
    KILL_AURA("Kill Aura", "gavinsmod.mod.combat.killaura", "gavinsmod.keybinds.combat", COMBAT, "killaura"),
    AUTO_CRIT("Auto Crit", "gavinsmod.mod.combat.autocrit", "gavinsmod.keybinds.combat", COMBAT, "autocrit"),
    AUTO_ATTACK("Auto Attack", "gavinsmod.mod.combat.autoattack", "gavinsmod.keybinds.combat", COMBAT, "autoattack"),
    //RENDER
    XRAY("Xray", "gavinsmod.mod.render.xray", "gavinsmod.keybinds.render", RENDER, "xray"),
    FULL_BRIGHT("Full Bright", "gavinsmod.mod.render.fullbright", "gavinsmod.keybinds.render", RENDER, "fullbright"),
    ANTI_HURT("Anti Hurt", "gavinsmod.mod.render.antihurt", "gavinsmod.keybinds.render", RENDER, "antihurt"),
    ANTI_PUMPKIN("Anti Pumpkin", "gavinsmod.mod.render.antipumpkin", "gavinsmod.keybinds.render", RENDER, "antipumpkin"),
    NO_RAIN("No Rain", "gavinsmod.mod.render.norain", "gavinsmod.keybinds.render", RENDER, "norain"),
    MOD_HPTAG("HP Tags", "gavinsmod.mod.render.hptags", "gavinsmod.keybinds.render", RENDER, "hptags"),
    WAYPOINT("Waypoint", "gavinsmod.mod.render.waypoints", "gavinsmod.keybinds.render", RENDER, "waypoints"),

    // ESP
    CHEST_ESP("Chest ESP", "gavinsmod.mod.esp.chest", "gavinsmod.keybinds.esp", ESPS, "chestesp"),
    ENTITY_ITEM_ESP("Item ESP", "gavinsmod.mod.esp.item", "gavinsmod.keybinds.esp", ESPS, "itemesp"),
    ENTITY_PLAYER_ESP("Player ESP", "gavinsmod.mod.esp.player", "gavinsmod.keybinds.esp", ESPS, "playeresp"),
    MOB_ESP("Mob ESP", "gavinsmod.mod.esp.mob", "gavinsmod.keybinds.esp", ESPS, "mobesp"),

    // TRACER
    CHEST_TRACER("Chest Tracer", "gavinsmod.mod.tracer.chest", "gavinsmod.keybinds.tracers", TRACERS, "chesttracer"),
    ENTITY_ITEM_TRACER("Item Tracer", "gavinsmod.mod.tracer.item", "gavinsmod.keybinds.tracers", TRACERS, "itemtracer"),
    ENTITY_PLAYER_TRACER("Player Tracer", "gavinsmod.mod.tracer.player", "gavinsmod.keybinds.tracers", TRACERS, "playertracer"),
    MOB_TRACER("Mob Tracer", "gavinsmod.mod.tracer.mob", "gavinsmod.keybinds.tracers", TRACERS, "mobtracer"),

    // MISC
    MOD_FPS_COUNTER("FPS Counter", "gavinsmod.mod.misc.fpscounter", "gavinsmod.keybinds.misc", MISC, "fpscounter"),
    RADAR("Radar", "gavinsmod.mod.misc.radar", "gavinsmod.keybinds.misc", MISC, "radar"),
    MOD_GUI_TEXT_OVERLAY("GUI Text Overlay", "gavinsmod.mod.misc.textoverlay", "gavinsmod.keybinds.misc", MISC, "textoverlay"),

    // GUI (translation key not needed)
    MOD_GUI("GUI", "gavinsmod.gui", "gavinsmod.keybinds.gui", GLFW.GLFW_KEY_I, GUI, "gui"),
    SETTINGS("Settings", "gavinsmod.gui.settings", "gavinsmod.keybinds.gui", GLFW.GLFW_KEY_O, GUI, "settings");


    /**
     * The translation key for the name of the mod.
     */
    private final String translationKey;

    /**
     * The literal name of the mod.
     */
    private final String name;

    /**
     * The keybinding category of the mod.
     */
    private final String keybindingCategory;

    /**
     * The chat command for the mod.
     */
    private final String chatCommand;

    /**
     * The keybinding for the mod.
     */
    private final int keyBinding;

    /**
     * The category that the mod belongs to.
     */
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
        keybindingCategory = category;
        this.translationKey = translationKey;
        keyBinding = key;
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
        return keybindingCategory;
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
        MOVEMENT("gavinsmod.mod.gui.movement"),
        RENDER("gavinsmod.mod.gui.render"),
        COMBAT("gavinsmod.mod.gui.combat"),
        MISC("gavinsmod.mod.gui.misc"),
        ESPS("gavinsmod.mod.gui.esps"),
        TRACERS("gavinsmod.mod.gui.tracers"),
        GUI("gavinsmod.mod.gui.gui"),
        NONE("none");

        /**
         * The translation key for the category.
         */
        public final String translationKey;

        /**
         * A mod category that contains a translation key. This is used to separate the mods within the guis.
         *
         * @param translationKey - The translation key for the category.
         */
        Category(String translationKey) {
            this.translationKey = translationKey;
        }
    }
}

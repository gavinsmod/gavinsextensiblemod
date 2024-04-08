/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
package com.peasenet.mods

import org.lwjgl.glfw.GLFW

/**
 * @author gt3ch1
 * @version 03-02-2023
 * An enum containing all the mods.
 */
@Deprecated("You should not be using this class!", ReplaceWith(""),DeprecationLevel.ERROR)
enum class Type
/**
 * A mod enum containing the name, translation key, category, keybinding, and the mod category.
 *
 * @param modName           - The name of the mod.
 * @param translationKey - The translation key of the mod.
 * @param category       - The settings category of the mod.
 * @param key            - The keybinding of the mod.
 * @param modCategory    - The mod category of the mod.
 */(
    @JvmField val modName: String,
    /**
     * The translation key for the name of the mod.
     */
    @JvmField val translationKey: String,
    /**
     * The keybinding category of the mod.
     */
    val category: String,
    /**
     * The keybinding for the mod.
     */
    @JvmField val keyBinding: Int,
    /**
     * The category that the mod belongs to.
     */
    @JvmField val modCategory: Category,
    /**
     * The chat command for the mod.
     */
    @JvmField val chatCommand: String
) {
    // MOVEMENT
    FLY(
        "Fly",
        "gavinsmod.mod.movement.fly",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "fly"
    ),
    FAST_MINE(
        "Fast Mine",
        "gavinsmod.mod.movement.fastmine",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "fastmine"
    ),
    FAST_PLACE(
        "Fast Place",
        "gavinsmod.mod.movement.fastplace",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "fastplace"
    ),
    AUTO_JUMP(
        "Auto Jump",
        "gavinsmod.mod.movement.autojump",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "autojump"
    ),
    ANTI_TRAMPLE(
        "Anti Trample",
        "gavinsmod.mod.movement.antitrample",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "antitrample"
    ),
    CLIMB(
        "Climb",
        "gavinsmod.mod.movement.climb",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "climb"
    ),
    NO_CLIP(
        "No Clip",
        "gavinsmod.mod.movement.noclip",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "noclip"
    ),
    NO_FALL(
        "No Fall",
        "gavinsmod.mod.movement.nofall",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "nofall"
    ),
    DOLPHIN(
        "Dolphin",
        "gavinsmod.mod.movement.dolphin",
        "gavinsmod.keybinds.movement",
        Category.MOVEMENT,
        "dolphin"
    ),
    SPEED("Speed", "gavinsmod.mod.movement.speed", "gavinsmod.keybinds.movement", Category.MOVEMENT, "speed"),  //COMBAT
    KILL_AURA(
        "Kill Aura",
        "gavinsmod.mod.combat.killaura",
        "gavinsmod.keybinds.combat",
        Category.COMBAT,
        "killaura"
    ),
    AUTO_CRIT(
        "Auto Crit",
        "gavinsmod.mod.combat.autocrit",
        "gavinsmod.keybinds.combat",
        Category.COMBAT,
        "autocrit"
    ),
    AUTO_ATTACK(
        "Auto Attack",
        "gavinsmod.mod.combat.autoattack",
        "gavinsmod.keybinds.combat",
        Category.COMBAT,
        "autoattack"
    ),  //RENDER
    XRAY(
        "Xray",
        "gavinsmod.mod.render.xray",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "xray"
    ),
    FULL_BRIGHT(
        "Full Bright",
        "gavinsmod.mod.render.fullbright",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "fullbright"
    ),
    ANTI_HURT(
        "Anti Hurt",
        "gavinsmod.mod.render.antihurt",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "antihurt"
    ),
    NO_FIRE(
        "No Fire",
        "gavinsmod.mod.render.nofire",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "nofire"
    ),
    NO_OVERLAY(
        "No Overlay",
        "gavinsmod.mod.render.nooverlay",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "nooverlay"
    ),
    ANTI_PUMPKIN(
        "Anti Pumpkin",
        "gavinsmod.mod.render.antipumpkin",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "antipumpkin"
    ),
    NO_NAUSEA(
        "No Nausea",
        "gavinsmod.mod.render.nonausea",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "nonausea"
    ),
    NO_RAIN(
        "No Rain",
        "gavinsmod.mod.render.norain",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "norain"
    ),
    MOD_HPTAG(
        "HP Tags",
        "gavinsmod.mod.render.hptags",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "hptags"
    ),
    WAYPOINT(
        "Waypoint",
        "gavinsmod.mod.render.waypoints",
        "gavinsmod.keybinds.render",
        Category.WAYPOINTS,
        "waypoints"
    ),
    RADAR(
        "Radar",
        "gavinsmod.mod.render.radar",
        "gavinsmod.keybinds.misc",
        Category.RENDER,
        "radar"
    ),
    BARRIER_DETECT(
        "Barrier Detect",
        "gavinsmod.mod.render.barrierdetect",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "barrierdetect"
    ),
    NO_VIGNETTE(
        "No Vignette",
        "gavinsmod.mod.render.novignette",
        "gavinsmod.keybinds.render",
        Category.RENDER,
        "novignette"
    ),  // ESP
    CHEST_ESP(
        "Chest ESP",
        "gavinsmod.mod.esp.chest",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "chestesp"
    ),
    ENTITY_ITEM_ESP(
        "Item ESP",
        "gavinsmod.mod.esp.item",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "itemesp"
    ),
    ENTITY_PLAYER_ESP(
        "Player ESP",
        "gavinsmod.mod.esp.player",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "playeresp"
    ),
    MOB_ESP(
        "Mob ESP",
        "gavinsmod.mod.esp.mob",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "mobesp"
    ),
    BEEHIVE_ESP(
        "Beehive ESP",
        "gavinsmod.mod.esp.beehive",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "beehiveesp"
    ),
    FURNACE_ESP(
        "Furnace ESP",
        "gavinsmod.mod.esp.furnace",
        "gavinsmod.keybinds.esp",
        Category.ESPS,
        "furnaceesp"
    ),  // TRACER
    CHEST_TRACER(
        "Chest Tracer",
        "gavinsmod.mod.tracer.chest",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "chesttracer"
    ),
    ENTITY_ITEM_TRACER(
        "Item Tracer",
        "gavinsmod.mod.tracer.item",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "itemtracer"
    ),
    ENTITY_PLAYER_TRACER(
        "Player Tracer",
        "gavinsmod.mod.tracer.player",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "playertracer"
    ),
    MOB_TRACER(
        "Mob Tracer",
        "gavinsmod.mod.tracer.mob",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "mobtracer"
    ),
    BEEHIVE_TRACER(
        "Beehive Tracer",
        "gavinsmod.mod.tracer.beehive",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "beehivetracer"
    ),
    FURNACE_TRACER(
        "Furnace Tracer",
        "gavinsmod.mod.tracer.furnace",
        "gavinsmod.keybinds.tracers",
        Category.TRACERS,
        "furnacetracer"
    ),  // MISC
    MOD_FPS_COUNTER(
        "FPS Counter",
        "gavinsmod.mod.misc.fpscounter",
        "gavinsmod.keybinds.misc",
        Category.MISC,
        "fpscounter"
    ),
    MOD_GUI_TEXT_OVERLAY(
        "GUI Text Overlay",
        "gavinsmod.mod.misc.textoverlay",
        "gavinsmod.keybinds.misc",
        Category.MISC,
        "textoverlay"
    ),
    FREECAM(
        "Freecam",
        "gavinsmod.mod.misc.freecam",
        "gavinsmod.keybinds.misc",
        Category.MISC,
        "freecam"
    ),  // GUI (translation key not needed)
    MOD_GUI("GUI", "gavinsmod.gui", "gavinsmod.keybinds.gui", GLFW.GLFW_KEY_I, Category.GUI, "gui"),
    SETTINGS("Settings", "gavinsmod.gui.settings", "gavinsmod.keybinds.gui", GLFW.GLFW_KEY_O, Category.GUI, "settings");
    /**
     * Creates a new mod type with an empty keybinding.
     *
     * @param name           - The name of the mod.
     * @param translationKey - The translation key of the mod.
     * @param category       - The settings category of the mod.
     * @param modCategory    - The mod category of the mod.
     */
    constructor(
        name: String,
        translationKey: String,
        category: String,
        modCategory: Category,
        chatCommand: String
    ) : this(name, translationKey, category, GLFW.GLFW_KEY_UNKNOWN, modCategory, chatCommand)

    /**
     * @author gt3ch1
     * @version 03-02-2023
     * A list of different mod categories.
     */
    enum class Category
    /**
     * A mod category that contains a translation key. This is used to separate the mods within the guis.
     *
     * @param translationKey - The translation key for the category.
     */(
        /**
         * The translation key for the category.
         */
        @JvmField val translationKey: String
    ) {
        MOVEMENT("gavinsmod.gui.movement"), RENDER("gavinsmod.gui.render"),
        COMBAT("gavinsmod.gui.combat"),
        MISC("gavinsmod.gui.misc"),
        ESPS("gavinsmod.gui.esps"),
        WAYPOINTS("gavinsmod.mod.render.waypoints"),
        TRACERS("gavinsmod.gui.tracers"),
        GUI("gavinsmod.gui.gui"),
        NONE("none")
    }
}
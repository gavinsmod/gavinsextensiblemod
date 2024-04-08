/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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

/**
 * A mod category that contains a translation key. This is used to separate the mods within the guis.
 *
 * @param translationKey - The translation key for the category.
 * @param keybindCategory - The keybind category for the category.
 * @author GT3CH1
 */
enum class ModCategory
(
    @JvmField val translationKey: String,
    @JvmField val keybindCategory: String
) {
    MOVEMENT("gavinsmod.gui.movement", KeyBindCategory.MOVEMENT.category),
    RENDER("gavinsmod.gui.render", KeyBindCategory.RENDER.category),
    COMBAT("gavinsmod.gui.combat", KeyBindCategory.COMBAT.category),
    MISC("gavinsmod.gui.misc", KeyBindCategory.MISC.category),
    ESP("gavinsmod.gui.esps", KeyBindCategory.ESP.category),
    WAYPOINTS("gavinsmod.mod.render.waypoints", "gavinsmod.keybinds.waypoints"),
    TRACERS("gavinsmod.gui.tracers", KeyBindCategory.TRACERS.category),
    GUI("gavinsmod.gui.gui", KeyBindCategory.GUI.category),
    NONE("none", "none")
}
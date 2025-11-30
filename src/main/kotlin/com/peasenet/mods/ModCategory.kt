/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.mods

import net.minecraft.client.KeyMapping

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
    @JvmField val keybindCategory: KeyMapping.Category,
) {
    MOVEMENT("gavinsmod.gui.movement", KeyBindCategory.MOVEMENT),
    RENDER("gavinsmod.gui.render", KeyBindCategory.RENDER),
    COMBAT("gavinsmod.gui.combat", KeyBindCategory.COMBAT),
    MISC("gavinsmod.gui.misc", KeyBindCategory.MISC),
    ESP("gavinsmod.gui.esps", KeyBindCategory.ESP),
    WAYPOINTS("gavinsmod.mod.render.waypoints", KeyBindCategory.WAYPOINTS),
    TRACERS("gavinsmod.gui.tracers", KeyBindCategory.TRACERS),
    GUI("gavinsmod.gui.gui", KeyBindCategory.GUI),
    NONE("none", KeyBindCategory.NONE)
}
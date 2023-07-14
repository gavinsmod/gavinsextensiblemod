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

import com.peasenet.gavui.Gui
import net.minecraft.client.world.ClientWorld

/**
 * @author gt3ch1
 * @version 03-02-2023
 * The interface of the base mod class.
 */
interface IMod {
    /**
     * Runs on enabling of mod.
     */
    fun onEnable()

    /**
     * Runs on disabling of mod.
     */
    fun onDisable()

    /**
     * Runs on every tick.
     */
    fun onTick()

    /**
     * Checks the keybinding of the mod.
     */
    fun checkKeybinding()

    /**
     * Whether the mod is enabled.
     *
     * @return True if the mod is enabled.
     */
    val isActive: Boolean

    /**
     * Enables the mod.
     */
    fun activate()

    /**
     * Disables the mod.
     */
    fun deactivate()

    /**
     * Toggles the mod.
     */
    fun toggle()

    /**
     * Get the mod category
     *
     * @return The mod category
     */
    val modCategory: ModCategory?

    /**
     * Reloads this mod by calling #deactivate and #activate.
     */
    fun reload()

    /**
     * Gets the translation key for the mod.
     *
     * @return The translation key for the mod.
     */
    val translationKey: String?

    /**
     * Gets the name of the mod.
     *
     * @return The name of the mod.
     */
    val name: String?

    /**
     * Gets the chat command of the mod.
     *
     * @return The chat command of the mod.
     */
    val chatCommand: String?

    /**
     * Whether the mod is in a deactivation state (where the mod is disabled, but there still needs work to be done).
     *
     * @return True if the mod is in a deactivation state.
     */
    val isDeactivating: Boolean

    /**
     * Whether the mod has settings.
     *
     * @return True if the mod has settings.
     */
    fun hasSettings(): Boolean

    /**
     * Gets the settings of the mod.
     *
     * @return The settings of the mod.
     */
    val settings: ArrayList<Gui>?

    /**
     * Reloads the settings of the mod.
     */
    fun reloadSettings()

    /**
     * The world that the player is in.
     *
     * @return The world that the player is in.
     */
    val world: ClientWorld?

}
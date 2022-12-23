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

import com.peasenet.settings.Setting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * The interface of the base mod class.
 */
public interface IMod {

    /**
     * Runs on enabling of mod.
     */
    void onEnable();

    /**
     * Runs on disabling of mod.
     */
    void onDisable();

    /**
     * Runs on every tick.
     */
    void onTick();

    /**
     * Checks the keybinding of the mod.
     */
    void checkKeybinding();

    /**
     * Whether the mod is enabled.
     *
     * @return True if the mod is enabled.
     */
    boolean isActive();

    /**
     * Enables the mod.
     */
    void activate();

    /**
     * Disables the mod.
     */
    void deactivate();

    /**
     * Toggles the mod.
     */
    void toggle();

    /**
     * Get the mod category
     *
     * @return The mod category
     */
    Type.Category getCategory();

    /**
     * Reloads this mod by calling #deactivate and #activate.
     */
    void reload();

    /**
     * Gets the translation key for the mod.
     *
     * @return The translation key for the mod.
     */
    String getTranslationKey();

    /**
     * Gets the name of the mod.
     *
     * @return The name of the mod.
     */
    String getName();

    /**
     * Gets the chat command of the mod.
     *
     * @return The chat command of the mod.
     */
    String getChatCommand();

    /**
     * Gets the type of the mod.
     *
     * @return The type of the mod.
     */
    Type getType();

    /**
     * Gets the player.
     *
     * @return The client player.
     */
    ClientPlayerEntity getPlayer();

    /**
     * Called when attacking another entity.
     *
     * @param target - The target entity.
     */
    void onAttack(Entity target);

    /**
     * Whether the mod is in a deactivation state (where the mod is disabled, but there still needs work to be done).
     *
     * @return True if the mod is in a deactivation state.
     */
    boolean isDeactivating();

    /**
     * Whether the mod has settings.
     *
     * @return True if the mod has settings.
     */
    boolean hasSettings();

    /**
     * Gets the settings of the mod.
     *
     * @return The settings of the mod.
     */
    ArrayList<Setting> getSettings();

    /**
     * Adds a setting to the mod.
     *
     * @param setting - The setting to add.
     */
    void addSetting(Setting setting);

    /**
     * Reloads the settings of the mod.
     */
    void reloadSettings();

    /**
     * The world that the player is in.
     *
     * @return The world that the player is in.
     */
    ClientWorld getWorld();
}

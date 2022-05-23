package com.peasenet.mods;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

/**
 * @author gt3ch1
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
     * Hook for rendering things after the entities in the world are rendered.
     * @param context The render context.
     */
    void afterEntities(WorldRenderContext context);

    /**
     * Toggles the mod.
     */
    void toggle();

    /**
     * Get the mod category
     * @return The mod category
     */
    Mods.Category getCategory();

    /**
     * Gets the translation key for the mod.
     * @return The translation key for the mod.
     */
    String getTranslationKey();

    /**
     * Gets the name of the mod.
     * @return
     */
    String getName();
}

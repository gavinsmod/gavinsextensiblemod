package com.peasenet.mods;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

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
     * Whether the mod is enabled.
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
     * Renders after entities.
     *
     * @param context
     */
    void afterEntities(WorldRenderContext context);

    /**
     * Get the mod category
     *
     * @return
     */
    ModCategory getCategory();
}

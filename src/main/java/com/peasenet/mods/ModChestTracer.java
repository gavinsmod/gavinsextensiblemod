package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the player to see tracers towards chests.
 */
public class ModChestTracer extends Mod {
    public ModChestTracer() {
        super(Mods.CHEST_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.CHEST_TRACER));
    }
}

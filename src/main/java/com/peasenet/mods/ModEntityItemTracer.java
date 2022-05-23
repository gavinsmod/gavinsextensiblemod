package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the player to see tracers towards items.
 */
public class ModEntityItemTracer extends Mod {
    public ModEntityItemTracer() {
        super(Mods.ENTITY_ITEM_TRACER, Mods.Category.RENDER, KeyBindUtils.registerKeyBindForType(Mods.ENTITY_ITEM_TRACER));
    }
}

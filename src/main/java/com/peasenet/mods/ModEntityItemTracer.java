package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the player to see tracers towards items.
 */
public class ModEntityItemTracer extends Mod {
    public ModEntityItemTracer() {
        super(Type.ENTITY_ITEM_TRACER, Type.Category.RENDER, KeyBindUtils.registerKeyBindForType(Type.ENTITY_ITEM_TRACER));
    }
}

package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModEntityItemTracer extends Mod {
    public ModEntityItemTracer() {
        super(ModType.ENTITY_ITEM_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.ENTITY_ITEM_TRACER));
    }
}

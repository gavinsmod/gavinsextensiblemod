package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModEntityItemTracer extends Mod {
    public ModEntityItemTracer() {
        super(Mods.ENTITY_ITEM_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.ENTITY_ITEM_TRACER));
    }
}

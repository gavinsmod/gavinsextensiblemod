package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModChestTracer extends Mod {
    public ModChestTracer() {
        super(Mods.CHEST_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.CHEST_TRACER));
    }
}

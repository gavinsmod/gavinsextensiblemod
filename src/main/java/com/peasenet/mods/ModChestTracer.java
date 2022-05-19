package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModChestTracer extends Mod {
    public ModChestTracer() {
        super(ModType.CHEST_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.CHEST_TRACER));
    }
}

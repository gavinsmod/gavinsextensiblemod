package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModMobTracer extends Mod {
    public ModMobTracer() {
        super(ModType.MOB_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.MOB_TRACER));
    }
}

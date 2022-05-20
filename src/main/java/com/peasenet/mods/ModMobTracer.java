package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModMobTracer extends Mod {
    public ModMobTracer() {
        super(Mods.MOB_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.MOB_TRACER));
    }
}

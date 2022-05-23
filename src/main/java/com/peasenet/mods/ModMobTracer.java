package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the client to see lines, called tracers, towards mobs.
 */
public class ModMobTracer extends Mod {
    public ModMobTracer() {
        super(Mods.MOB_TRACER, Mods.Category.RENDER, KeyBindUtils.registerKeyBindForType(Mods.MOB_TRACER));
    }
}

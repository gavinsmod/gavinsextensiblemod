package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the client to see lines, called tracers, towards mobs.
 */
public class ModMobTracer extends Mod {
    public ModMobTracer() {
        super(Type.MOB_TRACER, Type.Category.RENDER, KeyBindUtils.registerKeyBindForType(Type.MOB_TRACER));
    }
}

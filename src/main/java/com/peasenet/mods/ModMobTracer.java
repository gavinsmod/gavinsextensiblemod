package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/18/2022
 */
public class ModMobTracer extends Mod {
    public ModMobTracer() {
        super(ModType.MOB_TRACER, ModCategory.WORLD, KeyBindUtils.registerKeyBindForType(ModType.MOB_TRACER));
    }
}

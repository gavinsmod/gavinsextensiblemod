package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/18/2022
 */
public class ModMobEsp extends Mod {
    public ModMobEsp() {
        super(ModType.MOB_TRACER, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.MOB_TRACER));
    }
}

package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModMobEsp extends Mod {
    public ModMobEsp() {
        super(ModType.MOB_ESP, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.MOB_ESP));
    }
}

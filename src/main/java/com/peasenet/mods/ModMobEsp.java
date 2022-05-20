package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModMobEsp extends Mod {
    public ModMobEsp() {
        super(Mods.MOB_ESP, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.MOB_ESP));
    }
}

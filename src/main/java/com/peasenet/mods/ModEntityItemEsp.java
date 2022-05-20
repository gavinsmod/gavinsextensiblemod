package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModEntityItemEsp extends Mod {
    public ModEntityItemEsp() {
        super(Mods.ENTITY_ITEM_ESP, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.ENTITY_ITEM_ESP));
    }
}

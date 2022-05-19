package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModEntityItemEsp extends Mod {
    public ModEntityItemEsp() {
        super(ModType.ENTITY_ITEM_ESP, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(ModType.ENTITY_ITEM_ESP));
    }
}

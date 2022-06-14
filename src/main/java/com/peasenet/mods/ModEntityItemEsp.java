package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A mod that allows the player to see an esp (a box) around items.
 */
public class ModEntityItemEsp extends Mod {
    public ModEntityItemEsp() {
        super(Type.ENTITY_ITEM_ESP, Type.Category.ESPS, KeyBindUtils.registerKeyBindForType(Type.ENTITY_ITEM_ESP));
    }
}

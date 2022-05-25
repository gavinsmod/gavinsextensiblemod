package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the player to see an esp (a box) around items.
 */
public class ModEntityItemEsp extends Mod {
    public ModEntityItemEsp() {
        super(Mods.ENTITY_ITEM_ESP, Mods.Category.RENDER, KeyBindUtils.registerKeyBindForType(Mods.ENTITY_ITEM_ESP));
    }
}

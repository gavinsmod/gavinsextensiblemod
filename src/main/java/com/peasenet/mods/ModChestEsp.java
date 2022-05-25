package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the client to see an esp (a box) around chests.
 */
public class ModChestEsp extends Mod {
    public ModChestEsp() {
        super(Mods.CHEST_ESP, Mods.Category.RENDER, KeyBindUtils.registerEmptyKeyBind(Mods.CHEST_ESP));
    }
}

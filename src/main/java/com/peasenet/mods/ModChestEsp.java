package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A mod that allows the client to see an esp (a box) around chests.
 */
public class ModChestEsp extends Mod {
    public ModChestEsp() {
        super(Type.CHEST_ESP, Type.Category.ESPS, KeyBindUtils.registerEmptyKeyBind(Type.CHEST_ESP));
    }
}

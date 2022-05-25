package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the client to see an esp (a box) around chests.
 */
public class ModChestEsp extends Mod {
    public ModChestEsp() {
        super(Type.CHEST_ESP, Type.Category.RENDER, KeyBindUtils.registerEmptyKeyBind(Type.CHEST_ESP));
    }
}

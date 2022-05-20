package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

/**
 * @author gt3ch1
 * A mod that allows the client to see an esp (a box) around chests.
 */
public class ModChestEsp extends Mod implements WorldRenderEvents.AfterEntities {
    public ModChestEsp() {
        super(Mods.CHEST_ESP, ModCategory.RENDER, KeyBindUtils.registerEmptyKeyBind(Mods.CHEST_ESP));
    }
}

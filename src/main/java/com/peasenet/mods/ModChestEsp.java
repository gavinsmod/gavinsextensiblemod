package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

/**
 * @author gt3ch1
 * @version 5/17/2022
 */
public class ModChestEsp extends Mod implements WorldRenderEvents.AfterEntities {
    public ModChestEsp() {
        super(Mods.CHEST_ESP, ModCategory.RENDER, KeyBindUtils.reigsterEmptyKeyBind(Mods.CHEST_ESP));
    }
}

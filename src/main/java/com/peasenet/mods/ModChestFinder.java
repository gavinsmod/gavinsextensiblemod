package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

/**
 * @author gt3ch1
 * @version 5/17/2022
 */
public class ModChestFinder extends Mod implements WorldRenderEvents.AfterEntities {
    public ModChestFinder() {
        super(ModType.CHEST_FINDER, ModCategory.WORLD, KeyBindUtils.reigsterEmptyKeyBind(ModType.CHEST_FINDER));
    }
}

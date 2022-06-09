package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import com.peasenet.mixins.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;

/**
 * @author gt3ch1
 * @version 6/9/2022
 * A mod that allows the player to place blocks really fast.
 */
public class ModFastPlace extends Mod {
    public ModFastPlace() {
        super(Type.FAST_PLACE, Type.Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (GavinsModClient.getPlayer() != null) {
            var item = GavinsModClient.getPlayer().getMainHandStack().getItem() instanceof BlockItem;
            if (isEnabled && item)
                ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        }
    }
}

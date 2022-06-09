package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import com.peasenet.mixins.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;

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

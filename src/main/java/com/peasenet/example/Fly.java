package com.peasenet.example;

import com.peasenet.mods.Mods;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class Fly {
    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFlyKeybind(CallbackInfo info) {
        PlayerEntityAccessor accessor = (PlayerEntityAccessor) (Object) this;
        PlayerAbilities abilities = accessor.getAbilities();
        abilities.allowFlying = Mods.flyEnabled || abilities.creativeMode;
    }
}

package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFlyKeybind(CallbackInfo info) {
        PlayerEntityAccessor accessor = (PlayerEntityAccessor) this;
        PlayerAbilities abilities = accessor.getAbilities();
        abilities.allowFlying = GavinsMod.Fly.isActive() || abilities.creativeMode;
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public float checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.FastMine.isActive()) {
            ci.setReturnValue(500.0f);
            return 500.0f;
        }
        return 1.0F;
    }
}

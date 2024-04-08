package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinClientPlayerEntityFastMine {
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.isEnabled("fastmine"))
            ci.setReturnValue(500.0f);
    }
}

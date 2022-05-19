package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(at = @At("RETURN"), method = "isClimbing", cancellable = true)
    public void onLadder(CallbackInfoReturnable<Boolean> cir) {
        if(GavinsMod.ClimbEnabled()) {
            boolean enabled =  ((LivingEntity) (Object) this).horizontalCollision;
            cir.setReturnValue(enabled);
        }
    }
}

package com.peasenet.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    EntityAccessor accessor = (EntityAccessor) this;

    @Inject(method = "isOnGround", at = @At("RETURN"), cancellable = true)
    public boolean checkNoFall(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        accessor.setOnGround(true);
        return true;
    }
}

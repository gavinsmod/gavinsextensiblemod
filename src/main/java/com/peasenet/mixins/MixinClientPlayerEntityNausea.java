package com.peasenet.mixins;

import com.peasenet.main.Mods;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntityNausea {
    @Shadow
    public float prevNauseaIntensity;
    @Shadow
    public float nauseaIntensity;

    @Inject(method = "updateNausea", at = @At("HEAD"), cancellable = true)
    public void cancelNausea(CallbackInfo ci) {
        if (Mods.getMod("nonausea").isActive()) {
            this.prevNauseaIntensity = 0.0f;
            this.nauseaIntensity = 0.0f;
            ci.cancel();
        }
    }

}

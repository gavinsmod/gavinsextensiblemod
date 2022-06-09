package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRender {
    @Inject(method = "bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"), cancellable = true)
    public void checkAntiHurt(MatrixStack stack, float f, CallbackInfo ci) {
        if (GavinsMod.isEnabled(Type.ANTI_HURT)) {
            ci.cancel();
        }
    }
}

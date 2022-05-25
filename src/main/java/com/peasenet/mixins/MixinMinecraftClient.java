package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mods;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gt3ch1, ate47
 * Credit: <a href="https://github.com/ate47/Xray/">https://github.com/ate47/Xray/</a>
 * @version 5/18/2022
 */
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(at = @At(value = "HEAD"), method = "isAmbientOcclusionEnabled()Z", cancellable = true)
    private static void ixXrayOrFullbright(CallbackInfoReturnable<Boolean> ci) {
        boolean disabled = GavinsMod.isEnabled(Mods.XRAY) || GavinsMod.isEnabled(Mods.FULL_BRIGHT);
        ci.setReturnValue(!disabled);
        ci.cancel();
    }
}

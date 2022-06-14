package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gt3ch1
 * @version 6/14/2022
 */
@Mixin(Biome.class)
public class MixinBiome {
    @Inject(method = "getPrecipitation", at = @At("HEAD"), cancellable = true)
    public void getPrecipitation(CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (GavinsMod.isEnabled(Type.NO_RAIN)) {
            cir.setReturnValue(Biome.Precipitation.NONE);
            cir.cancel();
        }
    }
}

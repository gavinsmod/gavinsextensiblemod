package com.peasenet.mixins;

import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author gt3ch1
 * Credit: <a href="https://github.com/ate47/Xray/">https://github.com/ate47/Xray/</a>
 * @version 5/16/2022
 */
@Mixin(value = LightmapTextureManager.class)
public class MixinLightmapTextureManager {
//    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;getGamma*:Lnet/minecraft/client/option/SimpleOption;", opcode = Opcodes.GETFIELD), method = "update(F)V")
//    private double getFieldValue(SimpleOption<Double> gamma) {
//        if (GavinsMod.isEnabled(Type.XRAY) || GavinsMod.isEnabled(Type.FULL_BRIGHT))
//            return 10000;
//        else {
//            return gamma.getValue();
//        }
//    }
}
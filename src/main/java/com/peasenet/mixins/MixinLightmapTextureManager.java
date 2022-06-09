package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.LightmapTextureManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author gt3ch1
 * Credit: <a href="https://github.com/ate47/Xray/">https://github.com/ate47/Xray/</a>
 * @version 5/16/2022
 */
@Mixin(value = LightmapTextureManager.class)
public class MixinLightmapTextureManager {
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;gamma*:D", opcode = Opcodes.GETFIELD), method = "update(F)V")
    private double getFieldValue(GameOptions options) {
        if (GavinsMod.isEnabled(Type.XRAY) || GavinsMod.isEnabled(Type.FULL_BRIGHT))
            return 10000;
        else {
            return options.gamma;
        }
    }
}
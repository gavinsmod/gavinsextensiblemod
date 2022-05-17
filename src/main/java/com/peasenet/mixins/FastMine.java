package com.peasenet.mixins;

import com.peasenet.mods.Mods;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gt3ch1
 * @version 5/15/2022
 */
@Mixin(PlayerEntity.class)
public class FastMine {
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public float checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (Mods.fastMine) {
            ci.setReturnValue(500.0f);
            return 500.0f;
        }
        return 1.0F;
    }
}

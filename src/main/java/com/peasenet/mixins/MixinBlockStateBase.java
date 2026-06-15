package com.peasenet.mixins;

import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 * @author GT3CH1
 * @version 06-14-2026
 * @since 06-14-2026
 */

@Mixin(BlockStateBase.class)
public class MixinBlockStateBase {
    @Inject(
            method = "getShadeBrightness",
            at = @At("RETURN"),
            cancellable = true,
            order = 900
    )
        private void modifyBrightness(CallbackInfoReturnable<Float> cir) {
            if (cir.getReturnValue() < 1F) {
                cir.setReturnValue(1F);
            }
    }
}

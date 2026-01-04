package com.peasenet.mixins;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.peasenet.config.render.XrayConfig;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 * @author GT3CH1
 * @version 01-03-2026
 * @since 01-03-2026
 */

@Mixin(LiquidBlockRenderer.class)
public class MixinLiquidBlockRenderer {
    @Inject(at = @At("HEAD"), method = "isFaceOccludedByState", cancellable = true)
    private static void isFaceOccluded(CallbackInfoReturnable<Boolean> cir) {
        if (Mods.isActive(ChatCommand.Xray)) {
            XrayConfig xrayConfig = Settings.INSTANCE.getConfig(ChatCommand.Xray);
            if (!xrayConfig.getShowLiquids()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tesselate", cancellable = true)
    void test(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        if (Mods.isActive(ChatCommand.Xray)) {
            XrayConfig xrayConfig = Settings.INSTANCE.getConfig(ChatCommand.Xray);
            if (!xrayConfig.getShowLiquids()) {
                ci.cancel();
            }
        }
    }

    @ModifyConstant(method = "vertex", constant = @Constant(floatValue = 1.0f, ordinal = 0))
    private static float modifyAlpha(float original) {
        if (Mods.isActive(ChatCommand.Xray)) {
            XrayConfig xrayConfig = Settings.INSTANCE.getConfig(ChatCommand.Xray);
            if (!xrayConfig.getShowLiquids())
                return 0f;
        }
        return original;
    }
}

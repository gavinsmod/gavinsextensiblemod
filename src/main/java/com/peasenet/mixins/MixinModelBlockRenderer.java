package com.peasenet.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.peasenet.main.Mods;
import com.peasenet.mods.render.ModXray;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

/**
 *
 * @author GT3CH1
 * @version 06-14-2026
 * @since 06-14-2026
 */

@Mixin(ModelBlockRenderer.class)
public abstract class MixinModelBlockRenderer {
    private static ThreadLocal<Float> currentOpacity =
            ThreadLocal.withInitial(() -> 1F);

    @WrapOperation(
            method = "shouldRenderFace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z")
    )
    private boolean onRenderSmoothOrFlat(BlockState state,
                                         BlockState otherState, Direction side, Operation<Boolean> original,
                                         BlockAndTintGetter world, BlockState stateButFromTheOtherMethod,
                                         Direction sideButFromTheOtherMethod, BlockPos neighborPos) {

        if (Mods.isActive(ChatCommand.Xray) && (ModXray.Companion.shouldDrawFace(state))) {
            currentOpacity.set(1F);
            return false;
        }
        return original.call(state, otherState, side);
    }

    @WrapOperation(
            method = "putQuadWithTint(Lnet/minecraft/client/renderer/block/BlockQuadOutput;FFFLnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/resources/model/geometry/BakedQuad;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockQuadOutput;put(FFFLnet/minecraft/client/resources/model/geometry/BakedQuad;Lcom/mojang/blaze3d/vertex/QuadInstance;)V"))
    private void modifyOpacity(BlockQuadOutput output, float x, float y,
                               float z, BakedQuad quad, QuadInstance instance,
                               Operation<Void> original) {
        float opacity = currentOpacity.get();
        if (opacity < 1F)
            for (int i = 0; i < 4; i++) {
                int color = instance.getColor(i);
                instance.setColor(i,
                        ARGB.color(Math.round(ARGB.alpha(color) * opacity),
                                ARGB.red(color), ARGB.green(color), ARGB.blue(color)));
            }

        original.call(output, x, y, z, quad, instance);
    }
}

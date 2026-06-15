package com.peasenet.mixins;

import com.jcraft.jorbis.Block;
import com.mojang.blaze3d.vertex.PoseStack;
import com.peasenet.main.Mods;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 * @author GT3CH1
 * @version 06-14-2026
 * @since 06-14-2026
 */

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher {
    @Inject(at = @At("HEAD"), method = "submit")
    private <S extends BlockEntityRenderState> void onSubmit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
//        var xrayEnabled = Mods.isActive(ChatCommand.Xray);
//        if (xrayEnabled) {
//            ci.cancel();
//        }
    }
}

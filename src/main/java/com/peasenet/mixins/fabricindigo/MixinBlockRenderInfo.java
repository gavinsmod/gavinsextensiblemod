package com.peasenet.mixins.fabricindigo;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.peasenet.main.Mods;
import com.peasenet.mods.render.ModXray;
import com.peasenet.util.ChatCommand;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 * @author GT3CH1
 * @version 01-03-2026
 * @since 01-03-2026
 */
@Pseudo
@Mixin(value = AltModelBlockRendererImpl.class, remap = false)
public abstract class MixinBlockRenderInfo {
    @Shadow
    private BlockState blockState;

    // This handles drawing sides for block like things like fauna
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"), method = "shouldCullFace", require = 0, cancellable = true)
    void shouldDrawSide(Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (Mods.isActive(ChatCommand.Xray)) {
            cir.setReturnValue(!ModXray.Companion.shouldDrawFace(blockState));
        }
    }
}


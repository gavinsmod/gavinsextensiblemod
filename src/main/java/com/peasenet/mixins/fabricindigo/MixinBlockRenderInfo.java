package com.peasenet.mixins.fabricindigo;

import com.peasenet.main.Mods;
import com.peasenet.mods.render.ModXray;
import com.peasenet.util.ChatCommand;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
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
@Mixin(value = BlockRenderInfo.class, remap = false)
public abstract class MixinBlockRenderInfo {
    @Shadow
    public BlockState blockState;
    // This handles drawing sides for block like things like fauna
    @Inject(at = @At("HEAD"), method = "shouldDrawSide", require = 0, cancellable = true)
    void shouldDrawSide(Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (Mods.isActive(ChatCommand.Xray)) {
            if(!ModXray.Companion.shouldDrawFace(blockState)) {
                cir.setReturnValue(false);
            }
        }
    }
}

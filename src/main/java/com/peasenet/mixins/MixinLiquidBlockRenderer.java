package com.peasenet.mixins;

import com.peasenet.config.render.XrayConfig;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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
            if(xrayConfig.getShowLiquids())
                return;
            cir.setReturnValue(false);
        }
    }
}

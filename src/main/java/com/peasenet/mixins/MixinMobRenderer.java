package com.peasenet.mixins;

import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.config.tracer.ProjectileTracerConfig;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 * @author GT3CH1
 * @version 06-20-2026
 * @since 06-20-2026
 */

@Mixin(MobRenderer.class)
public abstract class MixinMobRenderer<T extends Mob> {
    @Inject(at = @At("HEAD"), method = "shouldShowName(Lnet/minecraft/world/entity/Mob;D)Z", cancellable = true)
    void renderHpTags(T entity, double distanceToCameraSq, CallbackInfoReturnable<Boolean> cir) {
        ProjectileTracerConfig ptConfig = Settings.INSTANCE.getConfig(ChatCommand.ProjectileTracer);
        boolean showEntityDistance = ptConfig.getShowEntityDistance();
        if (Mods.isActive(ChatCommand.HealthTag) || (Mods.isActive(ChatCommand.ProjectileTracer) && showEntityDistance)) {
            cir.setReturnValue(true);
        }
    }
}

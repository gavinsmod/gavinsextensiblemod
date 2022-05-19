package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    PlayerEntityAccessor accessor = (PlayerEntityAccessor) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFlyKeybind(CallbackInfo info) {
        PlayerAbilities abilities = accessor.getAbilities();
        abilities.allowFlying = GavinsMod.FlyEnabled() || abilities.creativeMode;
    }


    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public float checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.FastMineEnabled()) {
            ci.setReturnValue(500.0f);
            return 500.0f;
        }
        return 1.0F;
    }

    @Inject(method = "travel", at = @At("HEAD"),cancellable = true)
    public void checkAutoJump(Vec3d movementInput, CallbackInfo info) {
        PlayerEntity p = ((PlayerEntity)(Object)this);
        if(GavinsMod.AutoJumpEnabled()) {
            assert GavinsModClient.getMinecraftClient().player != null;
            if(GavinsModClient.getPlayer().isOnGround()) {
                GavinsModClient.getPlayer().jump();
            }
        }
    }
}

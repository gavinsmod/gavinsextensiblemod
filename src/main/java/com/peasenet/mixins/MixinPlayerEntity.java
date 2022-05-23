package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    PlayerEntityAccessor accessor = (PlayerEntityAccessor) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFlyKeybind(CallbackInfo info) {
        PlayerAbilities abilities = accessor.getAbilities();
        boolean flying = GavinsMod.FlyEnabled() || abilities.creativeMode || GavinsMod.NoClipEnabled();
        abilities.allowFlying = flying;
        if(GavinsMod.NoClipEnabled())
            accessor.getAbilities().flying = flying;
    }


    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public float checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.FastMineEnabled()) {
            ci.setReturnValue(500.0f);
            return 500.0f;
        }
        return 1.0F;
    }

    @Redirect(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", opcode = Opcodes.PUTFIELD))
    public void doNoClip(PlayerEntity p, boolean b) {
        //TODO: This does werid things with sprinting.
        if (GavinsMod.NoClipEnabled()) {
            p.noClip = true;
            b = true;
        } else {
            p.noClip = false;
            b = false;
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void checkAutoJump(Vec3d movementInput, CallbackInfo info) {
        PlayerEntity p = ((PlayerEntity) (Object) this);
        if (GavinsMod.AutoJumpEnabled() && !p.isCreative()) {
            GavinsModClient.getMinecraftClient().options.jumpKey.setPressed(true);
        }
    }
}

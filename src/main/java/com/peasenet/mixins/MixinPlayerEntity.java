package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.PlayerUtils;
import com.peasenet.util.math.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.StreamSupport;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFlyKeyBind(CallbackInfo info) {
        PlayerUtils.updateFlight();
        PlayerUtils.handleNoFall();
    }


    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.FastMineEnabled())
            ci.setReturnValue(500.0f);
    }

    @Redirect(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", opcode = Opcodes.PUTFIELD))
    public void doNoClip(PlayerEntity p, boolean noClip) {
        //TODO: This does werid things with sprinting.
        p.noClip = GavinsMod.NoClipEnabled();
        //NOTE: This is fine. It is required for fabric to work.
        noClip = GavinsMod.NoClipEnabled();
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void checkAutoJump(Vec3d movementInput, CallbackInfo info) {
        if (GavinsMod.AutoJumpEnabled() && !PlayerUtils.isCreative()) {
            PlayerUtils.doJump();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkKillAura(CallbackInfo ci) {
        if (GavinsModClient.getMinecraftClient().world != null && GavinsMod.KillAuraEnabled()) {
            // Sort entities by distance
            var stream = StreamSupport.stream(GavinsModClient.getMinecraftClient().world.getEntities().spliterator(), false)
                    .filter(e -> e instanceof MobEntity)
                    .sorted((e1, e2) -> (int) ((int) PlayerUtils.distanceToEntity(e1) - PlayerUtils.distanceToEntity(e2)))
                    .map(e -> (MobEntity) e);

            stream.forEach(entity -> {
                // check distance to player
                if (PlayerUtils.distanceToEntity(entity) > 5 && entity.getHealth() > 0)
                    return;
                PlayerUtils.setRotation(MathUtils.getRotationToEntity(entity));
                PlayerUtils.doAttackJump(entity);
            });
        }
    }
}

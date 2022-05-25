package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mods;
import com.peasenet.util.PlayerUtils;
import com.peasenet.util.math.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
        if (GavinsMod.isEnabled(Mods.FAST_MINE))
            ci.setReturnValue(500.0f);
    }

    @Redirect(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", opcode = Opcodes.PUTFIELD))
    public void doNoClip(PlayerEntity p, boolean noClip) {
        p.noClip = GavinsMod.isEnabled(Mods.NO_CLIP);
        //NOTE: This is fine. It is required for fabric to work.
        noClip = p.noClip;
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void checkAutoJump(Vec3d movementInput, CallbackInfo info) {
        if (GavinsMod.isEnabled(Mods.AUTO_JUMP) && !PlayerUtils.isCreative()) {
            PlayerUtils.doJump();
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void checkAutoCrit(Entity entity, CallbackInfo info) {
        if (GavinsMod.isEnabled(Mods.AUTO_CRIT))
            PlayerUtils.doJump();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkKillAura(CallbackInfo ci) {
        if (GavinsModClient.getMinecraftClient().world != null && GavinsMod.isEnabled(Mods.KILL_AURA)) {
            // Sort entities by distance
            var stream = StreamSupport.stream(GavinsModClient.getMinecraftClient().world.getEntities().spliterator(), false)
                    .filter(e -> e instanceof MobEntity)
                    .filter(Entity::isAlive)
                    .filter(e -> PlayerUtils.distanceToEntity(e) <= 12)
                    .sorted((e1, e2) -> (int) ((int) PlayerUtils.distanceToEntity(e1) - PlayerUtils.distanceToEntity(e2)))
                    .map(e -> (MobEntity) e);

            stream.forEach(entity -> {
                PlayerUtils.setRotation(MathUtils.getRotationToEntity(entity));
                PlayerUtils.attackEntity(entity);
            });
        }
    }
}

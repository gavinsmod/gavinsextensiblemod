package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
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
        if (GavinsMod.NoClipEnabled())
            accessor.getAbilities().flying = flying;
    }


    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void checkFastMine(BlockState state, CallbackInfoReturnable<Float> ci) {
        if (GavinsMod.FastMineEnabled())
            ci.setReturnValue(500.0f);
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
            // Get chunk position
            int chunk_x = GavinsModClient.getPlayer().getChunkPos().x;
            int chunk_z = GavinsModClient.getPlayer().getChunkPos().z;
            // Find entities within 3 blocks of player
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkKillAura(CallbackInfo ci) {
        if (GavinsModClient.getMinecraftClient().world != null) {
            GavinsModClient.getMinecraftClient().world.getEntities().forEach(entity -> {
                // check distance to player
                if(entity.distanceTo(GavinsModClient.getPlayer()) > 10)
                    return;
                var player = GavinsModClient.getPlayer();
                if (entity instanceof MobEntity && player.distanceTo(entity) < 5) {
                    // Get player eye position
                    var playerPos = new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
                    double diffX = entity.getX() - playerPos.x;
                    double diffY = (entity.getBoundingBox().getCenter().y - playerPos.y);
                    double diffZ = entity.getZ() - playerPos.z;
                    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    // Calculate angle
                    float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
                    float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
                    // Set player rotation
                    player.setYaw(yaw);
                    player.setPitch(pitch);
                    // Attack entity
                    GavinsModClient.getMinecraftClient().interactionManager.attackEntity(player, entity);
                    if(player.isOnGround())
                        GavinsModClient.getMinecraftClient().options.jumpKey.setPressed(true);
                    player.swingHand(Hand.MAIN_HAND);

                }
            });
        }
    }
}

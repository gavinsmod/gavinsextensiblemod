/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.mixins;

import com.mojang.authlib.GameProfile;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.mixinterface.IClientPlayerEntity;
import com.peasenet.mods.Type;
import com.peasenet.util.event.AirStrafeEvent;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gt3ch1
 * @version 02/27/2023
 */
@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements IClientPlayerEntity {
    @Shadow
    public float nextNauseaStrength;
    @Shadow
    @Final
    public
    ClientPlayNetworkHandler networkHandler;
    @Shadow
    public float lastNauseaStrength;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    public abstract void sendMessage(Text message, boolean overlay);

    public boolean isOnGround() {
        return super.isOnGround();
    }

    public void setPitch(float pitch) {
        super.setPitch(pitch);
    }

    public void setYaw(float yaw) {
        super.setYaw(yaw);
    }

    public Vec3d getPos() {
        return super.getPos();
    }

    @Override
    public double getPrevX() {
        return super.prevX;
    }

    @Override
    public double getPrevY() {
        return super.prevY;
    }

    @Override
    public double getPrevZ() {
        return super.prevZ;
    }

    @Override
    public double getEyeHeight() {
        return super.getStandingEyeHeight();
    }

    @Override
    public EntityPose getPose() {
        return super.getPose();
    }

    @Override
    public boolean isNoClip() {
        return super.noClip;
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return super.getEyeHeight(pose);
    }

    @Override
    public float getAttackCoolDownProgress(float f) {
        return super.getAttackCooldownProgress(f);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    @Shadow
    public abstract void swingHand(Hand hand);

    @Override
    public PlayerAbilities getAbilities() {
        return super.getAbilities();
    }

    @Override
    public double getFallDistance() {
        return super.fallDistance;
    }

    @Override
    public ClientPlayNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public BlockPos getBlockPos() {
        return super.getBlockPos();
    }

    @Shadow
    public abstract boolean isSubmergedInWater();

    @Shadow
    public abstract boolean isSneaking();

    @Override
    public ItemStack getMainHandStack() {
        return super.getMainHandStack();
    }

    @Override
    public boolean isFallFlying() {
        return super.isFallFlying();
    }

    @Override
    public GameProfile getGameProfile() {
        return super.getGameProfile();
    }

    @Override
    public float getBodyYaw() {
        return super.bodyYaw;
    }

    @Override
    public float getHeadYaw() {
        return super.headYaw;
    }

    @Inject(method = "updateNausea", at = @At("HEAD"), cancellable = true)
    public void cancelNausea(CallbackInfo ci) {
        if (Mods.getMod(Type.NO_NAUSEA).isActive()) {
            this.lastNauseaStrength = 0.0f;
            this.nextNauseaStrength = 0.0f;
            ci.cancel();
        }
    }

    @Override
    public float getOffGroundSpeed() { 
    
        var event = new AirStrafeEvent(super.getOffGroundSpeed());
        GavinsMod.eventManager.call(event);
        return event.getSpeed();
    }
}

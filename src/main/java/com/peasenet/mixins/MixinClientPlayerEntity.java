/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.mixins;

import com.mojang.authlib.GameProfile;
import com.peasenet.mixinterface.IClientPlayerEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author GT3CH1
 * @version 12/31/2022
 */
@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements IClientPlayerEntity {

    /**
     * The network handler used to send and receive packets.
     */
    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    /**
     * Creates a new MixinClientPlayerEntity. You do not need to call this.
     *
     * @param world   - The world the player is in.
     * @param profile - The player's profile.
     */
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

    public @NotNull Vec3d getPos() {
        return super.getEntityPos();
    }

    @Override
    public double getPrevX() {
        return super.lastX;
    }

    @Override
    public double getPrevY() {
        return super.lastY;
    }

    @Override
    public double getPrevZ() {
        return super.lastZ;
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
    public @NotNull Vec3d getVelocity() {
        return super.getVelocity();
    }

    @Override
    public float getEyeHeightWithPose() {
        return super.getEyeHeight(this.getPose());
    }

    @Override
    public float getAttackCoolDownProgress(float f) {
        return super.getAttackCooldownProgress(f);
    }

    @Override
    public boolean tryAttack(Entity target) {
//TODO:        return super.tryAttack(getserverWorld(), target);
        return false;
    }

    @Shadow
    public abstract void swingHand(Hand hand);

    @Override
    public PlayerAbilities getAbilities() {
        return super.getAbilities();
    }

    @Override
    public float getFallDistance() {
        return (float) super.fallDistance;
    }

    @Override
    public ClientPlayNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    @Shadow
    public abstract boolean isSubmergedInWater();

    @Shadow
    public abstract boolean isSneaking();

    @Override
    public BlockPos getBlockPos() {
        return super.getBlockPos();
    }

    @Override
    public ItemStack getMainHandStack() {
        return super.getMainHandStack();
    }

    @Override
    public boolean isFallFlying() {
        // TODO: super.isFallFlying();
        return false;
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

    @Override
    public double eyeHeight() {
        return 0;
    }

    @Override
    public double squaredDistanceTo(Entity e) {
        return super.squaredDistanceTo(e);
    }

    @Override
    public @NotNull World getWorld() {
        return super.getEntityWorld();
    }

    @Override
    public boolean horizontalCollision() {
        return super.horizontalCollision;
    }

    @Override
    public boolean isGliding() {
        return super.isGliding();
    }
}

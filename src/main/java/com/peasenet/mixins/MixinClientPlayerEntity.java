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
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author GT3CH1
 * @version 12/31/2022
 */
@Mixin(LocalPlayer.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayer implements IClientPlayerEntity {

    /**
     * The network handler used to send and receive packets.
     */
    @Shadow
    @Final
    public ClientPacketListener connection;

    /**
     * Creates a new MixinClientPlayerEntity. You do not need to call this.
     *
     * @param world   - The world the player is in.
     * @param profile - The player's profile.
     */
    public MixinClientPlayerEntity(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }


    public boolean onGround() {
        return super.onGround();
    }


    @Override
    public void setXRot(float xRot) {
        super.setXRot(xRot);
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(yRot);
    }

    @Override
    public Vec3 getPos() {
        return super.position();
    }


    @Override
    public double getPrevX() {
        return super.xo;
    }

    @Override
    public double getPrevY() {
        return super.yo;
    }

    @Override
    public double getPrevZ() {
        return super.zo;
    }

    @Override
    public float getEyeHeightWithPose() {
        return super.getEyeHeight(super.getPose());
    }

    @Override
    public GameProfile getGameProfile() {
        return super.getGameProfile();
    }

    @Override
    public boolean isNoClip() {
        return super.noPhysics;
    }

    @Override
    public float getAttackCoolDownProgress(float partialTicks) {
        return super.getAttackStrengthScale(partialTicks);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return false;
    }

    @Shadow
    public abstract void swing(InteractionHand hand);

    @Override
    public Abilities getAbilities() {
        return super.getAbilities();
    }

    @Override
    public double squaredDistanceTo(Entity e) {
        return super.distanceToSqr(e);
    }

    @Override
    public boolean isFallFlying() {
        return false;
    }

    @Override
    public Vec3 getVelocity() {
        return super.getDeltaMovement();
    }

    @Override
    public double getFallDistance() {
        return super.fallDistance;
    }

    @Shadow
    public abstract boolean isShiftKeyDown();

    @Override
    public ClientPacketListener getNetworkHandler() {
        return this.connection;
    }

    @Shadow
    public abstract void displayClientMessage(Component message, boolean overlay);

    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Shadow
    public abstract boolean isUnderWater();

    @Override
    public boolean isCollidingHorizontally() {
        return super.horizontalCollision;
    }
}

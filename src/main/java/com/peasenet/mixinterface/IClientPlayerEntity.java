/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mixinterface;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public interface IClientPlayerEntity {
    public float getNextNauseaStrength();

    void setNextNauseaStrength(float nextNauseaStrength);

    boolean isOnGround();

    void setPitch(float pitch);

    void setYaw(float yaw);

    Vec3d getPos();

    double getPrevX();

    double getPrevY();

    double getPrevZ();

    double getEyeHeight();

    float getEyeHeight(EntityPose pose);

    EntityPose getPose();

    double getY();

    double getZ();

    float getHeadYaw();

    float getBodyYaw();

    GameProfile getGameProfile();

    boolean isNoClip();

    float getAttackCooldownProgress(float f);

    boolean tryAttack(Entity e);

    void swingHand(Hand h);

    PlayerAbilities getAbilities();

    double squaredDistanceTo(Entity e);

    boolean isFallFlying();

    Vec3d getVelocity();

    double getFallDistance();

    boolean isSneaking();

    ClientPlayNetworkHandler getNetworkHandler();

    void setPos(double x, double y, double z);

    void sendMessage(Text message, boolean actionBar);

    BlockPos getBlockPos();

    ItemStack getMainHandStack();
}

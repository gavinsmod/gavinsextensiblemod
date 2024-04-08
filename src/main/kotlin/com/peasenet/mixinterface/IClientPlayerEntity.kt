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
package com.peasenet.mixinterface

import com.mojang.authlib.GameProfile
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.player.PlayerAbilities
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * The interface for the player entity.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
interface IClientPlayerEntity {
    /**
     * Whether the player is on the ground.
     *
     * @return Whether the player is on the ground.
     */
    fun isOnGround(): Boolean

    /**
     * Sets the player's pitch.
     *
     * @param pitch - The pitch to set.
     */
    fun setPitch(pitch: Float)

    /**
     * Sets the player's yaw.
     *
     * @param yaw - The yaw to set.
     */
    fun setYaw(yaw: Float)

    /**
     * Gets the current position of the player.
     *
     * @return The current position of the player.
     */
    fun getPos(): Vec3d

    /**
     * Gets the previous x position of the player.
     *
     * @return The previous x position of the player.
     */
    fun getPrevX(): Double

    /**
     * Gets the previous y position of the player.
     *
     * @return The previous y position of the player.
     */
    fun getPrevY(): Double

    /**
     * Gets the previous z position of the player.
     *
     * @return The previous z position of the player.
     */
    fun getPrevZ(): Double

    /**
     * Gets the eye height of the player.
     *
     * @return The eye height of the player.
     */
    fun eyeHeight(): Double

    /**
     * Gets the eye height of the player in the given pose.
     *
     * @param pose - The pose to get the eye height for.
     * @return The eye height of the player in the given pose.
     */
    fun getEyeHeight(pose: EntityPose?): Float

    /**
     * Gets the player's current pose.
     *
     * @return The player's current pose.
     */
    fun getPose(): EntityPose

    /**
     * Gets the head yaw of the player.
     *
     * @return The head yaw of the player.
     */
    fun getHeadYaw(): Float

    /**
     * Gets the body yaw of the player.
     *
     * @return The body yaw of the player.
     */
    fun getBodyYaw(): Float

    /**
     * Gets the game profile of the player.
     *
     * @return The game profile of the player.
     */
    fun getGameProfile(): GameProfile

    /**
     * Whether the player can currently no-clip.
     *
     * @return Whether the player can currently no-clip.
     */
    fun isNoClip(): Boolean

    /**
     * Get the current attack cool down progress.
     *
     * @param f - The partial ticks.
     * @return The current attack cool down progress.
     */
    fun getAttackCoolDownProgress(f: Float): Float

    /**
     * Trys to attack the given entity.
     *
     * @param e - The entity to attack.
     * @return Whether the attack was successful.
     */
    fun tryAttack(e: Entity?): Boolean

    /**
     * Swings the given hand.
     *
     * @param h - The hand to swing.
     */
    fun swingHand(h: Hand?)

    /**
     * Gets the player's abilities.
     *
     * @return The player's abilities.
     */
    fun getAbilities(): PlayerAbilities

    /**
     * Gets the square distance to the given entity.
     *
     * @param e - The entity to get the distance to.
     * @return The square distance to the given entity.
     */
    fun squaredDistanceTo(e: Entity?): Double


    /**
     * Whether the player is fall flying.
     *
     * @return Whether the player is fall flying.
     */
    fun isFallFlying(): Boolean

    /**
     * The velocity of the player.
     *
     * @return The velocity of the player.
     */
    fun getVelocity(): Vec3d

    /**
     * Gets the fall distance of the player.
     *
     * @return The fall distance of the player.
     */
    fun getFallDistance(): Float

    /**
     * Whether the player is sneaking.
     *
     * @return Whether the player is sneaking.
     */
    fun isSneaking(): Boolean

    /**
     * Gets the network handler for the player.
     *
     * @return The network handler for the player.
     */
//    fun getNetworkHandler(): ClientPlayNetworkHandler

    fun getNetworkHandler(): ClientPlayNetworkHandler

    /**
     * Sends a chat message.
     *
     * @param message   - The message to send.
     * @param actionBar - Whether the message should be sent as an action bar message.
     */
    fun sendMessage(message: Text?, actionBar: Boolean)

    /**
     * Get the block position of the player.
     *
     * @return The block position of the player.
     */
    fun getBlockPos(): BlockPos

    /**
     * Gets the item in the main hand.
     *
     * @return The item in the main hand.
     */
    fun getMainHandStack(): ItemStack

    /**
     * Whether the player is currently submerged in water.
     *
     * @return Whether the player is currently submerged in water.
     */
    fun isSubmergedInWater(): Boolean
    fun getEyeHeight(): Double
    fun getWorld(): World
}
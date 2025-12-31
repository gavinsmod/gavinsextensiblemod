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
package com.peasenet.mixinterface

import com.mojang.authlib.GameProfile
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Abilities
import net.minecraft.world.item.ItemStack
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.Vec3

/**
 * The interface for the player entity.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
interface IClientPlayerEntity {

    /**
     * Whether the player is on the ground.
     *
     * @return Whether the player is on the ground.
     */
    fun onGround(): Boolean

    /**
     * Sets the player's pitch.
     *
     * @param pitch - The pitch to set.
     */
    fun setXRot(pitch: Float)

    /**
     * Sets the player's yaw.
     *
     * @param yaw - The yaw to set.
     */
    fun setYRot(yaw: Float)

    /**
     * Gets the current position of the player.
     *
     * @return The current position of the player.
     */
    fun getPos(): Vec3

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


    fun getEyeHeightWithPose(): Float

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
    fun swing(h: InteractionHand?)

    /**
     * Gets the player's abilities.
     *
     * @return The player's abilities.
     */
    fun getAbilities(): Abilities

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
    fun getVelocity(): Vec3

    /**
     * Gets the fall distance of the player.
     *
     * @return The fall distance of the player.
     */
    fun getFallDistance(): Double

    /**
     * Whether the player is sneaking.
     *
     * @return Whether the player is sneaking.
     */
    fun isShiftKeyDown(): Boolean

    /**
     * Gets the network handler for the player.
     *
     * @return The network handler for the player.
     */

    fun getNetworkHandler(): ClientPacketListener

    /**
     * Sends a chat message.
     *
     * @param message   - The message to send.
     * @param actionBar - Whether the message should be sent as an action bar message.
     */
    fun displayClientMessage(message: Component?, actionBar: Boolean)

    /**
     * Gets the item in the main hand.
     *
     * @return The item in the main hand.
     */
    fun getMainHandItem(): ItemStack

    /**
     * Whether the player is currently submerged in water.
     *
     * @return Whether the player is currently submerged in water.
     */
    fun isUnderWater(): Boolean

    fun isCollidingHorizontally(): Boolean

    fun isCreative(): Boolean
    fun getDeltaMovement(): Vec3
}
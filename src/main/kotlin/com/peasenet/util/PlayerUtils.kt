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
package com.peasenet.util

import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.util.event.EventManager
import com.peasenet.util.event.PlayerAttackEvent
import com.peasenet.util.math.Rotation
import net.minecraft.client.render.Camera
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A helper class with utilities relating to the player.
 */
object PlayerUtils {
    private var lastAttackTime = 0

    /**
     * Sets the rotation of the player.
     *
     * @param rotation The rotation to set the view to.
     */
    fun setRotation(rotation: Rotation) {
        val player = GavinsModClient.player
        player!!.setPitch(rotation.pitch)
        player.setYaw(rotation.yaw)
    }

    val playerPos: Vec3d
        /**
         * Gets the current position of the player.
         *
         * @return The current position of the player.
         */
        get() {
            val player = GavinsModClient.player
            return player!!.getPos()
        }

    /**
     * Gets the new player position after the change in time.
     *
     * @param deltaTime The change in time.
     * @param camera    The camera to use.
     * @return The new player position.
     */
    @JvmStatic
    fun getNewPlayerPosition(deltaTime: Float, camera: Camera): Vec3d {
        val look = camera.horizontalPlane
        val player = GavinsModClient.player
        val px = player!!.getPrevX() + (playerPos.getX() - player.getPrevX()) * deltaTime + look.x()
        val py = (player.getPrevY() + (playerPos.getY() - player.getPrevY()) * deltaTime + look.y()
                + player.getEyeHeight(player.getPose()))
        val pz = player.getPrevZ() + (playerPos.getZ() - player.getPrevZ()) * deltaTime + look.z()
        return Vec3d(px, py, pz)
    }

    /**
     * Checks if the player is on the ground.
     *
     * @return True if the player is on the ground, false otherwise.
     */
    private fun onGround(): Boolean {
        val player = GavinsModClient.player!!
        return player.isOnGround()
    }

    /**
     * Attacks the given entity.
     *
     * @param entity The entity to attack.
     */
    fun attackEntity(entity: Entity?) {
        val player = GavinsModClient.player
        if (onGround() && !player!!.isNoClip() && player.getAttackCoolDownProgress(0.5f) > 0.90f) {
            val event = PlayerAttackEvent()
            EventManager.eventManager.call(event)
            GavinsModClient.minecraftClient.getPlayerInteractionManager().attackEntity(player as PlayerEntity, entity)

            player.tryAttack(entity)
            player.swingHand(Hand.MAIN_HAND)
            lastAttackTime = 0
        }
        lastAttackTime++
    }

    /**
     * Checks whether flight is enabled.
     */
    fun updateFlight() {
        val player = GavinsModClient.player
        if (player?.getAbilities() == null) return
        val abilities = player.getAbilities()
        abilities.allowFlying =
            GavinsMod.isEnabled(Type.FLY) || abilities.creativeMode || GavinsMod.isEnabled(Type.NO_CLIP)

        if (GavinsMod.isEnabled(Type.FLY) && GavinsMod.isEnabled(Type.NO_CLIP)) abilities.flying = true

        if (!abilities.creativeMode && !GavinsMod.isEnabled(Type.FLY) && !GavinsMod.isEnabled(Type.NO_CLIP)) abilities.flying =
            false
    }

    /**
     * Gets the distance between the player and the given entity.
     *
     * @param entity The entity to get the distance to.
     * @return The distance between the player and the given entity.
     */
    fun distanceToEntity(entity: Entity?): Double {
        val player = GavinsModClient.player
        return player!!.squaredDistanceTo(entity)
    }

    private val isFalling: Boolean
        /**
         * Gets whether the player is falling.
         *
         * @return True if the player is falling, false otherwise.
         */
        get() {
            val player = GavinsModClient.player
            return player!!.isFallFlying()
        }

    /**
     * Gets whether the player can be damaged by the current fall speed.
     *
     * @return True if the player can be damaged, false otherwise.
     */
    private fun fallSpeedCanDamage(): Boolean {
        val player = GavinsModClient.player
        return player!!.getVelocity().y < -0.5
    }

    /**
     * Handles No Fall. This will prevent the player from falling when enabled.
     */
    fun handleNoFall() {
        //TODO: This is apparently a bit weird in preventing the player from crouching + moving while flying.
        val player = GavinsModClient.player
        if (player != null) {
            if (player.getFallDistance() <= (if (isFalling) 1 else 2)) return
            if (player.isSneaking() && !fallSpeedCanDamage() && player.isFallFlying()) return
            if (GavinsMod.isEnabled(Type.NO_FALL)) {
                player.getNetworkHandler().sendPacket(OnGroundOnly(true))
            }
        }
    }

    @JvmStatic
    fun sendMessage(message: String, withPrefix: Boolean) {
        var newMessage = message
        if (withPrefix) newMessage = Mod.GAVINS_MOD_STRING + newMessage
        if (GavinsMod.miscConfig.isMessages) GavinsModClient.player!!.sendMessage(Text.literal(newMessage), false)
    }
}
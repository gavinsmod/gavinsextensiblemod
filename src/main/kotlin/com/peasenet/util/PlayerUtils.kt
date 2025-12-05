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
package com.peasenet.util

import com.peasenet.config.misc.MiscConfig
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.mods.Mod
import com.peasenet.util.event.EventManager
import com.peasenet.util.event.PlayerAttackEvent
import com.peasenet.util.math.Rotation
import net.minecraft.client.Camera
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.Vec3

/**
 * A helper class with utilities relating to the player.
 * @author GT3CH1
 * @version 04-11-2023
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
        player!!.setXRot(rotation.pitch)
        player.setYRot(rotation.yaw)
    }

    val playerPos: Vec3
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
    fun getNewPlayerPosition(deltaTime: Float, camera: Camera): Vec3 {
        val look = camera.forwardVector()
        val player = GavinsModClient.player
        val px = player!!.getPrevX() + (playerPos.x- player.getPrevX()) * deltaTime + look.x()
        val py = (player.getPrevY() + (playerPos.y - player.getPrevY()) * deltaTime + look.y()
                + player.getEyeHeightWithPose())
        val pz = player.getPrevZ() + (playerPos.z - player.getPrevZ()) * deltaTime + look.z()
        return Vec3(px, py, pz)
    }

    /**
     * Checks if the player is on the ground.
     *
     * @return True if the player is on the ground, false otherwise.
     */
    private fun onGround(): Boolean {
        val player = GavinsModClient.player!!
        return player.onGround()
    }

    /**
     * Attacks the given entity.
     *
     * @param entity The entity to attack.
     */
    fun attackEntity(entity: Entity?) {
        val player = GavinsModClient.player
        if (onGround() && !player!!.isNoClip() && player.getAttackCoolDownProgress(0.5f) > 0.90f && entity != null) {
            val event = PlayerAttackEvent()
            EventManager.eventManager.call(event)
            GavinsModClient.minecraftClient.getPlayerInteractionManager().attack(player as Player, entity)

            player.tryAttack(entity)
            player.swing(InteractionHand.MAIN_HAND)
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
        abilities.mayfly =
            GavinsMod.isEnabled("fly") || abilities.instabuild || GavinsMod.isEnabled("noclip")

        if (GavinsMod.isEnabled("fly") && GavinsMod.isEnabled("noclip")) abilities.flying = true

        if (!abilities.instabuild && !GavinsMod.isEnabled("fly") && !GavinsMod.isEnabled("noclip")) abilities.flying =
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
            if (player.isShiftKeyDown() && !fallSpeedCanDamage() && player.isFallFlying()) return
            if (GavinsMod.isEnabled("noclip")) {
                player.getNetworkHandler().send(StatusOnly(true, false))
            }
        }
    }

    @JvmStatic
    fun sendMessage(message: String, withPrefix: Boolean) {
        var newMessage = message
        val sendMessage = Settings.getConfig<MiscConfig>("misc").isMessages
        if (withPrefix) newMessage = Mod.GAVINS_MOD_STRING + newMessage
        if (sendMessage) GavinsModClient.player!!.displayClientMessage(Component.literal(newMessage), false)
    }

    @JvmStatic
    fun sendMessage(message: Component, withPrefix: Boolean) {
        val sendMessage = Settings.getConfig<MiscConfig>("misc").isMessages
        var newMessage = message.string
        if (withPrefix) newMessage = Mod.GAVINS_MOD_STRING + newMessage
        if (sendMessage) GavinsModClient.player!!.displayClientMessage(Component.literal(newMessage), false)
    }
}
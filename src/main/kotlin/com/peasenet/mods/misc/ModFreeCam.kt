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
package com.peasenet.mods.misc

import com.peasenet.config.misc.FreeCamConfig
import com.peasenet.gavui.util.Direction
import com.peasenet.main.Settings
import com.peasenet.mods.tracer.TracerMod
import com.peasenet.util.FakePlayer
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.AirStrafeEvent
import com.peasenet.util.event.data.OutputPacket
import com.peasenet.util.listeners.AirStrafeListener
import com.peasenet.util.listeners.PacketSendListener
import com.peasenet.util.listeners.RenderListener
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3
import org.joml.Matrix3x2fStack

/**
 * A mod that allows the camera to be moved freely.
 *
 * @author GT3CH1
 * @version 01-15-2025
 */
class ModFreeCam : MiscMod(
    "gavinsmod.mod.misc.freecam", "freecam"
), PacketSendListener, RenderListener, AirStrafeListener {
    private var fake: FakePlayer? = null

    companion object {
        private val config: FreeCamConfig
            get() {
                return Settings.getConfig("freecam")
            }
    }

    init {
        subSettings {
            title = translationKey
            direction = Direction.RIGHT
            slideSetting {
                title = "gavinsmod.generic.speed"
                value = config.freeCamSpeed
                callback = { config.freeCamSpeed = it.value }
            }
            toggleSetting {
                title = "gavinsmod.generic.esp"
                state = config.espEnabled
                callback = { config.espEnabled = it.state }
            }
            toggleSetting {
                title = "gavinsmod.generic.tracer"
                state = config.tracerEnabled
                callback = { config.tracerEnabled = it.state }
            }
            colorSetting {
                title = "gavinsmod.generic.color"
                color = config.color
                callback = { config.color = it.color }
            }
            slideSetting {
                title = "gavinsmod.generic.alpha"
                value = 1f
                callback = { config.alpha = it.value }
            }
        }
    }

    override fun activate() {
        super.activate()
        fake = FakePlayer()
        em.subscribe(PacketSendListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
        em.subscribe(AirStrafeListener::class.java, this)
    }

    override fun onTick() {
        super.onTick()
        if (!isActive) return
        val player = client.getPlayer()
        player.setOnGround(false)
        player.abilities.flying = true
        player.deltaMovement = (Vec3.ZERO)
        val scalar = config.freeCamSpeed
        if (player.input.keyPresses.shift && scalar != 0F) player.push(0.0, -0.75 * config.freeCamSpeed, 0.0)
        else if (player.input.keyPresses.jump && scalar != 0F) player.push(0.0, 0.75 * config.freeCamSpeed, 0.0)
    }

    override fun onDisable() {
        super.onDisable()

        fake!!.remove()
        em.unsubscribe(PacketSendListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        em.unsubscribe(AirStrafeListener::class.java, this)
        client.getPlayer().abilities.flying = false
        client.getPlayer().deltaMovement = (Vec3.ZERO)
        client.worldRenderer.allChanged()

    }


    override fun onPacketSend(packet: OutputPacket) {
        if (packet.packet is ServerboundMovePlayerPacket) packet.cancel()
    }

    override fun onAirStrafe(event: AirStrafeEvent) {
        val speed = 1f * (config.freeCamSpeed)
        event.speed = speed
    }


    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        if (config.espEnabled) renderEsp(matrixStack, partialTicks)
        if (config.tracerEnabled)
            renderTracer(matrixStack, partialTicks)
    }

    private fun renderTracer(matrixStack: PoseStack, partialTicks: Float) {
        val tracerOrigin = RenderUtils.getLookVec(partialTicks).scale(10.0)
        val end = RenderUtils.getLerpedBox(fake!!, partialTicks).center
        matrixStack.pushPose()
        RenderUtils.drawSingleLine(
            matrixStack,
            tracerOrigin,
            end,
            config.color,
            config.alpha
        )
        matrixStack.popPose()
    }

    private fun renderEsp(matrixStack: PoseStack, partialTicks: Float) {
        val bb = RenderUtils.getLerpedBox(fake!!, partialTicks)
        RenderUtils.renderEntityEsp(
            matrixStack,
            bb,
            config.color,
            config.alpha,
        )
    }

}

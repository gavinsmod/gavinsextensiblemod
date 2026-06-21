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
import com.peasenet.gui.mod.misc.GuiFreeCam
import com.peasenet.gui.mod.render.GuiOreEsp
import com.peasenet.util.PlayerUtils
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Matrix3x2fStack

/**
 * A mod that allows the camera to be moved freely.
 *
 * @author GT3CH1
 * @version 01-15-2025
 * TODO: 26.2
 */
class ModFreeCam : MiscMod(
    "gavinsmod.mod.misc.freecam", "freecam"
), PacketSendListener, RenderListener, AirStrafeListener {
    private var fake: FakePlayer? = null
    var camPos: Vec3 = Vec3.ZERO
        private set
    var prevCamPos: Vec3 = Vec3.ZERO
        private set
    var camYaw = 0f
        private set
    var camPitch = 0f
        private set

    companion object {
        private val config: FreeCamConfig
            get() {
                return Settings.getConfig("freecam")
            }
    }

    init {
        clickSetting {
            title = translationKey
            callback = {
                client.setScreen(GuiFreeCam())
            }
        }

    }

    override fun activate() {
        super.activate()
//        fake = FakePlayer()
        em.subscribe(PacketSendListener::class.java, this)
        em.subscribe(RenderListener::class.java, this)
        em.subscribe(AirStrafeListener::class.java, this)
        camYaw = client.getPlayer().yRot
        camPitch = client.getPlayer().xRot
        camPos = client.getPlayer().eyePosition
    }

    override fun onTick() {
        super.onTick()
        val player = client.getPlayer()

        val moveVector = player.input.moveVector
        val yawRad = (client.gameRenderer.mainCamera.yRot() * Mth.DEG_TO_RAD).toDouble()
        val sinYaw = Mth.sin(yawRad)
        val cosYaw = Mth.cos(yawRad)
        val offsetX = ((moveVector.x * cosYaw - moveVector.y * sinYaw) * config.freeCamSpeed).toDouble()
        val offsetZ = ((moveVector.x * sinYaw + moveVector.y * cosYaw) * config.freeCamSpeed).toDouble()
        val offsetY =
            if (player.input.keyPresses.shift) -config.freeCamSpeed else if (player.input.keyPresses.jump) config.freeCamSpeed else 0.0

        val offsetVec = Vec3(offsetX, 0.0, offsetZ)
            .add(0.0, offsetY.toDouble(), 0.0)
        prevCamPos = camPos
        camPos = camPos.add(offsetVec)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(PacketSendListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        em.unsubscribe(AirStrafeListener::class.java, this)
        client.getPlayer().abilities.flying = false
        client.getPlayer().deltaMovement = (Vec3.ZERO)
        client.reloadRenderer()

    }


    override fun onPacketSend(packet: OutputPacket) {
        if (packet.packet is ServerboundMovePlayerPacket) packet.cancel()
    }

    override fun onAirStrafe(event: AirStrafeEvent) {
        val speed = 1f * (config.freeCamSpeed)
        event.speed = speed
    }


    fun getCameraPos(partialTicks: Double): Vec3 {
        return Mth.lerp(partialTicks, prevCamPos, camPos)
    }

    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        if (config.espEnabled) renderEsp(matrixStack, partialTicks)
        if (config.tracerEnabled)
            renderTracer(matrixStack, partialTicks)
    }

    private fun renderTracer(matrixStack: PoseStack, partialTicks: Float) {
        val tracerOrigin = RenderUtils.getLookVec(partialTicks).scale(10.0)
        val end = RenderUtils.getLerpedBox(client.getPlayer(), partialTicks).center
        matrixStack.pushPose()
        RenderUtils.drawSingleLine(
            matrixStack,
            tracerOrigin,
            end,
            config.color,
            config.alpha,
            partialTicks
        )
        matrixStack.popPose()
    }

    private fun renderEsp(matrixStack: PoseStack, partialTicks: Float) {
        val bb = RenderUtils.getLerpedBox(client.getPlayer(), partialTicks)
        RenderUtils.renderEntityEsp(
            matrixStack,
            bb,
            config.color,
            config.alpha,
            partialTicks
        )
    }

    fun turn(yaw: Double, pitch: Double) {
        val yDelta = yaw.toFloat() * 0.15f;
        val xDelta = pitch.toFloat() * 0.15f;
        camYaw += yDelta
        camPitch += xDelta
        camPitch = Mth.clamp(camPitch, -90.0f, 90.0f)
    }
}

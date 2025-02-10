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

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.misc.FreeCamConfig
import com.peasenet.gavui.util.Direction
import com.peasenet.main.Settings
import com.peasenet.util.FakePlayer
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.AirStrafeEvent
import com.peasenet.util.event.data.OutputPacket
import com.peasenet.util.listeners.AirStrafeListener
import com.peasenet.util.listeners.PacketSendListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11

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
        player.isOnGround = false
        player.abilities.flying = true
        player.velocity = Vec3d.ZERO
        val scalar = config.freeCamSpeed
        if (player.input.playerInput.sneak && scalar != 0F) player.addVelocity(0.0, -0.75 * config.freeCamSpeed, 0.0)
        else if (player.input.playerInput.jump && scalar != 0F) player.addVelocity(0.0, 0.75 * config.freeCamSpeed, 0.0)
    }

    override fun onDisable() {
        super.onDisable()

        fake!!.remove()
        em.unsubscribe(PacketSendListener::class.java, this)
        em.unsubscribe(RenderListener::class.java, this)
        em.unsubscribe(AirStrafeListener::class.java, this)
        client.getPlayer().abilities.flying = false
        client.getPlayer().velocity = Vec3d.ZERO
        client.worldRenderer.reload()

    }


    override fun onPacketSend(packet: OutputPacket) {
        if (packet.packet is PlayerMoveC2SPacket) packet.cancel()
    }

    override fun onAirStrafe(event: AirStrafeEvent) {
        val speed = 1f * (config.freeCamSpeed)
        event.speed = speed
    }


    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (config.espEnabled) renderEsp(matrixStack)
        if (config.tracerEnabled)
            renderTracer(matrixStack, partialTicks)
    }

    private fun renderTracer(matrixStack: MatrixStack, partialTicks: Float) {
        RenderUtils.setupRenderWithShader(matrixStack)
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        RenderUtils.drawSingleLine(bufferBuilder, entry, partialTicks, fake!!, config.color, config.alpha)
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }

    private fun renderEsp(matrixStack: MatrixStack) {
        val region = RenderUtils.getCameraRegionPos()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        matrixStack.push()
        RenderUtils.applyRegionalRenderOffset(matrixStack, region)
        val color = config.color
        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), config.alpha)
        matrixStack.push()

        matrixStack.translate(
            fake!!.x - region.x, fake!!.y, fake!!.z - region.z
        )
        matrixStack.scale(
            fake!!.width + 0.1F, fake!!.height + 0.1F, fake!!.width + 0.1F
        )
        val bb = Box(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5)
        RenderUtils.drawOutlinedBox(bb, matrixStack)
        matrixStack.pop()
        matrixStack.pop()

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
    }

}

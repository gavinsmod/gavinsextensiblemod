/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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
import com.peasenet.config.FreeCamConfig
import com.peasenet.config.TracerConfig
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.FakePlayer
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.AirStrafeEvent
import com.peasenet.util.event.data.OutputPacket
import com.peasenet.util.listeners.AirStrafeListener
import com.peasenet.util.listeners.PacketSendListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.Vec3d

/**
 * A mod that allows the camera to be moved freely.
 *
 * @author GT3CH1
 * @version 04-11-2023
 */
class ModFreeCam : MiscMod(
    "Freecam",
    "gavinsmod.mod.misc.freecam",
    "freecam"
), PacketSendListener, RenderListener, AirStrafeListener {
    private var fake: FakePlayer? = null

    companion object {
        private val config: FreeCamConfig
            get() {
                return Settings.getConfig("freecam")
            }
    }

    init {
        val freeCamSpeed = SettingBuilder()
            .setTitle("gavinsmod.generic.speed")
            .setValue(config.freeCamSpeed)
            .buildSlider()
        freeCamSpeed.setCallback { config.freeCamSpeed = freeCamSpeed.value }
        val espEnabled = SettingBuilder()
            .setTitle("gavinsmod.generic.esp")
            .setState(config.espEnabled)
            .buildToggleSetting()
        espEnabled.setCallback { config.espEnabled = espEnabled.value }
        val tracerEnabled = SettingBuilder()
            .setTitle("gavinsmod.generic.tracer")
            .setState(config.tracerEnabled)
            .buildToggleSetting()
        tracerEnabled.setCallback { config.tracerEnabled = tracerEnabled.value }
        val color = SettingBuilder()
            .setTitle("gavinsmod.generic.color")
            .setColor(config.color)
            .buildColorSetting()
        color.setCallback { config.color = color.color }
        val alpha = SettingBuilder()
            .setTitle("gavinsmod.generic.alpha")
            .setValue(1f)
            .buildSlider()
        alpha.setCallback { config.alpha = alpha.value }
        val subSetting = SettingBuilder()
            .setWidth(100f)
            .setHeight(10f)
            .setTitle(translationKey)
            .setMaxChildren(5)
            .setDefaultMaxChildren(5)
            .buildSubSetting()

        subSetting.add(espEnabled)
        subSetting.add(tracerEnabled)
        subSetting.add(color)
        subSetting.add(alpha)
        subSetting.add(freeCamSpeed)
        addSetting(subSetting)
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
        if (player.input.sneaking && scalar != 0F)
            player.addVelocity(0.0, -0.75 * config.freeCamSpeed, 0.0)
        else if (player.input.jumping && scalar != 0F)
            player.addVelocity(0.0, 0.75 * config.freeCamSpeed, 0.0)
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
        if (config.espEnabled)
            renderEsp(matrixStack, partialTicks)
        if (config.tracerEnabled)
            renderTracer(matrixStack, partialTicks)
    }

    private fun renderTracer(matrixStack: MatrixStack, partialTicks: Float) {
        RenderUtils.setupRender(matrixStack)
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.applyModelViewMatrix()
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        RenderUtils.drawSingleLine(
            bufferBuilder,
            entry,
            partialTicks,
            fake!!.boundingBox.center,
            config.color,
            config.alpha,
            true
        )
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }

    private fun renderEsp(matrixStack: MatrixStack, partialTicks: Float) {
        RenderUtils.setupRenderWithShader(matrixStack)
        val entry = matrixStack.peek().positionMatrix
        val bufferBuilder = RenderUtils.getBufferBuilder()
        RenderUtils.drawOutlinedBox(
            partialTicks,
            bufferBuilder,
            entry,
            fake!!,
            config.color,
            config.alpha,
            false
        )
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }

}

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
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
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
        val freeCamSpeed =
            SettingBuilder().setTitle("gavinsmod.generic.speed").setValue(config.freeCamSpeed).buildSlider()
        freeCamSpeed.setCallback { config.freeCamSpeed = freeCamSpeed.value }
        val espEnabled =
            SettingBuilder().setTitle("gavinsmod.generic.esp").setState(config.espEnabled).buildToggleSetting()
        espEnabled.setCallback { config.espEnabled = espEnabled.value }
        val tracerEnabled =
            SettingBuilder().setTitle("gavinsmod.generic.tracer").setState(config.tracerEnabled).buildToggleSetting()
        tracerEnabled.setCallback { config.tracerEnabled = tracerEnabled.value }
        val color = SettingBuilder().setTitle("gavinsmod.generic.color").setColor(config.color).buildColorSetting()
        color.setCallback { config.color = color.color }
        val alpha = SettingBuilder().setTitle("gavinsmod.generic.alpha").setValue(1f).buildSlider()
        alpha.setCallback { config.alpha = alpha.value }
        val subSetting = SettingBuilder().setWidth(100f).setHeight(10f).setTitle(translationKey).setMaxChildren(5)
            .setDefaultMaxChildren(5).buildSubSetting()

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
        if (config.espEnabled) renderEsp(matrixStack, partialTicks)
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

    private fun renderEsp(matrixStack: MatrixStack, partialTicks: Float) {
        val region = RenderUtils.getCameraRegionPos()
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrixStack.push()
        RenderUtils.applyRegionalRenderOffset(matrixStack, region)
        val color = config.color
        RenderSystem.setShaderColor(color.red, color.green, color.blue, config.alpha)
        matrixStack.push()

        matrixStack.translate(
            fake!!.x - region.x, fake!!.y, fake!!.z - region.z
        );
        matrixStack.scale(
            fake!!.getWidth() + 0.1F, fake!!.getHeight() + 0.1F, fake!!.getWidth() + 0.1F
        );
        var bb = Box(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5)
        RenderUtils.drawOutlinedBox(bb, matrixStack)
        matrixStack.pop()
        matrixStack.pop()

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

}

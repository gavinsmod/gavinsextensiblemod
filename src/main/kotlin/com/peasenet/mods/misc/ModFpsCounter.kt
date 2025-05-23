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

import com.peasenet.config.misc.FpsColorConfig
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.Direction
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * @author GT3CH1
 * @version 01-15-2025
 * A mod that renders the current frames per second in the top right corner of the screen.
 */
class ModFpsCounter : MiscMod(
    "gavinsmod.mod.misc.fpscounter",
    "fpscounter",
), InGameHudRenderListener {

    private companion object {
        lateinit var fpsColorConfig: FpsColorConfig
    }

    init {
        fpsColorConfig = Settings.getConfig("fpsColors")
        subSettings {
            title = translationKey
            direction = Direction.RIGHT
            toggleSetting {
                title = "gavinsmod.settings.misc.fpscolors.enabled"
                state = fpsColorConfig.isColorsEnabled
                callback = { fpsColorConfig.isColorsEnabled = it.state }
            }
            colorSetting {
                title = "gavinsmod.settings.misc.fps.color.slow"
                color = fpsColorConfig.slowFps
                callback = { fpsColorConfig.slowFps = it.color }
            }
            colorSetting {
                title = "gavinsmod.settings.misc.fps.color.ok"
                color = fpsColorConfig.okFps
                callback = { fpsColorConfig.okFps = it.color }
            }
            colorSetting {
                title = "gavinsmod.settings.misc.fps.color.fast"
                color = fpsColorConfig.fastFps
                callback = { fpsColorConfig.fastFps = it.color }
            }

        }
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onRenderInGameHud(drawContext: DrawContext, delta: Float, forceRender: Boolean) {
        if (GavinsMod.isEnabled("gui") || GavinsMod.isEnabled("settings") || !isActive || !forceRender) return
        drawFpsOverlay(drawContext)
    }

    /**
     * Draws the FPS overlay if enabled.
     *
     * @param drawContext The draw context ot use.
     */
    private fun drawFpsOverlay(drawContext: DrawContext) {
        val matrixStack = drawContext.matrices
        val fps = GavinsModClient.minecraftClient.getFps()
        val fpsString = "FPS: $fps"
        val xCoordinate = GavinsModClient.minecraftClient.window.scaledWidth - (fpsString.length * 5 + 2)
        val box = BoxF(PointF((xCoordinate - 2).toFloat(), 0f), (fpsString.length * 5 + 4).toFloat(), 12f)
        val maximumFps = GavinsModClient.minecraftClient.options.maxFps.value
        var color = GavUISettings.getColor("gui.color.foreground")
        val colorEnabled = fpsColorConfig.isColorsEnabled
        val fastColor = fpsColorConfig.fastFps
        val okColor = fpsColorConfig.okFps
        val slowFps = fpsColorConfig.slowFps
        if (colorEnabled) {
            color =
                if (fps >= maximumFps * 0.85) fastColor else if (fps > maximumFps * 0.45 && fps < maximumFps * 0.85) okColor else slowFps
        }
        GuiUtil.drawBox(GavUISettings.getColor("gui.color.background"), box, matrixStack, 0.5f)
        drawContext.drawText(client.textRenderer, Text.literal(fpsString), xCoordinate, 2, color.asInt, false)
    }
}

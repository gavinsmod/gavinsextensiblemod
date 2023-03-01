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
package com.peasenet.mods.misc

import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SubSetting
import com.peasenet.settings.ToggleSetting
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 03-01-2023
 * A mod that renders the current frames per second in the top right corner of the screen.
 */
class ModFpsCounter : Mod(Type.MOD_FPS_COUNTER), InGameHudRenderListener {
    init {
        val fpsSetting = SubSetting(100, 10, "gavinsmod.settings.misc.fpscolors")
        val fpsColors = ToggleSetting("gavinsmod.settings.misc.fpscolors.enabled")
        fpsColors.setCallback { fpsColorConfig.isColorsEnabled = fpsColors.value }
        fpsColors.value = fpsColorConfig.isColorsEnabled
        val fpsSlowColor = ColorSetting("gavinsmod.settings.misc.fps.color.slow")
        fpsSlowColor.setCallback { fpsColorConfig.slowFps = fpsSlowColor.color }
        fpsSlowColor.color = fpsColorConfig.slowFps
        val fpsOkColor = ColorSetting("gavinsmod.settings.misc.fps.color.ok")
        fpsOkColor.setCallback { fpsColorConfig.okFps = fpsOkColor.color }
        fpsOkColor.color = fpsColorConfig.okFps
        val fpsFastColor = ColorSetting("gavinsmod.settings.misc.fps.color.fast")
        fpsFastColor.setCallback { fpsColorConfig.fastFps = fpsFastColor.color }
        fpsFastColor.color = fpsColorConfig.fastFps
        fpsSetting.add(fpsColors)
        fpsSetting.add(fpsSlowColor)
        fpsSetting.add(fpsOkColor)
        fpsSetting.add(fpsFastColor)
        addSetting(fpsSetting)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onRenderInGameHud(stack: MatrixStack, delta: Float) {
        if (GavinsMod.isEnabled(Type.MOD_GUI) || GavinsMod.isEnabled(Type.SETTINGS) || !isActive) return
        drawFpsOverlay(stack)
    }

    /**
     * Draws the FPS overlay if enabled.
     *
     * @param matrixStack - The matrix stack to use.
     */
    private fun drawFpsOverlay(matrixStack: MatrixStack) {
        val textRenderer = client.textRenderer
        val fps = GavinsModClient.getMinecraftClient().fps
        val fpsString = "FPS: $fps"
        val xCoordinate = GavinsModClient.getMinecraftClient().window.scaledWidth - (fpsString.length * 5 + 2)
        val box = BoxF(PointF((xCoordinate - 2).toFloat(), 0f), (fpsString.length * 5 + 4).toFloat(), 12f)
        val maximumFps = GavinsModClient.getMinecraftClient().options.maxFps.value
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
        textRenderer.draw(matrixStack, Text.literal(fpsString), xCoordinate.toFloat(), 2f, color.asInt)
    }
}
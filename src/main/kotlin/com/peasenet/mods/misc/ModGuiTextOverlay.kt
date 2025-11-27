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

import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Mods
import com.peasenet.mods.Mod
import com.peasenet.mods.gui.GuiMod
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d

/**
 * @author GT3CH1
 * @version 01-15-2025
 * A mod that shows the currently active mods in the top left screen.
 */
class ModGuiTextOverlay : MiscMod(
    "gavinsmod.mod.misc.textoverlay",
    "textoverlay",
), InGameHudRenderListener {

    override fun onEnable() {
        super.onEnable()
        em.subscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(InGameHudRenderListener::class.java, this)
    }

    override fun onRenderInGameHud(drawContext: DrawContext, delta: Float, forceRender: Boolean) {
        modList = Mods.mods.filter { mod: Mod -> mod !is GuiMod && mod !is ModGuiTextOverlay && mod.isActive }
        if (GavinsMod.isEnabled("gui") || GavinsMod.isEnabled("settings") || modList.isEmpty() || forceRender) return
        drawTextOverlay(drawContext)
    }

    /**
     * Draws the GUI text overlay if enabled, and if the main menu is not open.
     *
     * @param drawContext- The drawContext to use
     */
    private fun drawTextOverlay(drawContext: DrawContext) {
        val matrixStack = drawContext.matrices
        val textRenderer = GavinsModClient.minecraftClient.textRenderer
        var startingPoint = PointF(0f, 0f)
        val modsCount = modList.count()
        var longestModName = 0
        for (mod in modList) {
            longestModName = textRenderer.getWidth(I18n.translate(mod.translationKey)).coerceAtLeast(longestModName)
        }
        val box = BoxF(startingPoint, (longestModName + 4).toFloat(), modsCount * 10 + 2f)
        matrixStack.pushMatrix()
        GuiUtil.drawBox(
            GavUISettings.getColor("gui.color.background"),
            box,
            matrixStack,
            GavUISettings.getFloat("gui.alpha")
        )
        matrixStack.popMatrix()
        for ((index, mod) in modList.withIndex()) {
             drawContext.drawText(
                textRenderer,
                Text.translatable(mod.translationKey),
                startingPoint.x.toInt() + 2,
                startingPoint.y.toInt() + 2,
                GavUISettings.getColor("gui.color.foreground").asInt,
                true
            )
            if (modsCount > 1 && index < modsCount - 1) {
                val p1 = Vec3d(0.0, startingPoint.y + 11.5, 0.0)
                val p2 = Vec3d(longestModName + 4.0, startingPoint.y + 11.5, 0.0)
                matrixStack.pushMatrix()
                RenderUtils.drawSingleLine(matrixStack, p1, p2, GavUISettings.getColor("gui.color.border"), 1f, false)
                matrixStack.pushMatrix()
            }
            startingPoint = startingPoint.add(0.0f, 10.0f)
        }


    }

    companion object {
        lateinit var modList: List<Mod>
    }
}

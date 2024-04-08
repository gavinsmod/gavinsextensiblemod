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
package com.peasenet.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.util.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.GameRenderer
import net.minecraft.text.Text
import java.util.function.Consumer

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A parent class that holds all that is needed to render an in game gui.
 */
open class GuiElement(title: Text?) : Screen(title) {
    /**
     * The box that contains the menu title in the top left corner of the screen.
     */
    var titleBox: Gui? = null

    /**
     * A list of gui children to render.
     */
    var guis = ArrayList<Gui>()

    /**
     * The screen to go back to when this screen is closed.
     */
    protected var parent: Screen? = null

    private lateinit var overlay: Gui

    /**
     * The previously selected/clicked gui
     */
    private var selectedGui: Gui? = null
    public override fun init() {
        super.init()
        this.client = MinecraftClient.getInstance()
        this.textRenderer = GavinsModClient.minecraftClient.textRenderer
        titleBox = GuiBuilder()
            .setTopLeft(10, 1)
            .setWidth(textRenderer.getWidth(title).toFloat() + 4f)
            .setHeight(10f)
            .setTitle(title)
            .build()
        val clientWidth = client!!.window.scaledWidth
        val clientHeight = client!!.window.scaledHeight
        //TODO: Maybe make this a background?
//        overlay = Gui(PointF(0f, 0f), clientWidth + 1, clientHeight, Text.of(""))
//        overlay.setBackground(Colors.BLACK)
        overlay = GuiBuilder()
            .setWidth((clientWidth + 1).toFloat())
            .setHeight(clientHeight.toFloat())
            .setBackgroundColor(Colors.BLACK)
            .setTransparency(0.5f)
            .build()
        titleBox!!.isHoverable = false
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        guis.forEach(Consumer { gui: Gui -> gui.mouseScrolled(mouseX, mouseY, verticalAmount) })
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }


    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for (g in guis) if (g.mouseClicked(mouseX, mouseY, button)) {
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        for (gui in guis) {
            if (Gui.getClickedGui() != null && gui.uuid == Gui.getClickedGui().uuid) {
                gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
                GavinsMod.LOGGER.info(gui.uuid.toString() + " " + Gui.getClickedGui().toString())
                return true
            } else if (gui is GuiScroll) {
                if (gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true
            } else if (!gui.isDragging && gui.mouseWithinGui(mouseX, mouseY) && Gui.getClickedGui() == null) {
                gui.isDragging = true
                selectedGui = gui
                return true
            }
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        guis.forEach(Consumer { g: Gui -> g.isDragging = false })
        selectedGui = null
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

//        super.render(drawContext, mouseX, mouseY, delta)
        assert(client != null)
        val tr = client!!.textRenderer
//        RenderSystem.setShader { GameRenderer.getPositionProgram() }
//        RenderSystem.enableBlend()
        guis.forEach(Consumer { gui: Gui -> gui.render(drawContext, tr, mouseX, mouseY, delta) })
        if (titleBox != null) {
            titleBox!!.setBackground(GavUISettings.getColor("gui.color.background"))
            titleBox!!.render(drawContext, tr, mouseX, mouseY, delta)
        }
//        RenderUtils.resetRenderSystem()
    }

    /**
     * Resets all child guis to their default positions.
     */
    fun reset() {
        guis.forEach(Consumer { obj: Gui -> obj.resetPosition() })
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        guis.forEach(Consumer { gui: Gui -> gui.mouseWithinGui(mouseX, mouseY) })
        return true
    }

    override fun close() {
        client!!.setScreen(parent)
    }
}
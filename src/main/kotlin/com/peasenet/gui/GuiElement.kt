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
package com.peasenet.gui

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiScroll
import com.peasenet.main.GavinsModClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import java.util.function.Consumer

/**
 * @author GT3CH1
 * @version 03-02-2023
 * A parent class that holds all that is needed to render an in game gui.
 */
open class GuiElement(title: Component) : Screen(title) {
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


    /**
     * The previously selected/clicked gui
     */
    private var selectedGui: Gui? = null

    public override fun init() {
        this.minecraft = Minecraft.getInstance()
        this.font = GavinsModClient.minecraftClient.textRenderer
        titleBox = GuiBuilder<Gui>()
            .setTopLeft(10, 1)
            .setWidth(font.width(title).toFloat() + 4f)
            .setHeight(11f)
            .setTitle(title)
            .build()
        super.init()
    }


    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double,
    ): Boolean {
        guis.forEach(Consumer { gui: Gui -> gui.mouseScrolled(mouseX, mouseY, verticalAmount) })
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }


    override fun mouseClicked(click: MouseButtonEvent, doubled: Boolean): Boolean {
        for (g in guis) if (g.mouseClicked(click.x, click.y, click.button())) {
            return true
        }
        return super.mouseClicked(click, doubled)
    }

    override fun isPauseScreen(): Boolean {
        return false
    }

    override fun mouseDragged(click: MouseButtonEvent, deltaX: Double, deltaY: Double): Boolean {
        val mouseX = click.x
        val mouseY = click.y
        val button = click.button()
        for (gui in guis) {
            if (Gui.clickedGui != null && gui.uUID == Gui.clickedGui!!.uUID) {
                gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
                return true
            } else if (gui is GuiScroll) {
                if (gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true
            } else if (!gui.dragging && gui.mouseWithinGui(mouseX, mouseY) && Gui.clickedGui == null) {
                gui.dragging = true
                selectedGui = gui
                return true
            }
        }
        return false
    }

    override fun mouseReleased(click: MouseButtonEvent): Boolean {

        guis.forEach(Consumer { g: Gui -> g.dragging = false })
        selectedGui = null
        return super.mouseReleased(click)
    }


    override fun render(drawContext: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(drawContext, mouseX, mouseY, delta)
        val stack = drawContext.pose()
        stack.pushMatrix()
        drawContext.guiRenderState.up()
        titleBox!!.render(drawContext, font, mouseX, mouseY, delta)
        for (gui in guis) {
            gui.render(drawContext, font, mouseX, mouseY, delta)
        }
        stack.popMatrix()
    }


    override fun repositionElements() {
        // Refresh positions of all child guis
    }

    /**
     * Resets all child guis to their default positions.
     */
    fun reset() {
        guis.forEach(Consumer { it.resetPosition() })
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        guis.forEach { it.mouseWithinGui(mouseX, mouseY) }
        return true
    }

    override fun onClose() {
        minecraft!!.setScreen(parent)
    }


}
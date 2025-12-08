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

//import net.minecraft.client.gl.ShaderProgramKeys
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiScroll
import com.peasenet.main.GavinsModClient
import com.peasenet.settings.Setting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.util.function.Consumer

/**
 * @author GT3CH1
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


    /**
     * The previously selected/clicked gui
     */
    private var selectedGui: Gui? = null

    public override fun init() {
        this.client = MinecraftClient.getInstance()
        this.textRenderer = GavinsModClient.minecraftClient.textRenderer
        titleBox = GuiBuilder<Gui>()
            .setTopLeft(10, 1)
            .setWidth(textRenderer.getWidth(title).toFloat() + 4f)
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


    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        for (g in guis) if (g.mouseClicked(click.x, click.y, click.button())) {
            return true
        }
        return super.mouseClicked(click, doubled)
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun mouseDragged(click: Click, deltaX: Double, deltaY: Double): Boolean {
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

    override fun mouseReleased(click: Click): Boolean {

        guis.forEach(Consumer { g: Gui -> g.dragging = false })
        selectedGui = null
        return super.mouseReleased(click)
    }


    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(drawContext, mouseX, mouseY, delta)
        val stack = drawContext.matrices
        stack.pushMatrix()
        drawContext.state.goUpLayer()
        titleBox!!.render(drawContext, textRenderer, mouseX, mouseY, delta)
        for (gui in guis) {
            gui.render(drawContext, textRenderer, mouseX, mouseY, delta)
        }
        stack.popMatrix()
    }


    override fun refreshWidgetPositions() {
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

    override fun close() {
        client!!.setScreen(parent)
    }

    fun addSetting(setting: Setting) {
        if(setting.gui != null)
            guis.add(setting.gui!!)
    }

}
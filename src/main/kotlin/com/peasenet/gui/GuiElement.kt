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
import com.peasenet.gavui.math.PointF
import com.peasenet.main.GavinsModClient
import com.peasenet.mixins.ScreenAccessor
import com.peasenet.settings.Setting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import java.util.function.Consumer

/**
 * @author GT3CH1
 * @version 03-02-2023
 * A parent class that holds all that is needed to render an in game gui.
 */
open class GuiElement(title: Component, columns: Int = 0) :
    Screen(Minecraft.getInstance(), GavinsModClient.minecraftClient.textRenderer, title) {


    private var guiWidth = 200
    private val elementHeight = 11f;
    private val minWidth = 100
    private val heightPadding = 12f;
    private val widthPadding = 24f;
    private val xPadding = 4f;

    private val numColumns = columns.coerceAtLeast(0)
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
        (this as ScreenAccessor).setFont(GavinsModClient.minecraftClient.textRenderer)
        titleBox = GuiBuilder<Gui>()
            .setTopLeft(10, 1)
            .setWidth(font.width(title).toFloat() + 4f)
            .setHeight(11f)
            .setTitle(title)
            .build()
        super.init()
        if(numColumns > 0)
            resizeElements()
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


    override fun extractRenderState(drawContext: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractRenderState(drawContext, mouseX, mouseY, delta)
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
        minecraft.gui.setScreen(parent)
    }

    fun addSetting(setting: Setting) {
        if (setting.gui != null)
            guis.add(setting.gui!!)
    }

    protected fun resizeElements() {
        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        val screenHeight = Minecraft.getInstance().window.guiScaledHeight
        var longestWidth = 0f
        for (setting in guis) {
            val width = Minecraft.getInstance().font.width(setting.title)
            if (width > longestWidth) {
                longestWidth = width.toFloat() + widthPadding
            }
        }
        longestWidth = longestWidth.coerceAtLeast(minWidth.toFloat())
        guiWidth = ((longestWidth) * numColumns).toInt()
        val offsetX = (screenWidth - guiWidth) / 2f
        val numRows = Math.ceil(guis.size / numColumns.toDouble()).toInt()
        val offsetY = (screenHeight - (elementHeight * numRows)) / 2f
        var guiPosition = PointF(offsetX, offsetY)
        var currentColumnIdx = 0
        var currentRowIdx = 0
        for (setting in guis) {
            setting.position = guiPosition
            setting.width = (longestWidth - xPadding/2f)
            currentColumnIdx++
            if (currentColumnIdx == numColumns) {
                currentColumnIdx = 0
                guiPosition = PointF(offsetX, guiPosition.y + heightPadding)
                currentRowIdx++
            } else {
                guiPosition =
                    guiPosition.add((longestWidth), 0f)
            }
        }
    }
}
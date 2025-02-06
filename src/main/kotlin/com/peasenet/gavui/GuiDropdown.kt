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
package com.peasenet.gavui

import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.sound.SoundEvents
import java.util.function.Consumer


/**
 * @author GT3CH1
 * @version 7/1/2022
 * A simple dropdown gui element
 */
open class GuiDropdown : GuiDraggable {
    /**
     * Gets whether the dropdown is open.
     *
     * @return Whether the dropdown is open.
     */
    /**
     * Whether the dropdown is open.
     */
    var isOpen: Boolean = false
    /**
     * Gets the direction that this dropdown will be displayed in.
     *
     * @return The direction that this dropdown will be displayed in.
     */
    /**
     * Sets the direction that this dropdown will be displayed in.
     *
     * @param direction - The direction.
     */
    /**
     * The direction in which this element will "drop" to.
     */
    var direction: Direction = Direction.DOWN

    constructor(builder: GuiBuilder<out GuiDropdown>) : super(builder) {
        this.isOpen = builder.isOpen
    }

    override fun render(drawContext: DrawContext, tr: TextRenderer, mouseX: Int, mouseY: Int, delta: Float) {
        updateSymbol()
        val textColor =
            if (frozen) GavUISettings.getColor("gui.color.frozen") else GavUISettings.getColor("gui.color.foreground")
        super.drawSymbol(drawContext, tr, textColor, null, null)
        super.render(drawContext, tr, mouseX, mouseY, delta)
        if (!isOpen) return
        val toRender = children.stream().filter { child: Gui -> !child.isHidden }
        // convert toRender to ArrayList
        val toRenderList = ArrayList(toRender.toList())
        for (i in toRenderList.indices) {
            val child = toRenderList[i]
            when (direction) {
                Direction.DOWN -> child.position =
                    PointF(x, y2 + 2 + (i * 12))

                Direction.RIGHT -> child.position = PointF(x2 + 8, y + (i * 12))
            }
            child.render(drawContext, tr, mouseX, mouseY, delta)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isOpen) {
            // If the dropdown is open, check if the mouse is within the bounds of any of the buttons.
            for (g in children) {
                if (g.mouseClicked(mouseX, mouseY, button) && !g.isHidden) {
                    for (child in children) {
                        if (child is GuiDropdown && child != g) if (child.isOpen) child.toggleMenu()
                    }
                    g.show()
                    return true
                }
            }
        }
        // Check if the mouse is within the bounds of the dropdown.
        if (super.mouseClicked(mouseX, mouseY, button)) {
            // If the dropdown is open, close it.
            toggleMenu()
            return true
        }
        return false
    }

    override fun mouseWithinGui(mouseX: Double, mouseY: Double): Boolean {
        val inMain = super.mouseWithinGui(mouseX, mouseY)
        if (isOpen) {
            for (g in children) {
                if (g.mouseWithinGui(mouseX, mouseY) && !g.isHidden) return true
            }
        }
        return inMain
    }

    /**
     * Toggles the dropdown.
     */
    fun toggleMenu() {
        isOpen = !isOpen
        if (GavUISettings.getBool("gui.sound")) {
            if (isOpen) MinecraftClient.getInstance().player!!.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5f, 1f)
            else MinecraftClient.getInstance().player!!.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5f, 1f)
        }
        for (g in children) {
            if (!isOpen) g.hide()
            else g.show()
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        // calculate the offset between the mouse position and the top left corner of the gui
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            for (children in children) {
                if (children.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true
                }
            }
            isOpen = false
            children.forEach(Consumer { obj: Gui -> obj.hide() })
            resetDropdownsLocation()
            return true
        }
        return false
    }

    override fun resetPosition() {
        super.resetPosition()
        resetDropdownsLocation()
        isOpen = false
    }

    /**
     * Resets the position of all the mods in the dropdown.
     */
    protected fun resetDropdownsLocation() {
        // copy buttons to a new array
        for (element in children) {
            if (direction == Direction.RIGHT) {
                element.position = PointF(x2 + 12, y2 + (children.size) * 12)
            }
        }
    }

    override fun addElement(element: Gui) {
        children.add(element)
        if (direction == Direction.RIGHT) {
            element.position = PointF(x2 + 12, y2 + (children.size) * 12)
        }
        element.width = width
    }

    /**
     * Sets the symbol for the dropdown based off of what direction it is displayed in.
     */
    protected fun updateSymbol() {
        symbol = ' '
        symbolOffsetX = -10
        symbolOffsetY = 2
        if (!isOpen) {
            when (direction) {
                Direction.RIGHT -> {
                    symbol = '\u25B6'
                    symbolOffsetX = -8
                }

                Direction.DOWN -> {
                    symbol = '\u25BC'
                    symbolOffsetY = 3
                    symbolOffsetX = -8
                }
            }
        }
    }


    /**
     * A direction that represents which way the dropdown will be displayed.
     */
    enum class Direction {
        DOWN,
        RIGHT
    }
}

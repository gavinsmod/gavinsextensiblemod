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

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GuiUtil
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

/**
 * The base class for all gui elements.
 * @author GT3CH1
 * @version 02-02-2025
 * @since 2/28/2023
 */
open class Gui(builder: GuiBuilder<*>) {
    /**
     * The original position of the gui.
     */
    private val defaultPosition: BoxF

    /**
     * A randomly generated UUID for this gui.
     */
    val uUID: UUID = UUID.randomUUID()

    /**
     * Children of this gui.
     */
    var children: ArrayList<Gui> = ArrayList()
        protected set

    /**
     * The title of the gui.
     */
    var title: Text?

    /**
     * The translation key for the gui.
     */
    var translationKey: String? = null
        protected set

    /**
     * The symbol for the gui.
     */
    var symbol: Char = 0.toChar()

    /**
     * The offset for the symbol.
     */
    var symbolOffsetX: Int = -10

    /**
     * The offset for the symbol.
     */
    var symbolOffsetY: Int = 1

    /**
     * How transparent the gui is.
     */
    private var transparency: Float = 0f
        get() {
            if (field == -1f) {
                return GavUI.alpha
            }
            return field
        }
        set(transparency) {
            field = transparency
            if (transparency == -1f) {
                return
            }
            if (transparency < 0) {
                field = 0f
            }
            if (transparency > 1f) {
                field = 1f
            }
        }

    /**
     * Whether this element is a parent.
     */
    var isParent: Boolean = false

    /**
     * The box of the gui.
     */
    var box: BoxF
        private set

    /**
     * The background color of the gui.
     */
    var backgroundColor: Color = Colors.INDIGO
        private set

    /**
     * Whether this gui is being dragged.
     */
    var dragging: Boolean = false
        set(value) {
            field = value
            for (child in children) child.dragging = (value)
        }

    /**
     * Whether this gui is hidden.
     */
    var isHidden: Boolean = false
        set(hidden) {
            if (hidden) {
                if (this.hasChildren()) {
                    children.forEach(Consumer { obj: Gui -> obj.hide() })
                }
            } else {
                if (this.hasChildren()) {
                    children.forEach(Consumer { obj: Gui -> obj.show() })
                }
            }
            field = hidden
        }

    /**
     * Whether this gui has been shrunk for a scrollbar.
     */
    private var shrunkForScroll = false

    /**
     * Whether we can hover over this gui.
     */
    var isHoverable: Boolean = true

    /**
     * Whether to draw the border.
     */
    var drawBorder: Boolean = true
        private set

    /**
     * Clears all children from this gui.
     */
    fun clearChildren() {
        children = ArrayList()
    }

    /**
     * Adds an child element to this gui.
     *
     * @param child - The child element to add.
     */
    open fun addElement(child: Gui) {
        if (children.isEmpty()) {
            child.position = PointF(x2 + 100, y2 + 1)
            children.add(child)
            return
        }
        // get last gui
        val lastButton = children[children.size - 1]
        val lastY = lastButton.y2
        // set new gui position
        child.position = PointF(x, lastY + 2)
        children.add(child)
    }

    /**
     * Hides this gui.
     */
    fun hide() {
        isHidden = true
    }

    /**
     * Shows this gui.
     */
    fun show() {
        isHidden = false
    }

    /**
     * Sets the background color to the given color.
     *
     * @param color - The color to set the background to.
     */
    fun setBackground(color: Color) {
        backgroundColor = color
    }

    val x: Float
        /**
         * Gets the x coordinate for the top left corner of the dropdown.
         *
         * @return The x coordinate for the top left corner of the dropdown.
         */
        get() = box.x1

    val y: Float
        /**
         * Gets the y coordinate for the top left corner of the dropdown.
         *
         * @return The y coordinate for the top left corner of the dropdown.
         */
        get() = box.y1

    val x2: Float
        /**
         * Gets the x coordinate for the bottom right corner of the dropdown.
         *
         * @return The x coordinate for the bottom right corner of the dropdown.
         */
        get() = box.x2

    val y2: Float
        /**
         * Gets the y coordinate for the bottom right corner of the dropdown.
         *
         * @return The y coordinate for the bottom right corner of the dropdown.
         */
        get() = box.y2

    var width: Float
        /**
         * Gets the width of the dropdown.
         *
         * @return The width of the dropdown.
         */
        get() = box.width
        /**
         * Sets the width of the gui.
         *
         * @param width - The width of the gui.
         */
        set(width) {
            box = BoxF(box.topLeft, width, box.height)
        }

    /**
     * Shrinks the gui to fit a scrollbar.
     *
     * @param parent - The parent gui.
     */
    fun shrinkForScrollbar(parent: Gui) {
        if (shrunkForScroll && this.width == parent.width) return
        if (this.width == parent.width) width = width - 6
        shrunkForScroll = true
    }

    val height: Float
        /**
         * Gets the height of the dropdown.
         *
         * @return The height of the dropdown.
         */
        get() = box.height

    /**
     * Renders the clickable ui
     *
     * @param drawContext The draw context used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     * @param mouseX      The x coordinate of the mouse.
     * @param mouseY      The y coordinate of the mouse.
     * @param delta       The change in time since the last render.
     */
    open fun render(drawContext: DrawContext, tr: TextRenderer, mouseX: Int, mouseY: Int, delta: Float) {
        if (isHidden) return
        val matrixStack = drawContext.matrices
        var bg = backgroundColor
        if (mouseWithinGui(mouseX, mouseY) && isHoverable) bg = bg.brighten(0.25f)
        GuiUtil.drawBox(bg, box, matrixStack, transparency)
        var textColor = GavUI.textColor()
        if (title != null) {
            if (textColor.similarity(bg) < 0.3f) {
                textColor = textColor.invert()
                if (textColor.similarity(bg) < 0.3f) textColor = Colors.WHITE
            }
            drawText(drawContext, tr, title!!, x + 2, y + 1.5f, textColor)
        }
        drawSymbol(drawContext, tr, textColor)
        if (this.drawBorder) GuiUtil.drawOutline(GavUI.borderColor(), box, matrixStack)
        renderChildren(drawContext, tr, mouseX, mouseY, delta)
    }

    /**
     * Draws the GUI symbol.
     *
     * @param drawContext - The draw context to use.
     * @param tr          - The text renderer to use.
     */
    private fun drawSymbol(drawContext: DrawContext, tr: TextRenderer, color: Color) {
        if (symbol != '\u0000') drawText(drawContext, tr, Text.of(symbol.toString()), x2 - 9f, y + 1.5f, color, false)
    }

    /**
     * Renders the children of this gui.
     *
     * @param drawContext - The draw context to use to draw the children.
     * @param tr          - The text renderer to use to draw the children.
     * @param mouseX      - The x coordinate of the mouse.
     * @param mouseY      - The y coordinate of the mouse.
     * @param delta       - The change in time since the last render.
     */
    private fun renderChildren(drawContext: DrawContext, tr: TextRenderer, mouseX: Int, mouseY: Int, delta: Float) {
        if (!hasChildren()) return
        for (c in children) c.render(drawContext, tr, mouseX, mouseY, delta)
    }

    /**
     * Checks whether the mouse is clicked.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was clicked.
     * @return Whether the mouse was clicked.
     */
    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return false
    }

    /**
     * Checks whether the mouse was dragged.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was dragged.
     * @param deltaX - The change in x coordinate.
     * @param deltaY - The change in y coordinate.
     * @return Whether the mouse was dragged.
     */
    open fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return false
    }

    /**
     * Checks whether the mouse was scrolled
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param amount - The amount of scroll.
     * @return Whether the mouse was scrolled.
     */
    open fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return false
    }

    /**
     * Whether the current window is being dragged.`
     *
     * @return True if the current window is being dragged.
     */
    @Deprecated("Use dragging instead.", ReplaceWith("dragging"))
    fun isDragging(): Boolean {
        return dragging
    }

    /**
     * Resets the position to the default position.
     */
    open fun resetPosition() {
        box = BoxF.copy(defaultPosition)
    }

    var position: PointF
        get() = box.topLeft
        set(position) {
            box.topLeft = position
        }

    /**
     * Sets the middle of the gui element to the given point.
     *
     * @param position - The point to set the middle of the gui element to.
     */
    fun setMidPoint(position: PointF) {
        box.setMiddle(position)
    }

    /**
     * Gets whether the mouse coordinates are within the bounds of the gui.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @return Whether the mouse coordinates are within the bounds of the gui.
     */
    open fun mouseWithinGui(mouseX: Double, mouseY: Double): Boolean {
        return mouseX in x..x2 && mouseY >= y && mouseY <= y2 && !isHidden
    }

    fun mouseWithinGui(mouseX: Int, mouseY: Int): Boolean {
        return mouseWithinGui(mouseX.toDouble(), mouseY.toDouble())
    }

    /**
     * Whether this element has children.
     *
     * @return True if this element has children.
     */
    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }
    fun setShrunkForScrollbar(b: Boolean) {
        shrunkForScroll = b
        if (hasChildren()) for (c in children) c.setShrunkForScrollbar(b)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Gui) {
            return other.uUID == uUID
        }
        return false
    }


    /**
     * Sets the default position of the gui.
     *
     * @param newDefaultPosition - The new default position of the gui.
     */
    fun setDefaultPosition(newDefaultPosition: BoxF) {
        defaultPosition.from(newDefaultPosition)
    }

    //NOTE: I wrote this so that I don't have to deal with the API in drawContext and I can just use the textRenderer directly.
    protected fun drawText(
        drawContext: DrawContext,
        textRenderer: TextRenderer,
        text: Text,
        x: Float,
        y: Float,
        color: Color,
        shadow: Boolean = false,
    ) {
        drawContext.drawText(textRenderer, text.asOrderedText(), x.toInt(), y.toInt(), color.asInt, shadow)
    }

    protected fun drawText(
        drawContext: DrawContext,
        textRenderer: TextRenderer,
        text: String?,
        x: Float,
        y: Float,
        color: Color,
    ) {
        drawText(drawContext, textRenderer, Text.of(text), x, y, color, false)
    }

    companion object {
        /**
         * The gui that was clicked.
         */
        @JvmStatic
        var clickedGui: Gui? = null
    }

    init {
        this.title = builder.title
        var w = builder.width
        if (title != null && MinecraftClient.getInstance().textRenderer != null) w = max(
            w.toDouble(),
            MinecraftClient.getInstance().textRenderer.getWidth(title).toDouble()
        ).toFloat()
        box = BoxF(builder.topLeft, w, builder.height)
        defaultPosition = BoxF.copy(box)
        dragging = false
        this.translationKey = builder.translationKey
        position = builder.topLeft
        setBackground(builder.backgroundColor)
        isHoverable = builder.canHover
        symbol = (builder.symbol)
        isHidden = builder.isHidden
        isHoverable = builder.canHover
        transparency = builder.transparency
        drawBorder = builder.drawBorder
        isParent = builder.isParent
    }
}

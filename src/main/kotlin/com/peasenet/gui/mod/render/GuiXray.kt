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
package com.peasenet.gui.mod.render

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.mods.Type
import net.minecraft.block.Block
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.resource.language.I18n
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import java.util.*
import kotlin.math.ceil

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A gui that allows the player to search for blocks and add them to the xray list.
 */
class GuiXray
/**
 * Creates a new GUI menu with the given title.
 */
    : GuiElement(Text.translatable("gavinsmod.mod.render.xray")) {
    /**
     * The list of currently visible blocks.
     */
    private val visibleBlocks = blockList()

    /**
     * The background gui element.
     */
    private lateinit var box: Gui

    /**
     * The button that moves to a page behind the current one.
     */
    private lateinit var prevButton: GuiClick

    /**
     * The button that moves to a page ahead of the current one.
     */
    private lateinit var nextButton: GuiClick

    /**
     * The x coordinate of the main gui.
     */
    private var x = 0

    /**
     * The y coordinate of the main gui.
     */
    private var y = 0

    /**
     * The current page of the gui.
     */
    private var page = 0

    /**
     * The number of pages in the gui.
     */
    private var pageCount = 0

    /**
     * The number of blocks per page.
     */
    private var blocksPerPage = 0

    /**
     * The number of blocks per row.
     */
    private var blocksPerRow = 0

    /**
     * The search field.
     */
    private lateinit var search: TextFieldWidget

    /**
     * The toggle element to show all blocks or just enabled blocks.
     */
    private lateinit var enabledOnly: GuiToggle

    /**
     * The reset button to load the default blocks.
     */
    private lateinit var resetButton: GuiClick

    override fun init() {
        val screenWidth = minecraftClient.window.scaledWidth
        val screenHeight = minecraftClient.window.scaledHeight
        width = (screenWidth * 0.9f).toInt()
        height = (screenHeight * 0.78f).toInt()
        x = screenWidth / 20
        y = screenHeight / 20 + 20
        box = GuiBuilder()
            .setTopLeft(x, y)
            .setWidth(width.toFloat())
            .setHeight(height.toFloat())
            .setBackgroundColor(Colors.INDIGO)
            .setTransparency(0.5f)
            .setHoverable(false)
            .build()
        val blocksPerColumn = height / 18
        blocksPerRow = width / 18
        blocksPerPage = blocksPerRow * blocksPerColumn
        pageCount = ceil(blockList().size.toDouble() / blocksPerPage).toInt()
        parent = GavinsMod.guiSettings
        search = object : TextFieldWidget(textRenderer, x + width / 2 - 75, y - 15, 150, 12, Text.empty()) {
            override fun charTyped(chr: Char, keyCode: Int): Boolean {
                val pressed = super.charTyped(chr, keyCode)
                page = 0
                updateBlockList()
                return pressed
            }

            override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
                val pressed = super.keyPressed(keyCode, scanCode, modifiers)
                page = 0
                updateBlockList()
                return pressed
            }
        }
        prevButton = GuiBuilder()
            .setTopLeft(PointF(x + (width shr 1) - 89f, y - 16f))
            .setWidth(13f)
            .setHeight(13f)
            .setCallback(this::pageDown)
            .setHoverable(true)
            .buildClick()
        nextButton = GuiBuilder()
            .setTopLeft(PointF(x + width / 2 + 76f, y - 16f))
            .setWidth(13f)
            .setHeight(13f)
            .setCallback(this::pageUp)
            .setHoverable(true)
            .buildClick()
        enabledOnly = GuiBuilder()
            .setTopLeft(PointF(x + width / 2f - 170f, y - 15f))
            .setWidth(80f)
            .setHeight(10f)
            .setTitle(Text.literal("Enabled Only"))
            .setHoverable(true)
            .setCallback { page = 0; updateBlockList() }
            .buildToggle()
        val titleW = textRenderer.getWidth(Text.translatable("gavinsmod.mod.render.xray")) + 16
        val resetText = Text.translatable("gavinsmod.settings.reset")
        val width = textRenderer.getWidth(resetText)
        if (resetPos == null) resetPos = PointF(titleW.toFloat(), 1f)
        if (resetWidth.toDouble() == 0.0) resetWidth = (width + 4).toFloat()
//        resetButton = GuiClick(resetPos, width + 8, 10, resetText)
        resetButton = GuiBuilder()
            .setTopLeft(resetPos)
            .setWidth(resetWidth)
            .setHeight(10f)
            .setTitle(resetText)
            .setBackgroundColor(Colors.DARK_RED)
            .setCallback(this::resetCallback)
            .setHoverable(true)
            .buildClick()
        resetButton.setDefaultPosition(resetButton.box)
        addSelectableChild(search)
        updateBlockList()
        super.init()
    }

    private fun resetCallback() {
        GavinsMod.xrayConfig.loadDefaultBlocks()
        updateBlockList()
        page = 0
        minecraftClient.worldRenderer.reload()
    }

    /**
     * Decrements the page by one.
     */
    private fun pageDown() {
        if (page > 0) {
            page--
            updateBlockList()
        }
    }

    /**
     * Increments the page by one.
     */
    private fun pageUp() {
        if (page < pageCount - 1) {
            page++
            updateBlockList()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        search.keyPressed(keyCode, scanCode, modifiers)
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, keyCode: Int): Boolean {
        search.charTyped(chr, keyCode)
        return super.charTyped(chr, keyCode)
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val matrixStack = drawContext.matrices
        for (i in 0 until blocksPerPage) {
            if (i > visibleBlocks.size - 1) break
            val block = visibleBlocks.toTypedArray()[i]
            val stack = block.asItem().defaultStack
            val blockX = i % blocksPerRow * 18 + x + 2
            val blockY = i / blocksPerRow * 18 + y + 5
            val boxF = BoxF(blockX.toFloat(), blockY.toFloat(), 16f, 16f)
            var isHovering = isHovering(mouseX, blockX, mouseY, blockY)
            if (GavinsMod.xrayConfig.isInList(block)) {
                var c = GavUISettings.getColor("gui.color.enabled")
                if (isHovering)
                    c = c.brighten(0.75f)
                drawContext.fill(
                    blockX,
                    blockY,
                    blockX + 16,
                    blockY + 16,
                    c.getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
            } else if (isHovering && !GavinsMod.xrayConfig.isInList(block)) {
                drawContext.fill(
                    blockX,
                    blockY,
                    boxF.x2.toInt(),
                    boxF.y1.toInt(),
                    GavUISettings.getColor("gui.color.foreground").brighten(0.5f).getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
            }
            if (isHovering)
                drawContext.drawTooltip(client!!.textRenderer, Text.translatable(stack.translationKey), mouseX, mouseY)
            drawContext.drawItem(stack, blockX, blockY)
        }
        box.isHoverable = false
        box.render(drawContext, textRenderer, mouseX, mouseY, delta)
        search.render(drawContext, mouseX, mouseY, delta)
        prevButton.render(drawContext, textRenderer, mouseX, mouseY, delta)
        nextButton.render(drawContext, textRenderer, mouseX, mouseY, delta)
        enabledOnly.render(drawContext, textRenderer, mouseX, mouseY, delta)
        resetButton.render(drawContext, textRenderer, mouseX, mouseY, delta)
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal('\u25c0'.toString()),
            x + width / 2 - 86,
            y - 13,
            Colors.WHITE.asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal('\u25b6'.toString()),
            x + width / 2 + 80,
            y - 13,
            Colors.WHITE.asInt,
            false
        )
        super.render(drawContext, mouseX, mouseY, delta)
    }

    private fun isHovering(mouseX: Int, blockX: Int, mouseY: Int, blockY: Int) =
        mouseX > blockX && mouseX < blockX + 16 && mouseY > blockY && mouseY < blockY + 16

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // check if the mouse is over the search box
        if (search.isMouseOver(mouseX, mouseY)) {
            search.mouseClicked(mouseX, mouseY, button)
            search.isFocused = true
            return true
        }
        if (prevButton.mouseWithinGui(mouseX, mouseY)) {
            prevButton.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (nextButton.mouseWithinGui(mouseX, mouseY)) {
            nextButton.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (enabledOnly.mouseWithinGui(mouseX, mouseY)) {
            enabledOnly.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (resetButton.mouseWithinGui(mouseX, mouseY)) {
            resetButton.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (!(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)) return false
        search.isFocused = false
        val blockIndex = ((mouseY - y) / 19).toInt() * blocksPerRow + ((mouseX - x) / 18).toInt()
        if (blockIndex > visibleBlocks.size - 1) return false
        val block = visibleBlocks.toTypedArray()[blockIndex]
        if (button != 0) return false
        if (GavinsMod.xrayConfig.isInList(block)) GavinsMod.xrayConfig.removeBlock(block) else GavinsMod.xrayConfig.addBlock(
            block
        )
        getMod("xray")!!.reload()
        return super.mouseClicked(mouseX, mouseY, button)
    }

    /**
     * Updates the list of currently visible blocks to reflect that of the search field, and the current page.
     */
    private fun updateBlockList() {
        val searchText = search.text.lowercase(Locale.getDefault())
        visibleBlocks.clear()
        var tmpBlocks = ArrayList<Block?>()
        blockList().stream()
            .filter { block: Block -> block.translationKey.lowercase(Locale.getDefault()).contains(searchText) }
            .forEach { e: Block? -> tmpBlocks.add(e) }
        // get blocks in block list that are within the page.
        val enabled = enabledOnly.isOn
        if (enabled) tmpBlocks = ArrayList(tmpBlocks.stream().filter { b: Block? ->
            GavinsMod.xrayConfig.isInList(
                b!!
            )
        }.toList())
        pageCount = ceil(tmpBlocks.size.toDouble() / blocksPerPage).toInt()
        for (i in page * blocksPerPage until page * blocksPerPage + blocksPerPage) {
            if (tmpBlocks.isEmpty() || i > tmpBlocks.size - 1) break
            val block = tmpBlocks[i]
            if (block != null && block.translationKey.lowercase(Locale.getDefault()).contains(searchText)) {
                if (enabled && !GavinsMod.xrayConfig.isInList(block)) continue
                visibleBlocks.add(block)
            }
        }
    }

    companion object {
        private var resetWidth = 0f
        private var resetPos: PointF? = null

        /**
         * Gets the list of all blocks that do not translate to "air".
         *
         * @return The list of all blocks.
         */
        private fun blockList(): LinkedHashSet<Block> {
            val list = ArrayList<Block>()
            Registries.BLOCK.stream().sorted(Comparator.comparing { a: Block -> I18n.translate(a.translationKey) })
                .filter { b: Block -> !b.asItem().translationKey.contains("air") }.forEach { e: Block -> list.add(e) }
            return LinkedHashSet(list)
        }
    }
}

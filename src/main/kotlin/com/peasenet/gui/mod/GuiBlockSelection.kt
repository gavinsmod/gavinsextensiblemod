﻿/*
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
package com.peasenet.gui.mod

import com.peasenet.config.commons.BlockListConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.main.Settings
import com.peasenet.settings.ClickSetting
import com.peasenet.settings.ToggleSetting
import com.peasenet.settings.clickSetting
import com.peasenet.settings.toggleSetting
import net.minecraft.block.Block
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.resource.language.I18n
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import java.util.*
import kotlin.math.ceil

/**
 * A gui that allows the player to search for blocks and add them to a list.
 * @param T The type of the block list configuration, must extend BlockListConfig.
 * @param translationKey The translation key for the gui.
 * @param settingKey The key for the setting.
 * @see BlockListConfig
 * @author GT3CH1
 * @version 09-01-2024
 * @since 04-11-2023
 */
open class GuiBlockSelection<T : BlockListConfig<*>>(
    val translationKey: String, private val settingKey: String,
)
/**
 * Creates a new GUI menu with the given title.
 */
    : GuiElement(Text.translatable(translationKey)) {
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
    private lateinit var prevButton: ClickSetting

    /**
     * The button that moves to a page ahead of the current one.
     */
    private lateinit var nextButton: ClickSetting

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
    private lateinit var enabledOnly: ToggleSetting

    /**
     * The reset button to load the default blocks.
     */
    private lateinit var resetButton: ClickSetting

    override fun init() {
        val screenWidth = minecraftClient.window.scaledWidth
        val screenHeight = minecraftClient.window.scaledHeight
        width = (screenWidth * 0.9f).toInt()
        height = (screenHeight * 0.8f).toInt()
        x = screenWidth / 20
        y = screenHeight / 20 + 20
        box = GuiBuilder<Gui>()
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
        prevButton = clickSetting {
            topLeft = PointF(x + (this@GuiBlockSelection.width shr 1) - 89f, y - 16f)
            width = 13f
            height = 13f
            callback = {
                if (page > 0) {
                    page--
                    updateBlockList()
                }
            }
            hoverable = true
        }
        nextButton = clickSetting {
            topLeft = PointF(x + this@GuiBlockSelection.width / 2f + 76f, y - 16f)
            width = 13f
            height = 13f
            callback = {
                pageUp()
            }
            hoverable = true
        }
        enabledOnly = toggleSetting {
            topLeft = PointF(x + this@GuiBlockSelection.width / 2f - 170f, y - 15f)
            width = 80f
            height = 10f
            title = "gavinsmod.generic.enabledOnly"
            callback = {
                page = 0
                updateBlockList()
            }
        }

        val resetText = Text.translatable("gavinsmod.settings.reset")
        val width = textRenderer.getWidth(resetText)
        addSelectableChild(search)
        updateBlockList()
        super.init()

        if (titleBox != null)
            resetPos = PointF(titleBox!!.x2 + 4f, titleBox!!.y)
        if (resetWidth.toDouble() == 0.0) resetWidth = (width + 4).toFloat()
        resetButton = clickSetting {
            topLeft = resetPos
            this.width = resetWidth
            height = 10f
            title = "gavinsmod.settings.reset"
            color = Colors.DARK_RED
            callback = { resetCallback() }
            hoverable = true
        }


        guis.add(prevButton.gui)
        guis.add(nextButton.gui)
        guis.add(enabledOnly.gui)
        guis.add(resetButton.gui)
        
    }

    private fun resetCallback() {
        Settings.getConfig<T>(settingKey).loadDefaultBlocks()
        updateBlockList()
        page = 0
        minecraftClient.worldRenderer.reload()
        getMod(settingKey)?.reload()
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

        box.render(drawContext, textRenderer, mouseX, mouseY, delta)
        super.render(drawContext, mouseX, mouseY, delta)
        for (i in 0 until blocksPerPage) {
            if (i > visibleBlocks.size - 1) break
            val block = visibleBlocks.toTypedArray()[i]
            val stack = block.asItem().defaultStack
            val blockX = i % blocksPerRow * 18 + x + 1
            val blockY = i / blocksPerRow * 18 + y + 3
            val boxF = BoxF(blockX.toFloat(), blockY.toFloat(), 16f, 16f)
            val isHovering = isHovering(mouseX, blockX, mouseY, blockY)
            if (Settings.getConfig<T>(settingKey).isInList(block)) {
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
            } else if (isHovering && !Settings.getConfig<T>(settingKey).isInList(block)) {
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
                drawContext.drawTooltip(
                    client!!.textRenderer,
                    Text.translatable(stack.item.translationKey),
                    mouseX,
                    mouseY
                )
            drawContext.drawItem(stack, blockX, blockY)
        }
        box.canHover = false
        search.render(drawContext, mouseX, mouseY, delta)
//        prevButton.gui.render(drawContext, textRenderer, mouseX, mouseY, delta)
//        nextButton.gui.render(drawContext, textRenderer, mouseX, mouseY, delta)
//        enabledOnly.gui.render(drawContext, textRenderer, mouseX, mouseY, delta)
//        resetButton.gui.render(drawContext, textRenderer, mouseX, mouseY, delta)
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
        if (prevButton.gui.mouseWithinGui(mouseX, mouseY)) {
            prevButton.gui.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (nextButton.gui.mouseWithinGui(mouseX, mouseY)) {
            nextButton.gui.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (enabledOnly.gui.mouseWithinGui(mouseX, mouseY)) {
            enabledOnly.gui.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (resetButton.gui.mouseWithinGui(mouseX, mouseY)) {
            resetButton.gui.mouseClicked(mouseX, mouseY, button)
            return true
        }
        if (!(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)) return false
        search.isFocused = false
        val blockIndex = ((mouseY - y) / 19).toInt() * blocksPerRow + ((mouseX - x) / 18).toInt()
        if (blockIndex > visibleBlocks.size - 1) return false
        val block = visibleBlocks.toTypedArray()[blockIndex]
        if (button != 0) return false
        if (Settings.getConfig<T>(settingKey).isInList(block)) Settings.getConfig<T>(settingKey)
            .removeBlock(block) else Settings.getConfig<T>(settingKey).addBlock(
            block
        )
        getMod(settingKey)!!.reload()
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
        val enabled = enabledOnly.state
        if (enabled) tmpBlocks = ArrayList(tmpBlocks.stream().filter { b: Block? ->
            Settings.getConfig<T>(settingKey).isInList(
                b!!
            )
        }.toList())
        pageCount = ceil(tmpBlocks.size.toDouble() / blocksPerPage).toInt()
        for (i in page * blocksPerPage until page * blocksPerPage + blocksPerPage) {
            if (tmpBlocks.isEmpty() || i > tmpBlocks.size - 1) break
            val block = tmpBlocks[i]
            if (block != null && block.translationKey.lowercase(Locale.getDefault()).contains(searchText)) {
                if (enabled && !Settings.getConfig<T>(settingKey).isInList(block)) continue
                visibleBlocks.add(block)
            }
        }
    }

    companion object {
        private var resetWidth = 0f
        private var resetPos: PointF = PointF(0, 0)

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

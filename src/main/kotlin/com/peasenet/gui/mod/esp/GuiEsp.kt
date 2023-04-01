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
package com.peasenet.gui.mod.esp

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.Gui
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
import com.peasenet.mods.Mod
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.Registries
import net.minecraft.text.Text

/**
 * @author gt3ch1
 * @version 04-01-2023
 * A gui that allows the player to search for blocks and add them to the xray list.
 */
class GuiEsp
/**
 * Creates a new GUI menu with the given title.
 */
    : GuiElement(Text.translatable("gavinsmod.settings.mobesp")) {
    /**
     * The background gui element.
     */
    private var box: Gui? = null

    /**
     * The button that moves to a page behind the current one.
     */
    private var prevButton: GuiClick? = null

    /**
     * The button that moves to a page ahead of the current one.
     */
    private var nextButton: GuiClick? = null

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
     * The search field.
     */
    private var search: TextFieldWidget? = null

    /**
     * The toggle element to show all blocks or just enabled blocks.
     */
    private var enabledOnly: GuiToggle? = null


    private lateinit var hostileEspColor: ColorSetting
    private lateinit var peacefulEspColor: ColorSetting
    private lateinit var hostileEspToggle: ToggleSetting
    private lateinit var peacefulEspToggle: ToggleSetting


    private companion object {
        const val blocksPerRow: Int = 10
        const val blocksPerColumn = 8
        const val blockOffset: Int = 18
        const val searchMaxWidth = 150
        const val pageWidth = blocksPerRow * blockOffset
        const val pageHeight = blocksPerColumn * blockOffset
        const val itemsPerPage = blocksPerRow * blocksPerColumn
        var items = ArrayList<ItemStack>()
        var visibleItems = ArrayList<ItemStack>()
        val numPages: Int
            get() = visibleItems.size / (blocksPerRow * blocksPerColumn)
        var currentPage = 0
        val pageOffset: Int
            get() = currentPage * itemsPerPage
    }

    private fun pageUp() {
        if (currentPage < numPages) currentPage++
    }

    private fun pageDown() {
        if (currentPage > 0) currentPage--
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        search!!.isFocused = false
        if (guis.any { it.mouseClicked(mouseX, mouseY, button) }) {
            return true
        }
        if (search!!.isMouseOver(mouseX, mouseY)) {
            search!!.mouseClicked(mouseX, mouseY, button)
            search!!.isFocused = true
            return true
        }
        if (!(mouseX > x && mouseX < x + pageWidth && mouseY > y && mouseY < y + pageHeight)) return false
        search!!.isFocused = false
        val blockIndex = ((mouseY - y) / 19).toInt() * blocksPerRow + ((mouseX - x) / 18).toInt()
        if (blockIndex > visibleItems.size - 1) return false
        val block = visibleItems[blockIndex]
        var spawnEgg = (block.item as SpawnEggItem)
        if (button != 0) return false
        if (GavinsMod.espConfig!!.mobIsShown(spawnEgg)) GavinsMod.espConfig!!.removeMob(spawnEgg)
        else GavinsMod.espConfig!!.addMob(spawnEgg)
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun charTyped(chr: Char, keyCode: Int): Boolean {
        search!!.charTyped(chr, keyCode)
        return super.charTyped(chr, keyCode)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        search!!.keyPressed(keyCode, scanCode, modifiers)
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun init() {
        val screenWidth = minecraftClient.window.scaledWidth
        val screenHeight = minecraftClient.window.scaledHeight
        x = screenWidth - screenWidth / 2 - pageWidth / 2
        y = screenHeight - screenHeight / 2 - pageHeight / 2
        box = Gui(PointF(x.toFloat(), y.toFloat()), pageWidth, pageHeight, Text.literal(""))
        box!!.setBackground(Colors.INDIGO)
        parent = GavinsMod.guiSettings
        box!!.isHoverable = false
        items = ArrayList()
        val entities = Registries.ENTITY_TYPE.forEach {
            val spawnEgg = SpawnEggItem.forEntity(it)
            if (spawnEgg != null) items.add(spawnEgg.defaultStack)
        }
        visibleItems = items
        nextButton = GuiClick(PointF((x + pageWidth - 30f), (y + pageHeight).toFloat()), 30, 10, Text.literal("Next"))
        nextButton!!.setCallback { pageUp() }
        prevButton = GuiClick(PointF(x.toFloat(), (y + pageHeight).toFloat()), 30, 10, Text.literal("Prev"))
        prevButton!!.setCallback { pageDown() }
        val searchWidth = searchMaxWidth.coerceAtMost(pageWidth)
        search = object :
            TextFieldWidget(textRenderer, (x + (pageWidth - searchWidth) / 2), y - 15, searchWidth, 12, Text.empty()) {
            override fun charTyped(chr: Char, keyCode: Int): Boolean {
                val pressed = super.charTyped(chr, keyCode)
                currentPage = 0
                updateBlockList()
                return pressed
            }

            override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
                val pressed = super.keyPressed(keyCode, scanCode, modifiers)
                currentPage = 0
                updateBlockList()
                return pressed
            }
        }

        addSelectableChild(search)
        var tw = textRenderer.getWidth("Enabled Only")
        var height = 24f;
        var pos = PointF(10f, height)


        hostileEspColor = ColorSetting(
            "gavinsmod.settings.esp.mob.hostile.color", Mod.espConfig.hostileMobColor
        )
        hostileEspColor.setCallback { Mod.espConfig.hostileMobColor = hostileEspColor.color }
        hostileEspColor.color = Mod.espConfig.hostileMobColor
        hostileEspColor.gui.position = pos
        pos = pos.add(0f, 12f)

        peacefulEspColor = ColorSetting(
            "gavinsmod.settings.esp.mob.peaceful.color", Mod.espConfig.peacefulMobColor
        )
        peacefulEspColor.setCallback { Mod.espConfig.peacefulMobColor = peacefulEspColor.color }
        peacefulEspColor.color = Mod.espConfig.peacefulMobColor
        peacefulEspColor.gui.position = pos
        pos = pos.add(0f, 12f)

        enabledOnly = GuiToggle(pos, tw + 12, 10, Text.literal("Enabled Only"))
        enabledOnly!!.setCallback {
            updateBlockList()
        }
        pos = pos.add(0f, 12f)

        hostileEspToggle = ToggleSetting("gavinsmod.settings.esp.mob.hostile")
        hostileEspToggle.setCallback { Mod.espConfig.showHostileMobs = hostileEspToggle.value }
        hostileEspToggle.value = Mod.espConfig.showHostileMobs
        hostileEspToggle.gui.position = pos
        pos = pos.add(0f, 12f)

        peacefulEspToggle = ToggleSetting("gavinsmod.settings.esp.mob.peaceful")
        peacefulEspToggle.setCallback { Mod.espConfig.showPeacefulMobs = peacefulEspToggle.value }
        peacefulEspToggle.value = Mod.espConfig.showPeacefulMobs
        peacefulEspToggle.gui.position = pos

        tw = textRenderer.getWidth(I18n.translate(hostileEspColor.translationKey))
        tw = Math.max(tw, textRenderer.getWidth(I18n.translate(peacefulEspColor.translationKey)))
        tw = Math.max(tw, textRenderer.getWidth(I18n.translate(hostileEspToggle.translationKey)))
        tw = Math.max(tw, textRenderer.getWidth(I18n.translate(peacefulEspToggle.translationKey)))
        tw += 12
        hostileEspColor.gui.width = (tw).toFloat()
        peacefulEspColor.gui.width = (tw).toFloat()
        hostileEspToggle.gui.width = (tw).toFloat()
        peacefulEspToggle.gui.width = (tw).toFloat()
        enabledOnly!!.width = (tw).toFloat()
        guis.clear()
        guis.add(box!!)
        guis.add(enabledOnly!!)
        guis.add(peacefulEspToggle.gui)
        guis.add(peacefulEspColor.gui)
        guis.add(hostileEspToggle.gui)
        guis.add(hostileEspColor.gui)
        guis.add(nextButton!!)
        guis.add(prevButton!!)

        updateBlockList()

        super.init()
    }

    private fun updateBlockList() {
        var itms = items.filter {
            I18n.translate(it.translationKey).lowercase().contains(
                search!!.text.lowercase()
            )
        }
        if (enabledOnly!!.isOn) {
            itms = ArrayList(itms.filter {
                GavinsMod.espConfig!!.mobIsShown((it.item as SpawnEggItem))
            })
        }
        visibleItems = ArrayList(itms)

        if (visibleItems.size <= itemsPerPage) {
            prevButton!!.hide()
            nextButton!!.hide()
        } else {
            prevButton!!.show()
            nextButton!!.show()
        }
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {

        guis.forEach { it.render(matrixStack, textRenderer, mouseX, mouseY, delta) }
        search!!.render(matrixStack, mouseX, mouseY, delta)
        RenderSystem.disableBlend();
        for (i in 0 until itemsPerPage) {
            if ((pageOffset + i) > visibleItems.size - 1) break
            val block = visibleItems[pageOffset + i]
            val blockX = i % blocksPerRow * blockOffset + x + 1
            val blockY = i / blocksPerRow * blockOffset + y + 1
            val boxF = BoxF(blockX.toFloat(), blockY.toFloat(), 16f, 16f)
            var spawnEggItem = (block.item as SpawnEggItem)
            if (GavinsMod.espConfig!!.mobIsShown(spawnEggItem)) {
                fill(
                    matrixStack,
                    blockX,
                    blockY,
                    blockX + 16,
                    blockY + 16,
                    GavUISettings.getColor("gui.color.enabled").getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
            }
            if (mouseX > blockX && mouseX < blockX + 16 && mouseY > blockY && mouseY < blockY + 16) {
                fill(
                    matrixStack,
                    blockX,
                    blockY,
                    boxF.x2.toInt(),
                    boxF.y1.toInt(),
                    GavUISettings.getColor("gui.color.foreground").brighten(0.5f).getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
                renderTooltip(matrixStack, Text.translatable(block.translationKey), mouseX, mouseY)
            }
            client!!.itemRenderer.renderGuiItemIcon(matrixStack, block, blockX, blockY)
        }
        RenderSystem.enableBlend();
        super.render(matrixStack, mouseX, mouseY, delta)
    }
}

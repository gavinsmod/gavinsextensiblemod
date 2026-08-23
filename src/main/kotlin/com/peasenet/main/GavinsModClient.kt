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
package com.peasenet.main

import com.mojang.math.Axis
import com.peasenet.gavui.Gui
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gui.GuiMainMenu
import com.peasenet.gui.GuiSettings
import com.peasenet.gui.mod.*
import com.peasenet.main.GavinsMod.Companion.guiList
import com.peasenet.mixinterface.IClientPlayerEntity
import com.peasenet.mixinterface.IMinecraftClient
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.ModCommands
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.EventManager
import com.peasenet.util.event.TextRenderEvent
import com.peasenet.util.event.data.LevelRenderEndEventData
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer

/**
 * @author GT3CH1
 * @version 04-11-2023
 * The main part of the mod that handles checking mods.
 */
class GavinsModClient : ClientModInitializer {


    /**
     * Hook for chat commands.
     */
    private var modCommands: ModCommands? = null


    override fun onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized")

        ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick {
            if (player == null) return@StartTick
            for (m in Mods.mods) {
                m.checkKeybinding()
                if (m.isActive || m.isDeactivating) m.onTick()
            }
        })
        LevelRenderEvents.END_MAIN.register { context ->
            val eventData = LevelRenderEndEventData()
            val event = TextRenderEvent(eventData)
            EventManager.eventManager.call(event)
            if (event.isCancelled) return@register
            val target = eventData.targetVec
            val camera = minecraftClient.gameRenderer.mainCamera
            val cameraPos = camera.position().add(RenderUtils.getLookVec())
            val poseStack = context.poseStack()

            poseStack.pushPose()
            val translation = target.subtract(cameraPos)
            poseStack.translate(translation.x, translation.y, translation.z)
            val yaw = camera.yRot()
            val pitch = camera.xRot()
            poseStack.scale(eventData.scale, eventData.scale, eventData.scale)
            poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw))
            poseStack.mulPose(Axis.XN.rotationDegrees(pitch))
            val text = Component.literal(eventData.textToDraw)
            val textWidth = minecraftClient.textRenderer.width(eventData.textToDraw)
            val xOffset = -textWidth / 2.0f
            context.submitNodeCollector().submitText(
                poseStack,
                xOffset,
                0.0f,
                text.visualOrderText,
                eventData.dropShadow,
                eventData.displayMode,
                eventData.lightCoords,
                eventData.textColor.asInt,
               eventData.backgroundColor.asInt,
                eventData.outlineColor.asInt
            )
            poseStack.popPose()
        }



        guiSettings = GuiSettings()
        modCommands = ModCommands()
        setMainGui()
    }


    companion object {
        /**
         * Gets the minecraft client.
         * @see IMinecraftClient
         * @return The minecraft client.
         */
        val minecraftClient: IMinecraftClient
            get() = Minecraft.getInstance() as IMinecraftClient

        /**
         * Gets the minecraft client player.
         * @see IClientPlayerEntity
         * @return The minecraft client player.
         */
        val player: IClientPlayerEntity?
            get() = Minecraft.getInstance().player as IClientPlayerEntity?

        val modsToLoad = ArrayList<Mod>()


        /**
         * The gui used to display the main mod menu.
         */
        lateinit var gui: GuiMainMenu

        /**
         * The gui used to display the settings menu.
         */
        lateinit var guiSettings: GuiSettings


        fun setMainGui() {
            modsToLoad.forEach(Consumer { m: Mod -> Mods.addMod(m) })
            guiList[ModCategory.MOVEMENT] = GuiMovement()
            guiList[ModCategory.COMBAT] = GuiCombat()
            guiList[ModCategory.ESP] = GuiESP()
            guiList[ModCategory.MISC] = GuiMisc()
            val guiRender = GuiRender()
            // fix for issue #55
            val guis = ModGuiUtil.getGuiToggleFromCategory(
                ModCategory.WAYPOINTS, BoxF(guiRender.position, guiRender.width, guiRender.height)
            )
            guis.forEach { guiRender.addElement(it) }
            guiList[ModCategory.RENDER] = guiRender
            guiList[ModCategory.TRACERS] = GuiTracers()
            guiList.values.forEach(Consumer { g: Gui -> g.isParent = true })
            // remove all the guis that have no children
            guiList.values.removeIf { g: Gui -> g.children.isEmpty() }
            // collect all the guis that have children into an array list
            val guisWithChildren = ArrayList<Gui>()
            guiList.values.forEach { guisWithChildren.add(it) }
            gui = GuiMainMenu(guisWithChildren)
        }
    }
}
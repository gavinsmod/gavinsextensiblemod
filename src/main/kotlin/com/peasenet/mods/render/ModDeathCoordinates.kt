package com.peasenet.mods.render

import com.peasenet.gavui.color.Colors
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.util.ChatCommand
import com.peasenet.util.listeners.InGameHudRenderListener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import kotlin.jvm.optionals.getOrNull

/**
 * Draws the player's last death coordinates on the in-game HUD.
 * @author GT3CH1
 * @version 12-29-2025
 * @since 12-29-2025
 */
class ModDeathCoordinates :
    RenderMod("gavinsmod.mod.render.deathcoordinates", ChatCommand.DeathCoordinates.command),
    InGameHudRenderListener {
    override fun onEnable() {
        em.subscribe(InGameHudRenderListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(InGameHudRenderListener::class.java, this)
        super.onDisable()
    }

    override fun onRenderInGameHud(drawContext: GuiGraphics, delta: Float, forceRender: Boolean) {
        if (GavinsMod.isEnabled("gui") || GavinsMod.isEnabled("settings") || !isActive) return
        val yCoordinate = if (GavinsMod.isEnabled(ChatCommand.FpsCounter)) 12 else 2
        val lastDeathCoordinates = Minecraft.getInstance().player?.lastDeathLocation?.getOrNull()?.pos ?: return
        val x = lastDeathCoordinates.x
        val y = lastDeathCoordinates.y
        val z = lastDeathCoordinates.z
        val text = "Death Coordinates: X: §l$x§r Y: §l$y§r Z: §l$z"
        val xCoordinate =
            GavinsModClient.minecraftClient.window.guiScaledWidth - (GavinsModClient.minecraftClient.textRenderer.width(
                text
            )) - 2

        drawContext.drawString(
            GavinsModClient.Companion.minecraftClient.textRenderer,
            text,
            xCoordinate,
            yCoordinate,
            Colors.WHITE.asInt,
            true
        )
    }
}
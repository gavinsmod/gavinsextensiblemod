package com.peasenet.mods.tracer

import com.mojang.blaze3d.vertex.PoseStack
import com.peasenet.extensions.toVec3d
import com.peasenet.gavui.color.Colors
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import net.minecraft.client.Minecraft
import kotlin.jvm.optionals.getOrNull

/**
 * Draws a tracer line to the player's last death location.
 *
 * @author GT3CH1
 * @version 12-29-2025
 * @since 12-29-2025
 */
class ModDeathTracer :
    TracerMod<ModDeathTracer>("gavinsmod.mod.tracer.deathtracer", ChatCommand.DeathTracer.command) {
    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {

        val lastDeathCoordinates = Minecraft.getInstance().player?.lastDeathLocation?.getOrNull()?.pos ?: return
        val tracerOrigin = RenderUtils.getLookVec(partialTicks).scale(10.0)
        val end = lastDeathCoordinates.toVec3d()
        RenderUtils.drawSingleLine(
            matrixStack,
            tracerOrigin,
            end,
            Colors.RED_ORANGE,
            config.alpha,
        )
    }
}
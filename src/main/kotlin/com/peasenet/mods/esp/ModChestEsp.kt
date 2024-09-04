/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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
package com.peasenet.mods.esp

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.config.EspConfig
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.GavChunk
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.listeners.BlockEntityRenderListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.EnderChestBlockEntity
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the client to see an esp (a box) around chests.
 */
class ModChestEsp : EspMod<BlockEntity>(
    "Chest ESP",
    "gavinsmod.mod.esp.chest",
    "chestesp"
), RenderListener {
    init {
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.chest.color")
            .setColor(config.chestColor)
            .buildColorSetting()
        colorSetting.setCallback {
            config.chestColor = colorSetting.color
        }
        addSetting(colorSetting)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(RenderListener::class.java, this)
    }


    override fun onTick() {
        super.onTick()
        entityList.clear()
        val level = MinecraftClient.getInstance().world
        for (x in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
            for (z in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
                val chunk = level!!.getChunk(x + client.getPlayer().chunkPos.x, z + client.getPlayer().chunkPos.z)
                for ((_, blockEntity) in chunk.blockEntities) {
                    if (blockEntity is ChestBlockEntity || blockEntity is ShulkerBoxBlockEntity
                        || blockEntity is EnderChestBlockEntity
                    ) {
                        entityList.add(blockEntity)
                    }
                }
            }
        }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (entityList.isEmpty())
            return
        RenderUtils.setupRender(matrixStack)
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        entityList.forEach { e ->
            matrixStack.push()
            val pos = e.pos.subtract(RenderUtils.getCameraRegionPos().toVec3i())
            val bb = Box(
                pos.x.toDouble() + 1, pos.y.toDouble(), pos.z.toDouble() + 1,
                pos.x.toDouble(), pos.y + 1.0, pos.z.toDouble()
            )
            RenderUtils.drawOutlinedBox(bb, vertexBuffer!!, matrixStack, config.chestColor, 1.0f)
            matrixStack.pop()
        }
        RenderUtils.cleanupRender(matrixStack)
    }
}

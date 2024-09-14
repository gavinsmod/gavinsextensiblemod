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

import com.peasenet.config.BlockEspConfig
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.mods.ModCategory
import com.peasenet.mods.commons.BlockEspTracerCommon
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.SubSetting
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 09-06-2024
 * @since 09-01-2024
 * @see EspMod
 * @see BlockEspTracerCommon
 */
class ModBlockEsp : BlockEspTracerCommon<BlockEspConfig>("Block ESP",
    "gavinsmod.mod.esp.blockesp",
    "blockesp",
    ModCategory.ESP,
    { minecraftClient.setScreen(GuiBlockEsp()) }), RenderListener {

    private var count = 0

    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
    }

    companion object {
        fun getSettings(): BlockEspConfig = Settings.getConfig("blockesp") as BlockEspConfig
    }

    override fun preInit(subSetting: SubSetting) {
        // add structure toggle
        val toggleSetting = SettingBuilder()
            .setTitle("structure")
            .setState(getSettings().structureEsp)
            .buildToggleSetting()
        toggleSetting.setCallback { getSettings().structureEsp = toggleSetting.value }
        subSetting.add(toggleSetting)
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (chunks.isEmpty()) return
        count = 0
        RenderUtils.setupRenderWithShader(matrixStack)
        val bufferBuilder = RenderUtils.getBufferBuilder()
        synchronized(chunks) {
            val renderableChunks = chunks.values.filter { it.inRenderDistance() }.sortedBy { it.getRenderDistance() }
            renderableChunks.forEach {
                it.render(
                    matrixStack,
                    bufferBuilder,
                    getSettings().blockColor,
                    getSettings().alpha,
                    getSettings().structureEsp
                )
            }
        }
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }
}
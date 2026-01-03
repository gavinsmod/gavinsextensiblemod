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
package com.peasenet.mods.render

import com.peasenet.config.render.XrayConfig
import com.peasenet.gavui.util.Direction
import com.peasenet.gui.mod.render.GuiXray
import com.peasenet.main.GavinsMod
import com.peasenet.main.Settings
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.DrawState
import com.peasenet.util.event.data.TessellateBlock
import com.peasenet.util.listeners.BlockEntityRenderListener
import com.peasenet.util.listeners.ShouldDrawSideListener
import com.peasenet.util.listeners.TessellateBlockListener
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.client.Minecraft

/**
 * @author GT3CH1
 * @version 01-15-2025
 * A mod for xray like feature, allowing the player to see through certain blocks.
 */
class ModXray : RenderMod(
    "gavinsmod.mod.render.xray",
    "xray"
), ShouldDrawSideListener, TessellateBlockListener, BlockEntityRenderListener {
    init {
        subSettings {
            title = translationKey
            direction = Direction.RIGHT
            toggleSetting {
                title = "gavinsmod.settings.xray.culling"
                callback = {
                    Settings.getConfig<XrayConfig>("xray").blockCulling = it.state
                    if (isActive) reloadRenderer()
                }
            }
            toggleSetting {
                title = "gavinsmod.settings.xray.liquids"
                callback = {
                    Settings.getConfig<XrayConfig>("xray").showLiquids = it.state
                    if (isActive) reloadRenderer()
                }
            }
            clickSetting {
                title = "gavinsmod.settings.xray.blocks"
                callback = {
                    Minecraft.getInstance().setScreen(GuiXray())
                }
            }
        }
    }

    override fun onEnable() {
        em.subscribe(ShouldDrawSideListener::class.java, this)
        em.subscribe(TessellateBlockListener::class.java, this)
        em.subscribe(BlockEntityRenderListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(ShouldDrawSideListener::class.java, this)
        em.unsubscribe(TessellateBlockListener::class.java, this)
        em.unsubscribe(BlockEntityRenderListener::class.java, this)
        super.onDisable()
    }

    override fun activate() {
        client.setChunkCulling(Settings.getConfig<XrayConfig>("xray").blockCulling)
        super.activate()
        reloadRenderer()
    }

    /**
     * Reloads the renderer if and only if the setting "xray.forcereload" is true.
     */
    private fun reloadRenderer() {
        client.worldRenderer.allChanged()
    }

    override fun onTick() {
        if (isActive && !RenderUtils.isHighGamma) RenderUtils.setHighGamma() else if (!GavinsMod.isEnabled("fullbright") && !RenderUtils.isLastGamma && deactivating) {
            RenderUtils.setLowGamma()
            deactivating = !RenderUtils.isLastGamma
        }
    }

    override fun deactivate() {
        // check if full bright is disabled, if it is, reset gamma back to LAST_GAMMA
        if (!GavinsMod.isEnabled(ChatCommand.FullBright))
            RenderUtils.setLowGamma()
        client.setChunkCulling(true)
        reloadRenderer()
        deactivating = true
        RenderUtils.gamma = 4.0
        super.deactivate()
    }

    override fun onDrawSide(event: DrawState) {
        if (!isActive) return
        event.setShouldDraw(shouldDrawFace(event.state))
    }

    override fun onTessellateBlock(event: TessellateBlock) {
        if (!shouldDrawFace(event.blockState)) event.cancel()
    }

    override fun onRenderBlockEntity(er: BlockEntityRender) {
        if (!shouldDrawFace(client.getWorld().getBlockState(er.entity.blockPos))) er.cancel()
    }

    companion object {
        /**
         * Checks if a block is visible
         *
         * @param block Block to check
         * @return True if visible, false if not
         */
        fun shouldDrawFace(block: BlockState): Boolean {
            return Settings.getConfig<XrayConfig>("xray").isInList(block.block)
        }
    }
}

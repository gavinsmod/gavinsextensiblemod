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
package com.peasenet.mods.tracer

import com.peasenet.main.Settings
import com.peasenet.config.TracerConfig
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.EntityRender
import net.minecraft.block.entity.FurnaceBlockEntity

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A mod that allows the player to see tracers towards furnaces.
 */
class ModFurnaceTracer : TracerMod(
    "Furnace Tracer",
    "gavinsmod.mod.tracer.furnace",
    "furnacetracer"
) {
    init {
        val colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer.furnace.color")
            .setColor(config.furnaceColor)
            .buildColorSetting()
        colorSetting.setCallback { config.furnaceColor = colorSetting.color }
        addSetting(colorSetting)
    }

    override fun onEntityRender(er: EntityRender) {

    }

    override fun onRenderBlockEntity(er: BlockEntityRender) {
        if (er.entity is FurnaceBlockEntity) RenderUtils.renderSingleLine(
            er.stack!!, er.buffer!!, er.playerPos!!, er.center!!, config.furnaceColor, config.alpha
        )
    }

    companion object {
        val config: TracerConfig
        get() {
            return Settings.getConfig<TracerConfig>("tracer")
        }
    }
}

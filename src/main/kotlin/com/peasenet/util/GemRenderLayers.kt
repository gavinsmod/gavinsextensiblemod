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
package com.peasenet.util

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeRenderType
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderStateShard.LineStateShard
import java.util.*

/**
 * @author GT3CH1
 * @version 04-13-2025
 * @since 04-13-2025
 */
class GemRenderLayers {
    companion object {
        val LINES: CompositeRenderType = RenderType.create(
            "gem:lines", 1536, RenderPipelines.LINES,
            RenderType.CompositeState.builder()
                 .setLineState(LineStateShard(OptionalDouble.of(2.0)))
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false)
        )

        val ESP_LINES: CompositeRenderType = RenderType.create(
            "gem:esp_lines", 1536, GemRenderPipeline.ESP_LINE_STRIP,
            RenderType.CompositeState.builder()
                .setLineState(LineStateShard(OptionalDouble.of(2.0)))
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false)
        )

        val QUADS: CompositeRenderType = RenderType.create(
            "gem:quads", 1536, RenderPipelines.DEBUG_QUADS,
            RenderType.CompositeState.builder()
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false)
        )
        val TEXT: CompositeRenderType = RenderType.create(
            "gem:text", 1536, RenderPipelines.GUI_TEXT,
            RenderType.CompositeState.builder()
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false)
        )
    }
}

class GemRenderPipeline {
    companion object {
        val ESP_LINE_STRIP: RenderPipeline = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                .withLocation("gem/lines")
//                .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.LINES)
                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
//                .withBlend(BlendFunction.TRANSLUCENT)
                .build()
        )
    }
}

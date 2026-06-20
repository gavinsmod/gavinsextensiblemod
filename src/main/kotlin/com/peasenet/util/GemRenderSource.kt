package com.peasenet.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.StagedVertexBuffer
import net.minecraft.client.renderer.rendertype.RenderType

/**
 *
 * @author GT3CH1
 * @version 06-19-2026
 * @since 06-19-2026 
 */
class GemRenderSource {
    private val stagedBuffer = StagedVertexBuffer(
        { "peasenet:gem_render_source" },
        RenderType.BIG_BUFFER_SIZE,
    )

    private val draws = ArrayList<StagedVertexBuffer.Draw>()
    private val drawTypes = ArrayList<RenderType>()

    fun getBuffer(type: RenderType): VertexConsumer {
        if (drawTypes.contains(type) && draws.last() == type && type.canConsolidateConsecutiveGeometry())
            return stagedBuffer.getVertexBuilder(draws.last())
        val draw = stagedBuffer.appendDraw(
            type.format(), type.primitiveTopology()
        )
        draws.add(draw)
        drawTypes.add(type)
        return stagedBuffer.getVertexBuilder(draw)
    }

    fun uploadAndDraw() {
        try {
            if (draws.isEmpty())
                return;
            stagedBuffer.upload()
            for (i in draws.indices) {
                draw(drawTypes.get(i), draws.get(i))
            }
            stagedBuffer.endDraw()
        } finally {
            draws.clear()
            drawTypes.clear()
            stagedBuffer.close()
        }
    }

    private fun draw(type: RenderType, draw: StagedVertexBuffer.Draw) {
        val info = stagedBuffer.getExecuteInfo(draw)
        if (info != null) {
            type.prepare().drawFromBuffer(info)
        }
    }
}
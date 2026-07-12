package com.peasenet.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.StagedVertexBuffer
import net.minecraft.client.renderer.rendertype.RenderType
import org.lwjgl.opengl.GL11

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

    private var _disabledDepthTest = false

    /**
     * Gets a VertexConsumer with the given render type. If disablesDepthTest is false, this will not disable
     * GL_DEPTH_TEST and will not check to see if this is enabled before uploading.
     */
    fun getBuffer(type: RenderType = GemRenderLayers.LINES, disablesDepthTest: Boolean = true): VertexConsumer {
        if(disablesDepthTest) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            _disabledDepthTest = true
        }
        if (drawTypes.contains(type) && draws.last() == type && type.canConsolidateConsecutiveGeometry())
            return stagedBuffer.getVertexBuilder(draws.last())
        val draw = stagedBuffer.appendDraw(
            type.format(), type.primitiveTopology()
        )
        draws.add(draw)
        drawTypes.add(type)
        return stagedBuffer.getVertexBuilder(draw)
    }


    /**
     * Uploads and draws the buffer. If we disabled GL11 depth test but it is enabled, this will throw an exception.
     * This will also re-enable GL_DEPTH_TEST
     */
    fun uploadAndDraw() {
        try {
            if (draws.isEmpty())
                return;
            stagedBuffer.upload()
            for (i in draws.indices) {
                draw(drawTypes[i], draws[i])
            }
            stagedBuffer.endDraw()
        } finally {
            draws.clear()
            drawTypes.clear()
            stagedBuffer.close()
            if(_disabledDepthTest)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
        }
    }

    private fun draw(type: RenderType, draw: StagedVertexBuffer.Draw) {
        val info = stagedBuffer.getExecuteInfo(draw)
        if (info != null) {
            type.prepare().drawFromBuffer(info)
        }
    }
}
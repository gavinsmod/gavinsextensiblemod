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

package com.peasenet.util.block

import com.peasenet.extensions.add
import com.peasenet.extensions.and
import com.peasenet.extensions.nand
import com.peasenet.gavui.color.Color
import com.peasenet.util.RenderUtils
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

/**
 * A GavBlock is a block used for rendering block ESP and tracers.
 * @param x The x-coordinate of the block.
 * @param y The y-coordinate of the block.
 * @param z The z-coordinate of the block.
 * @param visibleFilter The filter to determine whether the block is visible.
 * @author GT3CH1
 * @version 01-25-2025
 * @since 09-12-2024
 */
class GavBlock(
    val x: Int,
    val y: Int,
    val z: Int,
    val visibleFilter: (BlockPos) -> Boolean = {
        false
    },
    val color: Color? = null,
) {

    constructor(blockPos: BlockPos, visibleFilter: (BlockPos) -> Boolean = { false }, color: Color? = null) : this(
        blockPos.x, blockPos.y, blockPos.z, visibleFilter, color
    )

    constructor(blockPos: BlockPos, color: Color? = null) : this(
        blockPos.x, blockPos.y, blockPos.z, { true }, color
    )


    /**
     * What edges are visible.
     */
    private var visibleEdges = Edge.All.mask

    /**
     * Gets whether the block is visible.
     */
    fun isVisible(): Boolean {
        return visibleEdges != Edge.None.mask && visibleFilter(
            pos
        )
    }


    override fun hashCode(): Int {
        return ((x and 0x3FFFFFF shl 38) or (z and 0x3FFFFFF shl 12) or (y and 0xFFF))
    }

    /**
     * Updates the edges of the block.
     */
    fun update() {
        val up = hasNeighbor(pos.above())
        val below = hasNeighbor(pos.below())
        val east = hasNeighbor(pos.east())
        val west = hasNeighbor(pos.west())
        val south = hasNeighbor(pos.south())
        val north = hasNeighbor(pos.north())
        var mask =
            (Neighbors.East and east) + (Neighbors.North and north) + (Neighbors.South and south) + (Neighbors.West and west) + (Neighbors.Above and up) + (Neighbors.Below and below)
        if (!(up && below && east && west && south && north)) {
            mask = Neighbors.entries.sumOf { it.mask }
        }
        setVisibleEdges(mask)
    }


    /**
     * Gets whether there is a neighbor at the given [blockPos] using [visibleFilter].
     * @param blockPos The position to check.
     * @return True if [visibleFilter] returns true for the given [blockPos].
     */
    private fun hasNeighbor(blockPos: BlockPos): Boolean {
        return visibleFilter(blockPos)
    }

    /**
     * Sets the visible edges of the block.
     * @param neighbors The mask of the neighbors.
     */
    private fun setVisibleEdges(neighbors: Int) {
        visibleEdges = Edge.All.mask
        if (neighbors == Neighbors.None.mask) {
            visibleEdges = Edge.None.mask
            return
        }
        if (hasNeighbor(pos.east())) {
            visibleEdges = visibleEdges nand Edge.Edge3 nand Edge.Edge11
            visibleEdges = visibleEdges nand Edge.Edge7 nand Edge.Edge8
        }
        if (hasNeighbor(pos.west())) {
            visibleEdges = visibleEdges nand Edge.Edge1 nand Edge.Edge9
            visibleEdges = visibleEdges nand Edge.Edge5 nand Edge.Edge6
        }
        if (hasNeighbor(pos.north())) {
            visibleEdges = visibleEdges nand Edge.Edge4 nand Edge.Edge12
            visibleEdges = visibleEdges nand Edge.Edge8 nand Edge.Edge5
        }
        if (hasNeighbor(pos.south())) {
            visibleEdges = visibleEdges nand Edge.Edge2 nand Edge.Edge10
            visibleEdges = visibleEdges nand Edge.Edge6 nand Edge.Edge7
        }
        if (hasNeighbor(pos.above())) {
            visibleEdges = visibleEdges nand Edge.Edge9 nand Edge.Edge10
            visibleEdges = visibleEdges nand Edge.Edge11 nand Edge.Edge12
        }
        if (hasNeighbor(pos.below())) {
            visibleEdges = visibleEdges nand Edge.Edge1 nand Edge.Edge2
            visibleEdges = visibleEdges nand Edge.Edge3 nand Edge.Edge4
        }
    }

    /**
     * The position of the block.
     */
    val pos = BlockPos(x, y, z)

    /**
     * Renders the edges of the block.
     * @param edges The edges to render.
     * @param blockPos The position of the block.
     * @param matrixStack The matrix stack to render with.
     * @param bufferBuilder The buffer builder to render with.
     * @param color The color to render with.
     * @param alpha The alpha to render with.
     */
    private fun renderEdges(
        edges: Int,
        cameraRelativePos: Vec3,
        matrixStack: PoseStack,
        color: Color,
        alpha: Float,
        buffer: VertexConsumer,
    ) {
        if (edges == Edge.All.mask) {
            renderAllEdges(cameraRelativePos, matrixStack, color, alpha, buffer)
            return
        }
        for (edge in RENDERABLE_EDGES) {
            val maskedVal = edges and edge
            if (maskedVal != 0) {
                renderEdge(edge, cameraRelativePos, matrixStack, color, alpha, buffer)
            }
        }
    }

    /**
     * Renders an edge of the block.
     * @param edge The edge to render.
     * @param blockPos The position of the block.
     * @param matrixStack The matrix stack to render with.
     * @param bufferBuilder The buffer builder to render with.
     * @param color The color to render with.
     * @param alpha The alpha to render with.
     */
    private fun renderEdge(
        edge: Edge,
        cameraRelativePos: Vec3,
        matrixStack: PoseStack,
        color: Color,
        alpha: Float,
        buffer: VertexConsumer,
    ) {
        when (edge) {
            Edge.Edge1 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack, cameraRelativePos, cameraRelativePos.add(0, 0, 1), color, alpha, buffer
                )
            }

            Edge.Edge2 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(0, 0, 1),
                    cameraRelativePos.add(1, 0, 1),
                    color,
                    alpha,
                    buffer
                )
            }

            Edge.Edge3 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack, cameraRelativePos.add(1, 0, 1), cameraRelativePos.add(1, 0, 0), color, alpha,buffer
                )
            }

            Edge.Edge4 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(1, 0, 0),
                    cameraRelativePos.add(0, 0, 0),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge5 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos,
                    cameraRelativePos.add(0, 1, 0),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge6 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(0, 0, 1),
                    cameraRelativePos.add(0, 1, 1),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge7 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(1, 0, 1),
                    cameraRelativePos.add(1, 1, 1),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge8 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(1, 0, 0),
                    cameraRelativePos.add(1, 1, 0),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge9 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(0, 1, 0),
                    cameraRelativePos.add(0, 1, 1),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge10 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(0, 1, 1),
                    cameraRelativePos.add(1, 1, 1),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge11 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(1, 1, 1),
                    cameraRelativePos.add(1, 1, 0),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.Edge12 -> {
                RenderUtils.drawSingleLineOptimized(
                    matrixStack,
                    cameraRelativePos.add(1, 1, 0),
                    cameraRelativePos.add(0, 1, 0),
                    color,
                    alpha,
                    buffer,
                )
            }

            Edge.All -> {
                renderAllEdges(cameraRelativePos, matrixStack, color, alpha, buffer)
            }

            Edge.None -> {}
        }
    }

    private fun renderAllEdges(
        cameraRelativePos: Vec3,
        matrixStack: PoseStack,
        color: Color,
        alpha: Float,
        buffer: VertexConsumer,
    ) {
        for (edge in RENDERABLE_EDGES) {
            renderEdge(edge, cameraRelativePos, matrixStack, color, alpha, buffer)
        }
    }

    /**
     * Renders the block.
     * @param matrixStack The matrix stack to render with.
     * @param bufferBuilder The buffer builder to render with.
     * @param color The color to render with.
     * @param partialTicks The partial ticks to render with.
     * @param alpha The alpha to render with.
     * @param structureEsp Whether to render the block as a structure.
     */
    fun render(
        matrixStack: PoseStack,
        color: Color,
        partialTicks: Float,
        alpha: Float,
        structureEsp: Boolean = false,
        tracers: Boolean = false,
        buffer: VertexConsumer,
        cameraPos: Vec3? = null,
        tracerStart: Vec3? = null,
    ) {

        val colorToRender = this.color ?: color
        val frameCameraPos = cameraPos ?: RenderUtils.getCameraPos(partialTicks)
        val cameraRelativePos = Vec3(
            x.toDouble() - frameCameraPos.x,
            y.toDouble() - frameCameraPos.y,
            z.toDouble() - frameCameraPos.z,
        )
        if (structureEsp) renderEdges(visibleEdges, cameraRelativePos, matrixStack, colorToRender, alpha, buffer)
        else renderEdges(Edge.All.mask, cameraRelativePos, matrixStack, colorToRender, alpha, buffer)
        if (tracers) {
            val tracerOrigin = tracerStart ?: frameCameraPos.add(RenderUtils.getLookVec())
            val tracerStartRelative = Vec3(
                tracerOrigin.x - frameCameraPos.x,
                tracerOrigin.y - frameCameraPos.y,
                tracerOrigin.z - frameCameraPos.z,
            )
            val tracerEndRelative = Vec3(
                x + 0.5 - frameCameraPos.x,
                y + 0.5 - frameCameraPos.y,
                z + 0.5 - frameCameraPos.z,
            )
            RenderUtils.drawSingleLineOptimized(
                matrixStack,
                tracerStartRelative,
                tracerEndRelative,
                colorToRender,
                alpha,
                buffer,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as GavBlock
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    private companion object {
        val RENDERABLE_EDGES = Edge.entries.filter { it != Edge.All && it != Edge.None }
    }
}

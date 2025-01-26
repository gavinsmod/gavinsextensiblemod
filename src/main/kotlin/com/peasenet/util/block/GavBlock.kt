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

import com.peasenet.extensions.and
import com.peasenet.extensions.nand
import com.peasenet.gavui.color.Color
import com.peasenet.util.RenderUtils
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

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
) {

    constructor(blockPos: BlockPos, visibleFilter: (BlockPos) -> Boolean = { false }) : this(
        blockPos.x,
        blockPos.y,
        blockPos.z,
        visibleFilter
    )


    /**
     * What edges are visible.
     */
    private var visibleEdges = Edge.All.mask

    /**
     * Gets whether the block is visible.
     */
    fun isVisible(): Boolean {
        return !(visibleEdges == Edge.None.mask || visibleEdges == Edge.All.mask) && visibleFilter(
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
        val up = hasNeighbor(pos.up())
        val below = hasNeighbor(pos.down())
        val east = hasNeighbor(pos.east())
        val west = hasNeighbor(pos.west())
        val south = hasNeighbor(pos.south())
        val north = hasNeighbor(pos.north())
        val mask =
            (Neighbors.East and east) + (Neighbors.North and north) + (Neighbors.South and south) + (Neighbors.West and west) + (Neighbors.Above and up) + (Neighbors.Below and below)
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
        if (hasNeighbor(pos.up())) {
            visibleEdges = visibleEdges nand Edge.Edge9 nand Edge.Edge10
            visibleEdges = visibleEdges nand Edge.Edge11 nand Edge.Edge12
        }
        if (hasNeighbor(pos.down())) {
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
        blockPos: BlockPos,
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        color: Color,
        alpha: Float,
    ) {
        Edge.entries.filter { it != Edge.All && it != Edge.None }.forEach { edge ->
            val maskedVal = edges and edge
            if (maskedVal != 0) {
                renderEdge(edge, blockPos, matrixStack, bufferBuilder, color, alpha)
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
        blockPos: BlockPos,
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        color: Color,
        alpha: Float,
    ) {
        val posMatrix = matrixStack.peek().positionMatrix
        when (edge) {
            Edge.Edge1 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge2 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge3 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge4 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge5 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge6 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge7 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge8 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge9 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge10 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge11 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.Edge12 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    alpha,
                    true
                )
            }

            Edge.All -> {
                renderEdge(Edge.Edge1, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge2, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge3, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge4, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge5, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge6, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge7, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge8, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge9, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge10, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge11, blockPos, matrixStack, bufferBuilder, color, alpha)
                renderEdge(Edge.Edge12, blockPos, matrixStack, bufferBuilder, color, alpha)
            }

            Edge.None -> {}
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
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        color: Color,
        partialTicks: Float,
        alpha: Float,
        structureEsp: Boolean = false,
        tracers: Boolean = false,
    ) {
        if (structureEsp) renderEdges(visibleEdges, pos, matrixStack, bufferBuilder, color, alpha)
        else renderEdges(Edge.All.mask, pos, matrixStack, bufferBuilder, color, alpha)
        if (tracers) {
            val regionVec = RenderUtils.getCameraRegionPos().toVec3d()
            val center = pos.toCenterPos().subtract(regionVec)
            RenderUtils.drawSingleLine(
                bufferBuilder, matrixStack.peek().positionMatrix, partialTicks, center, color, alpha
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
}


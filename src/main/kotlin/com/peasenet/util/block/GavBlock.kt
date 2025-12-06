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
import com.peasenet.extensions.toVec3d
import com.peasenet.gavui.color.Color
import com.peasenet.util.RenderUtils
import com.peasenet.util.RenderUtils.getCameraPos
import kotlinx.io.Buffer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Matrix3x2fStack

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
        val up = hasNeighbor(pos.up())
        val below = hasNeighbor(pos.down())
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
        blockPos: Vec3d,
        matrixStack: MatrixStack,
        color: Color,
        alpha: Float,
        buffer: VertexConsumer
    ) {
        if ((edges and Edge.All) == Edge.All.mask) {
            renderEdge(Edge.All, blockPos, matrixStack, color, alpha, buffer)
            return
        }

        Edge.entries.filter { it != Edge.All && it != Edge.None }.forEach { edge ->
            val maskedVal = edges and edge
            if (maskedVal != 0) {
                renderEdge(edge, blockPos, matrixStack, color, alpha, buffer)
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
        blockPos: Vec3d,
        matrixStack: MatrixStack,
        color: Color,
        alpha: Float,
        buffer: VertexConsumer
    ) {
        val startPos = blockPos.add((getCameraPos().negate()))
        when (edge) {
            Edge.Edge1 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos,
                    startPos.add(0, 0, 1),
                    color,
                    alpha,
                    false,
                )
            }
            Edge.Edge2 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(0, 0, 1),
                    startPos.add(1, 0, 1),
                    color,
                    alpha,

                    false
                )
            }
            Edge.Edge3 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 0, 1),
                    startPos.add(1, 0, 0),
                    color,
                    alpha,

                    false
                )
            }
            Edge.Edge4 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 0, 0),
                    startPos.add(0, 0, 0),
                    color,
                    alpha,

                    false
                )
            }
            Edge.Edge5 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos,
                    startPos.add(0, 1, 0),
                    color,
                    alpha,

                    false
                )
            }
            Edge.Edge6 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(0, 0, 1),
                    startPos.add(0, 1, 1),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge7 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 0, 1),
                    startPos.add(1, 1, 1),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge8 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 0, 0),
                    startPos.add(1, 1, 0),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge9 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(0, 1, 0),
                    startPos.add(0, 1, 1),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge10 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(0, 1, 1),
                    startPos.add(1, 1, 1),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge11 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 1, 1),
                    startPos.add(1, 1, 0),
                    color,
                    alpha,
                    false
                )
            }
            Edge.Edge12 -> {
                RenderUtils.drawSingleLine(
                    matrixStack,
                    startPos.add(1, 1, 0),
                    startPos.add(0, 1, 0),
                    color,
                    alpha,
                    false
                )
            }
            Edge.All -> {
                val bb = Box(
                    blockPos.x + 1, blockPos.y, blockPos.z + 1, blockPos.x, blockPos.y + 1.0, blockPos.z
                )
                RenderUtils.drawOutlinedBoxOptimized(bb, matrixStack, color, alpha, buffer)
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
        color: Color,
        partialTicks: Float,
        alpha: Float,
        structureEsp: Boolean = false,
        tracers: Boolean = false,
        buffer: VertexConsumer
    ) {

        val offsetPos = pos.toVec3d()
        if (structureEsp)
            renderEdges(visibleEdges, offsetPos, matrixStack, color, alpha, buffer)
        else
            renderEdges(Edge.All.mask, offsetPos, matrixStack, color, alpha, buffer)
        if (tracers) {
            val tracerOrigin = RenderUtils.getLookVec(partialTicks).multiply(10.0)
            RenderUtils.drawSingleLine(
                matrixStack,
                tracerOrigin,
                offsetPos.add(0.5, 0.5, 0.5),
                color,
                alpha,
                withOffset = true,
                depthTest = false
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

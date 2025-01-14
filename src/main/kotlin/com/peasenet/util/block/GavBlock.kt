package com.peasenet.util.block

import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsModClient
import com.peasenet.mods.esp.ModBlockEsp
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
 * @author GT3CH1
 * @version 09-12-2024
 * @since 09-12-2024
 */
class GavBlock(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun getKey(x: Int, y: Int, z: Int): Long {
            return ((x.toLong() and 0x3FFFFFF shl 38) or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF))
        }
    }

    /**
     * The key of the block.
     */
    val key = getKey(x, y, z)

    /**
     * What edges are visible.
     */
    private var visibleEdges = Edge.All.mask

    fun isVisible(): Boolean {
        return !(hasNeighborTopMiddle() && hasNeighborMiddleLeft() && hasNeighborMiddleRight()
                && hasNeighborBottomMiddle())
    }


    override fun hashCode(): Int {
        return ((x and 0x3FFFFFF shl 38) or (z and 0x3FFFFFF shl 12) or (y and 0xFFF))
    }

    /**
     * Updates the edges of the block.
     */
    fun update() {
        val topMiddle = hasNeighbor(1, 0, 0)
        val middleLeft = hasNeighbor(0, 0, -1)
        val middleRight = hasNeighbor(0, 0, 1)
        val bottomMiddle = hasNeighbor(-1, 0, 0)
        val above = hasNeighbor(0, 1, 0)
        val below = hasNeighbor(0, -1, 0)
        val mask =
            Neighbors.TopMiddle.mask * topMiddle or
                    Neighbors.MiddleLeft.mask * middleLeft or
                    Neighbors.MiddleRight.mask * middleRight or
                    Neighbors.BottomMiddle.mask * bottomMiddle or
                    Neighbors.Above.mask * above or
                    Neighbors.Below.mask * below
        setVisibleEdges(mask)
    }

    fun hasNeighborTopMiddle(): Boolean {
        return hasNeighbor(1, 0, 0) == 1
    }

    fun hasNeighborMiddleLeft(): Boolean {
        return hasNeighbor(0, 0, -1) == 1
    }

    fun hasNeighborMiddleRight(): Boolean {
        return hasNeighbor(0, 0, 1) == 1
    }

    fun hasNeighborBottomMiddle(): Boolean {
        return hasNeighbor(-1, 0, 0) == 1
    }

    fun hasNeighborAbove(): Boolean {
        return hasNeighbor(0, 1, 0) == 1
    }

    fun hasNeighborBelow(): Boolean {
        return hasNeighbor(0, -1, 0) == 1
    }

    /**
     * Gets whether there is a neighbor at the given coordinates.
     * @param x The x-coordinate of the neighbor.
     * @param y The y-coordinate of the neighbor.
     * @param z The z-coordinate of the neighbor.
     */
    private fun hasNeighbor(x: Int, y: Int, z: Int): Int {
        val neighbor = GavBlock(this.x + x, this.y + y, this.z + z)
        val neighborState = GavinsModClient.minecraftClient.getWorld().getBlockState(neighbor.pos)
        val res = ModBlockEsp.getSettings().isInList(neighborState.block)
        return if (res) 1 else 0
    }

    /**
     * Sets the visible edges of the block.
     * @param neighbors The mask of the neighbors.
     */
    private fun setVisibleEdges(neighbors: Int) {
        visibleEdges = Edge.All.mask
        if (neighbors == Neighbors.None.mask) {
            return
        }
        if (neighbors and Neighbors.TopMiddle.mask != 0) {
            visibleEdges = (visibleEdges and Edge.Edge3.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge11.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge7.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge8.mask.inv())
        }
        if (neighbors and Neighbors.MiddleLeft.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge4.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge12.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge8.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge5.mask.inv()
        }
        if (neighbors and Neighbors.MiddleRight.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge2.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge10.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge6.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge7.mask.inv()
        }
        if (neighbors and Neighbors.BottomMiddle.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge1.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge9.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge5.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge6.mask.inv()
        }
        if (neighbors and Neighbors.Above.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge9.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge10.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge11.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge12.mask.inv()

        }
        if (neighbors and Neighbors.Below.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge1.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge2.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge3.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge4.mask.inv()
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
        for (edge in Edge.entries) {
            if (edge == Edge.All || edge == Edge.None) continue
            val maskedVal = edges and edge.mask
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
        if (javaClass != other?.javaClass) return false

        other as GavBlock

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (key != other.key) return false

        return true
    }
}


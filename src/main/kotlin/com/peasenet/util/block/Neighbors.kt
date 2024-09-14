package com.peasenet.util.block

/**
 * An enum class that contains the neighbors of a block.
 * @param mask The mask of the neighbor.
 * @author GT3CH1
 * @version 09-12-2024
 * @since 09-12-2024
 */
enum class Neighbors(val mask: Int) {
    /**
     * The top middle (x+) neighbor.
     */
    TopMiddle(1 shl 2),

    /**
     * The middle left (z-) neighbor.
     */
    MiddleLeft(1 shl 4),

    /**
     * The middle right (z+) neighbor.
     */
    MiddleRight(1 shl 6),

    /**
     * The bottom middle (x-) neighbor.
     */
    BottomMiddle(1 shl 8),

    /**
     * The above (y+) neighbor.
     */
    Above(1 shl 10),

    /**
     * The below (y-) neighbor.
     */
    Below(1 shl 11),

    /**
     * No neighbors.
     */
    None(0);
}
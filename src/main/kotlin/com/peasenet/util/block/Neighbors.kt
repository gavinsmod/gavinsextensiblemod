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
     * The top left (x+, z-) neighbor.
     */
    TopLeft(1 shl 1),

    /**
     * The top middle (x+) neighbor.
     */
    TopMiddle(1 shl 2),

    /**
     * The top right (x+, z+) neighbor.
     */
    TopRight(1 shl 3),

    /**
     * The middle left (z-) neighbor.
     */
    MiddleLeft(1 shl 4),

    /**
     * The middle right (z+) neighbor.
     */
    MiddleRight(1 shl 6),

    /**
     * The bottom left (x-, z-) neighbor.
     */
    BottomLeft(1 shl 7),

    /**
     * The bottom middle (x-) neighbor.
     */
    BottomMiddle(1 shl 8),

    /**
     * The bottom right (x-, z+) neighbor.
     */
    BottomRight(1 shl 9),

    /**
     * The above (y+) neighbor.
     */
    Above(1 shl 10),

    /**
     * The below (y-) neighbor.
     */
    Below(1 shl 11),

    /**
     * The z axis top left (z-, y+) neighbor.
     */
    ZTopLeft(1 shl 12),

    /**
     * The z axis top right (z+, y+) neighbor.
     */
    ZTopRight(1 shl 13),

    /**
     * The z axis bottom left (z-, y-) neighbor.
     */
    ZBottomLeft(1 shl 14),

    /**
     * The z axis bottom right (z+, y-) neighbor.
     */
    ZBottomRight(1 shl 15),

    /**
     * The x-axis top left (x-, y+) neighbor.
     */
    XTopLeft(1 shl 16),

    /**
     * The x-axis top right (x+, y+) neighbor.
     */
    XTopRight(1 shl 17),

    /**
     * The x-axis bottom left (x-, y-) neighbor.
     */
    XBottomLeft(1 shl 18),

    /**
     * The x-axis bottom right (x+, y-) neighbor.
     */
    XBottomRight(1 shl 19),

    /**
     * All neighbors.
     */
    All(0xFFFFF),

    /**
     * No neighbors.
     */
    None(0);
}
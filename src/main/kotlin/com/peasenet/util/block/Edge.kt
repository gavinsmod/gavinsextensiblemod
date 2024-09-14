package com.peasenet.util.block

/**
 * An edge of a block.
 * @author GT3CH1
 * @version 09-12-2024
 * @since 09-12-2024
 */
enum class Edge(val mask: Int) {
    /**
     * The lower x-axis edge of a block.
     */
    Edge1(1 shl 1),

    /**
     * The lower z+ axis edge of a block.
     */
    Edge2(1 shl 2),

    /**
     * The lower x+ axis edge of a block.
     */
    Edge3(1 shl 3),

    /**
     * The lower z- axis edge of a block.
     */
    Edge4(1 shl 4),

    /**
     * The x- and z- corner of a block.
     */
    Edge5(1 shl 5),

    /**
     * The x- and z+ corner of a block.
     */
    Edge6(1 shl 6),

    /**
     * The x+ and z+ corner of a block.
     */
    Edge7(1 shl 7),

    /**
     * The x+ and z- corner of a block.
     */
    Edge8(1 shl 8),

    /**
     * The upper x- axis edge of a block.
     */
    Edge9(1 shl 9),

    /**
     * The upper z+ axis edge of a block.
     */
    Edge10(1 shl 10),

    /**
     * The upper x+ axis edge of a block.
     */
    Edge11(1 shl 11),

    /**
     * The upper z- axis edge of a block.
     */
    Edge12(1 shl 12),

    /**
     * All edges of a block.
     */
    All(0xFFFF),

    /**
     * No edges of a block.
     */
    None(0)
}


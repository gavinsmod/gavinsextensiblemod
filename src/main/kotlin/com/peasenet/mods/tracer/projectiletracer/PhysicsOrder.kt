package com.peasenet.mods.tracer.projectiletracer

/**
 * An enum representation on how to process physics for projectiles.
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
enum class PhysicsOrder {
    POSITION_DRAG_GRAVITY,
    DRAG_POSITION_GRAVITY,
    GRAVITY_DRAG_POSITION
}
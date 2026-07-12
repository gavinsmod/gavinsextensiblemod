package com.peasenet.mods.tracer.projectiletracer

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import net.minecraft.world.phys.Vec3

/**
 * A data class containing information about a projectile's physics, including gravity, drag, velocity, offset, position, color, and physics order.
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
data class ProjectileData(
    var gravity: Double = 0.03,
    var drag: Double = 0.99,
    var waterDrag: Double = 0.6,
    var velocity: Vec3 = Vec3.ZERO,
    var offset: Vec3 = Vec3.ZERO,
    var position: Vec3 = Vec3.ZERO,
    var color: Color = Colors.RED,
    var physicsOrder: PhysicsOrder = PhysicsOrder.POSITION_DRAG_GRAVITY,
)

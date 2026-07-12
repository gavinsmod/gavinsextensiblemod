package com.peasenet.mods.tracer.projectiletracer

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

/**
 * A class representing a list of trajectory points for a projectile, whether it has hit an entity, and the current hitbox
 * position.
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
data class ImpactData(
    val trajectoryPoints: List<Vec3>,
    val impact: HitResult?,
    val hitEntity: Entity?,
    private val hasHit: Boolean,
) {
    fun hitEntity(): Boolean {
        return hitEntity != null && hasHit
    }

    fun missEntity(): Boolean {
        return hitEntity == null && hasHit
    }
}

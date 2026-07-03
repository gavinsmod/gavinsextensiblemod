package com.peasenet.mods.tracer

import com.mojang.blaze3d.vertex.PoseStack
import com.peasenet.config.Config
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.tracer.GuiProjectileTracer
import com.peasenet.main.Settings
import com.peasenet.util.ChatCommand
import com.peasenet.util.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.CrossbowItem
import net.minecraft.world.item.EnderpearlItem
import net.minecraft.world.item.FishingRodItem
import net.minecraft.world.item.SnowballItem
import net.minecraft.world.item.TridentItem
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin


/**
 *
 * @author GT3CH1
 * @version 06-27-2026
 * @since 06-27-2026 
 */
class ModProjectileTracer : TracerMod<ModProjectileTracer>(
    translationKey = "gavinsmod.mod.tracer.projectile", chatCommand = ChatCommand.ProjectileTracer
) {

    init {
        clickSetting {
            title = "gavinsmod.mod.tracer.projectile"
            callback = {
                client.setScreen(GuiProjectileTracer())
            }
        }
    }

    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        val projectileList = getProjectileList(partialTicks)
        val eyePos = Minecraft.getInstance().player!!.getEyePosition(partialTicks)
        matrixStack.pushPose()
        for (projectile in projectileList) {
            val handToEyeDelta: Vec3 = handToEyeDelta(projectile.offset, projectile.position, eyePos, 1, partialTicks)
            val impactData = getProjectileImpactData(projectile.position, projectile)

            val trajectoryPoints = impactData.trajectoryPoints
            if (getConfig().showTrajectory) {
                for (i in 0 until trajectoryPoints.size - 1) {
                    val lerpedDelta = handToEyeDelta.scale((trajectoryPoints.size - (i * 1.0)) / trajectoryPoints.size)
                    val nextLerpedDelta =
                        handToEyeDelta.scale((trajectoryPoints.size - (i + 1 * 1.0)) / trajectoryPoints.size)
                    val pos = trajectoryPoints[i].add(lerpedDelta)
                    val dir = (trajectoryPoints[i + 1].add(nextLerpedDelta)).subtract(pos).scale(1.0)

                    val lineWidth = when (getConfig().trajectoryWidth) {
                        ProjectileTracerConfig.TRAJECTORY_WIDTH.SMALL -> 2.0f
                        ProjectileTracerConfig.TRAJECTORY_WIDTH.MEDIUM -> 5f
                        ProjectileTracerConfig.TRAJECTORY_WIDTH.LARGE -> 8.0f
                    }
                    when (getConfig().trajectoryStyle) {

                        ProjectileTracerConfig.TRAJECTORY_STYLE.LINE -> {
                            RenderUtils.drawSingleLine(
                                matrixStack,
                                start = pos,
                                end = pos.add(dir),
                                color = projectile.color,
                                alpha = getConfig().trajectoryAlpha,
                                lineWidth = lineWidth
                            )
                        }

                        ProjectileTracerConfig.TRAJECTORY_STYLE.DASHED -> {
                            RenderUtils.drawSingleLine(
                                matrixStack,
                                start = pos,
                                end = pos.add(dir.scale(0.5)),
                                color = projectile.color,
                                alpha = getConfig().trajectoryAlpha,
                                lineWidth = lineWidth
                            )
                        }

                        ProjectileTracerConfig.TRAJECTORY_STYLE.BOX -> {
                            val box = AABB(pos, pos).inflate(0.05)
                            RenderUtils.drawLinedBox(
                                box,
                                matrixStack,
                                projectile.color,
                                alpha = getConfig().trajectoryAlpha,
                                partialTicks
                            )
                        }
                    }

                }
            }
            if (impactData.miss() && getConfig().showImpact) {
                // draw a box at the impact point
                val impactPos = impactData.impact!!.location
                val box = AABB(impactPos, impactPos).inflate(ENTITY_AABB_SCALE)
                RenderUtils.drawLinedBox(
                    box, matrixStack, getConfig().impactColor, alpha = getConfig().impactAlpha, partialTicks
                )
            }
            if (impactData.hit() && getConfig().showHitEntity) {
                if (impactData.hitEntity == null) return
                val lerped = RenderUtils.getLerpedBox(impactData.hitEntity, partialTicks)
                RenderUtils.drawLinedBox(
                    lerped, matrixStack, getConfig().hitEntityColor, getConfig().hitEntityAlpha, partialTicks
                )
                if (getConfig().showHitEntityOutline) {
                    RenderUtils.drawOutlinedBox(
                        lerped, matrixStack, getConfig().hitEntityOutlineColor, getConfig().hitEntityAlpha, partialTicks
                    )
                }
            }
            matrixStack.popPose()

        }

    }


    fun getProjVelocity(partialTicks: Float, scale: Double, pull: Float = 1.0f): Vec3 {
        val player = client.getPlayer()
        val vel = player.getViewVector(partialTicks).scale(scale * pull)
        return vel;
    }

    fun getProjectileList(partialTicks: Float): List<ProjectileData> {
        val projectileData: MutableList<ProjectileData> = ArrayList()
        val player = client.getPlayer()
        val itemStack = player.mainHandItem
        val item = itemStack.item
        val position = player.getEyePosition(partialTicks)
        val useTicks = player.getTicksUsingItem()
        var newProjectileData = ProjectileData()
        when (item) {
            is BowItem -> {
                val pull = BowItem.getPowerForTime(useTicks)
                val velocity = getProjVelocity(partialTicks, BOW_SCALE, pull)
                if (pull <= 0.1) return projectileData
                newProjectileData = ProjectileData(
                    velocity = velocity, offset = BOW_OFFSET, position = position, gravity = BOW_GRAVITY,
                    color = getConfig().bowTrajectoryColor
                )
            }

            is CrossbowItem -> {
                val vel = getProjVelocity(partialTicks, CROSSBOW_SCALE)
                if (CrossbowItem.isCharged(itemStack)) {
                    newProjectileData = ProjectileData(
                        velocity = vel,
                        offset = CROSSBOW_OFFSET,
                        position = position,
                        color = getConfig().crossbowTrajectoryColor,
                        gravity = CROSSBOW_GRAVITY
                    )
                }
            }

            is FishingRodItem -> {
                val vel = getProjVelocity(partialTicks, FISHING_ROD_SCALE)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = FISHING_ROD_OFFSET,
                    position = position,
                    color = getConfig().fishingRodTrajectoryColor,
                    gravity = FISHING_ROD_GRAVITY,
                )
            }

            is SnowballItem -> {
                val vel = getProjVelocity(partialTicks, SNOWBALL_SCALE)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = SNOWBALL_OFFSET,
                    position = position,
                    color = getConfig().snowballTrajectoryColor,
                    gravity = SNOWBALL_GRAVITY,
                )

            }

            is EnderpearlItem -> {
                val vel = getProjVelocity(partialTicks, ENDERPEARL_SCALE)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = ENDERPEARL_OFFSET,
                    position = position,
                    color = getConfig().enderpearlTrajectoryColor,
                    gravity = ENDERPEARL_GRAVITY,
                )

            }

            is TridentItem -> {
                val vel = getProjVelocity(partialTicks, TRIDENT_SCALE)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = TRIDENT_OFFSET,
                    position = position,
                    gravity = TRIDENT_GRAVITY,
                    color = getConfig().tridentTrajectoryColor,
                )
            }
            else ->
                return emptyList()
        }
        projectileData.add(
            newProjectileData
        )
        return projectileData
    }

    fun getProjectileImpactData(projectilePosition: Vec3, projectileData: ProjectileData): ProjectileImpactData {
        var prevPos = projectileData.position
        var impact: HitResult? = null;
        var hitEntity: Entity? = null;
        var hasHit = false;
        val trajectoryPoints: MutableList<Vec3> = ArrayList()
        val drag = projectileData.drag
        val gravity = projectileData.gravity
        var velocity = projectileData.velocity.add(client.getPlayer().deltaMovement)
        var entityHitPos: Vec3? = null
        var newPos = projectilePosition
        for (i in 0..MAX_TRAJECTORY_POINTS) {
            trajectoryPoints.add(newPos)
            velocity = velocity.scale(drag)
            velocity = velocity.subtract(0.0, gravity, 0.0)
            newPos = newPos.add(velocity)
            val box = AABB(prevPos, newPos).deflate(0.5)
            val entities: List<Entity?>? = client.getWorld().getEntitiesOfClass(
                Entity::class.java, box
            ) { e -> !e.isSpectator && e.isAlive && (e !is Projectile) && (e !is ItemEntity) && (e !is ExperienceOrb) && (e !is EnderDragon) && (e !is LocalPlayer) }
            val closest = Double.MAX_VALUE;
            var closestEntity: Entity? = null;
            for (entity in entities!!) {
                assert(entity != null)
                val entityBox = entity!!.boundingBox.deflate(ENTITY_AABB_SCALE)
                val raycastHit = entityBox?.clip(prevPos, newPos)

                if (raycastHit?.isPresent ?: false) {
                    val distance = prevPos.distanceTo(raycastHit.get())
                    if (distance < closest) {
                        entityHitPos = raycastHit.get()
                        closestEntity = entity
                        hasHit = true
                    }
                }
            }

            val hitResult = client.getWorld().clip(
                ClipContext(
                    prevPos,
                    newPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    Minecraft.getInstance().player as Entity
                )
            )

            if (hitResult.type != HitResult.Type.MISS && prevPos.distanceToSqr(hitResult.location) < closest) {
                newPos = hitResult.location
                hasHit = true
                impact = hitResult
                trajectoryPoints.add(newPos)
                break
            }

            if (entityHitPos != null) {
                newPos = entityHitPos
                hitEntity = closestEntity
                hasHit = true
                trajectoryPoints.add(newPos)
                break
            }

            prevPos = newPos
        }
        return ProjectileImpactData(
            trajectoryPoints = trajectoryPoints,
            impact = impact,
            hitEntity = hitEntity,
            hasHit = hasHit,
        )

    }

    fun handToEyeDelta(offset: Vec3, startPos: Vec3, eye: Vec3, handMultiplier: Int, delta: Float): Vec3 {
        val player = client.getPlayer()
        val yaw = Math.toRadians(-player.getViewYRot(delta).toDouble())
        val pitch = Math.toRadians(-player.getViewXRot(delta).toDouble())
        val forward = player.getViewVector(delta)
        val up = Vec3(-sin(pitch) * sin(yaw), cos(pitch), -sin(pitch) * cos(yaw)).normalize()
        val right = forward.cross(up).normalize()
        var tmpOffset = offset;
        if (client.gameRenderer.mainCamera().isDetached) tmpOffset = offset.scale(0.0)

        return right.scale(handMultiplier * tmpOffset.x).add(up.scale(tmpOffset.y)).add(forward.scale(tmpOffset.z))
            .add(eye.subtract(startPos))
    }

    data class ProjectileData(
        var gravity: Double = 0.03,
        var drag: Double = 0.99,
        var waterDrag: Double = 0.6,
        var velocity: Vec3 = Vec3.ZERO,
        var offset: Vec3 = Vec3.ZERO,
        var position: Vec3 = Vec3.ZERO,
        var color: Color = Colors.RED,
    )

    data class ProjectileImpactData(
        val trajectoryPoints: List<Vec3>,
        val impact: HitResult?,
        val hitEntity: Entity?,
        private val hasHit: Boolean,
    ) {
        fun hit(): Boolean {
            return hitEntity != null && hasHit
        }

        fun miss(): Boolean {
            return hitEntity == null && hasHit
        }
    }

    companion object {
        val BOW_OFFSET = Vec3(0.2, -0.025, -0.2)
        const val BOW_SCALE = 3.5
        const val BOW_GRAVITY = 0.05

        val CROSSBOW_OFFSET = Vec3(0.09, -0.9, -0.2)
        const val CROSSBOW_SCALE = 3.0
        const val CROSSBOW_GRAVITY = 0.0325

        val FISHING_ROD_OFFSET = Vec3(0.2, -0.09, 0.2)
        const val FISHING_ROD_SCALE = 1.0
        const val FISHING_ROD_GRAVITY = 0.05

        val SNOWBALL_OFFSET = Vec3(0.2, -0.09, 0.2)
        const val SNOWBALL_SCALE = 1.75
        const val SNOWBALL_GRAVITY = 0.03

        val ENDERPEARL_OFFSET = Vec3(0.2, -0.09, 0.2)
        const val ENDERPEARL_SCALE = 1.75
        const val ENDERPEARL_GRAVITY = 0.03

        val TRIDENT_OFFSET = Vec3(0.2, .15, -0.2)
        const val TRIDENT_SCALE = 2.75
        const val TRIDENT_GRAVITY = 0.05

        const val ENTITY_AABB_SCALE = 0.1
        const val MAX_TRAJECTORY_POINTS = 200

        fun getConfig(): ProjectileTracerConfig {
            return Settings.getConfig(ChatCommand.ProjectileTracer)
        }

    }
}


class ProjectileTracerConfig : Config<ProjectileTracerConfig>() {
    enum class TRAJECTORY_STYLE {
        LINE, BOX, DASHED
    }

    enum class TRAJECTORY_WIDTH {
        SMALL, MEDIUM, LARGE
    }

    init {
        key = ChatCommand.ProjectileTracer.command
    }

    var alpha: Float = 1.0f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
        }

    var showImpact: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var impactColor = Colors.BLUE
        set(value) {
            field = value
            saveConfig()
        }

    var impactAlpha = 0.25f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var showHitEntity: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var trajectoryAlpha = 1.0f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var hitEntityColor = Colors.GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityAlpha = 0.25f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var showHitEntityOutline = true
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityOutlineColor = Colors.WHITE
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityOutlineAlpha = 0.5f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var showTrajectory = true
        set(value) {
            field = value
            saveConfig()
        }

    var bowTrajectoryColor = Colors.RED
        set(value) {
            field = value
            saveConfig()
        }

    var crossbowTrajectoryColor = Colors.PURPLE
        set(value) {
            field = value
            saveConfig()
        }

    var fishingRodTrajectoryColor = Colors.MEDIUM_SEA_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var snowballTrajectoryColor = Colors.WHITE
        set(value) {
            field = value
            saveConfig()
        }

    var enderpearlTrajectoryColor = Colors.DARK_SPRING_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var tridentTrajectoryColor = Colors.MEDIUM_SEA_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var trajectoryStyle = TRAJECTORY_STYLE.LINE
        set(value) {
            field = value
            saveConfig()
        }

    var trajectoryWidth = TRAJECTORY_WIDTH.MEDIUM
        set(value) {
            field = value
            saveConfig()
        }

}
package com.peasenet.mods.tracer

import com.peasenet.config.tracer.ProjectileTracerConfig
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.tracer.GuiProjectileTracer
import com.peasenet.main.Settings
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.peasenet.gavui.color.Color
import com.peasenet.mods.tracer.projectiletracer.ImpactData
import com.peasenet.mods.tracer.projectiletracer.PhysicsOrder
import com.peasenet.mods.tracer.projectiletracer.ProjectileData
import com.peasenet.util.ChatCommand
import com.peasenet.util.GemRenderLayers
import com.peasenet.util.GemRenderSource
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.EntityNameRender
import com.peasenet.util.event.data.LevelRenderEndEventData
import com.peasenet.util.listeners.EntityRenderNameListener
import com.peasenet.util.listeners.LevelRenderEndEventListener
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.*
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin


/**
 *
 * Draws a tracer for a projectile-based item (eg, bow, potion, egg, snowball) and other things to help the player aim better.
 *
 * @author GT3CH1
 * @version 7-12-2026
 * @since 06-27-2026 
 */
class ModProjectileTracer : TracerMod<ModProjectileTracer>(
    translationKey = "gavinsmod.mod.tracer.projectile", chatCommand = ChatCommand.ProjectileTracer
), EntityRenderNameListener, LevelRenderEndEventListener {

    init {
        clickSetting {
            title = "gavinsmod.mod.tracer.projectile"
            callback = {
                client.setScreen(GuiProjectileTracer())
            }
        }
    }


    // The last known impact position
    private var impactPos: Vec3? = null

    override fun onEnable() {
        em.subscribe(EntityRenderNameListener::class.java, this)
        em.subscribe(LevelRenderEndEventListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(EntityRenderNameListener::class.java, this)
        em.unsubscribe(LevelRenderEndEventListener::class.java, this)
        super.onDisable()
    }

    /**
     * Calculates the angle from given rotation parameters.
     * @param xRot The X rotation in degrees.
     * @param yRot The Y rotation in degrees.
     * @param h The height offset in degrees.
     * @return A [Vec3] representing the direction vector.
     */
    fun angleFromRotation(xRot: Float, yRot: Float, h: Double): Vec3 {
        val x = -Mth.sin(yRot * Mth.DEG_TO_RAD.toDouble()) * Mth.cos(xRot * Mth.DEG_TO_RAD.toDouble())
        val y = -Mth.sin(((xRot + h) * Mth.DEG_TO_RAD))
        val z = Mth.cos((yRot * Mth.DEG_TO_RAD).toDouble()) * Mth.cos((xRot * Mth.DEG_TO_RAD).toDouble())
        return Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    }

    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        val projectileList = getProjectileList(partialTicks)
        if (projectileList.isEmpty()) {
            impactPos = null
            return
        }

        val eyePos = Minecraft.getInstance().player!!.getEyePosition(partialTicks)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        val bufferSource = GemRenderSource()
        val buffer = bufferSource.getBuffer(GemRenderLayers.LINES)
        val config = getConfig()
        for (projectile in projectileList) {
            val impactData = getProjectileImpactData(projectile.position, projectile)
            if (config.showTrajectory) {
                renderTrajectory(
                    matrixStack,
                    projectile,
                    impactData,
                    config.trajectoryWidth,
                    config.trajectoryStyle,
                    config.trajectoryAlpha,
                    partialTicks,
                    eyePos,
                )
            }
            if (impactData.missEntity() && config.showImpact) {
                impactPos = impactData.impact!!.location
                renderImpact(matrixStack, config.impactColor, config.impactAlpha)
            }
            if (impactData.hitEntity() && config.showHitEntity) {
                val lerped = RenderUtils.getLerpedBox(impactData.hitEntity!!, partialTicks)
                impactPos = lerped.center
                renderEntityHit(
                    lerped,
                    matrixStack,
                    config.hitEntityColor,
                    config.hitEntityAlpha,
                    config.hitEntityOutlineColor,
                    config.hitEntityOutlineAlpha,
                    config.showHitEntityOutline,
                    buffer
                )
            }

        }
        bufferSource.uploadAndDraw()

        GL11.glEnable(GL11.GL_DEPTH_TEST)

    }

    /**
     * Renders a hitbox around the entity when a hit is detected.
     * @param box The [AABB] representing the entity's bounding box.
     * @param matrixStack The [PoseStack] for rendering transformations.
     * @param entityHitColor The color of the hitbox.
     * @param entityHitAlpha The alpha (transparency) of the hitbox.
     * @param entityHitOutlineColor The color of the hitbox outline.
     * @param entityHitOutlineAlpha The alpha (transparency) of the hitbox outline.
     * @param showOutline Whether to show the outline of the hitbox.
     * @param buffer The [VertexConsumer] for rendering the outline.
     */
    fun renderEntityHit(
        box: AABB,
        matrixStack: PoseStack,
        entityHitColor: Color,
        entityHitAlpha: Float,
        entityHitOutlineColor: Color,
        entityHitOutlineAlpha: Float,
        showOutline: Boolean,
        buffer: VertexConsumer,
    ) {
        RenderUtils.drawLinedBox(
            box, matrixStack, entityHitColor, entityHitAlpha
        )
        if (showOutline) {
            RenderUtils.drawOutlinedBox(
                box, matrixStack, entityHitOutlineColor, entityHitOutlineAlpha, 2f, buffer
            )
        }
    }

    /**
     * Renders an impact marker.
     * @param matrixStack The [PoseStack] for rendering transformations.
     * @param impactColor The color of the impact marker.
     * @param impactAlpha The alpha (transparency) of the impact marker.
     */
    fun renderImpact(matrixStack: PoseStack, impactColor: Color, impactAlpha: Float) {
        val box = AABB(impactPos!!, impactPos!!).inflate(ENTITY_AABB_SCALE)
        RenderUtils.drawLinedBox(
            box, matrixStack, impactColor, alpha = impactAlpha
        )
    }


    /**
     * Renders the trajectory of a projectile.
     * @param matrixStack The [PoseStack] for rendering transformations.
     * @param projectile The [ProjectileData] containing information about the projectile.
     * @param impactData The [ImpactData] containing information about the impact.
     * @param trajectoryWidth The width of the trajectory line.
     * @param trajectoryStyle The style of the trajectory line (LINE, DOTTED, DASHED).
     * @param trajectoryAlpha The alpha (transparency) of the trajectory line.
     * @param partialTicks The partial ticks for interpolation.
     * @param eyePos The position of the player's eyes.
     */
    fun renderTrajectory(
        matrixStack: PoseStack,
        projectile: ProjectileData,
        impactData: ImpactData,
        trajectoryWidth: ProjectileTracerConfig.TrajectoryWidth,
        trajectoryStyle: ProjectileTracerConfig.TrajectoryStyle,
        trajectoryAlpha: Float,
        partialTicks: Float,
        eyePos: Vec3,
    ) {
        val handToEyeDelta: Vec3 = handToEyeDelta(projectile.offset, projectile.position, eyePos, 1, partialTicks)
        val trajectoryPoints = impactData.trajectoryPoints
        for (i in 0 until trajectoryPoints.size - 1) {
            val lerpedDelta =
                handToEyeDelta.scale((trajectoryPoints.size - (i * 1.0)) / trajectoryPoints.size)
            val nextLerpedDelta =
                handToEyeDelta.scale((trajectoryPoints.size - (i + 1 * 1.0)) / trajectoryPoints.size)
            val pos = trajectoryPoints[i].add(lerpedDelta)
            val dir = (trajectoryPoints[i + 1].add(nextLerpedDelta)).subtract(pos).scale(1.0)

            val lineWidth = when (trajectoryWidth) {
                ProjectileTracerConfig.TrajectoryWidth.SMALL -> 2.0f
                ProjectileTracerConfig.TrajectoryWidth.MEDIUM -> 5f
                ProjectileTracerConfig.TrajectoryWidth.LARGE -> 8.0f
            }
            when (trajectoryStyle) {
                ProjectileTracerConfig.TrajectoryStyle.LINE -> {
                    RenderUtils.drawSingleLine(
                        matrixStack,
                        start = pos,
                        end = pos.add(dir),
                        color = projectile.color,
                        alpha = trajectoryAlpha,
                        lineWidth = lineWidth,
                    )
                }

                ProjectileTracerConfig.TrajectoryStyle.DASHED -> {
                    RenderUtils.drawSingleLine(
                        matrixStack,
                        start = pos,
                        end = pos.add(dir.scale(0.75)),
                        color = projectile.color,
                        alpha = trajectoryAlpha,
                        lineWidth = lineWidth,
                    )
                }

                ProjectileTracerConfig.TrajectoryStyle.DOTTED -> {
                    val scale = when (trajectoryWidth) {
                        ProjectileTracerConfig.TrajectoryWidth.SMALL -> 0.05
                        ProjectileTracerConfig.TrajectoryWidth.MEDIUM -> 0.1
                        ProjectileTracerConfig.TrajectoryWidth.LARGE -> 0.15
                    }
                    val box = AABB(pos, pos).inflate(scale)
                    RenderUtils.drawLinedBox(
                        box,
                        matrixStack,
                        projectile.color,
                        alpha = trajectoryAlpha
                    )
                }
            }

        }
    }

    /**
     * Gets the velocity of a projectile.
     * @param partialTicks The ticket delta.
     * @param throwPower The throw power of a projectile
     * @param pullPower The pull power of a projectile
     * @return vel A [Vec3] offset for the velocity of a projectile
     */
    fun getProjVelocity(partialTicks: Float, throwPower: Double, pullPower: Float = 1.0f): Vec3 {
        val player = client.getPlayer()
        val vel = player.getViewVector(partialTicks).scale(throwPower * pullPower)
        return vel
    }

    /**
     * Gets a list of projectile data from the players given projectile.
     * @param partialTicks The tick delta
     * @return A list of [ProjectileData] containing information about the projectile.
     */
    fun getProjectileList(partialTicks: Float): List<ProjectileData> {
        val projectileData: MutableList<ProjectileData> = ArrayList()
        val player = client.getPlayer()
        val itemStack = player.mainHandItem
        val item = itemStack.item
        val position = player.getEyePosition(partialTicks)
        val useTicks = player.getTicksUsingItem()
        var newProjectileData: ProjectileData
        when (item) {
            is BowItem -> {
                val pull = BowItem.getPowerForTime(useTicks)
                val velocity = getProjVelocity(partialTicks, BOW_SCALE, pull)
                if (pull <= 0.1) return projectileData
                newProjectileData = ProjectileData(
                    velocity = velocity, offset = BOW_OFFSET, position = position, gravity = BOW_GRAVITY,
                    color = getConfig().bowTrajectoryColor,
                    physicsOrder = PhysicsOrder.POSITION_DRAG_GRAVITY,
                    waterDrag = 0.5
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
                        gravity = CROSSBOW_GRAVITY,
                        physicsOrder = PhysicsOrder.POSITION_DRAG_GRAVITY
                    )
                } else {
                    return emptyList()
                }
            }

            is FishingRodItem -> {
                val direction = angleFromRotation(player.xRot, player.yRot, -5.0).normalize()
                val vel = direction.scale(1.4)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = FISHING_ROD_OFFSET,
                    position = position,
                    color = getConfig().fishingRodTrajectoryColor,
                    gravity = FISHING_ROD_GRAVITY,
                    drag = 0.90,
                    physicsOrder = PhysicsOrder.POSITION_DRAG_GRAVITY
                )
            }

            is SnowballItem -> {
                val vel = getProjVelocity(partialTicks, SnowballItem.PROJECTILE_SHOOT_POWER.toDouble())
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = SNOWBALL_OFFSET,
                    position = position,
                    color = getConfig().snowballTrajectoryColor,
                    gravity = SNOWBALL_GRAVITY,
                    physicsOrder = PhysicsOrder.GRAVITY_DRAG_POSITION,
                    waterDrag = 0.8
                )

            }

            is EggItem -> {
                val vel = getProjVelocity(partialTicks, EggItem.PROJECTILE_SHOOT_POWER.toDouble())
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = SNOWBALL_OFFSET,
                    position = position,
                    color = getConfig().snowballTrajectoryColor,
                    gravity = SNOWBALL_GRAVITY,
                    physicsOrder = PhysicsOrder.GRAVITY_DRAG_POSITION,
                    waterDrag = 0.8
                )
            }

            is EnderpearlItem -> {
                val vel = getProjVelocity(partialTicks, EnderpearlItem.PROJECTILE_SHOOT_POWER.toDouble())
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = ENDERPEARL_OFFSET,
                    position = position,
                    color = getConfig().enderpearlTrajectoryColor,
                    gravity = ENDERPEARL_GRAVITY,
                    physicsOrder = PhysicsOrder.GRAVITY_DRAG_POSITION
                )

            }

            is SplashPotionItem -> {
                val direction = angleFromRotation(player.xRot, player.yRot, -30.0).normalize()
                val vel = direction.scale(POTION_SCALE.toDouble())
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = POTION_OFFSET,
                    position = position,
                    color = getConfig().snowballTrajectoryColor,
                    gravity = 0.05,
                    physicsOrder = PhysicsOrder.GRAVITY_DRAG_POSITION,
                    waterDrag = 0.8
                )
            }

            is ExperienceBottleItem -> {
                val direction = angleFromRotation(player.xRot, player.yRot, -20.0).normalize()
                val vel = direction.scale(0.7)
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = POTION_OFFSET,
                    position = position,
                    gravity = 0.07,
                    physicsOrder = PhysicsOrder.GRAVITY_DRAG_POSITION,
                    waterDrag = 0.8
                )
            }

            is TridentItem -> {
                val vel = getProjVelocity(partialTicks, TRIDENT_SCALE)
                if (!player.isUsingItem) return emptyList()
                newProjectileData = ProjectileData(
                    velocity = vel,
                    offset = TRIDENT_OFFSET,
                    position = position,
                    gravity = TRIDENT_GRAVITY,
                    color = getConfig().tridentTrajectoryColor,
                    physicsOrder = PhysicsOrder.POSITION_DRAG_GRAVITY
                )
            }

            else -> {
                return emptyList()
            }
        }
        projectileData.add(
            newProjectileData
        )
        return projectileData
    }

    /**
     * Calculates an impact for the given projectile position and data.
     * @param projectilePosition The [Vec3] position of the projectile
     * @param projectileData The [ProjectileData] containing information about the projectile
     * @return An [ImpactData] containing information about the impact
     */
    fun getProjectileImpactData(projectilePosition: Vec3, projectileData: ProjectileData): ImpactData {
        var prevPos = projectileData.position
        var impact: HitResult? = null
        var hitEntity: Entity? = null
        var hasHit = false
        val trajectoryPoints: MutableList<Vec3> = ArrayList()
        var drag = projectileData.drag
        val gravity = projectileData.gravity
        var velocity = projectileData.velocity.add(client.getPlayer().deltaMovement)
        var entityHitPos: Vec3? = null
        var newPos = projectilePosition
        for (i in 0..MAX_TRAJECTORY_POINTS) {
            trajectoryPoints.add(newPos)
            // for all physics orders, start with the order in projectileData, then apply the other two in order.
            when (projectileData.physicsOrder) {
                PhysicsOrder.POSITION_DRAG_GRAVITY -> {
                    newPos = newPos.add(velocity)
                    velocity = velocity.scale(drag)
                    velocity = velocity.subtract(0.0, gravity, 0.0)
                }

                PhysicsOrder.DRAG_POSITION_GRAVITY -> {
                    velocity = velocity.scale(drag)
                    newPos = newPos.add(velocity)
                    velocity = velocity.subtract(0.0, gravity, 0.0)
                }

                PhysicsOrder.GRAVITY_DRAG_POSITION -> {
                    velocity = velocity.subtract(0.0, gravity, 0.0)
                    velocity = velocity.scale(drag)
                    newPos = newPos.add(velocity)
                }
            }
            val box = AABB(prevPos, newPos).deflate(0.5)
            val entities: List<Entity?>? = client.getWorld().getEntitiesOfClass(
                Entity::class.java, box
            ) { e -> !e.isSpectator && e.isAlive && (e !is Projectile) && (e !is ItemEntity) && (e !is ExperienceOrb) && (e !is EnderDragon) && (e !is LocalPlayer) }
            var closest = Double.MAX_VALUE
            var closestEntity: Entity? = null
            for (entity in entities!!) {
                assert(entity != null)
                val entityBox = entity!!.boundingBox.deflate(ENTITY_AABB_SCALE)
                val raycastHit = entityBox?.clip(prevPos, newPos)

                if (raycastHit?.isPresent ?: false) {
                    val distance = prevPos.distanceTo(raycastHit.get())
                    if (distance < closest) {
                        entityHitPos = raycastHit.get()
                        closestEntity = entity
                        closest = distance
                        hasHit = true
                    }
                }
            }
            val waterHitResult = client.getWorld().clip(
                ClipContext(
                    prevPos,
                    newPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.WATER,
                    client.getPlayer()
                )
            )
            val hitResult = client.getWorld().clip(
                ClipContext(
                    prevPos,
                    newPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    client.getPlayer()
                )
            )

            if (waterHitResult.type != HitResult.Type.MISS) {
                drag = projectileData.waterDrag
            }

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
        return ImpactData(
            trajectoryPoints,
            impact,
            hitEntity,
            hasHit,
        )

    }

    /**
     * Calculates the vector between the players eye and hand.
     * @param offset An offset that is used for calculating the position of the tracer start
     * @param startPos The current position of a projectile
     * @param eye The players eye position
     * @param handMultiplier What hand the player is using, 1 for right hand, -1 for left hand
     * @param delta The tick delta
     * @return A [Vec3] representing the vector between the players eye and hand.
     */
    fun handToEyeDelta(offset: Vec3, startPos: Vec3, eye: Vec3, handMultiplier: Int, delta: Float): Vec3 {
        val player = client.getPlayer()
        val yaw = Math.toRadians(-player.getViewYRot(delta).toDouble())
        val pitch = Math.toRadians(-player.getViewXRot(delta).toDouble())
        val forward = player.getViewVector(delta)
        val up = Vec3(-sin(pitch) * sin(yaw), cos(pitch), -sin(pitch) * cos(yaw)).normalize()
        val right = forward.cross(up).normalize()
        var tmpOffset = offset
        if (client.gameRenderer.mainCamera().isDetached) tmpOffset = offset.scale(0.0)

        return right.scale(handMultiplier * tmpOffset.x).add(up.scale(tmpOffset.y)).add(forward.scale(tmpOffset.z))
            .add(eye.subtract(startPos))
    }

    override fun onEntityNameRender(er: EntityNameRender) {
        if (!getConfig().showEntityDistance) {
            er.cancel()
            return
        }
        val distanceToPlayer = er.entity.distanceTo(client.getPlayer())
        // format as "distance: 10.0m" in red
        val style = Style.EMPTY.withColor(ChatFormatting.WHITE)
        val distanceFormat = String.format("%.1fm", distanceToPlayer)
        val text = Component.empty().append(Component.literal(distanceFormat)).withStyle(style)
        er.nameTag = text
    }

    override fun onLevelRenderEnd(event: LevelRenderEndEventData) {
        if (impactPos == null || !getConfig().showImpactDistance) {
            event.cancel()
            return
        }
        event.targetVec = impactPos!!
        val impactDistance = event.targetVec.distanceTo(client.getPlayer().getEyePosition(0.0f))
        // scale from 1/16 to 1/8 based on distance, with a max of 1/8
        val scale = (1 / 32f) + (impactDistance / 100f)
        val clampedScale = if (scale > 1 / 8f) 1 / 8f else scale
        // scale y offset between 0.5 and 1.0 based on distance, with a max of 1.0
        val yOffset = (0.25 + impactDistance / 10f).coerceIn(0.5, 2.0)
        event.targetVec = event.targetVec.add(0.0, yOffset, 0.0)

        event.scale = clampedScale.toFloat()
        event.textToDraw = "${String.format("%.1f", impactDistance)}m"
        event.backgroundColor = Colors.BLACK
    }


    companion object {
        val BOW_OFFSET = Vec3(0.2, -0.025, -0.2)
        const val BOW_SCALE = 3.5
        const val BOW_GRAVITY = 0.05

        val CROSSBOW_OFFSET = Vec3(0.09, -0.9, -0.2)
        const val CROSSBOW_SCALE = 3.15
        const val CROSSBOW_GRAVITY = 0.04

        val FISHING_ROD_OFFSET = Vec3(0.2, -0.2, -0.1)
        const val FISHING_ROD_GRAVITY = 0.05

        val SNOWBALL_OFFSET = Vec3(0.2, -0.09, 0.2)
        const val SNOWBALL_GRAVITY = 0.03

        val POTION_OFFSET = Vec3(0.2, -0.09, 0.2)
        const val POTION_SCALE = SplashPotionItem.PROJECTILE_SHOOT_POWER

        val ENDERPEARL_OFFSET = Vec3(0.2, -0.09, 0.2)
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

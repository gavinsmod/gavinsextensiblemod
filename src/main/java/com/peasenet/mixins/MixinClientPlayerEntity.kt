/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mixins

import com.mojang.authlib.GameProfile
import com.peasenet.main.GavinsMod
import com.peasenet.mixinterface.IClientPlayerEntity
import com.peasenet.util.event.AirStrafeEvent
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.EntityPose
import net.minecraft.entity.player.PlayerAbilities
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.gen.Accessor

/**
 * @author gt3ch1
 * @version 03-02-2023
 */
@Mixin(ClientPlayerEntity::class)
abstract class MixinClientPlayerEntity(world: ClientWorld?, profile: GameProfile?) :
    AbstractClientPlayerEntity(world, profile), IClientPlayerEntity {
    @Shadow
    var nextNauseaStrength = 0f

    override fun isOnGround(): Boolean {
        return super.onGround
    }

    override fun getBlockPos(): BlockPos {
        return super.getBlockPos()
    }

    override fun getPrevX(): Double {
        return super.prevX
    }

    @Accessor("networkHandler")
    abstract override fun getNetworkHandler(): ClientPlayNetworkHandler
    override fun getPrevY(): Double {
        return super.prevY
    }

    override fun getPrevZ(): Double {
        return super.prevZ
    }

    override fun isNoClip(): Boolean {
        return super.noClip
    }

    override fun getAttackCoolDownProgress(f: Float): Float {
        return super.getAttackCooldownProgress(f)
    }

    override fun eyeHeight(): Double {
        return super.getEyeY()
    }

    @Shadow
    abstract override fun isSubmergedInWater(): Boolean


    override fun getPose(): EntityPose {
        return super.getPose()
    }

    @Shadow
    abstract override fun isSneaking(): Boolean

    override fun getFallDistance(): Float {
        return super.fallDistance
    }


    override fun getHeadYaw(): Float {
        return super.headYaw
    }

    override fun getPos(): Vec3d {
        return super.getPos()
    }


    override fun getVelocity(): Vec3d {
        return super.getVelocity()
    }

    override fun getAbilities(): PlayerAbilities {
        return super.getAbilities()
    }

    @Shadow
    abstract override fun getBodyYaw(): Float

    override fun getMainHandStack(): ItemStack {
        return super.getMainHandStack()
    }

    override fun getOffGroundSpeed(): Float {
        val speed = super.getOffGroundSpeed()
        val evt = AirStrafeEvent(speed)
        GavinsMod.eventManager?.call(evt)

        return evt.speed
    }

    override fun isFallFlying(): Boolean {
        return super.isFallFlying()
    }

    override fun getGameProfile(): GameProfile {
        return super.getGameProfile()
    }

    @Shadow
    var lastNauseaStrength = 0f
}
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

import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsMod.Companion.isEnabled
import com.peasenet.mixinterface.IMinecraftClient
import com.peasenet.mods.Type
import com.peasenet.util.event.PlayerAttackEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.network.message.MessageHandler
import net.minecraft.client.option.GameOptions
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.Window
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.hit.HitResult
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.io.File

/**
 * @author gt3ch1
 * @version 03-02-2023
 */
@Mixin(MinecraftClient::class)
abstract class MixinMinecraftClient : IMinecraftClient {
    override fun getPlayerInteractionManager(): ClientPlayerInteractionManager {
        return MinecraftClient.getInstance().interactionManager!!
    }

    override fun getFps(): Int {
        return MinecraftClient.getInstance().currentFps
    }


    @Shadow
    @Final
    override lateinit var options: GameOptions

    @Shadow
    @Final
    override lateinit var textRenderer: TextRenderer

    @Shadow
    @Final
    override lateinit var worldRenderer: WorldRenderer

    @Shadow
    var chunkCullingEnabled = false


    override fun getWorld(): ClientWorld {
        return MinecraftClient.getInstance().world!!
    }

    @Shadow
    @Final
    override lateinit var runDirectory: File

    @Shadow
    var crosshairTarget: HitResult? = null

    @Final
    @Shadow
    override lateinit var entityRenderDispatcher: EntityRenderDispatcher

    @Shadow
    private var itemUseCooldown = 0

    @Final
    @Shadow
    override lateinit var window: Window

    @get:Shadow
    abstract override val messageHandler: MessageHandler

    @Inject(
        at = [At(
            value = "FIELD",
            target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;",
            ordinal = 0
        )], method = ["doAttack()Z"]
    )
    private fun doAttack(cir: CallbackInfoReturnable<Boolean>) {
        val event = PlayerAttackEvent()
        GavinsMod.eventManager!!.call(event)
    }

    override fun setItemUseCooldown(cooldown: Int) {
        itemUseCooldown = cooldown
    }

    override fun player(): ClientPlayerEntity {
        return MinecraftClient.getInstance().player!!
    }

    @Shadow
    abstract override fun setScreen(s: Screen?)
    override fun setChunkCulling(b: Boolean) {
        chunkCullingEnabled = b
    }

    override fun crosshairTarget(): HitResult {
        return crosshairTarget!!
    }

    private companion object {
        @JvmStatic
        @JvmName("isAmbientOcclusionEnabled")
        @Inject(at = [At(value = "HEAD")], method = ["isAmbientOcclusionEnabled()Z"], cancellable = true)
        private fun isXrayOrFullBright(ci: CallbackInfoReturnable<Boolean>) {
            val disabled = isEnabled(Type.XRAY) || isEnabled(Type.FULL_BRIGHT)
            ci.returnValue = !disabled
            ci.cancel()
        }
    }
}